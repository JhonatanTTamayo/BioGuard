package org.bioguard.exception;
/**
 * Excepción lanzada cuando ocurren errores en la lectura o escritura de archivos.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class PersistenciaException extends BioGuardException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public PersistenciaException(String mensaje) {
        super("Error de persistencia: " + mensaje);
    }
    /**
     * Constructor que acepta un mensaje y una causa.
     * 
     * @param mensaje Descripción del error
     * @param causa La excepción que causó este error
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super("Error de persistencia: " + mensaje, causa);
    }
}
