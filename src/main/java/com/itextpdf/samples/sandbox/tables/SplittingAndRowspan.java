package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class SplittingAndRowspan {
    public static final String DEST = "./target/sandbox/tables/splitting_and_rowspan.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SplittingAndRowspan().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(300, 160));

        doc.add(new Paragraph("Table with setKeepTogether(true):"));

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setKeepTogether(true);
        table.setMarginTop(10);

        Cell cell = new Cell(3, 1);
        cell.add(new Paragraph("G"));
        cell.add(new Paragraph("R"));
        cell.add(new Paragraph("P"));

        table.addCell(cell);
        table.addCell("row 1");
        table.addCell("row 2");
        table.addCell("row 3");

        doc.add(table);

        doc.add(new AreaBreak());

        doc.add(new Paragraph("Table with setKeepTogether(false):"));
        table.setKeepTogether(false);

        doc.add(table);

        doc.close();
    }
}
