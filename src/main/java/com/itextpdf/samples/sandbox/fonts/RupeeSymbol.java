package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class RupeeSymbol {
    public static final String DEST = "./target/sandbox/fonts/rupee_symbol.pdf";

    public static final String FONT1 = "./src/main/resources/font/PlayfairDisplay-Regular.ttf";
    public static final String FONT2 = "./src/main/resources/font/PT_Sans-Web-Regular.ttf";
    public static final String FONT3 = "./src/main/resources/font/FreeSans.ttf";

    // "The Rupee character ₹ and the Rupee symbol ₨"
    public static final String RUPEE = "The Rupee character \u20B9 and the Rupee symbol \u20A8";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RupeeSymbol().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font1 = PdfFontFactory.createFont(FONT1, PdfEncodings.IDENTITY_H);
        PdfFont font2 = PdfFontFactory.createFont(FONT2, PdfEncodings.IDENTITY_H);
        PdfFont font3 = PdfFontFactory.createFont(FONT3, PdfEncodings.IDENTITY_H);
        PdfFont font4 = PdfFontFactory.createFont(FONT3, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);

        doc.add(new Paragraph(RUPEE).setFont(font1));
        doc.add(new Paragraph(RUPEE).setFont(font2));
        doc.add(new Paragraph(RUPEE).setFont(font3));
        doc.add(new Paragraph(RUPEE).setFont(font4));

        doc.close();
    }
}
