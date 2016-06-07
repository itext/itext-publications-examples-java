/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/21625435/c-sharp-add-rectangle-into-pdfpcell-itextsharp
 * <p>
 * Adding a Rectangle object inside a table cell.
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class RectangleInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/rectangle_in_cell.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RectangleInCell().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Option 1:"));
        Table table = new Table(3);
        table.addCell(new Cell().add("A rectangle:)"));
        PdfFormXObject template = new PdfFormXObject(new Rectangle(120, 80));
        new PdfCanvas(template, pdfDoc).setFillColor(Color.RED)
                .rectangle(0, 0, 120, 80)
                .fill();
        table.addCell(new Cell().add(new Image(template).setAutoScale(true)).setPadding(3));
        table.addCell("The rectangle is scaled to fit inside the cell, you see a padding.");
        doc.add(table);
        doc.add(new Paragraph("Option 2:"));
        table = new Table(3);
        table.addCell("A rectangle:");
        Cell cell = new Cell().add(new Image(template));
        table.addCell(cell);
        table.addCell("The rectangle keeps its original size, but can overlap other cells in the same row.");
        doc.add(table);
        doc.add(new Paragraph("Option 3:"));
        table = new Table(3);
        table.addCell("A rectangle:");
        cell = new Cell().add(new Image(template).setAutoScale(true));
        table.addCell(cell);
        table.addCell("The rectangle is scaled to fit inside the cell, no padding.");
        doc.add(table);
        PdfCanvas cb = new PdfCanvas(pdfDoc.getLastPage());
        cb.moveTo(228, 810);
        cb.lineTo(338, 810);
        cb.stroke();

        doc.close();
    }
}
