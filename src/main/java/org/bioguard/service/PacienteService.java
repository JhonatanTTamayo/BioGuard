package org.bioguard.service;
import org.bioguard.model.Paciente;
import org.bioguard.repository.PacienteRepository;
import org.bioguard.exception.PacienteException;
import org.bioguard.exception.DocumentoDuplicadoException;
import org.bioguard.exception.PersistenciaException;
import java.util.List;
import java.util.Optional;
/**
 * Servicio de negocio para gestionar operaciones de pacientes.
 * Implementa la lógica de validación y procesamiento de datos de pacientes.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class PacienteService {
    /**
     * Registra un nuevo paciente en el sistema.
     * Valida que no exista un paciente con el mismo documento.
     * 
     * @param documento Documento de identificación
     * @param nombre Nombre del paciente
     * @param apellido Apellido del paciente
     * @param edad Edad del paciente
     * @param correo Correo electrónico
     * @param genero Género del paciente
     * @param ciudad Ciudad de residencia
     * @param pais País de residencia
     * @throws PacienteException Si hay error en la validación
     * @throws DocumentoDuplicadoException Si el documento ya existe
     * @throws PersistenciaException Si hay error en la persistencia
     */
    public static void registrarPaciente(String documento, String nombre, String apellido,
                                        int edad, String correo, String genero,
                                        String ciudad, String pais)
            throws PacienteException, DocumentoDuplicadoException, PersistenciaException {
        // Validaciones
        validarDocumento(documento);
        validarNombre(nombre);
        validarApellido(apellido);
        validarEdad(edad);
        validarCorreo(correo);
        Paciente paciente = new Paciente(documento, nombre, apellido, edad, correo, genero, ciudad, pais);
        PacienteRepository.guardar(paciente);
    }
    /**
     * Obtiene la información de un paciente por su documento.
     * 
     * @param documento Documento del paciente
     * @return Optional con el paciente si existe
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<Paciente> obtenerPaciente(String documento) throws PersistenciaException {
        return PacienteRepository.obtener(documento);
    }
    /**
     * Verifica si un paciente existe en el sistema.
     * 
     * @param documento Documento a verificar
     * @return true si existe, false en caso contrario
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static boolean existePaciente(String documento) throws PersistenciaException {
        return PacienteRepository.existe(documento);
    }
    /**
     * Obtiene la lista de todos los pacientes registrados.
     * 
     * @return Lista de pacientes
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<Paciente> obtenerTodosPacientes() throws PersistenciaException {
        return PacienteRepository.obtenerTodos();
    }
    /**
     * Valida que el documento tenga un formato válido.
     * 
     * @param documento Documento a validar
     * @throws PacienteException Si el documento es inválido
     */
    private static void validarDocumento(String documento) throws PacienteException {
        if (documento == null || documento.trim().isEmpty()) {
            throw new PacienteException("El documento no puede estar vacío");
        }
        if (documento.length() < 5) {
            throw new PacienteException("El documento debe tener al menos 5 caracteres");
        }
    }
    /**
     * Valida que el nombre sea válido.
     * 
     * @param nombre Nombre a validar
     * @throws PacienteException Si el nombre es inválido
     */
    private static void validarNombre(String nombre) throws PacienteException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new PacienteException("El nombre no puede estar vacío");
        }
        if (nombre.length() < 2) {
            throw new PacienteException("El nombre debe tener al menos 2 caracteres");
        }
    }
    /**
     * Valida que el apellido sea válido.
     * 
     * @param apellido Apellido a validar
     * @throws PacienteException Si el apellido es inválido
     */
    private static void validarApellido(String apellido) throws PacienteException {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new PacienteException("El apellido no puede estar vacío");
        }
        if (apellido.length() < 2) {
            throw new PacienteException("El apellido debe tener al menos 2 caracteres");
        }
    }
    /**
     * Valida que la edad sea razonable.
     * 
     * @param edad Edad a validar
     * @throws PacienteException Si la edad es inválida
     */
    private static void validarEdad(int edad) throws PacienteException {
        if (edad < 0 || edad > 150) {
            throw new PacienteException("La edad debe estar entre 0 y 150 años");
        }
    }
    /**
     * Valida que el correo electrónico tenga un formato válido.
     * 
     * @param correo Correo a validar
     * @throws PacienteException Si el correo es inválido
     */
    private static void validarCorreo(String correo) throws PacienteException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new PacienteException("El correo no puede estar vacío");
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            throw new PacienteException("El correo debe tener un formato válido (ej: usuario@dominio.com)");
        }
    }
}
