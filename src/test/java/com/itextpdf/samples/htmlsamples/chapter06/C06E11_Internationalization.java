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

public class C06E11_Internationalization {

	/** An array with the paths to extra fonts. */
	public static final String[] FONTS = {
			"src/main/resources/fonts/noto/NotoSans-Regular.ttf",
			"src/main/resources/fonts/noto/NotoSans-Bold.ttf",
			"src/main/resources/fonts/noto/NotoSansCJKsc-Regular.otf",
			"src/main/resources/fonts/noto/NotoSansCJKjp-Regular.otf",
			"src/main/resources/fonts/noto/NotoSansCJKkr-Regular.otf",
			"src/main/resources/fonts/noto/NotoNaskhArabic-Regular.ttf",
			"src/main/resources/fonts/noto/NotoSansHebrew-Regular.ttf"
	};
	/** The Base URI of the HTML page. */
	public static final String BASEURI = "src/main/resources/html/";
	/** The path to the source HTML file. */
	public static final String SRC = String.format("%sfonts_i18n.html", BASEURI);
	/** The target folder for the result. */
	public static final String TARGET = "target/results/ch06/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%sfonts_i18n.pdf", TARGET);

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
        C06E11_Internationalization app = new C06E11_Internationalization();
        app.createPdf(SRC, FONTS, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src the path to the source HTML file
     * @param fonts an array containing paths to extra fonts
     * @param dest the path to the resulting PDF
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String[] fonts, String dest) throws IOException {
    	ConverterProperties properties = new ConverterProperties();
    	FontProvider fontProvider = new DefaultFontProvider(false, false, false);
    	for (String font : fonts) {
    		FontProgram fontProgram = FontProgramFactory.createFont(font);
    		fontProvider.addFont(fontProgram);
    	}
    	properties.setFontProvider(fontProvider);
		HtmlConverter.convertToPdf(new File(src), new File(dest), properties);
    }
}
