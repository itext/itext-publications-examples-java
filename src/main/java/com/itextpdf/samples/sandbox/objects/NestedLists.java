package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class NestedLists {
    public static final String DEST = "./target/sandbox/objects/nested_list.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NestedLists().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);

        List topLevel = new List();
        ListItem topLevelItem = new ListItem();
        topLevelItem.add(new Paragraph().add("Item 1"));
        topLevel.add(topLevelItem);

        List secondLevel = new List();
        secondLevel.add("Sub Item 1");
        ListItem secondLevelItem = new ListItem();
        secondLevelItem.add(new Paragraph("Sub Item 2"));
        secondLevel.add(secondLevelItem);
        topLevelItem.add(secondLevel);

        List thirdLevel = new List();
        thirdLevel.add("Sub Sub Item 1");
        thirdLevel.add("Sub Sub Item 2");
        secondLevelItem.add(thirdLevel);

        document.add(topLevel);

        document.close();
    }
}
