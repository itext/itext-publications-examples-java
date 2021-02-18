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

public class F03_Embedded {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f03_embedded.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F03_Embedded().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.CP1250, EmbeddingStrategy.PREFER_EMBEDDED);

        // Font is an inheritable property, setting it on a document implies that this font
        // will be used for document's children, unless they have this property overwritten.
        doc.setFont(font);

        doc.add(new Paragraph("Odkud jste?"));

        // The text line is "Uvidíme se za chvilku. Měj se."
        doc.add(new Paragraph("Uvid\u00edme se za chvilku. M\u011bj se."));

        // The text line is "Dovolte, abych se představil."
        doc.add(new Paragraph("Dovolte, abych se p\u0159edstavil."));
        doc.add(new Paragraph("To je studentka."));

        // The text line is "Všechno v pořádku?"
        doc.add(new Paragraph("V\u0161echno v po\u0159\u00e1dku?"));

        // The text line is "On je inženýr. Ona je lékař."
        doc.add(new Paragraph("On je in\u017een\u00fdr. Ona je l\u00e9ka\u0159."));
        doc.add(new Paragraph("Toto je okno."));

        // The text line is "Zopakujte to prosím"
        doc.add(new Paragraph("Zopakujte to pros\u00edm."));

        doc.close();
    }
}
