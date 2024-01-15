/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensing.base.LicenseKey;

/**
 * Converts an HTML file with special entities to a PDF.
 */
public class C07E12_SpecialCharacters {

    /**
     * An HTML file as a String value.
     */
    public static final String HTML = "<html><head></head>" +
            "<body style=\"font-size:12.0pt; font-family:Arial\">" +
            "<p>Special symbols: " +
            "&larr;  &darr; &harr; &uarr; &rarr; &euro; &copy; &#9786;</p>" +
            "</body></html>";

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/special_characters.pdf";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
				+ "/itextkey-html2pdf_typography.json")) {
			LicenseKey.loadLicenseFile(license);
		}
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new C07E12_SpecialCharacters().createPdf(HTML, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param html the source HTML file as a String value.
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String html, String dest) throws IOException {
        HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
    }
}
