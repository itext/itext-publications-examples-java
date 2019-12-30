/*
 * Copyright 2016-2017, iText Group NV.
 * This example was created by Bruno Lowagie.
 * It was written in the context of the following book:
 * https://leanpub.com/itext7_pdfHTML
 * Go to http://developers.itextpdf.com for more info.
 */
package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML file with special entities to a PDF.
 */
public class C07E12_SpecialCharacters {

	/** An HTML file as a String value. */
	public static final String HTML = "<html><head></head>" +
        "<body style=\"font-size:12.0pt; font-family:Arial\">" +
        "<p>Special symbols: " +
        "&larr;  &darr; &harr; &uarr; &rarr; &euro; &copy; &#9786;</p>" +
        "</body></html>";
	/** The target folder for the result. */
	public static final String TARGET = "target/results/ch07/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%sspecial_characters.pdf", TARGET);

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
        new C07E12_SpecialCharacters().createPdf(HTML, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String html, String dest) throws IOException {
        HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
    }
}
