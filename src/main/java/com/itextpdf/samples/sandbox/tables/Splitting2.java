package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class Splitting2 {
    public static final String DEST = "./target/sandbox/tables/splitting2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new Splitting2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Test");
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        for (int i = 1; i < 6; i++) {
            table.addCell("key " + i);
            table.addCell("value " + i);
        }

        for (int i = 0; i < 27; i++) {
            doc.add(p);
        }

        doc.add(table);

        for (int i = 0; i < 24; i++) {
            doc.add(p);
        }

        table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        for (int i = 1; i < 6; i++) {
            table.addCell("key " + i);
            table.addCell("value " + i);
        }

        table.setKeepTogether(true);

        doc.add(table);

        doc.close();
    }
}
