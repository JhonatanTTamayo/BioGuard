package org.bioguard.exception;
/**
 * Excepción lanzada cuando se intenta registrar un paciente con un documento que ya existe.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class DocumentoDuplicadoException extends PacienteException {
    /**
     * Constructor que acepta un mensaje de error.
     * 
     * @param documento El documento duplicado
     */
    public DocumentoDuplicadoException(String documento) {
        super("Documento duplicado: " + documento + " ya está registrado en el sistema.");
    }
}
