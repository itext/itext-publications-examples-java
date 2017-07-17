/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
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
	
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");        
    	File file = new File(DEST);
    	file.getParentFile().mkdirs();
        E08_QrCode app = new E08_QrCode();
        app.createPdf(SRC, DEST);
    }
    
    public void createPdf(String src, String dest) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties
        	.setBaseUri(new File(src).getParentFile().getAbsolutePath())
        	.setCssApplierFactory(new QRCodeTagCssApplierFactory())
        	.setTagWorkerFactory(new QRCodeTagWorkerFactory());
        HtmlConverter.convertToPdf(new FileInputStream(src), new FileOutputStream(dest), converterProperties);
    }
}
