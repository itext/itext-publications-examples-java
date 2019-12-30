/*
 * Copyright 2016-2020, iText Group NV.
 * This example was created by Bruno Lowagie.
 * It was written in the context of the following book:
 * https://leanpub.com/itext7_pdfHTML
 * Go to http://developers.itextpdf.com for more info.
 */
package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts a simple HTML file to PDF using an InputStream and an OutputStream
 * as arguments for the convertToPdf() method.
 */
public class C07E04_CreateFromURL {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/url2pdf_1.pdf";

    /**
     * The target folder for the result.
     */
    public static final String ADDRESS = "https://stackoverflow.com/help/on-topic";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new C07E04_CreateFromURL().createPdf(new URL(ADDRESS), DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param url  the URL object for the web page
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(URL url, String dest) throws IOException {
        HtmlConverter.convertToPdf(url.openStream(), new FileOutputStream(dest));
    }
}