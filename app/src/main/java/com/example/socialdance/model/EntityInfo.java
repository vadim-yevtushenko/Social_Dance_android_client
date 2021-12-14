package com.example.socialdance.model;


public class EntityInfo {
    private int id;
    private String country;
    private String city;
    private String street;
    private String building;
    private String suites;
    private String phoneNumber;
    private String email;

    public EntityInfo() {
    }

    public EntityInfo(String country, String city, String street, String building, String suites, String phoneNumber, String email) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.suites = suites;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public EntityInfo(int id, String country, String city, String street, String building, String suites, String phoneNumber, String email) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.suites = suites;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getCountry() {
        if (country == null){
            return "";
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        if (city == null){
            return "";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        if (street == null){
            return "";
        }
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null){
            return "";
        }
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        if (email == null){
            return "";
        }
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        if (building == null){
            return "";
        }
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getSuites() {
        if (suites == null){
            return "";
        }
        return suites;
    }

    public void setSuites(String suites) {
        this.suites = suites;
    }

    @Override
    public String toString() {
        return "EntityInfo{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", suites='" + suites + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
