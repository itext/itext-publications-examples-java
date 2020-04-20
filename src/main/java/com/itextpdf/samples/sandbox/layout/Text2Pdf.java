package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Text2Pdf {
    public static final String TEXT = "./src/main/resources/txt/tree.txt";
    public static final String DEST = "./target/sandbox/layout/text2pdf.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

    	new Text2Pdf().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf).setTextAlignment(TextAlignment.JUSTIFIED);

        parseTextAndFillDocument(document, TEXT);

        document.close();
    }

    private static void parseTextAndFillDocument(Document doc, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            boolean title = true;
            String line;
            while ((line = br.readLine()) != null) {
                Paragraph paragraph;
                if (title) {

                    // If the text line is a title, then set a bold font
                    paragraph = new Paragraph(line).setFont(bold);
                } else {

                    // If the text line is not a title, then set a normal font
                    paragraph = new Paragraph(line).setFont(normal);
                }

                doc.add(paragraph);
                title = line.isEmpty();
            }
        }
    }
}
