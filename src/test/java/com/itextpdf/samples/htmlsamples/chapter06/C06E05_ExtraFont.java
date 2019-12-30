/*
 * Copyright 2016-2017, iText Group NV.
 * This example was created by Bruno Lowagie.
 * It was written in the context of the following book:
 * https://leanpub.com/itext7_pdfHTML
 * Go to http://developers.itextpdf.com for more info.
 */
package com.itextpdf.samples.htmlsamples.chapter06;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;

public class C06E05_ExtraFont {

	/** The path to an extra font. */
	public static final String FONT = "src/main/resources/fonts/cardo/Cardo-Regular.ttf";
	/** The Base URI of the HTML page. */
	public static final String BASEURI = "src/main/resources/html/";
	/** The path to the source HTML file. */
	public static final String SRC = String.format("%sfonts_extra.html", BASEURI);
	/** The target folder for the result. */
	public static final String TARGET = "target/results/ch06/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%sfonts_extrafont.pdf", TARGET);

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
        C06E05_ExtraFont app = new C06E05_ExtraFont();
        app.createPdf(SRC, FONT, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src the path to the source HTML file
     * @param font the path to an extra font
     * @param dest the path to the resulting PDF
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String font, String dest) throws IOException {
    	ConverterProperties properties = new ConverterProperties();
    	FontProvider fontProvider = new DefaultFontProvider();
    	FontProgram fontProgram = FontProgramFactory.createFont(font);
    	fontProvider.addFont(fontProgram);
    	properties.setFontProvider(fontProvider);
		HtmlConverter.convertToPdf(new File(src), new File(dest), properties);
    }
}
