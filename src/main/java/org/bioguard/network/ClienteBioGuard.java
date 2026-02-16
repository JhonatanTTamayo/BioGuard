package org.bioguard.network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * Cliente para conectarse al servidor BioGuard.
 * Proporciona una interfaz de consola para enviar comandos al servidor.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class ClienteBioGuard {
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    private Scanner scanner;
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    /**
     * Constructor del cliente BioGuard.
     */
    public ClienteBioGuard() {
        this.scanner = new Scanner(System.in);
    }
    /**
     * Conecta el cliente al servidor BioGuard.
     * 
     * @return true si la conexión fue exitosa, false en caso contrario
     */
    public boolean conectar() {
        try {
            socket = new Socket(HOST, PUERTO);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado al servidor BioGuard en " + HOST + ":" + PUERTO);
            return true;
        } catch (IOException e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
            return false;
        }
    }
    /**
     * Inicia el cliente y muestra el menú principal.
     */
    public void iniciar() {
        if (!conectar()) {
            return;
        }
        mostrarBienvenida();
        procesarComandos();
        desconectar();
    }
    /**
     * Muestra mensaje de bienvenida.
     */
    private void mostrarBienvenida() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Sistema Distribuido de Vigilancia Genómica BioGuard");
        System.out.println("=".repeat(60));
        System.out.println("Escriba 'help' para ver los comandos disponibles");
        System.out.println("Escriba 'salir' para desconectar\n");
    }
    /**
     * Muestra la ayuda de comandos.
     */
    private void mostrarAyuda() {
        System.out.println("\n" + "-".repeat(60));
        System.out.println("COMANDOS DISPONIBLES:");
        System.out.println("-".repeat(60));
        System.out.println("REGISTRAR_PACIENTE    Registrar un nuevo paciente");
        System.out.println("VERIFICAR_PACIENTE    Verificar si un paciente existe");
        System.out.println("OBTENER_PACIENTE      Obtener información de un paciente");
        System.out.println("CARGAR_VIRUS          Cargar un virus desde archivo FASTA");
        System.out.println("ENVIAR_MUESTRA_DNA    Enviar una muestra de ADN");
        System.out.println("PROCESAR_DIAGNOSTICO  Procesar diagnóstico de una muestra");
        System.out.println("DETECTAR_MUTACIONES   Detectar mutaciones de un paciente");
        System.out.println("LISTAR_VIRUS          Listar todos los virus registrados");
        System.out.println("GENERAR_REPORTE_ALTO_RIESGO  Generar reporte de alto riesgo");
        System.out.println("help                  Mostrar esta ayuda");
        System.out.println("salir                 Salir del sistema");
        System.out.println("-".repeat(60) + "\n");
    }
    /**
     * Loop principal para procesar comandos del usuario.
     */
    private void procesarComandos() {
        String comando;
        while (true) {
            System.out.print("BioGuard> ");
            comando = scanner.nextLine().trim();
            if (comando.isEmpty()) {
                continue;
            }
            if (comando.equalsIgnoreCase("help")) {
                mostrarAyuda();
                continue;
            }
            if (comando.equalsIgnoreCase("salir")) {
                break;
            }
            procesarComando(comando);
        }
        salida.println(ProtocoloBioGuard.SALIR);
    }
    /**
     * Procesa un comando ingresado por el usuario.
     * 
     * @param comando Comando a procesar
     */
    private void procesarComando(String comando) {
        String[] partes = comando.split("\\s+");
        String tipoComando = partes[0].toUpperCase();
        try {
            switch (tipoComando) {
                case "REGISTRAR_PACIENTE":
                    procesarRegistroPaciente();
                    break;
                case "VERIFICAR_PACIENTE":
                    procesarVerificacionPaciente();
                    break;
                case "OBTENER_PACIENTE":
                    procesarObtenerPaciente();
                    break;
                case "CARGAR_VIRUS":
                    procesarCargarVirus();
                    break;
                case "ENVIAR_MUESTRA_DNA":
                    procesarEnviarMuestra();
                    break;
                case "PROCESAR_DIAGNOSTICO":
                    procesarDiagnostico();
                    break;
                case "DETECTAR_MUTACIONES":
                    procesarMutaciones();
                    break;
                case "LISTAR_VIRUS":
                    procesarListarVirus();
                    break;
                case "GENERAR_REPORTE_ALTO_RIESGO":
                    procesarGenerarReporte();
                    break;
                default:
                    System.out.println("Comando desconocido. Escriba 'help' para ayuda");
            }
        } catch (IOException e) {
            System.err.println("Error de comunicación: " + e.getMessage());
        }
    }
    private void procesarRegistroPaciente() throws IOException {
        System.out.println("\n--- Registrar Nuevo Paciente ---");
        System.out.print("Documento: ");
        String documento = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Edad: ");
        String edad = scanner.nextLine();
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Género: ");
        String genero = scanner.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();
        System.out.print("País: ");
        String pais = scanner.nextLine();
        String comando = ProtocoloBioGuard.REGISTRAR_PACIENTE + "|" + documento + "|" + nombre + "|" +
                apellido + "|" + edad + "|" + correo + "|" + genero + "|" + ciudad + "|" + pais;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarVerificacionPaciente() throws IOException {
        System.out.print("\nDocumento del paciente: ");
        String documento = scanner.nextLine();
        String comando = ProtocoloBioGuard.VERIFICAR_PACIENTE + "|" + documento;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarObtenerPaciente() throws IOException {
        System.out.print("\nDocumento del paciente: ");
        String documento = scanner.nextLine();
        String comando = ProtocoloBioGuard.OBTENER_PACIENTE + "|" + documento;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarCargarVirus() throws IOException {
        System.out.print("\nRuta del archivo FASTA del virus: ");
        String rutaArchivo = scanner.nextLine();
        String comando = ProtocoloBioGuard.CARGAR_VIRUS + "|" + rutaArchivo;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarEnviarMuestra() throws IOException {
        System.out.print("\nRuta del archivo FASTA de la muestra: ");
        String rutaArchivo = scanner.nextLine();
        String comando = ProtocoloBioGuard.ENVIAR_MUESTRA_DNA + "|" + rutaArchivo;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarDiagnostico() throws IOException {
        System.out.print("\nDocumento del paciente: ");
        String documento = scanner.nextLine();
        System.out.print("Fecha de la muestra (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        String comando = ProtocoloBioGuard.PROCESAR_DIAGNOSTICO + "|" + documento + "|" + fecha;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarMutaciones() throws IOException {
        System.out.print("\nDocumento del paciente: ");
        String documento = scanner.nextLine();
        System.out.print("Fecha actual (YYYY-MM-DD): ");
        String fecha = scanner.nextLine();
        String comando = ProtocoloBioGuard.DETECTAR_MUTACIONES + "|" + documento + "|" + fecha;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarListarVirus() throws IOException {
        String comando = ProtocoloBioGuard.LISTAR_VIRUS;
        enviarYRecibirRespuesta(comando);
    }
    private void procesarGenerarReporte() throws IOException {
        System.out.print("\nRuta del archivo de salida: ");
        String rutaSalida = scanner.nextLine();
        String comando = ProtocoloBioGuard.GENERAR_REPORTE_ALTO_RIESGO + "|" + rutaSalida;
        enviarYRecibirRespuesta(comando);
    }
    /**
     * Envía un comando al servidor y recibe la respuesta.
     * 
     * @param comando Comando a enviar
     * @throws IOException Si hay error de comunicación
     */
    private void enviarYRecibirRespuesta(String comando) throws IOException {
        salida.println(comando);
        String respuesta = entrada.readLine();
        if (respuesta != null) {
            if (respuesta.startsWith(ProtocoloBioGuard.OK)) {
                System.out.println("✓ " + respuesta.substring(ProtocoloBioGuard.OK.length() + 1));
            } else if (respuesta.startsWith(ProtocoloBioGuard.ERROR)) {
                System.err.println("✗ " + respuesta.substring(ProtocoloBioGuard.ERROR.length() + 1));
            } else {
                System.out.println(respuesta);
            }
        }
        System.out.println();
    }
    /**
     * Desconecta el cliente del servidor.
     */
    private void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("\nDesconectado del servidor BioGuard");
        } catch (IOException e) {
            System.err.println("Error al desconectar: " + e.getMessage());
        }
    }
    /**
     * Punto de entrada principal del cliente.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ClienteBioGuard cliente = new ClienteBioGuard();
        cliente.iniciar();
    }
}
