package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class IndentationInCell {
    public static final String DEST = "./target/sandbox/tables/indentation_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new IndentationInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        Cell cell = new Cell().add(new Paragraph("TO:\n\n   name"));
        table.addCell(cell);

        cell = new Cell().add(new Paragraph("TO:\n\n\u00a0\u00a0\u00a0name"));
        table.addCell(cell);

        cell = new Cell();
        cell.add(new Paragraph("TO:"));
        Paragraph p = new Paragraph("name");
        p.setMarginLeft(10);
        cell.add(p);
        table.addCell(cell);

        cell = new Cell();
        cell.add(new Paragraph("TO:"));
        p = new Paragraph("name");
        p.setTextAlignment(TextAlignment.RIGHT);
        cell.add(p);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
