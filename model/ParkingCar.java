package com.seatgeek.placesautocompletedemo.model;

/**
 * Created by Arun Singh on 12/12/2016.
 */
public class ParkingCar {
    private String atmName;
    private Float distance;
    private String atmAddress;
    private double latitude;
    private double longitude;
    private int idLocation;
    private String  loc_Name;
    private String  loc_Address;
    private String  loc_Des;
    private String  loc_Open;
    private String  loc_Close;
    private String  loc_Phone;
    private int  loc_Type;
    private int  loc_Scale;
    private int  loc_Price;


    public String getLoc_Name() {
        return loc_Name;
    }

    public void setLoc_Name(String loc_Name) {
        this.loc_Name = loc_Name;
    }

    public String getLoc_Address() {
        return loc_Address;
    }

    public void setLoc_Address(String loc_Address) {
        this.loc_Address = loc_Address;
    }

    public String getLoc_Des() {
        return loc_Des;
    }

    public void setLoc_Des(String loc_Des) {
        this.loc_Des = loc_Des;
    }

    public String getLoc_Open() {
        return loc_Open;
    }

    public void setLoc_Open(String loc_Open) {
        this.loc_Open = loc_Open;
    }

    public String getLoc_Close() {
        return loc_Close;
    }

    public void setLoc_Close(String loc_Close) {
        this.loc_Close = loc_Close;
    }

    public String getLoc_Phone() {
        return loc_Phone;
    }

    public void setLoc_Phone(String loc_Phone) {
        this.loc_Phone = loc_Phone;
    }

    public int getLoc_Type() {
        return loc_Type;
    }

    public void setLoc_Type(int loc_Type) {
        this.loc_Type = loc_Type;
    }

    public int getLoc_Scale() {
        return loc_Scale;
    }

    public void setLoc_Scale(int loc_Scale) {
        this.loc_Scale = loc_Scale;
    }

    public int getLoc_Price() {
        return loc_Price;
    }

    public void setLoc_Price(int loc_Price) {
        this.loc_Price = loc_Price;
    }

    public int getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getAtmAddress() {
        return atmAddress;
    }

    public void setAtmAddress(String atmAddress) {
        this.atmAddress = atmAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
