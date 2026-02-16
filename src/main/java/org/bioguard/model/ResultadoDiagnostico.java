package org.bioguard.model;
/**
 * Representa el resultado del diagnóstico de un virus detectado en una muestra.
 * Incluye la posición exacta donde fue encontrado en la secuencia.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class ResultadoDiagnostico {
    private String virusDetectado;
    private int posicionInicio;
    private int posicionFin;
    /**
     * Constructor para crear un resultado de diagnóstico.
     * 
     * @param virusDetectado Nombre del virus detectado
     * @param posicionInicio Posición de inicio donde se encontró el virus
     * @param posicionFin Posición de fin donde se encontró el virus
     */
    public ResultadoDiagnostico(String virusDetectado, int posicionInicio, int posicionFin) {
        this.virusDetectado = virusDetectado;
        this.posicionInicio = posicionInicio;
        this.posicionFin = posicionFin;
    }
    // Getters
    public String getVirusDetectado() { return virusDetectado; }
    public int getPosicionInicio() { return posicionInicio; }
    public int getPosicionFin() { return posicionFin; }
    /**
     * Convierte el resultado al formato CSV.
     * Formato: virus,posicion_inicio,posicion_fin
     * 
     * @return Línea CSV del resultado
     */
    @Override
    public String toString() {
        return virusDetectado + "," + posicionInicio + "," + posicionFin;
    }
}
