package com.itextpdf.samples.sandbox.fonts.tutorial;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class F06_Different_encodings {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f06_different_encodings.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F06_Different_encodings().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont french = PdfFontFactory.createFont(FONT, "WINANSI", EmbeddingStrategy.PREFER_EMBEDDED);
        PdfFont czech = PdfFontFactory.createFont(FONT, "Cp1250", EmbeddingStrategy.PREFER_EMBEDDED);
        PdfFont russian = PdfFontFactory.createFont(FONT, "Cp1251", EmbeddingStrategy.PREFER_EMBEDDED);

        // The text line is "Vous êtes d'où?"
        doc.add(new Paragraph("Vous \u00eates d'o\u00f9?").setFont(french));

        // The text line is "À tout à l'heure. À bientôt."
        doc.add(new Paragraph("\u00c0 tout \u00e0 l'heure. \u00c0 bient\u00f4t.").setFont(french));

        // The text line is "Je me présente."
        doc.add(new Paragraph("Je me pr\u00e9sente.").setFont(french));

        // The text line is "C'est un étudiant."
        doc.add(new Paragraph("C'est un \u00e9tudiant.").setFont(french));

        // The text line is "Ça va?"
        doc.add(new Paragraph("\u00c7a va?").setFont(french));

        // The text line is "Il est ingénieur. Elle est médecin."
        doc.add(new Paragraph("Il est ing\u00e9nieur. Elle est m\u00e9decin.").setFont(french));

        // The text line is "C'est une fenêtre."
        doc.add(new Paragraph("C'est une fen\u00eatre.").setFont(french));

        // The text line is "Répétez, s'il vous plaît."
        doc.add(new Paragraph("R\u00e9p\u00e9tez, s'il vous pla\u00eet.").setFont(french));
        doc.add(new Paragraph("Odkud jste?").setFont(czech));

        // The text line is "Uvidíme se za chvilku. Měj se."
        doc.add(new Paragraph("Uvid\u00edme se za chvilku. M\u011bj se.").setFont(czech));

        // The text line is "Dovolte, abych se představil."
        doc.add(new Paragraph("Dovolte, abych se p\u0159edstavil.").setFont(czech));
        doc.add(new Paragraph("To je studentka.").setFont(czech));

        // The text line is "Všechno v pořádku?"
        doc.add(new Paragraph("V\u0161echno v po\u0159\u00e1dku?").setFont(czech));

        // The text line is "On je inženýr. Ona je lékař."
        doc.add(new Paragraph("On je in\u017een\u00fdr. Ona je l\u00e9ka\u0159.").setFont(czech));
        doc.add(new Paragraph("Toto je okno.").setFont(czech));

        // The text line is "Zopakujte to prosím"
        doc.add(new Paragraph("Zopakujte to pros\u00edm.").setFont(czech));

        // The text line is "Откуда ты?"
        doc.add(new Paragraph("\u041e\u0442\u043a\u0443\u0434\u0430 \u0442\u044b?")
                .setFont(russian));

        // The text line is "Увидимся позже. Увидимся."
        doc.add(new Paragraph("\u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f "
                        + "\u043f\u043E\u0437\u0436\u0435. \u0423\u0432\u0438\u0434\u0438\u043c\u0441\u044f.")
                .setFont(russian));

        // The text line is "Позвольте мне представиться."
        doc.add(new Paragraph("\u041f\u043e\u0437\u0432\u043e\u043b\u044c\u0442\u0435 \u043c\u043d\u0435 "
                        + "\u043f\u0440\u0435\u0434\u0441\u0442\u0430\u0432\u0438\u0442\u044c\u0441\u044f.")
                .setFont(russian));

        // The text line is "Это студент."
        doc.add(new Paragraph("\u042d\u0442\u043e \u0441\u0442\u0443\u0434\u0435\u043d\u0442.")
                .setFont(russian));

        // The text line is "Хорошо?"
        doc.add(new Paragraph("\u0425\u043e\u0440\u043e\u0448\u043e?")
                .setFont(russian));

        // The text line is "Он инженер. Она доктор."
        doc.add(new Paragraph("\u041e\u043d \u0438\u043d\u0436\u0435\u043d\u0435\u0440. "
                        + "\u041e\u043d\u0430 \u0434\u043e\u043a\u0442\u043e\u0440.")
                .setFont(russian));

        // The text line is "Это окно."
        doc.add(new Paragraph("\u042d\u0442\u043e \u043e\u043a\u043d\u043e.")
                .setFont(russian));

        // The text line is "Повторите, пожалуйста."
        doc.add(new Paragraph("\u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u0435, "
                        + "\u043f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430.")
                .setFont(russian));

        doc.close();
    }
}
