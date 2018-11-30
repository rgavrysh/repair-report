package org.repair.model;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private String city;
    private String street;
    private String streetNumber;
    private String apartment;
    private String postalCode;

    public Address() {
    }

    public Address(String city, String street, String streetNumber, String apartment, String postalCode) {
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.apartment = apartment;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", apartment=" + apartment +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                Objects.equals(street, address.street) &&
                Objects.equals(streetNumber, address.streetNumber) &&
                Objects.equals(apartment, address.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, streetNumber, apartment);
    }
}
