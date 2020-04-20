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

	/**
	 * The path to the resulting PDF file.
	 */
	public static final String DEST = "./target/htmlsamples/ch01/helloWorld01.pdf";

	/**
	 * The HTML-string that we are going to convert to PDF.
	 */
	public static final String HTML = "<h1>Test</h1><p>Hello World</p>";

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

		new C01E01_HelloWorld().createPdf(HTML, DEST);
	}

	/**
	 * Creates the PDF file.
	 *
	 * @param html the HTML as a String value
	 * @param dest the path of the resulting PDF
	 * @throws IOException signals that an I/O exception has occurred.
	 */
	public void createPdf(String html, String dest) throws IOException {
		HtmlConverter.convertToPdf(html, new FileOutputStream(dest));
	}
}
