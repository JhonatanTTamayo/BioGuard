package org.bioguard.repository;
import org.bioguard.model.Virus;
import org.bioguard.model.NivelInfecciosidad;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.FASTAParser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * Repositorio para gestionar la persistencia de virus.
 * Los virus se almacenan en archivos FASTA individuales en el directorio /data/virus
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class VirusRepository {
    private static final String DIRECTORIO_VIRUS = "data/virus";
    /**
     * Inicializa el repositorio creando el directorio si no existe.
     * 
     * @throws PersistenciaException Si hay error al crear el directorio
     */
    public static void inicializar() throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_VIRUS));
        } catch (IOException e) {
            throw new PersistenciaException("Error al inicializar repositorio de virus", e);
        }
    }
    /**
     * Guarda un virus en un archivo FASTA.
     * El archivo se nombra como: nombreVirus.fasta
     * 
     * @param virus Virus a guardar
     * @throws PersistenciaException Si hay error en la escritura
     */
    public static void guardar(Virus virus) throws PersistenciaException {
        String rutaArchivo = DIRECTORIO_VIRUS + "/" + virus.getNombre() + ".fasta";
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            writer.println(">" + virus.getNombre() + "|" + virus.getInfecciosidad().getDescripcion());
            writer.println(virus.getSecuencia());
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar virus", e);
        }
    }
    /**
     * Obtiene un virus por su nombre.
     * 
     * @param nombreVirus Nombre del virus
     * @return Optional con el virus si existe
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<Virus> obtener(String nombreVirus) throws PersistenciaException {
        String rutaArchivo = DIRECTORIO_VIRUS + "/" + nombreVirus + ".fasta";
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return Optional.empty();
        }
        try {
            Virus virus = FASTAParser.parsearVirusFromFASTA(rutaArchivo);
            return Optional.of(virus);
        } catch (Exception e) {
            throw new PersistenciaException("Error al obtener virus: " + nombreVirus, e);
        }
    }
    /**
     * Obtiene todos los virus registrados.
     * 
     * @return Lista de todos los virus
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<Virus> obtenerTodos() throws PersistenciaException {
        List<Virus> virus = new ArrayList<>();
        File directorio = new File(DIRECTORIO_VIRUS);
        if (!directorio.exists()) {
            return virus;
        }
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith(".fasta"));
        if (archivos == null) {
            return virus;
        }
        for (File archivo : archivos) {
            try {
                Virus v = FASTAParser.parsearVirusFromFASTA(archivo.getAbsolutePath());
                virus.add(v);
            } catch (Exception e) {
                // Log del error pero continuar con los demás virus
                System.err.println("Error al cargar virus: " + archivo.getName());
            }
        }
        return virus;
    }
    /**
     * Elimina un virus del repositorio.
     * 
     * @param nombreVirus Nombre del virus a eliminar
     * @return true si se eliminó, false si no existía
     * @throws PersistenciaException Si hay error en la operación
     */
    public static boolean eliminar(String nombreVirus) throws PersistenciaException {
        String rutaArchivo = DIRECTORIO_VIRUS + "/" + nombreVirus + ".fasta";
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return false;
        }
        if (!archivo.delete()) {
            throw new PersistenciaException("No se pudo eliminar el archivo del virus: " + nombreVirus);
        }
        return true;
    }
    /**
     * Verifica si un virus existe.
     * 
     * @param nombreVirus Nombre del virus
     * @return true si existe, false en caso contrario
     */
    public static boolean existe(String nombreVirus) {
        String rutaArchivo = DIRECTORIO_VIRUS + "/" + nombreVirus + ".fasta";
        return new File(rutaArchivo).exists();
    }
}
