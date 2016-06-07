/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28431148/itext-chapter-font-overrides-paragraph-font
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ChapterAndTitle extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/chapter_and_title.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChapterAndTitle().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont chapterFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);
        Paragraph title = new Paragraph("This is the title").setFont(chapterFont).setFontSize(16);
        title.setDestination("title");
        doc.add(title);

        PdfOutline root = pdfDoc.getOutlines(false);
        root.addOutline("This is the title").addDestination(PdfDestination.makeDestination(new PdfString("title")));

        Paragraph p = new Paragraph("This is the paragraph");
        doc.add(p);

        doc.close();
    }
}
