package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable4 {
    public static final String DEST = "./target/sandbox/tables/simple_table4.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable4().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

        Paragraph right = new Paragraph("This is right, because we create a paragraph with an indentation to the " +
                "left and as we are adding the paragraph in composite mode, all the properties of the paragraph are preserved.");

        right.setMarginLeft(20);

        Cell rightCell = new Cell().add(right);
        table.addCell(rightCell);

        doc.add(table);

        doc.close();
    }
}
