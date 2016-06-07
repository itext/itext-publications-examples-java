/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Part of a set of classes based on a sample database.
 */
package com.itextpdf.samples.sandbox.zugferd.pojo;

/**
 * Plain Old Java Object containing info about an Item.
 *
 * @author Bruno Lowagie (iText Software)
 */
public class Item {
    protected int item;
    protected Product product;
    protected int quantity;
    protected double cost;

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  #").append(item);
        sb.append(product.toString());
        sb.append("\tQuantity: ").append(quantity);
        sb.append("\tCost: ").append(cost).append("\u20ac");
        return sb.toString();
    }
}
