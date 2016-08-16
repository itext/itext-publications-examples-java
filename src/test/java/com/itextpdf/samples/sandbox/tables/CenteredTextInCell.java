/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19703715/centered-text-in-itext-pdf-table-cell
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
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class CenteredTextInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/centered_text_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CenteredTextInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(FontConstants.HELVETICA_BOLD));
        Paragraph para = new Paragraph("Test").setFont(font);
        para.setFixedLeading(0);
        para.setMultipliedLeading(1);
        Table table = new Table(1);
        Cell cell = new Cell();
        cell.setHeight(50);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.add(para);
        table.addCell(cell);
        doc.add(table);

        doc.close();
    }
}
