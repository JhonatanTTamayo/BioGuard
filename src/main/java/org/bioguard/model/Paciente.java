package org.bioguard.model;
/**
 * Representa un paciente en el sistema BioGuard.
 * Contiene la información personal de un paciente registrado.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class Paciente {
    private String documento;
    private String nombre;
    private String apellido;
    private int edad;
    private String correo;
    private String genero;
    private String ciudad;
    private String pais;
    /**
     * Constructor completo para crear un paciente con todos sus datos.
     * 
     * @param documento Documento de identificación único del paciente
     * @param nombre Nombre del paciente
     * @param apellido Apellido del paciente
     * @param edad Edad del paciente
     * @param correo Correo electrónico del paciente
     * @param genero Género del paciente
     * @param ciudad Ciudad de residencia del paciente
     * @param pais País de residencia del paciente
     */
    public Paciente(String documento, String nombre, String apellido, int edad,
                    String correo, String genero, String ciudad, String pais) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
        this.genero = genero;
        this.ciudad = ciudad;
        this.pais = pais;
    }
    // Getters
    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getEdad() { return edad; }
    public String getCorreo() { return correo; }
    public String getGenero() { return genero; }
    public String getCiudad() { return ciudad; }
    public String getPais() { return pais; }
    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setPais(String pais) { this.pais = pais; }
    /**
     * Convierte el paciente a formato CSV.
     * 
     * @return Línea CSV con los datos del paciente
     */
    @Override
    public String toString() {
        return String.join(",", documento, nombre, apellido, String.valueOf(edad),
                          correo, genero, ciudad, pais);
    }
}
