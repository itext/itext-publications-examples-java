package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.PatternColor;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfPatternCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;

public class TextPattern {
    public static final String DEST = "./target/sandbox/objects/text_pattern.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TextPattern().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        String fillText = "this is the fill text! ";
        float fillTextWidth = font.getWidth(fillText, 6);

        PdfPattern.Tiling tilingPattern = new PdfPattern.Tiling(fillTextWidth, 60, fillTextWidth, 60);
        PdfPatternCanvas patternCanvas = new PdfPatternCanvas(tilingPattern, pdfDoc);
        patternCanvas.beginText().setFontAndSize(font, 6.f);
        float x = 0;
        for (float y = 0; y < 60; y += 10) {
            patternCanvas.setTextMatrix(x - fillTextWidth, y);
            patternCanvas.showText(fillText);
            patternCanvas.setTextMatrix(x, y);
            patternCanvas.showText(fillText);
            x += (fillTextWidth / 6);
        }
        patternCanvas.endText();

        canvas.rectangle(10, 10, 575, 822);
        canvas.setFillColor(new PatternColor(tilingPattern));
        canvas.fill();

        doc.close();
    }
}
