/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * These examples are written by Bruno Lowagie in the context of an article about fonts.
 */
package com.itextpdf.samples.sandbox.fonts.tutorial;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class F01_Unembedded extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/fonts/tutorial/f01_unembedded.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new F01_Unembedded().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Vous \u00EAtes d\'o\u00F9?"));
        doc.add(new Paragraph("\u00C0 tout \u00E0 l\'heure. \u00C0 bient\u00F4t."));
        doc.add(new Paragraph("Je me pr\u00E9sente."));
        doc.add(new Paragraph("C\'est un \u00E9tudiant."));
        doc.add(new Paragraph("\u00C7a va?"));
        doc.add(new Paragraph("Il est ing\u00E9nieur. Elle est m\u00E9decin."));
        doc.add(new Paragraph("C\'est une fen\u00EAtre."));
        doc.add(new Paragraph("R\u00E9p\u00E9tez, s\'il vous pla\u00EEt."));
        doc.close();
    }
}
