package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable9 {
    public static final String DEST = "./target/sandbox/tables/simple_table9.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable9().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("With 3 columns:"));

        Table table = new Table(UnitValue.createPercentArray(new float[] {10, 10, 80}));
        table.setMarginTop(5);
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Col c");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider.");
        doc.add(table);

        doc.add(new Paragraph("With 2 columns:"));

        table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setMarginTop(5);
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell(new Cell(1, 2).add(new Paragraph("Value b")));
        table.addCell(new Cell(1, 2).add(new Paragraph("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider.")));
        table.addCell("Col a");
        table.addCell("Col b");
        table.addCell("Value a");
        table.addCell("Value b");
        table.addCell(new Cell(1, 2).add(new Paragraph("Value b")));
        table.addCell(new Cell(1, 2).add(new Paragraph("This is a long description for column c. " +
                "It needs much more space hence we made sure that the third column is wider.")));

        doc.add(table);

        doc.close();
    }
}
