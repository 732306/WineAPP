package com.unizar.wineapp;

/**
 * Clase Variedad.
 * Esta clase  define objetos Variedad, contiene el nombre de la variedad y
 * la cantidad de vinos de dicha variedad.
 *
 * @author: Alejandro y Alberto
 */
public class Variedad {
    private String nombre;
    private int cantidad;

    /**
     * Constructor
     * @param nombre
     * @param cantidad
     */
    public Variedad (String nombre,int cantidad ){
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    /**
     * Constructor
     * @param nombre
     */
    public Variedad (String nombre){
        this(nombre,0);
    }

    /**
     *
     * @return String con nombre de la variedad
     */
    public String getNombre (){ return this.nombre; }

    /**
     * @return Entero con la cantidad de vinos de dicha variedad.
     */
    public int getCantidad(){ return this.cantidad;}

    /**
     * Método to string.
     * @return
     */
    public String toString() {
        return this.nombre + " (" + this.cantidad + ")";
    }

}
