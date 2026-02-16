# BioGuard - Diagrama de Clases y Arquitectura

## Diagrama de Clases

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         ENTIDADES (Model)                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────┐          ┌──────────────────┐                     │
│  │    Paciente      │          │      Virus       │                     │
│  ├──────────────────┤          ├──────────────────┤                     │
│  │ - documento: PK  │          │ - nombre: PK     │                     │
│  │ - nombre         │          │ - secuencia      │                     │
│  │ - apellido       │          │ - infecciosidad  │                     │
│  │ - edad           │          │ - tipoVirus      │                     │
│  │ - correo         │          └──────────────────┘                     │
│  │ - genero         │                                                    │
│  │ - ciudad         │          ┌──────────────────────────┐              │
│  │ - pais           │          │   MuestraDNA             │              │
│  └──────────────────┘          ├──────────────────────────┤              │
│                                │ - idMuestra (documento+  │              │
│                                │   fecha)                 │              │
│  ┌──────────────────┐          │ - documento (FK)         │              │
│  │ DiagnosticoVirus │          │ - secuencia              │              │
│  ├──────────────────┤          │ - fecha                  │              │
│  │ - idDiagnostico  │          └──────────────────────────┘              │
│  │ - documento (FK) │                                                    │
│  │ - virus          │          ┌──────────────────────────┐              │
│  │ - posInicio      │          │   ResultadoDiagnostico   │              │
│  │ - posFin         │          ├──────────────────────────┤              │
│  │ - fecha          │          │ - virusDetectado: String │              │
│  └──────────────────┘          │ - posicionInicio: int    │              │
│                                │ - posicionFin: int       │              │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│              SERVICIOS DE NEGOCIO (Service Layer)                       │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ PacienteService              │  │ VirusService                 │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ + registrarPaciente()        │  │ + cargarVirusFromFASTA()     │    │
│  │ + obtenerPaciente()          │  │ + validarSecuencia()         │    │
│  │ + existePaciente()           │  │ + obtenerTodosVirus()        │    │
│  │ + validarDocumentoDuplicado()│  │ + buscarVirus()              │    │
│  │ + getAllPacientes()          │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ DiagnosticoService           │  │ MuestraDNAService            │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ + procesarMuestra()          │  │ + guardarMuestra()           │    │
│  │ + detenerVirus()             │  │ + obtenerMuestrasHistoricas()│    │
│  │ + generarReporteDiagnostico()│  │ + compararMuestras()         │    │
│  │ + detectarMutaciones()       │  │ + detectarMutaciones()       │    │
│  │ + generarReportAltaRiesgo()  │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│           PERSISTENCIA (Repository/DAO Layer)                            │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ PacienteRepository           │  │ VirusRepository              │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ - archivo: pacientes.csv     │  │ - directorio: /virus         │    │
│  │ + guardar()                  │  │ + guardar()                  │    │
│  │ + obtener()                  │  │ + cargar()                   │    │
│  │ + actualizar()               │  │ + obtenerTodos()             │    │
│  │ + existe()                   │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ MuestraRepository            │  │ DiagnosticoRepository        │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ - directorio: /muestras      │  │ - directorio: /diagnosticos  │    │
│  │ + guardar()                  │  │ + guardar()                  │    │
│  │ + obtener()                  │  │ + obtener()                  │    │
│  │ + obtenerHistorico()         │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│         COMUNICACIÓN (Networking & Threading)                           │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ ServidorBioGuard             │  │ ManejadorClienteThread       │    │
│  │ (extends Thread)             │  │ (implements Runnable)        │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ + iniciar()                  │  │ - socket: Socket             │    │
│  │ + detener()                  │  │ - entrada: BufferedReader    │    │
│  │ + run()                      │  │ - salida: PrintWriter        │    │
│  │ - aceptarConexiones()        │  │ + procesarComandos()         │    │
│  └──────────────────────────────┘  │ + run()                      │    │
│                                    └──────────────────────────────┘    │
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ ClienteBioGuard              │  │ ProtocoloBioGuard            │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ - socket: Socket             │  │ + REGISTRAR_PACIENTE         │    │
│  │ - entrada: BufferedReader    │  │ + VERIFICAR_PACIENTE         │    │
│  │ - salida: PrintWriter        │  │ + CARGAR_VIRUS               │    │
│  │ + conectar()                 │  │ + ENVIAR_MUESTRA_DNA         │    │
│  │ + enviarComando()            │  │ + GENERAR_REPORTE_RIESGO     │    │
│  │ + recebirRespuesta()         │  │ + GENERAR_REPORTE_MUTACIONES │    │
│  │ + desconectar()              │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│           MANEJO DE EXCEPCIONES (Excepciones Personalizadas)            │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌───────────────────────────────────────────────────────────────────┐ │
│  │                   BioGuardException                               │ │
│  │              (extends Exception - Raíz)                           │ │
│  └───────────────────────────────────────────────────────────────────┘ │
│                             ▲                                           │
│        ┌────────────────────┼────────────────────┐                     │
│        │                    │                    │                     │
│   ┌─────────┐         ┌──────────────┐    ┌──────────────┐             │
│   │Paciente │         │VirusException│    │ Diagnostico  │             │
│   │Exception│         │              │    │ Exception    │             │
│   └─────────┘         └──────────────┘    └──────────────┘             │
│        ▲                      ▲                   ▲                     │
│        │                      │                   │                    │
│  ┌─────┴─────┐        ┌──────┴──────┐    ┌──────┴──────┐               │
│  │ Documento │        │ Secuencia   │    │ Muestra     │               │
│  │Duplicado  │        │ Invalida    │    │ No Valida   │               │
│  │Exception  │        │Exception    │    │ Exception   │               │
│  └───────────┘        └─────────────┘    └─────────────┘               │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                  PersistenciaException                           │   │
│  │          (Error en lectura/escritura de archivos)               │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│         UTILIDADES (Utilities & Helpers)                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ FASTAParser                  │  │ CSVParser                    │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ + parsearArchivo()           │  │ + parsearLinea()             │    │
│  │ + extraerNombre()            │  │ + construirLinea()           │    │
│  │ + extraerSecuencia()         │  │ + escaparComillas()          │    │
│  │ + validarSequence()          │  └──────────────────────────────┘    │
│  └──────────────────────────────┘                                       │
│                                                                           │
│  ┌──────────────────────────────┐  ┌──────────────────────────────┐    │
│  │ ConfiguracionSSL             │  │ AlgoritmoComparacion         │    │
│  ├──────────────────────────────┤  ├──────────────────────────────┤    │
│  │ + crearSSLContext()          │  │ + buscarSubcadena()          │    │
│  │ + cargarCertificado()        │  │ + encontrarDiferencias()     │    │
│  │ + obtenerSSLSocket()         │  │ + calcularMutaciones()       │    │
│  └──────────────────────────────┘  └──────────────────────────────┘    │
│                                                                           │
└─────────────────────────────────────────────────────────────────────────┘

## Estructura de Paquetes

org.bioguard
├── model
│   ├── Paciente
│   ├── Virus
│   ├── MuestraDNA
│   ├── DiagnosticoVirus
│   └── ResultadoDiagnostico
├── exception
│   ├── BioGuardException
│   ├── PacienteException
│   ├── VirusException
│   ├── DiagnosticoException
│   └── PersistenciaException
├── service
│   ├── PacienteService
│   ├── VirusService
│   ├── DiagnosticoService
│   └── MuestraDNAService
├── repository
│   ├── PacienteRepository
│   ├── VirusRepository
│   ├── MuestraRepository
│   └── DiagnosticoRepository
├── network
│   ├── ServidorBioGuard
│   ├── ManejadorClienteThread
│   ├── ClienteBioGuard
│   └── ProtocoloBioGuard
└── util
    ├── FASTAParser
    ├── CSVParser
    ├── ConfiguracionSSL
    └── AlgoritmoComparacion

## Flujo de Datos

1. **Registro de Paciente:**
   Cliente -> ServidorBioGuard -> PacienteService -> PacienteRepository -> pacientes.csv

2. **Carga de Virus:**
   Cliente -> ServidorBioGuard -> VirusService -> FASTAParser -> VirusRepository -> /virus

3. **Diagnóstico:**
   Cliente -> ServidorBioGuard -> DiagnosticoService -> MuestraRepository -> 
   Búsqueda de Virus -> DiagnosticoRepository -> CSV de diagnóstico

4. **Mutaciones:**
   MuestraDNAService -> Comparar con historial -> AlgoritmoComparacion -> Reporte de mutaciones

## Relaciones entre Clases

- Paciente 1 --- * MuestraDNA (Un paciente puede tener múltiples muestras)
- Paciente 1 --- * DiagnosticoVirus (Un paciente puede tener múltiples diagnósticos)
- Virus 1 --- * DiagnosticoVirus (Un virus puede aparecer en múltiples diagnósticos)
- MuestraDNA 1 --- * DiagnosticoVirus (Una muestra puede tener múltiples diagnósticos)

## Principios SOLID Aplicados

- **S**ingle Responsibility: Cada clase tiene una responsabilidad única
- **O**pen/Closed: Las clases están abiertas para extensión, cerradas para modificación
- **L**iskov Substitution: Las excepciones siguen una jerarquía consistente
- **I**nterface Segregation: Las interfaces son específicas para cada rol
- **D**ependency Inversion: Las capas superiores dependen de abstracciones

