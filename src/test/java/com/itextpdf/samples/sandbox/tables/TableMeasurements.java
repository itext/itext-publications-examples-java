/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/34539028
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class TableMeasurements extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/tables_measurements.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableMeasurements().manipulatePdf(DEST);
    }

    public static float millimetersToPoints(float value) {
        return (value / 25.4f) * 72f;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Table table = new Table(10);
        table.setWidth(millimetersToPoints(100));
        table.addCell(getCell(10));
        table.addCell(getCell(5));
        table.addCell(getCell(3));
        table.addCell(getCell(2));
        table.addCell(getCell(3));
        table.addCell(getCell(5));
        table.addCell(getCell(1));
        doc.add(table);
        doc.close();
    }

    private Cell getCell(int cm) {
        Cell cell = new Cell(1, cm);
        Paragraph p = new Paragraph(
                String.format("%smm", 10 * cm)).setFontSize(8);
        p.setTextAlignment(TextAlignment.CENTER);
        p.setMultipliedLeading(0.5f);
        p.setMarginTop(0);
        cell.add(p);
        return cell;
    }
}