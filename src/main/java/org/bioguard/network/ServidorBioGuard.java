package org.bioguard.network;
import org.bioguard.repository.PacienteRepository;
import org.bioguard.repository.VirusRepository;
import org.bioguard.repository.MuestraRepository;
import org.bioguard.repository.DiagnosticoRepository;
import org.bioguard.exception.PersistenciaException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Servidor TCP/IP para el sistema BioGuard.
 * Acepta conexiones de clientes y delega el procesamiento a hilos de trabajo.
 * Utiliza un pool de hilos para manejar múltiples clientes concurrentemente.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class ServidorBioGuard {
    private static final int PUERTO = 5000;
    private static final int NUM_HILOS = 10;
    private ServerSocket serverSocket;
    private ExecutorService poolHilos;
    private boolean activo;
    /**
     * Constructor del servidor BioGuard.
     */
    public ServidorBioGuard() {
        this.activo = false;
    }
    /**
     * Inicia el servidor BioGuard.
     * Inicializa los repositorios y comienza a aceptar conexiones.
     * 
     * @throws IOException Si hay error al crear el socket del servidor
     * @throws PersistenciaException Si hay error al inicializar los repositorios
     */
    public void iniciar() throws IOException, PersistenciaException {
        // Inicializar repositorios
        PacienteRepository.inicializar();
        VirusRepository.inicializar();
        MuestraRepository.inicializar();
        DiagnosticoRepository.inicializar();
        // Crear pool de hilos
        poolHilos = Executors.newFixedThreadPool(NUM_HILOS);
        // Crear socket del servidor
        serverSocket = new ServerSocket(PUERTO);
        activo = true;
        System.out.println("=" .repeat(60));
        System.out.println("Sistema BioGuard - Servidor iniciado");
        System.out.println("Puerto: " + PUERTO);
        System.out.println("Hilos disponibles: " + NUM_HILOS);
        System.out.println("=" .repeat(60));
        System.out.println("Esperando conexiones de clientes...\n");
        // Aceptar conexiones
        aceptarConexiones();
    }
    /**
     * Loop principal que acepta conexiones de clientes.
     */
    private void aceptarConexiones() {
        try {
            while (activo) {
                Socket socketCliente = serverSocket.accept();
                System.out.println("[Servidor] Nueva conexión: " + socketCliente.getInetAddress());
                // Delegar el procesamiento a un hilo del pool
                ManejadorClienteThread manejador = new ManejadorClienteThread(socketCliente);
                poolHilos.execute(manejador);
            }
        } catch (IOException e) {
            if (activo) {
                System.err.println("Error al aceptar conexión: " + e.getMessage());
            }
        }
    }
    /**
     * Detiene el servidor y libera recursos.
     */
    public void detener() {
        try {
            activo = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (poolHilos != null) {
                poolHilos.shutdown();
            }
            System.out.println("\n[Servidor] Detenido correctamente");
        } catch (IOException e) {
            System.err.println("Error al detener servidor: " + e.getMessage());
        }
    }
    /**
     * Verifica si el servidor está activo.
     * 
     * @return true si está activo, false en caso contrario
     */
    public boolean estaActivo() {
        return activo;
    }
    /**
     * Punto de entrada principal del servidor.
     * 
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ServidorBioGuard servidor = new ServidorBioGuard();
        // Registrar hook para detener el servidor al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> servidor.detener()));
        try {
            servidor.iniciar();
        } catch (IOException e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        } catch (PersistenciaException e) {
            System.err.println("Error al inicializar repositorios: " + e.getMessage());
        }
    }
}
