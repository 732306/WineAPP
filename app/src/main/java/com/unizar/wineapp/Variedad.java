package com.unizar.wineapp;

public class Variedad {
    private String nombre;
    private int cantidad;

    public Variedad (String nombre,int cantidad ){
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Variedad (String nombre){
        this(nombre,0);
    }

    public String getNombre (){
        return this.nombre;
    }
    public int getCantidad(){ return this.cantidad;}

    public String toString() {
        return this.nombre + " (" + this.cantidad + ")";
    }

}
