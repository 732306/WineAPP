package com.unizar.wineapp;

import java.io.Serializable;

/**
 * Clase Vino.
 * Esta clase define objetos vino con la información obtenida de la BD.
 *
 * @author: Alejandro y Alberto
 */
public class Vino  implements Serializable {
    public short id;
    private String country;
    private String description;
    private String designation;
    private String points;
    private String price;
    private String province;
    private String region_1;
    private String region_2;
    private String taster_name;
    private String taster_twitter_handle;
    private String title;
    private String variety;
    private String winery;

    /**
     * @return Id del vino.
     */
    public short getId(){ return id; }

    /**
     * @return String con el país del vino.
     */
    public String getCountry() { return country; }

    /**
     * @return String con los puntos del vino.
     */
    public String getPoints() { return points; }

    /**
     * @return String con el precio del vino.
     */
    public String getPrice() { return price; }

    /**
     * @return String con el título del vino.
     */
    public String getTitle(){ return title; }

    /**
     * @return String con la descripción del vino.
     */
    public String getDescription(){return description;}

    /**
     * @return String con la provincia de origen del vino.
     */
    public String getProvince(){return province;}

    /**
     * @return Devuelve el nombre del Taster que opinó sobre el vino y le otorgó una puntuación
     */
    public String getTaster(){return taster_name;}

    /**
     * @return
     */
    public String toString() {
        return "ID: '" + this.id + "' Nombre: '" + title + "' Country: '" + this.country + "', Description: '" + this.description + "', Designation: '" + this.designation + " Precio:" +this.price+ " ";
    }

}
