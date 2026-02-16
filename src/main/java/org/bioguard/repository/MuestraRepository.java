package org.bioguard.repository;
import org.bioguard.model.MuestraDNA;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.FASTAParser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * Repositorio para gestionar la persistencia de muestras de ADN.
 * Las muestras se organizan en subdirectorios por paciente (usando su documento).
 * Estructura: /data/muestras/documento_paciente/fecha.fasta
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class MuestraRepository {
    private static final String DIRECTORIO_MUESTRAS = "data/muestras";
    /**
     * Inicializa el repositorio creando el directorio si no existe.
     * 
     * @throws PersistenciaException Si hay error al crear el directorio
     */
    public static void inicializar() throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_MUESTRAS));
        } catch (IOException e) {
            throw new PersistenciaException("Error al inicializar repositorio de muestras", e);
        }
    }
    /**
     * Guarda una muestra de ADN en el repositorio.
     * Se crea un subdirectorio por paciente si no existe.
     * 
     * @param muestra Muestra a guardar
     * @throws PersistenciaException Si hay error en la escritura
     */
    public static void guardar(MuestraDNA muestra) throws PersistenciaException {
        try {
            String directoriopaciente = DIRECTORIO_MUESTRAS + "/" + muestra.getDocumento();
            Files.createDirectories(Paths.get(directoriopaciente));
            String rutaArchivo = directoriopaciente + "/" + muestra.getFecha() + ".fasta";
            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
                writer.println(">" + muestra.getIdMuestra());
                writer.println(muestra.getSecuencia());
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar muestra", e);
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
    public static Optional<MuestraDNA> obtener(String documento, String fecha) 
            throws PersistenciaException {
        String rutaArchivo = DIRECTORIO_MUESTRAS + "/" + documento + "/" + fecha + ".fasta";
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return Optional.empty();
        }
        try {
            MuestraDNA muestra = FASTAParser.parsearMuestraFromFASTA(rutaArchivo);
            return Optional.of(muestra);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener muestra", e);
        }
    }
    /**
     * Obtiene todas las muestras de un paciente en orden cronológico.
     * 
     * @param documento Documento del paciente
     * @return Lista de muestras del paciente
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<MuestraDNA> obtenerHistorico(String documento) throws PersistenciaException {
        List<MuestraDNA> muestras = new ArrayList<>();
        String directoriopaciente = DIRECTORIO_MUESTRAS + "/" + documento;
        File directorio = new File(directoriopaciente);
        if (!directorio.exists()) {
            return muestras;
        }
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".fasta"));
        if (archivos == null) {
            return muestras;
        }
        // Ordenar archivos por nombre (fecha)
        java.util.Arrays.sort(archivos);
        for (File archivo : archivos) {
            try {
                MuestraDNA muestra = FASTAParser.parsearMuestraFromFASTA(archivo.getAbsolutePath());
                muestras.add(muestra);
            } catch (Exception e) {
                System.err.println("Error al cargar muestra: " + archivo.getName());
            }
        }
        return muestras;
    }
    /**
     * Obtiene la muestra más reciente de un paciente.
     * 
     * @param documento Documento del paciente
     * @return Optional con la muestra más reciente
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<MuestraDNA> obtenerMasReciente(String documento) 
            throws PersistenciaException {
        List<MuestraDNA> historico = obtenerHistorico(documento);
        if (historico.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(historico.get(historico.size() - 1));
    }
}
