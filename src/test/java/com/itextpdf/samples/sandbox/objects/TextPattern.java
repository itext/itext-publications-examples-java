/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/31262460/how-to-add-x-offset-to-text-pattern-with-every-x-step-itext
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.PatternColor;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfPatternCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class TextPattern extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/text_pattern.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TextPattern().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        String filltext = "this is the fill text! ";
        float filltextWidth = font.getWidth(filltext, 6);

        PdfPattern.Tiling tilingPattern = new PdfPattern.Tiling(filltextWidth, 60, filltextWidth, 60);
        PdfPatternCanvas patternCanvas = new PdfPatternCanvas(tilingPattern, pdfDoc);
        patternCanvas.beginText().setFontAndSize(font, 6.f);
        float x = 0;
        for (float y = 0; y < 60; y += 10) {
            patternCanvas.setTextMatrix(x - filltextWidth, y);
            patternCanvas.showText(filltext);
            patternCanvas.setTextMatrix(x, y);
            patternCanvas.showText(filltext);
            x += (filltextWidth / 6);
        }
        patternCanvas.endText();

        canvas.rectangle(0, 0, 595, 842);
        canvas.setFillColor(new PatternColor(tilingPattern));
        canvas.fill();

        doc.close();
    }
}
