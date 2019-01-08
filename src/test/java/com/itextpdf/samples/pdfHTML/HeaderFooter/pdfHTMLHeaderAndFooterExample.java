/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.pdfHTML.HeaderFooter;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class pdfHTMLHeaderAndFooterExample {

    public static final String sourceFolder = "src/test/resources/pdfHTML/";
    public static final String destinationFolder = "target/output/pdfHTML/";
    public static final String LICENSE = "src/test/resources/pdfHTML/itextkey_trial.xml";

    public static final String[] files = {"ipsum"};

    public static void main(String[] args) throws IOException, InterruptedException {
        LicenseKey.loadLicenseFile(LICENSE);
        for (String name : files) {
            String htmlSource = sourceFolder + name + ".html";
            String resourceFolder = sourceFolder + name + "/";
            String pdfDest = destinationFolder + name + ".pdf";
            File file = new File(pdfDest);

            System.out.println("Parsing: " + htmlSource);
            file.getParentFile().mkdirs();
            new pdfHTMLHeaderAndFooterExample().parseWithHeaderAndFooter(htmlSource, pdfDest, resourceFolder);

        }
    }

    public void parseWithHeaderAndFooter(String htmlSource, String pdfDest, String resoureLoc) throws IOException, InterruptedException {
        File pdf = new File(pdfDest);
        pdf.getParentFile().mkdirs();

        //Create Document
        PdfWriter writer= new PdfWriter(pdfDest);
        PdfDocument pdfDocument = new PdfDocument(writer);
        //Create event-handlers
        String header = "pdfHtml Header and footer example using page-events";
        Header headerHandler = new Header(header);
        PageXofY footerHandler = new PageXofY(pdfDocument);

        //Assign event-handlers
        pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,headerHandler);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE,footerHandler);

        //Convert
        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(resoureLoc);
        HtmlConverter.convertToDocument(new FileInputStream(htmlSource), pdfDocument, converterProperties);
        //Write the total number of pages to the placeholder
        footerHandler.writeTotal(pdfDocument);
        pdfDocument.close();


    }

    //Header event handler
    protected class Header implements IEventHandler {
        String header;
        public Header(String header) {
            this.header = header;
        }
        @Override
        public void handleEvent(Event event) {
            //Retrieve document and
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            canvas.setFontSize(18f);
            //Write text at position
            canvas.showTextAligned(header,
                    pageSize.getWidth() / 2,
                    pageSize.getTop() - 30, TextAlignment.CENTER);
        }
    }

    //page X of Y 
    protected class PageXofY implements IEventHandler {
        protected PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 300;
        protected float y = 25;
        protected float space = 4.5f;
        protected float descent = 3;
        public PageXofY(PdfDocument pdf) {
            placeholder =
                    new PdfFormXObject(new Rectangle(0, 0, side, side));
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdf.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(
                    page.getLastContentStream(), page.getResources(), pdf);
            Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
            Paragraph p = new Paragraph()
                    .add("Page ").add(String.valueOf(pageNumber)).add(" of");
            canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
            pdfCanvas.addXObject(placeholder, x + space, y - descent);
            pdfCanvas.release();
        }
        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),
                    0, descent, TextAlignment.LEFT);
        }
    }



}
