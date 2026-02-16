package org.bioguard.service;
import org.bioguard.model.Virus;
import org.bioguard.repository.VirusRepository;
import org.bioguard.exception.VirusException;
import org.bioguard.exception.SecuenciaInvalidaException;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.FASTAParser;
import java.util.List;
import java.util.Optional;
/**
 * Servicio de negocio para gestionar operaciones de virus.
 * Maneja la carga, validación y consulta de virus en el sistema.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class VirusService {
    /**
     * Carga un virus desde un archivo FASTA.
     * Valida que la secuencia sea correcta antes de almacenar.
     * 
     * @param rutaArchivoFASTA Ruta del archivo FASTA del virus
     * @throws VirusException Si hay error en la validación del virus
     * @throws SecuenciaInvalidaException Si la secuencia genética es inválida
     * @throws PersistenciaException Si hay error en la persistencia
     */
    public static void cargarVirusDesdeArchivo(String rutaArchivoFASTA)
            throws VirusException, SecuenciaInvalidaException, PersistenciaException {
        try {
            Virus virus = FASTAParser.parsearVirusFromFASTA(rutaArchivoFASTA);
            validarVirus(virus);
            VirusRepository.guardar(virus);
        } catch (SecuenciaInvalidaException | PersistenciaException e) {
            throw e;
        } catch (Exception e) {
            throw new VirusException("Error al cargar virus desde archivo: " + e.getMessage());
        }
    }
    /**
     * Obtiene un virus específico por su nombre.
     * 
     * @param nombreVirus Nombre del virus
     * @return Optional con el virus si existe
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<Virus> obtenerVirus(String nombreVirus) throws PersistenciaException {
        return VirusRepository.obtener(nombreVirus);
    }
    /**
     * Obtiene la lista de todos los virus registrados en el sistema.
     * 
     * @return Lista de virus
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<Virus> obtenerTodosVirus() throws PersistenciaException {
        return VirusRepository.obtenerTodos();
    }
    /**
     * Valida que un virus tenga datos correctos.
     * 
     * @param virus Virus a validar
     * @throws VirusException Si el virus es inválido
     */
    private static void validarVirus(Virus virus) throws VirusException {
        if (virus.getNombre() == null || virus.getNombre().trim().isEmpty()) {
            throw new VirusException("El nombre del virus no puede estar vacío");
        }
        if (virus.getSecuencia() == null || virus.getSecuencia().isEmpty()) {
            throw new VirusException("La secuencia del virus no puede estar vacía");
        }
        if (virus.getInfecciosidad() == null) {
            throw new VirusException("El nivel de infecciosidad del virus no puede ser nulo");
        }
    }
    /**
     * Verifica si un virus existe en el sistema.
     * 
     * @param nombreVirus Nombre del virus
     * @return true si existe, false en caso contrario
     */
    public static boolean existeVirus(String nombreVirus) {
        return VirusRepository.existe(nombreVirus);
    }
}
