/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/33873180/itext-pdf-add-text-in-absolute-position-on-top-of-the-1st-page
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class WriteOnFirstPage extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/columntext/write_on_first_page.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WriteOnFirstPage().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        PdfFormXObject template = new PdfFormXObject(new Rectangle(523, 50));
        PdfCanvas templateCanvas = new PdfCanvas(template, pdfDoc);

        doc.add(new Image(template));
        for (int i = 0; i < 100; i++) {
            doc.add(new Paragraph("test"));
        }

        templateCanvas
                .beginText()
                .setFontAndSize(PdfFontFactory.createFont(FontConstants.HELVETICA), 12)
                .showText(String.format("There are %s pages in this document", pdfDoc.getNumberOfPages()))
                .endText()
                .stroke();

        doc.close();
    }
}