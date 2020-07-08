package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ParagraphSpacingBefore {
    public static final String DEST = "./target/sandbox/objects/paragraph_spacing_before.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ParagraphSpacingBefore().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph paragraph1 = new Paragraph("First paragraph");
        doc.add(paragraph1);

        Paragraph paragraph2 = new Paragraph("Second paragraph");
        paragraph2.setMarginTop(380f);
        doc.add(paragraph2);

        doc.close();
    }
}
