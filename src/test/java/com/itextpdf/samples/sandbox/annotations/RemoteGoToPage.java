/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19999048/how-to-create-hyperlink-from-a-pdf-to-another-pdf-to-a-specified-page-using-itex
 * <p>
 * Creating a link from one PDF to another
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class RemoteGoToPage extends GenericTest {
    // !IMPORTANT We change the order of SRC and DEST because we want to check via CompareTool
    // comprehensive file rather then simple
    // So DEST file DOES NOT mean destination but DEST-file-which-we-will-check-from-GenericTest
    public static final String SRC = "./target/test/resources/sandbox/annotations/subdir/xyz2.pdf";
    public static final String DEST = "./target/test/resources/sandbox/annotations/remote_go_to_page.pdf";

    public static void main(String[] args) throws Exception {
        new RemoteGoToPage().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        File file = new File(SRC);
        file.getParentFile().mkdirs();
        RemoteGoToPage app = new RemoteGoToPage();
        app.createPdf(SRC);
        app.createPdf2(DEST);
    }


    private void createPdf(String src) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(src)));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("page 1"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 2"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 3"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 4"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 5"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 6"));
        doc.add(new AreaBreak());
        doc.add(new Paragraph("page 7"));
        doc.close();
    }

    private void createPdf2(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new FileOutputStream(dest)));
        Paragraph chunk = new Paragraph(new Link("Link", PdfAction.createGoToR("subdir/xyz2.pdf", 6)));
        new Document(pdfDoc).add(chunk);
        pdfDoc.close();
    }
}
