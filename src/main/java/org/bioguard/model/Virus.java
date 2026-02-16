package org.bioguard.model;
/**
 * Representa un virus en la base de datos del sistema BioGuard.
 * Almacena información sobre el virus y su secuencia genética.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class Virus {
    private String nombre;
    private String secuencia;
    private NivelInfecciosidad infecciosidad;
    /**
     * Constructor para crear un virus con todos sus datos.
     * 
     * @param nombre Nombre único del virus
     * @param secuencia Secuencia genética del virus (caracteres ATCG)
     * @param infecciosidad Nivel de infecciosidad del virus
     */
    public Virus(String nombre, String secuencia, NivelInfecciosidad infecciosidad) {
        this.nombre = nombre;
        this.secuencia = secuencia;
        this.infecciosidad = infecciosidad;
    }
    // Getters
    public String getNombre() { return nombre; }
    public String getSecuencia() { return secuencia; }
    public NivelInfecciosidad getInfecciosidad() { return infecciosidad; }
    // Setters
    public void setSecuencia(String secuencia) { this.secuencia = secuencia; }
    public void setInfecciosidad(NivelInfecciosidad infecciosidad) {
        this.infecciosidad = infecciosidad;
    }
    /**
     * Retorna una representación del virus en formato similar a FASTA.
     * 
     * @return Representación del virus
     */
    @Override
    public String toString() {
        return nombre + "|" + infecciosidad.getDescripcion() + "\n" + secuencia;
    }
}
