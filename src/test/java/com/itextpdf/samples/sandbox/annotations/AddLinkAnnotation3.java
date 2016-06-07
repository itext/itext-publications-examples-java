/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/28554413/how-to-add-overlay-text-with-link-annotations-to-existing-pdf
 */
package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
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
@Ignore
public class AddLinkAnnotation3 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_link_annotation3.pdf";
    public static final String SRC = "./src/test/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLinkAnnotation3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Link link = new Link("The Best iText Questions on StackOverflow",
                PdfAction.createURI("http://pages.itextpdf.com/ebook-stackoverflow-questions.html"));
        link.setBackgroundColor(Color.RED);
        link.setFont(bold);
        Paragraph p = new Paragraph("Download ");
        p.add(link);
        p.add(" and discover \nmore than 200 questions and answers.");
        // TODO DEVSIX-549
        doc.showTextAligned(p, 30, 600, 1, TextAlignment.LEFT,
                VerticalAlignment.TOP, (float) Math.PI / 6);
        doc.close();
    }
}
