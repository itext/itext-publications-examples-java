package com.itextpdf.samples.html;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.licensekey.LicenseKey;

public class E01_NoCss {

	public static final String SRC = "src/main/resources/html/movie/1_no_css.html";
	public static final String DEST = "target/results/html/1_no_css.pdf";
	
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");        
    	File file = new File(DEST);
    	file.getParentFile().mkdirs();
        E01_NoCss app = new E01_NoCss();
        app.createPdf(SRC, DEST);
    }
    
    public void createPdf(String src, String dest) throws IOException {
		HtmlConverter.convertToPdf(new File(src), new File(dest));
    }
}
