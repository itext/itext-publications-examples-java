package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class NestedTables6 {
    public static final String DEST = "./target/sandbox/tables/nested_tables6.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTables6().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(1200, 800));

        // Header part
        Table mainTable = new Table(UnitValue.createPercentArray(1));
        mainTable.setWidth(1000);

        // Notice that in itext7 there is no getDefaultCell method
        // and you should set paddings, margins and other properties exactly on the element
        // you want to handle them
        Table subTable2 = new Table(new float[] {200, 100, 200, 200, 300});
        subTable2.addCell("test 1");
        subTable2.addCell("test 2");
        subTable2.addCell("test 3");
        subTable2.addCell("test 4");
        subTable2.addCell("test 5");

        Cell cell2 = new Cell().add(subTable2);
        cell2.setPadding(0);
        mainTable.addCell(cell2);

        doc.add(mainTable);

        doc.close();
    }
}
