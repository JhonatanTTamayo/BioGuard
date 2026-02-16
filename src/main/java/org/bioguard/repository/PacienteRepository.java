package org.bioguard.repository;
import org.bioguard.model.Paciente;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.exception.DocumentoDuplicadoException;
import org.bioguard.util.CSVParser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * Repositorio para gestionar la persistencia de pacientes en archivo CSV.
 * Implementa operaciones CRUD sobre el archivo pacientes.csv
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class PacienteRepository {
    private static final String ARCHIVO_PACIENTES = "data/pacientes.csv";
    private static final String HEADER_CSV = "documento,nombre,apellido,edad,correo,genero,ciudad,pais";
    /**
     * Inicializa el repositorio creando el archivo si no existe.
     * 
     * @throws PersistenciaException Si hay error al crear el archivo
     */
    public static void inicializar() throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get("data"));
            File archivo = new File(ARCHIVO_PACIENTES);
            if (!archivo.exists()) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(archivo))) {
                    writer.println(HEADER_CSV);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al inicializar repositorio de pacientes", e);
        }
    }
    /**
     * Guarda un nuevo paciente en el archivo.
     * Valida que el documento no sea duplicado.
     * 
     * @param paciente Paciente a guardar
     * @throws PersistenciaException Si hay error en la escritura
     * @throws DocumentoDuplicadoException Si el documento ya existe
     */
    public static void guardar(Paciente paciente) 
            throws PersistenciaException, DocumentoDuplicadoException {
        if (existe(paciente.getDocumento())) {
            throw new DocumentoDuplicadoException(paciente.getDocumento());
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PACIENTES, true))) {
            writer.println(paciente.toString());
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar paciente", e);
        }
    }
    /**
     * Obtiene un paciente por su documento.
     * 
     * @param documento Documento del paciente
     * @return Optional con el paciente si existe
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Optional<Paciente> obtener(String documento) throws PersistenciaException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PACIENTES))) {
            String linea;
            reader.readLine(); // Saltar header
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = CSVParser.parsearLinea(linea);
                if (campos.length >= 8 && campos[0].equals(documento)) {
                    Paciente p = new Paciente(campos[0], campos[1], campos[2],
                            Integer.parseInt(campos[3]), campos[4], campos[5], campos[6], campos[7]);
                    return Optional.of(p);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al obtener paciente", e);
        }
        return Optional.empty();
    }
    /**
     * Verifica si un paciente existe por su documento.
     * 
     * @param documento Documento a verificar
     * @return true si existe, false en caso contrario
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static boolean existe(String documento) throws PersistenciaException {
        return obtener(documento).isPresent();
    }
    /**
     * Obtiene todos los pacientes registrados.
     * 
     * @return Lista de todos los pacientes
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static List<Paciente> obtenerTodos() throws PersistenciaException {
        List<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PACIENTES))) {
            String linea;
            reader.readLine(); // Saltar header
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = CSVParser.parsearLinea(linea);
                if (campos.length >= 8) {
                    Paciente p = new Paciente(campos[0], campos[1], campos[2],
                            Integer.parseInt(campos[3]), campos[4], campos[5], campos[6], campos[7]);
                    pacientes.add(p);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al obtener pacientes", e);
        }
        return pacientes;
    }
    /**
     * Actualiza los datos de un paciente existente.
     * 
     * @param paciente Paciente con datos actualizados
     * @throws PersistenciaException Si hay error en la actualización
     */
    public static void actualizar(Paciente paciente) throws PersistenciaException {
        List<Paciente> pacientes = obtenerTodos();
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_PACIENTES))) {
            writer.println(HEADER_CSV);
            for (Paciente p : pacientes) {
                if (p.getDocumento().equals(paciente.getDocumento())) {
                    writer.println(paciente.toString());
                } else {
                    writer.println(p.toString());
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al actualizar paciente", e);
        }
    }
}
