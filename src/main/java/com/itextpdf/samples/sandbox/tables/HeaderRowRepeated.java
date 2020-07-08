package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class HeaderRowRepeated {
    public static final String DEST = "./target/sandbox/tables/header_row_repeated.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HeaderRowRepeated().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // table with 2 columns:
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        // header row:
        table.addHeaderCell("Key");
        table.addHeaderCell("Value");
        table.setSkipFirstHeader(true);

        // many data rows:
        for (int i = 1; i < 51; i++) {
            table.addCell("key: " + i);
            table.addCell("value: " + i);
        }

        doc.add(table);

        doc.close();
    }
}
