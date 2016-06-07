/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is based on an answer given on StackOverflow:
 * http://stackoverflow.com/questions/22186014/itextsharp-re-use-font-embedded-in-acrofield
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class ReuseFont extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/reuse_font.pdf";
    public static final String SRC = "./src/test/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ReuseFont().manipulatePdf(DEST);
    }

    public PdfFont findFontInForm(PdfDocument pdfDoc, PdfName fontName) throws IOException {
        PdfDictionary acroForm = pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.AcroForm);
        if (acroForm == null) {
            return null;
        }
        PdfDictionary dr = acroForm.getAsDictionary(PdfName.DR);
        if (dr == null) {
            return null;
        }
        PdfDictionary font = dr.getAsDictionary(PdfName.Font);
        if (font == null) {
            return null;
        }
        for (PdfName key : font.keySet()) {
            if (key.equals(fontName)) {
                return PdfFontFactory.createFont(font.getAsDictionary(key));
            }
        }
        return null;
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfFont font = findFontInForm(pdfDoc, new PdfName("Calibri"));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas.beginText();
        canvas.setFontAndSize(font, 13);
        canvas.moveText(36, 806);
        canvas.showText("Some text in Calibri");
        canvas.endText();
        canvas.stroke();

        pdfDoc.close();
    }
}
