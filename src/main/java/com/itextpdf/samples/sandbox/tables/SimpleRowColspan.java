package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleRowColspan {
    public static final String DEST = "./target/sandbox/tables/simple_row_colspan.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleRowColspan().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(new float[] {1, 2, 2, 2, 1}));

        Cell cell = new Cell(2, 1).add(new Paragraph("S/N"));
        table.addCell(cell);

        cell = new Cell(1, 3).add(new Paragraph("Name"));
        table.addCell(cell);

        cell = new Cell(2, 1).add(new Paragraph("Age"));
        table.addCell(cell);

        table.addCell("SURNAME");
        table.addCell("FIRST NAME");
        table.addCell("MIDDLE NAME");
        table.addCell("1");
        table.addCell("James");
        table.addCell("Fish");
        table.addCell("Stone");
        table.addCell("17");

        doc.add(table);

        doc.close();
    }
}
