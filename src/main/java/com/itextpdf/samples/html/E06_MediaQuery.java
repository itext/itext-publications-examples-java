/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.itextpdf.styledxmlparser.css.util.CssUtils;

public class E06_MediaQuery {

	public static final String SRC = "src/main/resources/html/responsive/responsive.html";
	public static final String DEST = "target/results/html/6_responsive_%s.pdf";
	
    public static final PageSize[] pageSizes = {
            PageSize.A4.rotate(),
            new PageSize(720, PageSize.A4.getHeight()),
            new PageSize(PageSize.A5.getWidth(), PageSize.A4.getHeight())
    };
	
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
    	File file = new File(DEST);
    	file.getParentFile().mkdirs();
        E06_MediaQuery app = new E06_MediaQuery();
        int count = 1;
        for (PageSize size : pageSizes) {
        	app.createPdf(SRC, String.format(DEST, count++), size);
        }
    }
    
    public void createPdf(String src, String dest, PageSize size) throws IOException {        
    	PdfWriter writer = new PdfWriter(dest);
    	PdfDocument pdf = new PdfDocument(writer);
    	pdf.setTagged();
    	pdf.setDefaultPageSize(size);
	
    	ConverterProperties properties = new ConverterProperties();
    	properties.setBaseUri(new File(src).getParentFile().getAbsolutePath());
    	MediaDeviceDescription mediaDescription = new MediaDeviceDescription(MediaType.SCREEN);
    	mediaDescription.setWidth(CssUtils.parseAbsoluteLength(String.valueOf(size.getWidth())));
    	properties.setMediaDeviceDescription(mediaDescription);
    	properties.setCreateAcroForm(true);
    	try (FileInputStream fileInputStream = new FileInputStream(src)) {
			HtmlConverter.convertToPdf(fileInputStream, pdf, properties);
		}
    }
}
