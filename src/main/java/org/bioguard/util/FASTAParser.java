package org.bioguard.util;
import org.bioguard.model.MuestraDNA;
import org.bioguard.model.NivelInfecciosidad;
import org.bioguard.model.Virus;
import org.bioguard.exception.SecuenciaInvalidaException;
import org.bioguard.exception.PersistenciaException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/**
 * Parser para archivos en formato FASTA.
 * Permite leer y parsear archivos que contienen información genética.
 * 
 * @author Jhonatan
 * @version 1.0
 */
public class FASTAParser {
    private static final String CARACTERES_VALIDOS = "ATCG";
    /**
     * Parsea un archivo FASTA para cargar un virus.
     * Formato esperado:
     * >nombre_virus|nivel_infecciosidad
     * SECUENCIA_ATCG
     * 
     * @param rutaArchivo Ruta del archivo FASTA
     * @return Objeto Virus con la información extraída
     * @throws PersistenciaException Si hay error al leer el archivo
     * @throws SecuenciaInvalidaException Si la secuencia es inválida
     */
    public static Virus parsearVirusFromFASTA(String rutaArchivo) 
            throws PersistenciaException, SecuenciaInvalidaException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            String header = null;
            StringBuilder secuencia = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                if (linea.startsWith(">")) {
                    header = linea.substring(1); // Remover el >
                } else {
                    secuencia.append(linea);
                }
            }
            if (header == null || secuencia.length() == 0) {
                throw new PersistenciaException("Archivo FASTA inválido: formato incorrecto");
            }
            return crearVirusDesdeHeader(header, secuencia.toString());
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer archivo FASTA: " + rutaArchivo, e);
        }
    }
    /**
     * Parsea un archivo FASTA para obtener una muestra de ADN.
     * Formato esperado:
     * >documento|fecha
     * SECUENCIA_ATCG
     * 
     * @param rutaArchivo Ruta del archivo FASTA
     * @return Objeto MuestraDNA con la información extraída
     * @throws PersistenciaException Si hay error al leer el archivo
     * @throws SecuenciaInvalidaException Si la secuencia es inválida
     */
    public static MuestraDNA parsearMuestraFromFASTA(String rutaArchivo) 
            throws PersistenciaException, SecuenciaInvalidaException {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            String header = null;
            StringBuilder secuencia = new StringBuilder();
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                if (linea.startsWith(">")) {
                    header = linea.substring(1);
                } else {
                    secuencia.append(linea);
                }
            }
            if (header == null || secuencia.length() == 0) {
                throw new PersistenciaException("Archivo FASTA inválido: formato incorrecto");
            }
            return crearMuestraDesdeHeader(header, secuencia.toString());
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer archivo FASTA: " + rutaArchivo, e);
        }
    }
    /**
     * Valida que una secuencia genética contenga solo caracteres válidos (ATCG).
     * 
     * @param secuencia La secuencia a validar
     * @throws SecuenciaInvalidaException Si la secuencia contiene caracteres inválidos
     */
    public static void validarSecuencia(String secuencia) throws SecuenciaInvalidaException {
        if (secuencia == null || secuencia.isEmpty()) {
            throw new SecuenciaInvalidaException("La secuencia no puede estar vacía");
        }
        for (char c : secuencia.toUpperCase().toCharArray()) {
            if (CARACTERES_VALIDOS.indexOf(c) == -1) {
                throw new SecuenciaInvalidaException("Carácter inválido: " + c);
            }
        }
    }
    /**
     * Crea un objeto Virus a partir del header y secuencia.
     * 
     * @param header Header del formato FASTA (nombre|nivel)
     * @param secuencia La secuencia genética
     * @return Objeto Virus
     * @throws SecuenciaInvalidaException Si hay error en la validación
     */
    private static Virus crearVirusDesdeHeader(String header, String secuencia) 
            throws SecuenciaInvalidaException {
        String[] partes = header.split("\\|");
        if (partes.length != 2) {
            throw new SecuenciaInvalidaException("Header de virus inválido: " + header);
        }
        String nombre = partes[0].trim();
        String nivelStr = partes[1].trim();
        validarSecuencia(secuencia);
        try {
            NivelInfecciosidad nivel = NivelInfecciosidad.fromString(nivelStr);
            return new Virus(nombre, secuencia.toUpperCase(), nivel);
        } catch (IllegalArgumentException e) {
            throw new SecuenciaInvalidaException("Nivel de infecciosidad inválido: " + nivelStr);
        }
    }
    /**
     * Crea un objeto MuestraDNA a partir del header y secuencia.
     * 
     * @param header Header del formato FASTA (documento|fecha)
     * @param secuencia La secuencia genética
     * @return Objeto MuestraDNA
     * @throws SecuenciaInvalidaException Si hay error en la validación
     */
    private static MuestraDNA crearMuestraDesdeHeader(String header, String secuencia) 
            throws SecuenciaInvalidaException {
        String[] partes = header.split("\\|");
        if (partes.length != 2) {
            throw new SecuenciaInvalidaException("Header de muestra inválido: " + header);
        }
        String documento = partes[0].trim();
        String fecha = partes[1].trim();
        validarSecuencia(secuencia);
        return new MuestraDNA(documento, fecha, secuencia.toUpperCase());
    }
}
