package com.unizar.wineapp;

import java.io.Serializable;

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

    public short getId(){
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getPoints() {
        return points;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){return description;}
    public String getProvince(){return province;}
    public String getTaster(){return taster_name;}

    public String toString() {
        return "ID: '" + this.id + "' Nombre: '" + title + "' Country: '" + this.country + "', Description: '" + this.description + "', Designation: '" + this.designation + " Precio:" +this.price+ " ";
    }

}
