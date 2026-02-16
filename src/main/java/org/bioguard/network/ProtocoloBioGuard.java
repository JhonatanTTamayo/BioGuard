package org.bioguard.network;
/**
 * Define los comandos y constantes del protocolo de comunicación BioGuard.
 * Establece los comandos que pueden enviarse entre cliente y servidor.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class ProtocoloBioGuard {
    // Comandos del protocolo
    public static final String REGISTRAR_PACIENTE = "REGISTRAR_PACIENTE";
    public static final String VERIFICAR_PACIENTE = "VERIFICAR_PACIENTE";
    public static final String OBTENER_PACIENTE = "OBTENER_PACIENTE";
    public static final String CARGAR_VIRUS = "CARGAR_VIRUS";
    public static final String ENVIAR_MUESTRA_DNA = "ENVIAR_MUESTRA_DNA";
    public static final String PROCESAR_DIAGNOSTICO = "PROCESAR_DIAGNOSTICO";
    public static final String DETECTAR_MUTACIONES = "DETECTAR_MUTACIONES";
    public static final String GENERAR_REPORTE_ALTO_RIESGO = "GENERAR_REPORTE_ALTO_RIESGO";
    public static final String LISTAR_VIRUS = "LISTAR_VIRUS";
    public static final String SALIR = "SALIR";
    // Respuestas
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String EXISTE = "EXISTE";
    public static final String NO_EXISTE = "NO_EXISTE";
    // Separadores
    public static final String SEPARADOR_CAMPOS = "|";
    public static final String SEPARADOR_DATOS = ":";
    /**
     * Constructor privado para evitar instanciación.
     */
    private ProtocoloBioGuard() {
    }
}
