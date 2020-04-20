package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import java.io.File;
import java.io.IOException;

public class TableTab {
    public static final String DEST = "./target/sandbox/objects/table_tab.pdf";
    public static final String[][] DATA = {
            {"John Edward Jr.", "AAA"},
            {"Pascal Einstein W. Alfi", "BBB"},
            {"St. John", "CCC"}
    };

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableTab().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(createParagraphWithTab("Name: ", DATA[0][0], DATA[0][1]));
        doc.add(createParagraphWithTab("Surname: ", DATA[1][0], DATA[1][1]));
        doc.add(createParagraphWithTab("School: ", DATA[2][0], DATA[2][1]));

        doc.close();
    }

    private static Paragraph createParagraphWithTab(String key, String value1, String value2) {
        Paragraph p = new Paragraph();
        p.addTabStops(new TabStop(200f, TabAlignment.LEFT));
        p.add(key);
        p.add(value1);
        p.add(new Tab());
        p.add(value2);
        return p;
    }
}
