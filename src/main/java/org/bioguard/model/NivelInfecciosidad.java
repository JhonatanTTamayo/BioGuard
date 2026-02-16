package org.bioguard.model;
/**
 * Enumeración que define los niveles de infecciosidad de los virus.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public enum NivelInfecciosidad {
    POCO_INFECCIOSO("Poco Infeccioso"),
    NORMAL("Normal"),
    ALTAMENTE_INFECCIOSO("Altamente Infeccioso");
    private final String descripcion;
    NivelInfecciosidad(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * Obtiene la descripción del nivel de infecciosidad.
     * 
     * @return Descripción del nivel
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * Convierte una cadena a su correspondiente NivelInfecciosidad.
     * 
     * @param valor La cadena a convertir
     * @return El NivelInfecciosidad correspondiente
     * @throws IllegalArgumentException si el valor no corresponde a ningún nivel
     */
    public static NivelInfecciosidad fromString(String valor) {
        for (NivelInfecciosidad nivel : NivelInfecciosidad.values()) {
            if (nivel.descripcion.equalsIgnoreCase(valor)) {
                return nivel;
            }
        }
        throw new IllegalArgumentException("Nivel de infecciosidad inválido: " + valor);
    }
}
