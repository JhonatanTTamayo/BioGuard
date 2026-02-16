package org.example;

import org.bioguard.network.ServidorBioGuard;
import org.bioguard.network.ClienteBioGuard;

/**
 * Punto de entrada principal para el Sistema BioGuard.
 * Permite elegir entre ejecutar el servidor o el cliente.
 *
 * @author Jhonatan
 * @version 1.0
 */
public class Main {
    /**
     * Punto de entrada del programa.
     *
     * @param args Array de argumentos:
     *             - "servidor" para iniciar el servidor
     *             - "cliente" para iniciar el cliente
     *             - sin argumentos muestra el menÃº
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            mostrarMenu();
        } else if (args[0].equalsIgnoreCase("servidor")) {
            iniciarServidor();
        } else if (args[0].equalsIgnoreCase("cliente")) {
            iniciarCliente();
        } else {
            System.out.println("Argumento invÃ¡lido. Use: servidor o cliente");
        }
    }

    /**
     * Muestra el menÃº de selecciÃ³n.
     */
    private static void mostrarMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Sistema Distribuido de Vigilancia GenÃ³mica BioGuard");
        System.out.println("=".repeat(60));
        System.out.println("\nUso: java -cp target/classes org.example.Main [opciones]");
        System.out.println("\nOpciones:");
        System.out.println("  servidor   - Inicia el servidor BioGuard (puerto 5000)");
        System.out.println("  cliente    - Inicia el cliente BioGuard");
        System.out.println("\nEjemplos:");
        System.out.println("  java -cp target/classes org.example.Main servidor");
        System.out.println("  java -cp target/classes org.example.Main cliente");
        System.out.println("=".repeat(60) + "\n");
    }

    /**
     * Inicia el servidor BioGuard.
     */
    private static void iniciarServidor() {
        ServidorBioGuard servidor = new ServidorBioGuard();
        Runtime.getRuntime().addShutdownHook(new Thread(servidor::detener));
        try {
            servidor.iniciar();
        } catch (Exception e) {
            System.err.println("Error al iniciar servidor: " + e.getMessage());
        }
    }

    /**
     * Inicia el cliente BioGuard.
     */
    private static void iniciarCliente() {
        ClienteBioGuard cliente = new ClienteBioGuard();
        cliente.iniciar();
    }
}
