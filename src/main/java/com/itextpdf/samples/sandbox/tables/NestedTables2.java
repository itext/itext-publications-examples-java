package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class NestedTables2 {
    public static final String DEST = "./target/sandbox/tables/nested_tables2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedTables2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(new float[] {1, 15}));

        for (int i = 1; i <= 20; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("It is not smart to use iText 2.1.7!");
        }

        Table innertable = new Table(UnitValue.createPercentArray(new float[] {1, 15}));

        for (int i = 0; i < 90; i++) {
            innertable.addCell(String.valueOf(i + 1));
            innertable.addCell("Upgrade if you're a professional developer!");
        }

        table.addCell("21");
        table.addCell(innertable);

        for (int i = 22; i <= 40; i++) {
            table.addCell(String.valueOf(i));
            table.addCell("It is not smart to use iText 2.1.7!");
        }

        doc.add(table);

        doc.close();
    }
}
