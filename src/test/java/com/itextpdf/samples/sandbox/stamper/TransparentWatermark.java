/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to a question by a customer.
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;


@Category(SampleTest.class)
public class TransparentWatermark extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/transparent_watermark.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TransparentWatermark().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfCanvas under = new PdfCanvas(pdfDoc.getFirstPage().newContentStreamBefore(), new PdfResources(), pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA));
        Paragraph p = new Paragraph("This watermark is added UNDER the existing content")
                .setFont(font).setFontSize(15);
        new Canvas(under, pdfDoc, pdfDoc.getDefaultPageSize())
                .showTextAligned(p, 297, 550, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        PdfCanvas over = new PdfCanvas(pdfDoc.getFirstPage());
        over.setFillColor(Color.BLACK);
        p = new Paragraph("This watermark is added ON TOP OF the existing content")
                .setFont(font).setFontSize(15);
        new Canvas(over, pdfDoc, pdfDoc.getDefaultPageSize())
                .showTextAligned(p, 297, 500, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        p = new Paragraph("This TRANSPARENT watermark is added ON TOP OF the existing content")
                .setFont(font).setFontSize(15);
        over.saveState();
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.5f);
        over.setExtGState(gs1);
        new Canvas(over, pdfDoc, pdfDoc.getDefaultPageSize())
                .showTextAligned(p, 297, 450, 1, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        over.restoreState();
        pdfDoc.close();
    }
}
