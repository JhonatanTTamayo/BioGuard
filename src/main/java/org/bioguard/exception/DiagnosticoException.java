package org.bioguard.exception;
/**
 * Excepción relacionada con operaciones de diagnóstico.
 * Se lanza cuando ocurren errores en el procesamiento de diagnósticos.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class DiagnosticoException extends BioGuardException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public DiagnosticoException(String mensaje) {
        super(mensaje);
    }
    /**
     * Constructor que acepta un mensaje y una causa.
     * 
     * @param mensaje Descripción del error
     * @param causa La excepción que causó este error
     */
    public DiagnosticoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
