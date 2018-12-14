package com.unizar.wineapp;

public class Vino {
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

    public String toString() {
        return "ID: '" + this.id + "' Country: '" + this.country + "', Description: '" + this.description + "', Designation: '" + this.designation + "'";
    }

}
