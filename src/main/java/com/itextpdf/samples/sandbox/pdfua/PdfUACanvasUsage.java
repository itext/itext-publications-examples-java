package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.validation.ValidationContainer;
import com.itextpdf.pdfua.checkers.PdfUA1Checker;
import com.itextpdf.pdfua.exceptions.PdfUAConformanceException;

import java.io.File;
import java.io.IOException;

public class PdfUACanvasUsage {
    public static final String DEST = "./target/sandbox/pdfua/pdf_ua_canvas.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUACanvasUsage().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(
                    new PdfWriter(dest, new WriterProperties().addUAXmpMetadata()));
        pdfDoc.setTagged();
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("English pangram");
        //validation
        ValidationContainer validationContainer = new ValidationContainer();
        validationContainer.addChecker(new PdfUA1Checker(pdfDoc));
        pdfDoc.getDiContainer().register(ValidationContainer.class, validationContainer);
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        PdfPage page1 = pdfDoc.addNewPage();
        PdfCanvas canvas = new PdfCanvas(page1);

        canvas.beginText()
                .setFontAndSize(font, 12);

        //Pdf/UA conformance exception handling
        try {
            canvas.showText("Hello World");
        } catch (PdfUAConformanceException exception) {
            //do handling here
        }

        TagTreePointer tagPointer = new TagTreePointer(pdfDoc)
                    .setPageForTagging(page1)
                    .addTag(StandardRoles.P);

        canvas.openTag(tagPointer.getTagReference())
                .saveState()
                .beginText()
                .setFontAndSize(font, 12)
                .moveText(200, 200)
                .showText("Hello World!")
                .endText()
                .restoreState()
                .closeTag();
        pdfDoc.close();
    }
}
