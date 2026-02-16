package org.bioguard.service;
import org.bioguard.model.MuestraDNA;
import org.bioguard.repository.MuestraRepository;
import org.bioguard.exception.MuestraNoValidaException;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.FASTAParser;
import java.util.List;
import java.util.Optional;
/**
 * Servicio de negocio para gestionar muestras de ADN.
 * Maneja la carga, validación y procesamiento de muestras genéticas.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class MuestraDNAService {
    /**
     * Registra una nueva muestra de ADN para un paciente.
     * 
     * @param documento Documento del paciente
     * @param fecha Fecha de la muestra (formato YYYY-MM-DD)
     * @param secuencia Secuencia genética de la muestra
     * @throws MuestraNoValidaException Si la muestra es inválida
     * @throws PersistenciaException Si hay error en la persistencia
     */
    public static void registrarMuestra(String documento, String fecha, String secuencia)
            throws MuestraNoValidaException, PersistenciaException {
        validarMuestra(documento, fecha, secuencia);
        MuestraDNA muestra = new MuestraDNA(documento, fecha, secuencia);
        MuestraRepository.guardar(muestra);
    }
    /**
     * Carga una muestra desde un archivo FASTA.
     * 
     * @param rutaArchivoFASTA Ruta del archivo FASTA de la muestra
     * @throws MuestraNoValidaException Si hay error en la validación
     * @throws PersistenciaException Si hay error en la persistencia
     */
    public static void cargarMuestraDesdeArchivo(String rutaArchivoFASTA)
            throws MuestraNoValidaException, PersistenciaException {
        try {
            MuestraDNA muestra = FASTAParser.parsearMuestraFromFASTA(rutaArchivoFASTA);
            MuestraRepository.guardar(muestra);
        } catch (Exception e) {
            throw new MuestraNoValidaException("Error al cargar muestra: " + e.getMessage());
        }
    }
    /**
     * Obtiene una muestra específica de un paciente.
     * 
     * @param documento Documento del paciente
     * @param fecha Fecha de la muestra
     * @return Optional con la muestra si existe
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<MuestraDNA> obtenerMuestra(String documento, String fecha)
            throws PersistenciaException {
        return MuestraRepository.obtener(documento, fecha);
    }
    /**
     * Obtiene todas las muestras de un paciente (histórico).
     * 
     * @param documento Documento del paciente
     * @return Lista de muestras del paciente en orden cronológico
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<MuestraDNA> obtenerHistoricoMuestras(String documento)
            throws PersistenciaException {
        return MuestraRepository.obtenerHistorico(documento);
    }
    /**
     * Obtiene la muestra más reciente de un paciente.
     * 
     * @param documento Documento del paciente
     * @return Optional con la muestra más reciente
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<MuestraDNA> obtenerMuestraMasReciente(String documento)
            throws PersistenciaException {
        return MuestraRepository.obtenerMasReciente(documento);
    }
    /**
     * Valida que una muestra sea correcta.
     * 
     * @param documento Documento del paciente
     * @param fecha Fecha de la muestra
     * @param secuencia Secuencia genética
     * @throws MuestraNoValidaException Si la muestra es inválida
     */
    private static void validarMuestra(String documento, String fecha, String secuencia)
            throws MuestraNoValidaException {
        if (documento == null || documento.trim().isEmpty()) {
            throw new MuestraNoValidaException("El documento no puede estar vacío");
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new MuestraNoValidaException("La fecha no puede estar vacía");
        }
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new MuestraNoValidaException("La fecha debe estar en formato YYYY-MM-DD");
        }
        if (secuencia == null || secuencia.isEmpty()) {
            throw new MuestraNoValidaException("La secuencia no puede estar vacía");
        }
    }
}
