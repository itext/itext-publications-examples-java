/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28418108/itext-how-to-add-an-inner-table-surrounded-by-text-to-a-table
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class NestedTableProblem {
    public static final String DEST = "./target/sandbox/tables/nested_table_problem.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTableProblem().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 792));

        doc.setMargins(30, 21, 35, 21);

        // inner table
        Table innerTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        innerTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        innerTable.addCell(new Cell().setBorder(new SolidBorder(ColorConstants.RED, 1))
                .add(new Paragraph("Goodbye World")));
        innerTable.setWidth(UnitValue.createPercentValue(80));

        // outer table
        Table outerTable = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        outerTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell cell = new Cell();
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        cell.add(new Paragraph("Hello World"));
        cell.add(innerTable);
        cell.add(new Paragraph("Hello World"));

        outerTable.addCell(cell);
        doc.add(outerTable);

        doc.close();
    }
}
