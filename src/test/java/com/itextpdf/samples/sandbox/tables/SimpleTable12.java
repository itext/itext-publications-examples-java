/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/31263533/how-to-create-nested-column-using-itextsharp
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable12 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table12.pdf";

    protected PdfFont font;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable12().manipulatePdf(DEST);
    }

    public Cell createCell(String content, int colspan, int rowspan, int border) {
        Cell cell = new Cell(rowspan, colspan).add(new Paragraph(content).setFont(font).setFontSize(10));
        cell.setBorder(null);
        if (8 == (border & 8)) {
            cell.setBorderRight(new SolidBorder(1));
            cell.setBorderBottom(new SolidBorder(1));
        }
        if (4 == (border & 4)) {
            cell.setBorderLeft(new SolidBorder(1));
        }
        if (2 == (border & 2)) {
            cell.setBorderBottom(new SolidBorder(1));
        }
        if (1 == (border & 1)) {
            cell.setBorderTop(new SolidBorder(1));
        }
        cell.setTextAlignment(TextAlignment.CENTER);
        return cell;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        // Note that it is not necessary to create new PageSize object,
        // but for testing reasons (connected to parallelization) we call constructor here
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        Table table = new Table(8);
        table.addCell(createCell("Examination", 1, 2, 15));
        table.addCell(createCell("Board", 1, 2, 15));
        table.addCell(createCell("Month and Year of Passing", 1, 2, 15));
        table.addCell(createCell("", 1, 1, 1));
        table.addCell(createCell("Marks", 2, 1, 1));
        table.addCell(createCell("Percentage", 1, 2, 15));
        table.addCell(createCell("Class / Grade", 1, 2, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("Obtained", 1, 1, 15));
        table.addCell(createCell("Out of", 1, 1, 15));
        table.addCell(createCell("12th / I.B. Diploma", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("Aggregate (all subjects)", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        table.addCell(createCell("", 1, 1, 15));
        doc.add(table);

        doc.close();
    }
}
