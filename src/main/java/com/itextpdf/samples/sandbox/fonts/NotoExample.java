package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class NotoExample {
    public static final String DEST = "./target/sandbox/fonts/chinese.pdf";

    public static final String FONT = "./src/main/resources/font/NotoSansCJKsc-Regular.otf";

    /*
     * "These are the protagonists in 'Hero', a movie by Zhang Yimou:
     * 無名 (Nameless), 殘劍 (Broken Sword), 飛雪 (Flying Snow), 如月 (Moon), 秦王 (the King), and 長空 (Sky)."
     */
    public static final String TEXT = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
            + "\u7121\u540d (Nameless), \u6b98\u528d (Broken Sword), "
            + "\u98db\u96ea (Flying Snow), \u5982\u6708 (Moon), "
            + "\u79e6\u738b (the King), and \u9577\u7a7a (Sky).";

    // "十锊埋伏"
    public static final String CHINESE = "\u5341\u950a\u57cb\u4f0f";

    // "誰も知らない"
    public static final String JAPANESE = "\u8ab0\u3082\u77e5\u3089\u306a\u3044";

    // "빈집"
    public static final String KOREAN = "\ube48\uc9d1";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new NotoExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        doc.setFont(font);

        doc.add(new Paragraph(TEXT));
        doc.add(new Paragraph(CHINESE));
        doc.add(new Paragraph(JAPANESE));
        doc.add(new Paragraph(KOREAN));

        doc.close();
    }
}
