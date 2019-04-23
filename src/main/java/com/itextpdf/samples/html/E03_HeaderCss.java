/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

public class E03_HeaderCss {

	public static final String SRC = "src/main/resources/html/movie/3_header_css.html";
	public static final String DEST = "target/results/html/3_header_css.pdf";
	
    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
    	File file = new File(DEST);
    	file.getParentFile().mkdirs();
        E03_HeaderCss app = new E03_HeaderCss();
        app.createPdf(SRC, DEST);
    }
    
    /**
     * <p>createPdf.</p>
     *
     * @param src a {@link java.lang.String} object.
     * @param dest a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public void createPdf(String src, String dest) throws IOException {
		HtmlConverter.convertToPdf(new File(src), new File(dest));
    }
}
