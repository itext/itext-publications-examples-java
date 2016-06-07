/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27083206/itextshape-clickable-polygon-or-path
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfStampAnnotation;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddRotatedAnnotation extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_rotated_annotation.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddRotatedAnnotation().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfAction action = PdfAction.createURI("http://pages.itextpdf.com/ebook-stackoverflow-questions.html");
        Rectangle linkLocation1 = new Rectangle(30, 770, 90, 30);
        PdfAnnotation link1 = new PdfLinkAnnotation(linkLocation1)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(action)
                .setColor(Color.RED.getColorValue());
        pdfDoc.getFirstPage().addAnnotation(link1);

        Rectangle linkLocation2 = new Rectangle(30, 670, 30, 90);
        PdfAnnotation link2 = new PdfLinkAnnotation(linkLocation2)
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                .setAction(action)
                .setColor(Color.GREEN.getColorValue());
        pdfDoc.getFirstPage().addAnnotation(link2);

        Rectangle linkLocation3 = new Rectangle(150, 770, 90, 30);
        PdfAnnotation stamp1 = new PdfStampAnnotation(linkLocation3)
                .setStampName(new PdfName("Confidential"))
                .setContents("Landscape");
        pdfDoc.getFirstPage().addAnnotation(stamp1);

        Rectangle linkLocation4 = new Rectangle(150, 670, 90, 90);
        PdfAnnotation stamp2 = new PdfStampAnnotation(linkLocation4)
                .setStampName(new PdfName("Confidential"))
                .setContents("Portrait")
                .put(PdfName.Rotate, new PdfNumber(90));
        pdfDoc.getFirstPage().addAnnotation(stamp2);

        Rectangle linkLocation5 = new Rectangle(250, 670, 90, 90);
        PdfAnnotation stamp3 = new PdfStampAnnotation(linkLocation5)
                .setStampName(new PdfName("Confidential"))
                .setContents("Portrait")
                .put(PdfName.Rotate, new PdfNumber(45));
        pdfDoc.getFirstPage().addAnnotation(stamp3);

        pdfDoc.close();
    }
}
