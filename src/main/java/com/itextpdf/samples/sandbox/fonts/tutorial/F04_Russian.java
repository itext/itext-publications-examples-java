package com.itextpdf.samples.sandbox.fonts.tutorial;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class F04_Russian {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f04_russian.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F04_Russian().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // CP1250 encoding type doesn't support russian characters,
        // and because of that the text in the resultant pdf will be incorrect.
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.CP1250, EmbeddingStrategy.PREFER_EMBEDDED);
        doc.setFont(font);

        // The text line is "Откуда ты?"
        doc.add(new Paragraph("\u041e\u0442\u043a\u0443\u0434\u0430 \u0442\u044b?"));

        // The text line is "Увидимся позже. Увидимся."
        doc.add(new Paragraph("\u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f "
                + "\u043f\u043E\u0437\u0436\u0435. \u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f."));

        // The text line is "Позвольте мне представиться."
        doc.add(new Paragraph("\u041f\u043e\u0437\u0432\u043e\u043b\u044c\u0442\u0435 \u043c\u043d\u0435 "
                + "\u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u0438\u0442\u044c\u0441\u044f."));

        // The text line is "Это студент."
        doc.add(new Paragraph("\u042d\u0442\u043e \u0441\u0442\u0443\u0434\u0435\u043d\u0442."));

        // The text line is "Хорошо?"
        doc.add(new Paragraph("\u0425\u043e\u0440\u043e\u0448\u043e?"));

        // The text line is "Он инженер. Она доктор."
        doc.add(new Paragraph("\u041e\u043d \u0438\u043d\u0436\u0435\u043d\u0435\u0440. "
                + "\u041e\u043d\u0430 \u0434\u043e\u043a\u0442\u043e\u0440."));

        // The text line is "Это окно."
        doc.add(new Paragraph("\u042d\u0442\u043e \u043e\u043a\u043d\u043e."));

        // The text line is "Повторите, пожалуйста."
        doc.add(new Paragraph("\u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435, "
                + "\u043f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430."));

        doc.close();
    }
}
