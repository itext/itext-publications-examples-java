/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Part of a set of classes based on a sample database.
 */
package com.itextpdf.samples.sandbox.zugferd.pojo;

import java.util.Date;
import java.util.List;

/**
 * Plain Old Java Object containing info about an Invoice.
 *
 * @author Bruno Lowagie (iText Software)
 */
public class Invoice {
    protected int id;
    protected Customer customer;
    protected double total;
    protected List<Item> items;
    protected Date invoiceDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice id: ").append(id).append(" Date: ").append(invoiceDate).append(" Total cost: ").append(total).append("\u20ac\n");
        sb.append("Customer: ").append(customer.toString()).append("\n");
        for (Item item : items) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}
