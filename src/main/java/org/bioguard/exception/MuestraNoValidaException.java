package org.bioguard.exception;
/**
 * Excepción lanzada cuando una muestra de ADN no es válida o no cumple los requisitos.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class MuestraNoValidaException extends DiagnosticoException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public MuestraNoValidaException(String mensaje) {
        super("Muestra de ADN no válida: " + mensaje);
    }
}
