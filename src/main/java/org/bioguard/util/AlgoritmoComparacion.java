package org.bioguard.util;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementa algoritmos de comparación y búsqueda de secuencias genéticas.
 * Utiliza búsqueda de subcadena para encontrar virus y detectar mutaciones.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class AlgoritmoComparacion {
    /**
     * Busca una subcadena (virus) dentro de una cadena (muestra).
     * Implementa búsqueda simple (naive string matching).
     * 
     * @param secuenciaCompleta Secuencia donde buscar (muestra de ADN)
     * @param subcadena Secuencia a buscar (virus)
     * @return Lista de posiciones iniciales donde se encontró la subcadena, o vacía si no se encontró
     */
    public static List<Integer> buscarSubcadena(String secuenciaCompleta, String subcadena) {
        List<Integer> posiciones = new ArrayList<>();
        if (subcadena == null || subcadena.isEmpty() || 
            secuenciaCompleta == null || secuenciaCompleta.isEmpty()) {
            return posiciones;
        }
        // Búsqueda simple (naive approach)
        for (int i = 0; i <= secuenciaCompleta.length() - subcadena.length(); i++) {
            if (secuenciaCompleta.substring(i, i + subcadena.length()).equals(subcadena)) {
                posiciones.add(i);
            }
        }
        return posiciones;
    }
    /**
     * Encuentra todas las diferencias entre dos secuencias de ADN.
     * Retorna los rangos donde existen cambios.
     * 
     * @param secuenciaAnterior Secuencia anterior (historico)
     * @param secuenciaActual Secuencia actual
     * @return Lista de pares [inicio, fin] donde hay diferencias
     */
    public static List<int[]> encontrarDiferencias(String secuenciaAnterior, String secuenciaActual) {
        List<int[]> diferencias = new ArrayList<>();
        if (secuenciaAnterior == null || secuenciaActual == null) {
            return diferencias;
        }
        int minLongitud = Math.min(secuenciaAnterior.length(), secuenciaActual.length());
        int inicio = -1;
        // Encontrar rangos de diferencias
        for (int i = 0; i < minLongitud; i++) {
            if (secuenciaAnterior.charAt(i) != secuenciaActual.charAt(i)) {
                if (inicio == -1) {
                    inicio = i;
                }
            } else {
                if (inicio != -1) {
                    diferencias.add(new int[]{inicio, i - 1});
                    inicio = -1;
                }
            }
        }
        // Si la diferencia llega al final
        if (inicio != -1) {
            diferencias.add(new int[]{inicio, minLongitud - 1});
        }
        // Si hay diferencia de longitud
        if (secuenciaAnterior.length() != secuenciaActual.length()) {
            int posicion = minLongitud;
            int maxLongitud = Math.max(secuenciaAnterior.length(), secuenciaActual.length());
            diferencias.add(new int[]{posicion, maxLongitud - 1});
        }
        return diferencias;
    }
    /**
     * Calcula la similaridad entre dos secuencias usando el algoritmo de distancia de Hamming.
     * Solo funciona con secuencias de igual longitud.
     * 
     * @param secuencia1 Primera secuencia
     * @param secuencia2 Segunda secuencia
     * @return Distancia de Hamming (número de diferencias)
     */
    public static int distanciaHamming(String secuencia1, String secuencia2) {
        if (secuencia1 == null || secuencia2 == null) {
            return 0;
        }
        int distancia = 0;
        int minLongitud = Math.min(secuencia1.length(), secuencia2.length());
        for (int i = 0; i < minLongitud; i++) {
            if (secuencia1.charAt(i) != secuencia2.charAt(i)) {
                distancia++;
            }
        }
        // Sumar la diferencia de longitud
        distancia += Math.abs(secuencia1.length() - secuencia2.length());
        return distancia;
    }
}
