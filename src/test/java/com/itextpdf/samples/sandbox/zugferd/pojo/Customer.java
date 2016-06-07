/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Part of a set of classes based on a sample database.
 */
package com.itextpdf.samples.sandbox.zugferd.pojo;

/**
 * Plain Old Java Object containing info about a Customer.
 *
 * @author Bruno Lowagie (iText Software)
 */
public class Customer {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String street;
    protected String postalcode;
    protected String city;
    protected String countryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\n");
        sb.append("    First Name: ").append(firstName).append("\n");
        sb.append("    Last Name: ").append(lastName).append("\n");
        sb.append("    Street: ").append(street).append("\n");
        sb.append("    City: ").append(countryId).append(" ").append(postalcode).append(" ").append(city);
        return sb.toString();
    }
}
