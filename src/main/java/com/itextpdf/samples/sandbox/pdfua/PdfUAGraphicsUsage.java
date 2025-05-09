package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;

import java.io.File;
import java.io.IOException;

public class PdfUAGraphicsUsage {
    public static final String DEST = "./target/sandbox/pdfua/pdf_ua_graphics.pdf";

    public static final String DOG = "./src/main/resources/img/dog.bmp";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUAGraphicsUsage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        final WriterProperties writerProperties = new WriterProperties();
        PdfDocument pdfDoc = new PdfUADocument(new PdfWriter(dest, writerProperties),
                new PdfUAConfig(PdfUAConformance.PDF_UA_1, "Some title", "en-US"));
        Document document = new Document(pdfDoc);
        Image img = new Image(ImageDataFactory.create(DOG));
        img.getAccessibilityProperties().setAlternateDescription("Alternative description");
        document.add(img);
        document.close();
    }
}
