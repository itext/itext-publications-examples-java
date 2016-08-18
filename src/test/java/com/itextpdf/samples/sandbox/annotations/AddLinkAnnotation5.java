/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/28554413/how-to-add-overlay-text-with-link-annotations-to-existing-pdf
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddLinkAnnotation5 extends GenericTest {
    public static final String SRC = "./src/test/resources/pdfs/primes.pdf";
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_link_annotation5.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLinkAnnotation5().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        Link link = new Link("This is a link. Click it and you'll be forwarded to another page in this document.",
                PdfAction.createGoTo(PdfExplicitDestination.createFit(3)));
        link.setBackgroundColor(Color.RED);
        link.getLinkAnnotation().setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT);

        Paragraph p = new Paragraph(link).setWidth(240);
        doc.showTextAligned(p, 320, 695, 1, TextAlignment.LEFT,
                VerticalAlignment.TOP, 0);
        doc.close();
    }
}
