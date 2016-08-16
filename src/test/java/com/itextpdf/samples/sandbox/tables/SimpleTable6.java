/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/28617095/strikethrough-in-cell-using-itext-in-android-java
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable6 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table6.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable6().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        table.addCell("0123456789");
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA));
        table.addCell(new Cell().add(new Paragraph("0123456789").setFont(font).setLineThrough()));
        Text text1 = new Text("0123456789");
        text1.setUnderline(1.5f, -1);
        table.addCell(new Paragraph(text1));
        Text text2 = new Text("0123456789");
        text2.setUnderline(1.5f, 3.5f);
        table.addCell(new Paragraph(text2));
        doc.add(table);

        doc.close();
    }
}
