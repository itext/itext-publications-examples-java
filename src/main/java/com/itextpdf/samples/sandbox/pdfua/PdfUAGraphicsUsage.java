package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.ValidationContainer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.pdfua.checkers.PdfUA1Checker;

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
        PdfDocument pdfDoc = new PdfDocument(
                new PdfWriter(dest, new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
        pdfDoc.setTagged();
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("English pangram");
        //validation
        ValidationContainer validationContainer = new ValidationContainer();
        validationContainer.addChecker(new PdfUA1Checker(pdfDoc));
        pdfDoc.getDiContainer().register(ValidationContainer.class, validationContainer);
        Document document = new Document(pdfDoc);
        Image img = new Image(ImageDataFactory.create(DOG));
        img.getAccessibilityProperties().setAlternateDescription("Alternative description");
        document.add(img);
        document.close();
    }
}
