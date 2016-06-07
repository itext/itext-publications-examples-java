/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class PageLabels extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/page_labels.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PageLabels().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfViewerPreferences viewerPreferences = new PdfViewerPreferences();
        viewerPreferences.setPrintScaling(PdfViewerPreferences.PdfViewerPreferencesConstants.NONE);
        pdfDoc.getCatalog().setPageMode(PdfName.UseThumbs);
        pdfDoc.getCatalog().setPageLayout(PdfName.TwoPageLeft);
        pdfDoc.getCatalog().setViewerPreferences(viewerPreferences);

        doc.add(new Paragraph("Hello World"));
        doc.add(new Paragraph("Hello People"));
        doc.add(new AreaBreak());

        PdfFont bf = PdfFontFactory.createFont(FontConstants.HELVETICA);

        // we add the text to the direct content, but not in the right order
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getPage(2));
        canvas.beginText();
        canvas.setFontAndSize(bf, 12);
        canvas.moveText(88.66f, 788);
        canvas.showText("ld");
        canvas.moveText(-22f, 0);
        canvas.showText("Wor");
        canvas.moveText(-15.33f, 0);
        canvas.showText("llo");
        canvas.moveText(-15.33f, 0);
        canvas.showText("He");
        canvas.endText();
        PdfFormXObject tmp = new PdfFormXObject(new Rectangle(250, 25));
        new PdfCanvas(tmp, pdfDoc).beginText().
                setFontAndSize(bf, 12).
                moveText(0, 7).
                showText("Hello People").
                endText();
        canvas.addXObject(tmp, 36, 763);

        pdfDoc.setDefaultPageSize(new PageSize(PageSize.A4).rotate());
        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World"));

        pdfDoc.setDefaultPageSize(new PageSize(842, 595));

        doc.add(new AreaBreak());
        doc.add(new Paragraph("Hello World"));

        pdfDoc.setDefaultPageSize(PageSize.A4);
        doc.add(new AreaBreak());
        pdfDoc.getLastPage().setCropBox(new Rectangle(40, 40, 525, 755));
        doc.add(new Paragraph("Hello World"));

        doc.add(new AreaBreak());
        pdfDoc.getLastPage().getPdfObject().put(PdfName.UserUnit, new PdfNumber(5));
        doc.add(new Paragraph("Hello World"));

        doc.add(new AreaBreak());
        pdfDoc.getLastPage().setArtBox(new Rectangle(36, 36, 523, 770));
        Link link = new Link("World", PdfAction.createURI("http://maps.google.com"));
        Paragraph p = new Paragraph("Hello ");
        p.add(link);
        doc.add(p);
        PdfAnnotation a = new PdfTextAnnotation(
                new Rectangle(36, 755, 30, 30))
                .setTitle(new PdfString("Example"))
                .setContents("This is a post-it annotation");
        pdfDoc.getLastPage().addAnnotation(a);
        pdfDoc.getPage(1).setPageLabel(PageLabelNumberingStyleConstants.UPPERCASE_LETTERS, null);
        pdfDoc.getPage(3).setPageLabel(PageLabelNumberingStyleConstants.DECIMAL_ARABIC_NUMERALS, null);
        pdfDoc.getPage(4).setPageLabel(PageLabelNumberingStyleConstants.DECIMAL_ARABIC_NUMERALS, "Custom-", 2);

        doc.close();
    }
}
