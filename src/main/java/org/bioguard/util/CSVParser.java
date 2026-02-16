package org.bioguard.util;
import java.util.ArrayList;
import java.util.List;
/**
 * Parser para leer y escribir archivos en formato CSV.
 * Maneja escapado de comillas y comas dentro de campos.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class CSVParser {
    /**
     * Parsea una línea CSV y retorna un arreglo de campos.
     * Maneja campos con comillas y espacios.
     * 
     * @param linea Línea CSV a parsear
     * @return Arreglo de campos
     */
    public static String[] parsearLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoActual = new StringBuilder();
        boolean dentroDeLas = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '"') {
                dentroDeLas = !dentroDeLas;
            } else if (c == ',' && !dentroDeLas) {
                campos.add(campoActual.toString().trim());
                campoActual = new StringBuilder();
            } else {
                campoActual.append(c);
            }
        }
        // Agregar el último campo
        campos.add(campoActual.toString().trim());
        return campos.toArray(new String[0]);
    }
    /**
     * Construye una línea CSV a partir de un arreglo de campos.
     * Escapa comillas si es necesario.
     * 
     * @param campos Arreglo de campos
     * @return Línea CSV
     */
    public static String construirLinea(String[] campos) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            if (i > 0) resultado.append(",");
            String campo = campos[i];
            if (campo.contains(",") || campo.contains("\"") || campo.contains("\n")) {
                resultado.append("\"").append(escaparComillas(campo)).append("\"");
            } else {
                resultado.append(campo);
            }
        }
        return resultado.toString();
    }
    /**
     * Escapa comillas dobles en una cadena para formato CSV.
     * 
     * @param cadena Cadena a escapar
     * @return Cadena escapada
     */
    private static String escaparComillas(String cadena) {
        return cadena.replace("\"", "\"\"");
    }
}
