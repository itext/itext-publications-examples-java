package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class TableSpace {
    public static final String DEST = "./target/sandbox/objects/table_space.pdf";
    public static final String FONT = "./src/main/resources/font/PTM55FT.ttf";
    public static final String[][] DATA = {
            {"John Edward Jr.", "AAA"},
            {"Pascal Einstein W. Alfi", "BBB"},
            {"St. John", "CCC"}
    };

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableSpace().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.CP1250, EmbeddingStrategy.PREFER_EMBEDDED);

        doc.add(createParagraphWithSpaces(font, String.format("%s: %s", "Name", DATA[0][0]), DATA[0][1]));
        doc.add(createParagraphWithSpaces(font, String.format("%s: %s", "Surname", DATA[1][0]), DATA[1][1]));
        doc.add(createParagraphWithSpaces(font, String.format("%s: %s", "School", DATA[2][0]), DATA[2][1]));

        doc.close();
    }

    private static Paragraph createParagraphWithSpaces(PdfFont font, String value1, String value2) {
        Paragraph p = new Paragraph();
        p.setFont(font);
        p.add(String.format("%-35s", value1));
        p.add(value2);
        return p;
    }
}
