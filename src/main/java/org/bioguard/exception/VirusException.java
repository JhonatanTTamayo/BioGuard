package org.bioguard.exception;
/**
 * Excepción relacionada con operaciones de virus.
 * Se lanza cuando ocurren errores en la gestión de virus.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class VirusException extends BioGuardException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public VirusException(String mensaje) {
        super(mensaje);
    }
    /**
     * Constructor que acepta un mensaje y una causa.
     * 
     * @param mensaje Descripción del error
     * @param causa La excepción que causó este error
     */
    public VirusException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
