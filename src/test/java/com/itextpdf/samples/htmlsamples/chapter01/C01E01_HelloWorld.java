/*
 * Copyright 2016-2017, iText Group NV.
 * This example was created by Bruno Lowagie.
 * It was written in the context of the following book:
 * https://leanpub.com/itext7_pdfHTML
 * Go to http://developers.itextpdf.com for more info.
 */
package com.itextpdf.samples.htmlsamples.chapter01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts a simple Hello World HTML String to a PDF document.
 */
public class C01E01_HelloWorld {

	/** The HTML-string that we are going to convert to PDF. */
	public static final String HTML = "<h1>Test</h1><p>Hello World</p>";
	/** The target folder for the result. */
	public static final String TARGET = "target/results/ch01/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%stest-01.pdf", TARGET);
	
	/**
	 * The main method of this example.
	 *
	 * @param args no arguments are needed to run this example.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(TARGET);
        file.mkdirs();
        new C01E01_HelloWorld().createPdf(HTML, DEST);
    }
    
    /**
     * Creates the PDF file.
     *
     * @param html the HTML as a String value
     * @param dest the path of the resulting PDF
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String html, String dest) throws IOException {
        HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
    }
}
