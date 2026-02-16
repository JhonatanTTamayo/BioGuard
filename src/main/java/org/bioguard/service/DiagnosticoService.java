package org.bioguard.service;
import org.bioguard.model.MuestraDNA;
import org.bioguard.model.Virus;
import org.bioguard.model.Paciente;
import org.bioguard.model.ResultadoDiagnostico;
import org.bioguard.model.NivelInfecciosidad;
import org.bioguard.repository.DiagnosticoRepository;
import org.bioguard.repository.MuestraRepository;
import org.bioguard.repository.VirusRepository;
import org.bioguard.repository.PacienteRepository;
import org.bioguard.exception.DiagnosticoException;
import org.bioguard.exception.PersistenciaException;
import org.bioguard.util.AlgoritmoComparacion;
import org.bioguard.util.CSVParser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * Servicio de negocio para gestionar diagnósticos de virus en muestras de ADN.
 * Implementa la lógica de búsqueda de virus, detección de mutaciones y generación de reportes.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class DiagnosticoService {
    /**
     * Procesa una muestra de ADN y detecta los virus presentes en ella.
     * Genera un reporte de diagnóstico y lo almacena.
     * 
     * @param documento Documento del paciente
     * @param fecha Fecha de la muestra
     * @return Lista de virus detectados con sus posiciones
     * @throws DiagnosticoException Si hay error en el procesamiento
     * @throws PersistenciaException Si hay error en la persistencia
     */
    public static List<ResultadoDiagnostico> procesarMuestra(String documento, String fecha)
            throws DiagnosticoException, PersistenciaException {
        try {
            // Obtener la muestra
            Optional<MuestraDNA> muestraOpt = MuestraRepository.obtener(documento, fecha);
            if (!muestraOpt.isPresent()) {
                throw new DiagnosticoException("Muestra no encontrada para el paciente: " + documento);
            }
            MuestraDNA muestra = muestraOpt.get();
            List<ResultadoDiagnostico> resultados = new ArrayList<>();
            // Obtener todos los virus
            List<Virus> virus = VirusRepository.obtenerTodos();
            // Detectar cada virus en la muestra
            for (Virus v : virus) {
                List<Integer> posiciones = AlgoritmoComparacion.buscarSubcadena(
                        muestra.getSecuencia(), v.getSecuencia());
                for (int pos : posiciones) {
                    int posFin = pos + v.getSecuencia().length() - 1;
                    resultados.add(new ResultadoDiagnostico(v.getNombre(), pos, posFin));
                }
            }
            // Guardar el diagnóstico
            DiagnosticoRepository.guardarDiagnostico(documento, fecha, resultados);
            return resultados;
        } catch (PersistenciaException e) {
            throw e;
        } catch (Exception e) {
            throw new DiagnosticoException("Error al procesar muestra: " + e.getMessage());
        }
    }
    /**
     * Detecta mutaciones comparando la muestra actual con las anteriores.
     * 
     * @param documento Documento del paciente
     * @param fechaActual Fecha de la muestra actual
     * @return Mapa de fecha_anterior -> lista de rangos de diferencias
     * @throws DiagnosticoException Si hay error en la comparación
     * @throws PersistenciaException Si hay error en la lectura
     */
    public static Map<String, List<int[]>> detectarMutaciones(String documento, String fechaActual)
            throws DiagnosticoException, PersistenciaException {
        try {
            Map<String, List<int[]>> mutaciones = new HashMap<>();
            Optional<MuestraDNA> muestraActualOpt = MuestraRepository.obtener(documento, fechaActual);
            if (!muestraActualOpt.isPresent()) {
                throw new DiagnosticoException("Muestra actual no encontrada");
            }
            MuestraDNA muestraActual = muestraActualOpt.get();
            List<MuestraDNA> historico = MuestraRepository.obtenerHistorico(documento);
            // Comparar con todas las muestras anteriores
            for (MuestraDNA muestraAnterior : historico) {
                if (!muestraAnterior.getFecha().equals(fechaActual)) {
                    List<int[]> diferencias = AlgoritmoComparacion.encontrarDiferencias(
                            muestraAnterior.getSecuencia(), muestraActual.getSecuencia());
                    if (!diferencias.isEmpty()) {
                        mutaciones.put(muestraAnterior.getFecha(), diferencias);
                    }
                }
            }
            return mutaciones;
        } catch (PersistenciaException e) {
            throw e;
        } catch (Exception e) {
            throw new DiagnosticoException("Error al detectar mutaciones: " + e.getMessage());
        }
    }
    /**
     * Genera un reporte de pacientes de alto riesgo.
     * Incluye aquellos con más de 3 virus altamente infecciosos.
     * 
     * @param rutaArchivoSalida Ruta del archivo donde guardar el reporte
     * @throws DiagnosticoException Si hay error en la generación del reporte
     * @throws PersistenciaException Si hay error en la lectura de datos
     */
    public static void generarReporteAltaRiesgo(String rutaArchivoSalida)
            throws DiagnosticoException, PersistenciaException {
        try {
            List<Paciente> pacientes = PacienteRepository.obtenerTodos();
            StringBuilder reporte = new StringBuilder();
            reporte.append("documento,cantidad_virus_totales,cantidad_altamente_infecciosos,")
                   .append("virus_bajo_riesgo,virus_alto_riesgo\n");
            for (Paciente paciente : pacientes) {
                Map<String, List<ResultadoDiagnostico>> diagnosticos = 
                        DiagnosticoRepository.obtenerDiagnosticosPaciente(paciente.getDocumento());
                Map<String, Integer> virusDetectados = new HashMap<>();
                Map<String, NivelInfecciosidad> virusNiveles = new HashMap<>();
                // Procesar todos los diagnósticos del paciente
                for (List<ResultadoDiagnostico> resultados : diagnosticos.values()) {
                    for (ResultadoDiagnostico resultado : resultados) {
                        virusDetectados.put(resultado.getVirusDetectado(),
                                virusDetectados.getOrDefault(resultado.getVirusDetectado(), 0) + 1);
                        // Obtener nivel de infecciosidad
                        try {
                            Optional<Virus> virusOpt = VirusRepository.obtener(resultado.getVirusDetectado());
                            if (virusOpt.isPresent()) {
                                virusNiveles.put(resultado.getVirusDetectado(),
                                        virusOpt.get().getInfecciosidad());
                            }
                        } catch (Exception e) {
                            // Continuar si no se encuentra el virus
                        }
                    }
                }
                // Contar virus altamente infecciosos
                long cantidadAltaRiesgo = virusNiveles.entrySet().stream()
                        .filter(e -> e.getValue() == NivelInfecciosidad.ALTAMENTE_INFECCIOSO)
                        .count();
                // Si hay más de 3 virus altamente infecciosos, incluir en reporte
                if (cantidadAltaRiesgo >= 3) {
                    List<String> virusBajoRiesgo = new ArrayList<>();
                    List<String> virusAltoRiesgo = new ArrayList<>();
                    virusNiveles.forEach((nombre, nivel) -> {
                        if (nivel == NivelInfecciosidad.ALTAMENTE_INFECCIOSO) {
                            virusAltoRiesgo.add(nombre);
                        } else {
                            virusBajoRiesgo.add(nombre);
                        }
                    });
                    reporte.append(CSVParser.construirLinea(new String[]{
                            paciente.getDocumento(),
                            String.valueOf(virusDetectados.size()),
                            String.valueOf(cantidadAltaRiesgo),
                            virusBajoRiesgo.toString(),
                            virusAltoRiesgo.toString()
                    })).append("\n");
                }
            }
            // Guardar reporte
            try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivoSalida))) {
                writer.print(reporte.toString());
            }
        } catch (IOException e) {
            throw new DiagnosticoException("Error al guardar reporte de alto riesgo", e);
        } catch (PersistenciaException e) {
            throw e;
        } catch (Exception e) {
            throw new DiagnosticoException("Error al generar reporte de alto riesgo: " + e.getMessage());
        }
    }
}
