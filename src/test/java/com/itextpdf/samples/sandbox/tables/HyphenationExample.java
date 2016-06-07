/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.hyphenation.Hyphenation;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.hyphenation.Hyphenator;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileOutputStream;

@Category(SampleTest.class)
public class HyphenationExample extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/hyphenation_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new HyphenationExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        Hyphenation s = Hyphenator.hyphenate("de", "DE", "Leistungsscheinziffer", 2, 2);
        System.out.println(s);

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.setMargins(0, 0, 0, 0);
        Table table = new Table(1);
        table.setWidthPercent(10);
        Text text = new Text("Leistungsscheinziffer");
        text.setHyphenation(new HyphenationConfig("de", "DE", 2, 2));
        table.addCell(new Cell().add(new Paragraph(text)));
        Paragraph paragraph = new Paragraph();
        paragraph.setHyphenation(new HyphenationConfig("de", "DE", 2, 2));
        paragraph.add("Leistungsscheinziffer");
        table.addCell(new Cell().add(paragraph));

        // soft hyphens
        table.addCell(new Cell().add(new Paragraph("Le\u00adistun\u00ADgssch\u00ADeinziffe\u00ADr").setHyphenation(new HyphenationConfig(3, 2))));

        doc.add(table);

        doc.close();
    }
}
