package com.itextpdf.samples.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfXObject;
import com.itextpdf.licensekey.LicenseKey;

public class E05_Background {

	public static final String SRC = "src/main/resources/html/movie/4_external_css.html";
	public static final String STATIONERY = "src/main/resources/html/movie/stationery.pdf";
	public static final String DEST = "target/results/html/5_background.pdf";
	
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
    	File file = new File(DEST);
    	file.getParentFile().mkdirs();
        E05_Background app = new E05_Background();
        app.createPdf(SRC, STATIONERY, DEST);
    }
    
    public void createPdf(String src, String stationery, String dest) throws IOException {        
    	PdfWriter writer = new PdfWriter(dest);
    	PdfDocument pdf = new PdfDocument(writer);
    	IEventHandler handler = new Background(pdf, stationery);
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, handler);
    	ConverterProperties properties = new ConverterProperties();
    	properties.setBaseUri(new File(src).getParentFile().getAbsolutePath());
		HtmlConverter.convertToPdf(new FileInputStream(src), pdf, properties);
    }
    
    class Background implements IEventHandler {
		PdfXObject stationery;
		
		public Background(PdfDocument pdf, String src) throws IOException {
			PdfDocument template = new PdfDocument(new PdfReader(src));
			PdfPage page = template.getPage(1);
			stationery = page.copyAsFormXObject(pdf);
			template.close();
		}
		
		@Override
		public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdf);
            canvas.addXObject(stationery, 0, 0);
		}
		
	}
}
