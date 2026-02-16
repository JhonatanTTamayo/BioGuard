package org.bioguard.model;
/**
 * Representa una muestra de ADN enviada por un paciente para análisis.
 * Cada muestra contiene la secuencia genética del paciente en un momento específico.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class MuestraDNA {
    private String documento;
    private String fecha;
    private String secuencia;
    /**
     * Constructor para crear una muestra de ADN.
     * 
     * @param documento Documento del paciente propietario de la muestra
     * @param fecha Fecha de la muestra en formato YYYY-MM-DD
     * @param secuencia Secuencia genética de la muestra
     */
    public MuestraDNA(String documento, String fecha, String secuencia) {
        this.documento = documento;
        this.fecha = fecha;
        this.secuencia = secuencia;
    }
    // Getters
    public String getDocumento() { return documento; }
    public String getFecha() { return fecha; }
    public String getSecuencia() { return secuencia; }
    // Setters
    public void setSecuencia(String secuencia) { this.secuencia = secuencia; }
    /**
     * Obtiene el identificador único de la muestra.
     * Formato: documento|fecha
     * 
     * @return Identificador único de la muestra
     */
    public String getIdMuestra() {
        return documento + "|" + fecha;
    }
    /**
     * Convierte la muestra al formato FASTA.
     * 
     * @return Representación en formato FASTA
     */
    @Override
    public String toString() {
        return ">" + getIdMuestra() + "\n" + secuencia;
    }
}
