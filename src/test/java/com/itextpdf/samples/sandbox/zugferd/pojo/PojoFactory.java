/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * Part of a set of classes based on a sample database.
 */
package com.itextpdf.samples.sandbox.zugferd.pojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Factory that creates Invoice, Customer, Product, and Item classes.
 *
 * @author Bruno Lowagie (iText Software)
 */
public class PojoFactory {

    protected static PojoFactory factory = null;
    protected Connection connection;
    protected HashMap<Integer, Customer> customerCache = new HashMap<Integer, Customer>();
    protected HashMap<Integer, Product> productCache = new HashMap<Integer, Product>();
    protected PreparedStatement getCustomer;
    protected PreparedStatement getProduct;
    protected PreparedStatement getItems;

    private PojoFactory() throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection(
                "jdbc:hsqldb:src/test/resources/zugferd/db/invoices", "SA", "");
        getCustomer = connection.prepareStatement("SELECT * FROM Customer WHERE id = ?");
        getProduct = connection.prepareStatement("SELECT * FROM Product WHERE id = ?");
        getItems = connection.prepareStatement("SELECT * FROM Item WHERE invoiceid = ?");
    }

    public static PojoFactory getInstance() throws SQLException {
        if (factory == null || factory.connection.isClosed()) {
            try {
                factory = new PojoFactory();
            } catch (ClassNotFoundException cnfe) {
                throw new SQLException(cnfe.getMessage());
            }
        }
        return factory;
    }

    public void close() throws SQLException {
        connection.close();
    }

    public List<Invoice> getInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<Invoice>();
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM Invoice");
        while (rs.next()) {
            invoices.add(getInvoice(rs));
        }
        stm.close();
        return invoices;
    }

    public Invoice getInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("id"));
        invoice.setCustomer(getCustomer(rs.getInt("customerid")));
        List<Item> items = getItems(rs.getInt("id"));
        invoice.setItems(items);
        double total = 0;
        for (Item item : items)
            total += item.getCost();
        invoice.setTotal(total);
        invoice.setInvoiceDate(rs.getDate("invoicedate"));
        return invoice;
    }

    public Item getItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItem(rs.getInt("Item"));
        Product product = getProduct(rs.getInt("ProductId"));
        item.setProduct(product);
        item.setQuantity(rs.getInt("Quantity"));
        item.setCost(item.getQuantity() * product.getPrice());
        return item;
    }

    public Customer getCustomer(int id) throws SQLException {
        if (customerCache.containsKey(id))
            return customerCache.get(id);
        getCustomer.setInt(1, id);
        ResultSet rs = getCustomer.executeQuery();
        if (rs.next()) {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setFirstName(rs.getString("FirstName"));
            customer.setLastName(rs.getString("LastName"));
            customer.setStreet(rs.getString("Street"));
            customer.setPostalcode(rs.getString("Postalcode"));
            customer.setCity(rs.getString("City"));
            customer.setCountryId(rs.getString("CountryID"));
            customerCache.put(id, customer);
            return customer;
        }
        return null;
    }

    public Product getProduct(int id) throws SQLException {
        if (productCache.containsKey(id))
            return productCache.get(id);
        getProduct.setInt(1, id);
        ResultSet rs = getProduct.executeQuery();
        if (rs.next()) {
            Product product = new Product();
            product.setId(id);
            product.setName(rs.getString("Name"));
            product.setPrice(rs.getDouble("Price"));
            product.setVat(rs.getDouble("Vat"));
            productCache.put(id, product);
            return product;
        }
        return null;
    }

    public List<Item> getItems(int invoiceid) throws SQLException {
        List items = new ArrayList<Item>();
        getItems.setInt(1, invoiceid);
        ResultSet rs = getItems.executeQuery();
        while (rs.next()) {
            items.add(getItem(rs));
        }
        return items;
    }
}
