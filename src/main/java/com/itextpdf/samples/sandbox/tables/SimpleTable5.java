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

public class SimpleTable5 {
    public static final String DEST = "./target/sandbox/tables/simple_table5.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable5().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        Cell cell = new Cell(1, 5).add(new Paragraph("Table XYZ (Continued)"));
        table.addHeaderCell(cell);

        cell = new Cell(1, 5).add(new Paragraph("Continue on next page"));
        table.addFooterCell(cell);

        table.setSkipFirstHeader(true);
        table.setSkipLastFooter(true);

        for (int i = 0; i < 350; i++) {
            table.addCell(String.valueOf(i + 1));
        }

        doc.add(table);

        doc.close();
    }
}
