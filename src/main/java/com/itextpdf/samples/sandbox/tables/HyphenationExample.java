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
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class HyphenationExample {
    public static final String DEST = "./target/sandbox/tables/hyphenation_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new HyphenationExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // See hyphenation example of specified word in console
        // For the correct run of sample, please, add an itext.hyph dependency,
        // which could be found on the following web-page: https://mvnrepository.com/artifact/com.itextpdf/hyph
        Hyphenation s = Hyphenator.hyphenate("de", "DE", "Leistungsscheinziffer", 2, 2);
        System.out.println(s);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        doc.setMargins(0, 0, 0, 0);

        Table table = new Table(UnitValue.createPercentArray(1)).setFixedLayout();
        table.setWidth(UnitValue.createPercentValue(10));

        Text text = new Text("Leistungsscheinziffer");
        text.setHyphenation(new HyphenationConfig("de", "DE", 2, 2));
        table.addCell(new Cell().add(new Paragraph(text)));

        Paragraph paragraph = new Paragraph();
        paragraph.setHyphenation(new HyphenationConfig("de", "DE", 2, 2));
        paragraph.add("Leistungsscheinziffer");
        table.addCell(new Cell().add(paragraph));

        // soft hyphens
        table.addCell(new Cell().add(new Paragraph("Le\u00adistun\u00ADgssch\u00ADeinziffe\u00ADr")
                .setHyphenation(new HyphenationConfig(3, 2))));

        doc.add(table);

        doc.close();
    }
}
