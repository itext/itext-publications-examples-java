package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.IOException;

public class DivExample {
    public final static String DEST = "./target/sandbox/objects/divExample.pdf";

    public final static String TEXT1 = "Test document which can be altered and ignored. "
            + "This text should come above the rectangle.";
    public final static String TEXT2 = "Test document which can be altered and ignored. "
            + "Some text in the Div. For more information, please visit https://itextpdf.com/";
    public final static String TEXT3 = "This text should come below the rectangle "
            + "and thereafter a normal flow should happen ";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DivExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDocument);

        Paragraph para = new Paragraph(TEXT1);
        doc.add(para);
        doc.add(para);

        Paragraph divHeader = new Paragraph("Notice:")
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD));
        Paragraph divText = new Paragraph(TEXT2)
                .setFontSize(11);
        Div div = new Div()
                .add(divHeader)
                .add(divText)
                .setWidth(400)
                .setPadding(3f)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setBorder(new SolidBorder(0.5f));
        doc.add(div);

        para = new Paragraph(TEXT3);
        doc.add(para);

        pdfDocument.close();
    }
}
