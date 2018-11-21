package org.repair.dto;

public class ProjectDTO {

    private String clientName;
    private String clientPhone;
    private String street;
    private String streetNumber;

    public ProjectDTO() {
    }

    public ProjectDTO(String clientName, String clientPhone, String street, String streetNumber) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.street = street;
        this.streetNumber = streetNumber;
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
}
