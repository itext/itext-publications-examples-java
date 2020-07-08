package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML file with a Base64 image to PDF document.
 */
public class C07E03_Base64Image {

	/**
	 * The path to the resulting PDF file.
	 */
	public static final String DEST = "./target/htmlsamples/ch07/base64.pdf";

	/** The Base URI of the HTML page. */
	public static final String BASEURI = "./src/main/resources/htmlsamples/html/";
	/** The path to the source HTML file. */
	public static final String SRC = String.format("%sbase64.html", BASEURI);
	
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

        new C07E03_Base64Image().createPdf(BASEURI, SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param baseUri the base URI
     * @param src the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String baseUri, String src, String dest) throws IOException {
        HtmlConverter.convertToPdf(new File(src), new File(dest));
    }
}
