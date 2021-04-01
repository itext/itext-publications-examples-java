package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class ColumnTextPhrase {
    public static final String DEST = "./target/sandbox/objects/column_text_phrase.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextPhrase().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD,
                PdfEncodings.CP1252, EmbeddingStrategy.PREFER_EMBEDDED);
        Paragraph pz = new Paragraph("Hello World!").setFont(font).setFontSize(13);
        new Canvas(new PdfCanvas(pdfDoc.getFirstPage()), new Rectangle(120, 48, 80, 580))
                .add(pz);

        // The Leading is used in this paragraph, the Leading is a spacing between lines of text
        font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        pz = new Paragraph("Hello World!").setFont(font).setFixedLeading(20);
        new Canvas(new PdfCanvas(pdfDoc.getFirstPage()), new Rectangle(120, 48, 80, 480))
                .add(pz);

        pdfDoc.close();
    }
}
