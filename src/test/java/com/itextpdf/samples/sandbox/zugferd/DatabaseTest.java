/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.zugferd;

import com.itextpdf.samples.sandbox.zugferd.pojo.Invoice;
import com.itextpdf.samples.sandbox.zugferd.pojo.PojoFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * A simple example to test the database
 */
public class DatabaseTest {
    public static void main(String[] args) throws SQLException {
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices)
            System.out.println(invoice.toString());
        factory.close();
    }
}
