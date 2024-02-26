package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class FreeSansBold {
    public static final String DEST = "./target/sandbox/fonts/free_sans_bold.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    public static final String FONTBOLD = "./src/main/resources/font/FreeSansBold.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FreeSansBold().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // "FreeSans regular: Đ"
        Paragraph p = new Paragraph("FreeSans regular: \u0110").setFont(font);
        doc.add(p);

        PdfFont bold = PdfFontFactory.createFont(FONTBOLD, PdfEncodings.IDENTITY_H);

        // "FreeSans bold: Đ"
        p = new Paragraph("FreeSans bold: \u0110").setFont(bold);
        doc.add(p);

        doc.close();
    }
}
