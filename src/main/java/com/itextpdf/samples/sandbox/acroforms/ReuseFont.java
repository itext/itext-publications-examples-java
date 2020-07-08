package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.IOException;

public class ReuseFont {
    public static final String DEST = "./target/sandbox/acroforms/reuse_font.pdf";

    public static final String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReuseFont().manipulatePdf(DEST);
    }

    /*
     * Method searches and returns font object by the passed font name.
     */
    public PdfFont findFontInForm(PdfDocument pdfDoc, PdfName fontName) throws IOException {
        PdfDictionary acroformDict = pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.AcroForm);
        if (acroformDict == null) {
            return null;
        }

        PdfDictionary dr = acroformDict.getAsDictionary(PdfName.DR);
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

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

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
