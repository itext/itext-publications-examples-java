package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class NestedTablesAligned {
    public static final String DEST = "./target/sandbox/tables/nested_tables_aligned.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTablesAligned().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, PageSize.A4.rotate());

        float[] columnWidths = {200f, 200f, 200f};

        Table table = new Table(columnWidths);
        buildNestedTables(table);

        doc.add(table);

        doc.close();
    }

    private static void buildNestedTables(Table outerTable) {
        Table innerTable1 = new Table(UnitValue.createPercentArray(1));
        innerTable1.setWidth(100f);
        innerTable1.setHorizontalAlignment(HorizontalAlignment.LEFT);
        innerTable1.addCell("Cell 1");
        innerTable1.addCell("Cell 2");
        outerTable.addCell(innerTable1);

        Table innerTable2 = new Table(UnitValue.createPercentArray(2));
        innerTable2.setWidth(100f);
        innerTable2.setHorizontalAlignment(HorizontalAlignment.CENTER);
        innerTable2.addCell("Cell 3");
        innerTable2.addCell("Cell 4");
        outerTable.addCell(innerTable2);

        Table innerTable3 = new Table(UnitValue.createPercentArray(2));
        innerTable3.setWidth(100f);
        innerTable3.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        innerTable3.addCell("Cell 5");
        innerTable3.addCell("Cell 6");
        outerTable.addCell(innerTable3);
    }
}
