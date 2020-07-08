package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class RemoveListSymbol {
    public static final String DEST = "./target/sandbox/objects/remove_list_symbol.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RemoveListSymbol().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph paragraph = new Paragraph("A list without list symbol");
        doc.add(paragraph);

        List list = new List();

        // List symbol replaced, not deleted
        list.setListSymbol("");
        list.add(new ListItem("Item 1"));
        list.add(new ListItem("Item 2"));
        list.add(new ListItem("Item 3"));

        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        table.setMarginTop(5);
        table.addCell(new Cell().add(new Paragraph("List:")));
        table.addCell(list);
        doc.add(table);

        doc.close();
    }
}
