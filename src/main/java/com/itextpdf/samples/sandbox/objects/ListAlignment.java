package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.IOException;

/*
 * ListAlignment.java
 *
 * Example showing how to apply text alignment to list items.
 * Demonstrates justified alignment for multi-line list item content.
 */

public class ListAlignment {
    public static final String DEST = "./target/sandbox/objects/list_alignment.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListAlignment().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        List list = new List();

        String text = "test 1 2 3 ";
        for (int i = 0; i < 5; i++) {
            text = text + text;
        }
        ListItem item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        text = "a b c align ";
        for (int i = 0; i < 5; i++) {
            text = text + text;
        }
        item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        text = "supercalifragilisticexpialidociousss ";
        for (int i = 0; i < 3; i++) {
            text = text + text;
        }
        item = new ListItem(text);
        item.setTextAlignment(TextAlignment.JUSTIFIED);
        list.add(item);

        doc.add(list);

        doc.close();
    }
}
