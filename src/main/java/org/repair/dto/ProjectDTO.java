package org.repair.dto;

public class ProjectDTO {

    private String clientName;
    private String clientPhone;
    private String street;
    private String streetNumber;
    private String apartment;
    private String city;
    private String postal;
    private Double walls;
    private Double floor;
    private Double ceiling;
    private Double slopes;

    public ProjectDTO() {
    }

    public ProjectDTO(String clientName, String clientPhone, String street, String streetNumber) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public ProjectDTO(String clientName, String clientPhone, String street, String streetNumber,
                      String apartment, String city, String postal, Double walls, Double floor,
                      Double ceiling, Double slopes) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.street = street;
        this.streetNumber = streetNumber;
        this.apartment = apartment;
        this.city = city;
        this.postal = postal;
        this.walls = walls;
        this.floor = floor;
        this.ceiling = ceiling;
        this.slopes = slopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public Double getWalls() {
        return walls;
    }

    public void setWalls(Double walls) {
        this.walls = walls;
    }

    public Double getFloor() {
        return floor;
    }

    public void setFloor(Double floor) {
        this.floor = floor;
    }

    public Double getCeiling() {
        return ceiling;
    }

    public void setCeiling(Double ceiling) {
        this.ceiling = ceiling;
    }

    public Double getSlopes() {
        return slopes;
    }

    public void setSlopes(Double slopes) {
        this.slopes = slopes;
    }
}
