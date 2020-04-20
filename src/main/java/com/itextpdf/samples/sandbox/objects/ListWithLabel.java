package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.IOException;

public class ListWithLabel {
    public static final String DEST = "./target/sandbox/objects/list_with_label.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListWithLabel().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(new float[]{1, 10});
        table.setWidth(200);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(new Paragraph("Label"));
        table.addCell(cell);

        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        List list = new List();
        list.add(new ListItem("Value 1"));
        list.add(new ListItem("Value 2"));
        list.add(new ListItem("Value 3"));
        cell.add(list);
        table.addCell(cell);

        doc.add(table);

        doc.close();
    }
}
