package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class FillFormSpecialChars2 {
    public static final String DEST = "./target/sandbox/acroforms/fill_form_special_chars2.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    public static final String SRC = "./src/main/resources/pdfs/form.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillFormSpecialChars2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // This method tells to generate an appearance Stream while flattening for all form fields that don't have one.
        // Generating appearances will slow down form flattening,
        // but otherwise the results can be unexpected in Acrobat.
        form.setGenerateAppearance(true);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        // ӧ character is used here
        form.getField("Name").setValue("\u04e711111", font, 12f);

        // If no fields have been explicitly included, then all fields are flattened.
        // Otherwise only the included fields are flattened.
        form.flattenFields();

        pdfDoc.close();
    }
}
