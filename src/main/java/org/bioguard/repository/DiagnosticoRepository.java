package org.bioguard.repository;
import org.bioguard.model.ResultadoDiagnostico;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.CSVParser;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * Repositorio para gestionar la persistencia de diagnósticos.
 * Los diagnósticos se almacenan en archivos CSV organizados por paciente.
 * Estructura: /data/diagnosticos/documento_paciente/diagnosticos.csv
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class DiagnosticoRepository {
    private static final String DIRECTORIO_DIAGNOSTICOS = "data/diagnosticos";
    private static final String HEADER_DIAGNOSTICO = "virus,posicion_inicio,posicion_fin";
    /**
     * Inicializa el repositorio creando el directorio si no existe.
     * 
     * @throws PersistenciaException Si hay error al crear el directorio
     */
    public static void inicializar() throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_DIAGNOSTICOS));
        } catch (IOException e) {
            throw new PersistenciaException("Error al inicializar repositorio de diagnósticos", e);
        }
    }
    /**
     * Guarda los resultados de un diagnóstico para un paciente.
     * 
     * @param documento Documento del paciente
     * @param fecha Fecha del diagnóstico
     * @param resultados Lista de resultados a guardar
     * @throws PersistenciaException Si hay error en la escritura
     */
    public static void guardarDiagnostico(String documento, String fecha, 
                                          List<ResultadoDiagnostico> resultados) 
            throws PersistenciaException {
        try {
            String directoriopaciente = DIRECTORIO_DIAGNOSTICOS + "/" + documento;
            Files.createDirectories(Paths.get(directoriopaciente));
            String rutaArchivo = directoriopaciente + "/" + fecha + "_diagnostico.csv";
            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
                writer.println(HEADER_DIAGNOSTICO);
                for (ResultadoDiagnostico resultado : resultados) {
                    writer.println(resultado.toString());
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar diagnóstico", e);
        }
    }
    /**
     * Obtiene todos los diagnósticos de un paciente.
     * 
     * @param documento Documento del paciente
     * @return Mapa de fecha -> lista de resultados de diagnóstico
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Map<String, List<ResultadoDiagnostico>> obtenerDiagnosticosPaciente(String documento) 
            throws PersistenciaException {
        Map<String, List<ResultadoDiagnostico>> diagnosticos = new HashMap<>();
        String directoriopaciente = DIRECTORIO_DIAGNOSTICOS + "/" + documento;
        File directorio = new File(directoriopaciente);
        if (!directorio.exists()) {
            return diagnosticos;
        }
        File[] archivos = directorio.listFiles((dir, name) -> name.endsWith("_diagnostico.csv"));
        if (archivos == null) {
            return diagnosticos;
        }
        for (File archivo : archivos) {
            try {
                String fecha = archivo.getName().replace("_diagnostico.csv", "");
                List<ResultadoDiagnostico> resultados = leerDiagnostico(archivo.getAbsolutePath());
                diagnosticos.put(fecha, resultados);
            } catch (IOException e) {
                System.err.println("Error al cargar diagnóstico: " + archivo.getName());
            }
        }
        return diagnosticos;
    }
    /**
     * Lee un archivo de diagnóstico específico.
     * 
     * @param rutaArchivo Ruta del archivo a leer
     * @return Lista de resultados de diagnóstico
     * @throws IOException Si hay error en la lectura
     */
    private static List<ResultadoDiagnostico> leerDiagnostico(String rutaArchivo) throws IOException {
        List<ResultadoDiagnostico> resultados = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            reader.readLine(); // Saltar header
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] campos = CSVParser.parsearLinea(linea);
                if (campos.length >= 3) {
                    ResultadoDiagnostico resultado = new ResultadoDiagnostico(
                            campos[0], Integer.parseInt(campos[1]), Integer.parseInt(campos[2])
                    );
                    resultados.add(resultado);
                }
            }
        }
        return resultados;
    }
}
