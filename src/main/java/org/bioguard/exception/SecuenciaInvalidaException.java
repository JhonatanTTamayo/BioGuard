package org.bioguard.exception;
/**
 * Excepción lanzada cuando una secuencia genética es inválida.
 * Una secuencia válida solo debe contener los caracteres: A, T, C, G
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class SecuenciaInvalidaException extends VirusException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public SecuenciaInvalidaException(String mensaje) {
        super("Secuencia genética inválida: " + mensaje);
    }
}
