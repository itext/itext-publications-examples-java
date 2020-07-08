package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.File;

public class RotatedCell {
    public static final String DEST = "./target/sandbox/tables/rotated_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RotatedCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();

        for (int i = 0; i < 8; i++) {
            Cell cell = new Cell().add(new Paragraph(String.format("May %s, 2016", i + 15)));
            cell.setRotationAngle(Math.toRadians(90));
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(cell);
        }

        for (int i = 0; i < 16; i++) {
            table.addCell("hi");
        }

        doc.add(table);

        doc.close();
    }
}
