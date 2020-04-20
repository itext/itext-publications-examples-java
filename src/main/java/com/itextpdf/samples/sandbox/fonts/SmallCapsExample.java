/**
 * Some text displayed using a Small Caps font.
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class SmallCapsExample {
    public static final String DEST = "./target/sandbox/fonts/small_caps_example.pdf";

    public static final String FONT = "./src/main/resources/font/Delicious-SmallCaps.otf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SmallCapsExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Paragraph p = new Paragraph("This is some text displayed using a Small Caps font.")
                .setFont(font);
        doc.add(p);

        doc.close();
    }
}
