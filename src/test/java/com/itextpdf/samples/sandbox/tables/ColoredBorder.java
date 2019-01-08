/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/35073619
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ColoredBorder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/colored_border.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColoredBorder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table;
        table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        Cell cell;
        cell = new Cell().add(new Paragraph("Cell 1"));
        cell.setBorderTop(new SolidBorder(ColorConstants.RED, 1));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLUE, 1));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Cell 2"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.GREEN, 5));
        cell.setBorderTop(new SolidBorder(ColorConstants.YELLOW, 8));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Cell 3"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.RED, 1));
        cell.setBorderBottom(new SolidBorder(ColorConstants.BLUE, 1));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Cell 4"));
        cell.setBorderLeft(new SolidBorder(ColorConstants.GREEN, 5));
        cell.setBorderTop(new SolidBorder(ColorConstants.YELLOW, 8));
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
