package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TabAlignment;

import java.io.File;
import java.io.IOException;

/*
 * Tabs.java
 *
 * Example showing different ways to use tabs in paragraphs.
 * Demonstrates leading tabs, inline tabs, and combined tab usage.
 */

public class Tabs {
    public static final String DEST = "./target/sandbox/objects/tabs.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Tabs().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Hello World.");
        doc.add(p);

        p = new Paragraph();
        p.addTabStops(new TabStop(60f, TabAlignment.LEFT));
        p.add(new Tab());
        p.add("Hello World with tab.");
        doc.add(p);

        p = new Paragraph();
        p.addTabStops(new TabStop(200f, TabAlignment.LEFT));
        p.add(new Text("Hello World with"));
        p.add(new Tab());
        p.add(new Text("an inline tab."));
        doc.add(p);

        p = new Paragraph();
        p.addTabStops(new TabStop(60f, TabAlignment.LEFT));
        p.add(new Tab());
        p.addTabStops(new TabStop(200f, TabAlignment.LEFT));
        p.add(new Text("Hello World with"));
        p.add(new Tab());
        p.add(new Text("an inline tab."));
        doc.add(p);

        doc.close();
    }
}
