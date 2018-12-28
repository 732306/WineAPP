package com.unizar.wineapp;

public class Variedad {
    private String nombre;
    private int cantidad;

    public String getNombre (){
        return this.nombre;
    }
    public int getCantidad(){ return this.cantidad;}

    public String toString() {
        return this.nombre + " (" + this.cantidad + ")";
    }

}
