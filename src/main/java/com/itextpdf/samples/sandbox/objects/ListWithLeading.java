package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ListWithLeading {
    public static final String DEST = "./target/sandbox/objects/list_with_leading.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ListWithLeading().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        List list1 = new List()
                .setSymbolIndent(12)

                // Bullet •
                .setListSymbol("\u2022")
                .setFont(font);
        list1.add(new ListItem("Value 1"))
                .add(new ListItem("Value 2"))
                .add(new ListItem("Value 3"));
        doc.add(list1);

        List list2 = new List()
                .setSymbolIndent(12)

                // Bullet •
                .setListSymbol("\u2022");

        // The Leading is a spacing between lines of text
        list2.add((ListItem) new ListItem().add(new Paragraph("Value 1").setFixedLeading(30).setMargins(0, 0, 0, 0)))
                .add((ListItem) new ListItem().add(new Paragraph("Value 2").setFixedLeading(30).setMargins(0, 0, 0, 0)))
                .add((ListItem) new ListItem().add(new Paragraph("Value 3").setFixedLeading(30).setMargins(0, 0, 0, 0)));
        list2.setMarginLeft(60);
        doc.add(list2);

        doc.close();
    }
}
