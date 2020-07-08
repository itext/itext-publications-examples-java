package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class KeepingTogetherInnerTable {
    public static final String DEST = "./target/sandbox/tables/keeping_together_inner_table.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KeepingTogetherInnerTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(300, 160));

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setMarginTop(10);

        Cell cell = new Cell();
        cell.add(new Paragraph("G"));
        cell.add(new Paragraph("R"));
        cell.add(new Paragraph("O"));
        cell.add(new Paragraph("U"));
        cell.add(new Paragraph("P"));
        table.addCell(cell);

        Table inner = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        inner.setKeepTogether(true);
        inner.addCell("row 1");
        inner.addCell("row 2");
        inner.addCell("row 3");
        inner.addCell("row 4");
        inner.addCell("row 5");

        cell = new Cell().add(inner);
        cell.setPadding(0);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
