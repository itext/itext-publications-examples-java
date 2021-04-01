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

public class F08_Unicode {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f08_unicode.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F08_Unicode().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // The 3rd argument indicates whether the font is to be embedded into the target document
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H,
                EmbeddingStrategy.PREFER_NOT_EMBEDDED);
        doc.setFont(font);

        // The text line is "Vous êtes d'où?"
        doc.add(new Paragraph("Vous \u00EAtes d\'o\u00F9?"));

        // The text line is "À tout à l'heure. À bientôt."
        doc.add(new Paragraph("\u00C0 tout \u00E0 l\'heure. \u00C0 bient\u00F4t."));

        // The text line is "Je me présente."
        doc.add(new Paragraph("Je me pr\u00E9sente."));

        // The text line is "C'est un étudiant."
        doc.add(new Paragraph("C\'est un \u00E9tudiant."));

        // The text line is "Ça va?"
        doc.add(new Paragraph("\u00C7a va?"));

        // The text line is "Il est ingénieur. Elle est médecin."
        doc.add(new Paragraph("Il est ing\u00E9nieur. Elle est m\u00E9decin."));

        // The text line is "C'est une fenêtre."
        doc.add(new Paragraph("C\'est une fen\u00EAtre."));

        // The text line is "Répétez, s'il vous plaît."
        doc.add(new Paragraph("R\u00E9p\u00E9tez, s\'il vous pla\u00EEt."));
        doc.add(new Paragraph("Odkud jste?"));

        // The text line is "Uvidíme se za chvilku. Měj se."
        doc.add(new Paragraph("Uvid\u00EDme se za chvilku. M\u011Bj se."));

        // The text line is "Dovolte, abych se představil."
        doc.add(new Paragraph("Dovolte, abych se p\u0159edstavil."));
        doc.add(new Paragraph("To je studentka."));

        // The text line is "Všechno v pořádku?"
        doc.add(new Paragraph("V\u0161echno v po\u0159\u00E1dku?"));

        // The text line is "On je inženýr. Ona je lékař."
        doc.add(new Paragraph("On je in\u017Een\u00FDr. Ona je l\u00E9ka\u0159."));
        doc.add(new Paragraph("Toto je okno."));

        // The text line is "Zopakujte to prosím"
        doc.add(new Paragraph("Zopakujte to pros\u00EDm."));

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
