/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.html.qrcode.QRCodeTagCssApplierFactory;
import com.itextpdf.samples.html.qrcode.QRCodeTagWorkerFactory;

public class E08_QrCode {

	public static final String SRC = "src/main/resources/html/tags/qrcode.html";
	public static final String DEST = "target/results/html/8_qrcode.pdf";
	
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
        E08_QrCode app = new E08_QrCode();
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
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties
        	.setBaseUri(new File(src).getParentFile().getAbsolutePath())
        	.setCssApplierFactory(new QRCodeTagCssApplierFactory())
        	.setTagWorkerFactory(new QRCodeTagWorkerFactory());
        try (FileInputStream fileInputStream = new FileInputStream(src); FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
            HtmlConverter.convertToPdf(fileInputStream, fileOutputStream, converterProperties);
        }
    }
}
