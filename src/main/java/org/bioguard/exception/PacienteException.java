package org.bioguard.exception;
/**
 * Excepción relacionada con operaciones de pacientes.
 * Se lanza cuando ocurren errores en la gestión de pacientes.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class PacienteException extends BioGuardException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param mensaje Descripción del error
     */
    public PacienteException(String mensaje) {
        super(mensaje);
    }
    /**
     * Constructor que acepta un mensaje y una causa.
     * 
     * @param mensaje Descripción del error
     * @param causa La excepción que causó este error
     */
    public PacienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
