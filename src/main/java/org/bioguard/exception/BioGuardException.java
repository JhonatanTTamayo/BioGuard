package org.bioguard.exception;
/**
 * Excepción raíz del sistema BioGuard.
 * Todas las excepciones personalizadas del sistema heredan de esta clase.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class BioGuardException extends Exception {
    /**
     * Constructor por defecto de BioGuardException.
     */
    public BioGuardException() {
        super();
    }
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public BioGuardException(String mensaje) {
        super(mensaje);
    }
    /**
     * Constructor que acepta un mensaje y una causa.
     * 
     * @param mensaje Descripción del error
     * @param causa La excepción que causó este error
     */
    public BioGuardException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    /**
     * Constructor que acepta solo la causa.
     * 
     * @param causa La excepción que causó este error
     */
    public BioGuardException(Throwable causa) {
        super(causa);
    }
}
