package org.bioguard.network;
import org.bioguard.service.PacienteService;
import org.bioguard.service.VirusService;
import org.bioguard.service.MuestraDNAService;
import org.bioguard.service.DiagnosticoService;
import org.bioguard.model.Paciente;
import org.bioguard.model.Virus;
import org.bioguard.exception.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Manejador de cliente para procesar solicitudes en un hilo separado.
 * Cada conexión de cliente es procesada por una instancia de esta clase.
 * Implementa Runnable para ejecutarse en un hilo del pool de trabajo.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class ManejadorClienteThread implements Runnable {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String idCliente;
    /**
     * Constructor del manejador de cliente.
     * 
     * @param socket Socket de conexión con el cliente
     */
    public ManejadorClienteThread(Socket socket) {
        this.socket = socket;
        this.idCliente = "Cliente_" + socket.getInetAddress().toString();
    }
    /**
     * Método principal que ejecuta el manejador de cliente.
     * Configura los streams y procesa comandos hasta que el cliente se desconecta.
     */
    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("[" + idCliente + "] Conectado");
            String comando;
            while ((comando = entrada.readLine()) != null && !comando.equals(ProtocoloBioGuard.SALIR)) {
                procesarComando(comando);
            }
            System.out.println("[" + idCliente + "] Desconectado");
        } catch (IOException e) {
            System.err.println("[" + idCliente + "] Error de conexión: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    /**
     * Procesa un comando recibido del cliente.
     * 
     * @param comando Comando con formato: COMANDO|parametro1|parametro2|...
     */
    private void procesarComando(String comando) {
        try {
            String[] partes = comando.split("\\|");
            String nombreComando = partes[0].trim();
            switch (nombreComando) {
                case ProtocoloBioGuard.REGISTRAR_PACIENTE:
                    procesarRegistroPaciente(partes);
                    break;
                case ProtocoloBioGuard.VERIFICAR_PACIENTE:
                    procesarVerificacionPaciente(partes);
                    break;
                case ProtocoloBioGuard.OBTENER_PACIENTE:
                    procesarObtenerPaciente(partes);
                    break;
                case ProtocoloBioGuard.CARGAR_VIRUS:
                    procesarCargarVirus(partes);
                    break;
                case ProtocoloBioGuard.ENVIAR_MUESTRA_DNA:
                    procesarEnviarMuestra(partes);
                    break;
                case ProtocoloBioGuard.PROCESAR_DIAGNOSTICO:
                    procesarDiagnostico(partes);
                    break;
                case ProtocoloBioGuard.DETECTAR_MUTACIONES:
                    procesarDetectarMutaciones(partes);
                    break;
                case ProtocoloBioGuard.LISTAR_VIRUS:
                    procesarListarVirus();
                    break;
                case ProtocoloBioGuard.GENERAR_REPORTE_ALTO_RIESGO:
                    procesarGenerarReporte(partes);
                    break;
                default:
                    enviarError("Comando desconocido: " + nombreComando);
            }
        } catch (Exception e) {
            enviarError("Error procesando comando: " + e.getMessage());
        }
    }
    /**
     * Procesa el registro de un nuevo paciente.
     * Formato: REGISTRAR_PACIENTE|documento|nombre|apellido|edad|correo|genero|ciudad|pais
     */
    private void procesarRegistroPaciente(String[] partes) {
        try {
            if (partes.length < 9) {
                enviarError("Formato incorrecto. Use: REGISTRAR_PACIENTE|documento|nombre|apellido|edad|correo|genero|ciudad|pais");
                return;
            }
            PacienteService.registrarPaciente(
                    partes[1], partes[2], partes[3], Integer.parseInt(partes[4]),
                    partes[5], partes[6], partes[7], partes[8]
            );
            enviarOK("Paciente registrado exitosamente");
        } catch (DocumentoDuplicadoException e) {
            enviarError(e.getMessage());
        } catch (PacienteException e) {
            enviarError("Error en paciente: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        } catch (NumberFormatException e) {
            enviarError("La edad debe ser un número entero");
        }
    }
    /**
     * Verifica si un paciente existe.
     * Formato: VERIFICAR_PACIENTE|documento
     */
    private void procesarVerificacionPaciente(String[] partes) {
        try {
            if (partes.length < 2) {
                enviarError("Formato incorrecto. Use: VERIFICAR_PACIENTE|documento");
                return;
            }
            boolean existe = PacienteService.existePaciente(partes[1]);
            if (existe) {
                salida.println(ProtocoloBioGuard.EXISTE);
            } else {
                salida.println(ProtocoloBioGuard.NO_EXISTE);
            }
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Obtiene la información de un paciente.
     * Formato: OBTENER_PACIENTE|documento
     */
    private void procesarObtenerPaciente(String[] partes) {
        try {
            if (partes.length < 2) {
                enviarError("Formato incorrecto. Use: OBTENER_PACIENTE|documento");
                return;
            }
            Optional<Paciente> paciente = PacienteService.obtenerPaciente(partes[1]);
            if (paciente.isPresent()) {
                Paciente p = paciente.get();
                salida.println(ProtocoloBioGuard.OK + "|" + p.toString());
            } else {
                enviarError("Paciente no encontrado");
            }
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Carga un virus desde un archivo FASTA.
     * Formato: CARGAR_VIRUS|ruta_archivo
     */
    private void procesarCargarVirus(String[] partes) {
        try {
            if (partes.length < 2) {
                enviarError("Formato incorrecto. Use: CARGAR_VIRUS|ruta_archivo");
                return;
            }
            VirusService.cargarVirusDesdeArchivo(partes[1]);
            enviarOK("Virus cargado exitosamente");
        } catch (SecuenciaInvalidaException e) {
            enviarError("Secuencia inválida: " + e.getMessage());
        } catch (VirusException e) {
            enviarError("Error en virus: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Envía una muestra de ADN desde el cliente.
     * Formato: ENVIAR_MUESTRA_DNA|ruta_archivo
     */
    private void procesarEnviarMuestra(String[] partes) {
        try {
            if (partes.length < 2) {
                enviarError("Formato incorrecto. Use: ENVIAR_MUESTRA_DNA|ruta_archivo");
                return;
            }
            MuestraDNAService.cargarMuestraDesdeArchivo(partes[1]);
            enviarOK("Muestra cargada exitosamente");
        } catch (MuestraNoValidaException e) {
            enviarError("Muestra no válida: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Procesa el diagnóstico de una muestra.
     * Formato: PROCESAR_DIAGNOSTICO|documento|fecha
     */
    private void procesarDiagnostico(String[] partes) {
        try {
            if (partes.length < 3) {
                enviarError("Formato incorrecto. Use: PROCESAR_DIAGNOSTICO|documento|fecha");
                return;
            }
            DiagnosticoService.procesarMuestra(partes[1], partes[2]);
            enviarOK("Diagnóstico procesado exitosamente");
        } catch (DiagnosticoException e) {
            enviarError("Error en diagnóstico: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Detecta mutaciones en un paciente.
     * Formato: DETECTAR_MUTACIONES|documento|fecha_actual
     */
    private void procesarDetectarMutaciones(String[] partes) {
        try {
            if (partes.length < 3) {
                enviarError("Formato incorrecto. Use: DETECTAR_MUTACIONES|documento|fecha_actual");
                return;
            }
            Map<String, java.util.List<int[]>> mutaciones = DiagnosticoService.detectarMutaciones(
                    partes[1], partes[2]);
            if (mutaciones.isEmpty()) {
                enviarOK("No se detectaron mutaciones");
            } else {
                StringBuilder resultado = new StringBuilder(ProtocoloBioGuard.OK);
                mutaciones.forEach((fecha, cambios) -> {
                    resultado.append("|").append(fecha).append(":");
                    for (int[] cambio : cambios) {
                        resultado.append("[").append(cambio[0]).append(",").append(cambio[1]).append("]");
                    }
                });
                salida.println(resultado.toString());
            }
        } catch (DiagnosticoException e) {
            enviarError("Error en mutaciones: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Lista todos los virus registrados.
     */
    private void procesarListarVirus() {
        try {
            List<Virus> virus = VirusService.obtenerTodosVirus();
            StringBuilder respuesta = new StringBuilder(ProtocoloBioGuard.OK);
            for (Virus v : virus) {
                respuesta.append("|").append(v.getNombre()).append(",")
                        .append(v.getInfecciosidad().getDescripcion());
            }
            salida.println(respuesta.toString());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Genera un reporte de pacientes de alto riesgo.
     * Formato: GENERAR_REPORTE_ALTO_RIESGO|ruta_salida
     */
    private void procesarGenerarReporte(String[] partes) {
        try {
            if (partes.length < 2) {
                enviarError("Formato incorrecto. Use: GENERAR_REPORTE_ALTO_RIESGO|ruta_salida");
                return;
            }
            DiagnosticoService.generarReporteAltaRiesgo(partes[1]);
            enviarOK("Reporte generado en: " + partes[1]);
        } catch (DiagnosticoException e) {
            enviarError("Error en reporte: " + e.getMessage());
        } catch (PersistenciaException e) {
            enviarError("Error de persistencia: " + e.getMessage());
        }
    }
    /**
     * Envía una respuesta OK al cliente.
     */
    private void enviarOK(String mensaje) {
        salida.println(ProtocoloBioGuard.OK + "|" + mensaje);
    }
    /**
     * Envía un mensaje de error al cliente.
     */
    private void enviarError(String mensaje) {
        salida.println(ProtocoloBioGuard.ERROR + "|" + mensaje);
    }
    /**
     * Cierra la conexión con el cliente.
     */
    private void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar socket: " + e.getMessage());
        }
    }
}
