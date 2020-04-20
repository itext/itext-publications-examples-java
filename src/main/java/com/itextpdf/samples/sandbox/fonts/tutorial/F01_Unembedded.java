package com.itextpdf.samples.sandbox.fonts.tutorial;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class F01_Unembedded {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f01_unembedded.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F01_Unembedded().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

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

        doc.close();
    }
}
