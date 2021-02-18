package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfResources;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfFreeTextAnnotation;

import java.io.File;

public class SetCustomFontInDefaultAppearance {
    public static final String DEST = "./target/sandbox/annotations/customFontInDA.pdf";
    public static final String FONT = "./src/main/resources/font/Vollkorn-Regular.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SetCustomFontInDefaultAppearance().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        PdfFont font = PdfFontFactory.createFont(FONT, null, EmbeddingStrategy.PREFER_EMBEDDED);

        // Set the full font to be included and all subset ranges to be removed.
        font.setSubset(false);

        PdfResources acroResources = new PdfResources();
        PdfName fontResourceName = acroResources.addFont(pdf, font);
        PdfAcroForm.getAcroForm(pdf, true).setDefaultResources(acroResources.getPdfObject());

        Rectangle rect = new Rectangle(100, 700, 200, 120);
        String annotationText = "Annotation text";

        /* Set a default appearance string:
         * Tf - a text font operator
         * 24 - a font size (zero value meas that the font shall be auto-sized)
         * fontResourceName - a font value (shall match a resource name in the Font entry
         * of the default resource dictionary)
         * 1 0 0 rg - a color value (red)
         */
        PdfString daString = new PdfString(fontResourceName + " 24 Tf 1 0 0 rg");
        PdfAnnotation annotation = new PdfFreeTextAnnotation(rect, new PdfString(annotationText, PdfEncodings.UNICODE_BIG))
                .setDefaultAppearance(daString);
        pdf
                .addNewPage()
                .addAnnotation(annotation);

        pdf.close();
    }
}
