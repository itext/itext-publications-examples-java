package com.itextpdf.samples.sandbox.fonts.tutorial;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class F02_Unembedded {
    public static final String DEST = "./target/sandbox/fonts/tutorial/f02_unembedded.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new F02_Unembedded().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

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

        doc.close();
    }
}
