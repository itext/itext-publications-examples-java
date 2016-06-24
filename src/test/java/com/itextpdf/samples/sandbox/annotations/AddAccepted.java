/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/37652181
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class AddAccepted extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_accepted.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello_sticky_note.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddAccepted().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfPage page = pdfDoc.getFirstPage();
        PdfAnnotation sticky = page.getAnnotations().get(0);
        Rectangle stickyRectangle = sticky.getRectangle().toRectangle();
        PdfAnnotation replySticky = new PdfTextAnnotation(stickyRectangle)
                .setStateModel(new PdfString("Review"))
                .setState(new PdfString("Accepted"))
                .setIconName(new PdfName("Comment"))
                .setInReplyTo(sticky)
                .setText(new PdfString("Bruno"))
                .setOpen(false)
                .setContents("Accepted by Bruno")
                .setFlags(sticky.getFlags() + PdfAnnotation.HIDDEN);
        pdfDoc.getFirstPage().addAnnotation(replySticky);
        pdfDoc.close();
    }
}
