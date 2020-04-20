package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.ColorConstants;
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
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class RectangleInCell {
    public static final String DEST = "./target/sandbox/objects/rectangle_in_cell.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RectangleInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFormXObject formXObject = new PdfFormXObject(new Rectangle(120, 80));
        new PdfCanvas(formXObject, pdfDoc).setFillColor(ColorConstants.RED)
                .rectangle(0, 0, formXObject.getWidth(), formXObject.getHeight())
                .fill();

        doc.add(new Paragraph("Option 1:"));
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell("A rectangle:");
        table.addCell(new Cell().add(new Image(formXObject).setAutoScale(true)).setPadding(10));
        table.addCell("The rectangle is scaled to fit inside the cell, you see a padding.");
        doc.add(table);

        doc.add(new Paragraph("Option 2:"));
        table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell("A rectangle:");
        Cell cell = new Cell().add(new Image(formXObject));
        table.addCell(cell);
        table.addCell("The rectangle keeps its original size, but can overlap other cells in the same row.");
        doc.add(table);

        doc.add(new Paragraph("Option 3:"));
        table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell("A rectangle:");
        cell = new Cell().add(new Image(formXObject).setAutoScale(true));
        table.addCell(cell);
        table.addCell("The rectangle is scaled to fit inside the cell, no padding.");
        doc.add(table);

        doc.close();
    }
}
