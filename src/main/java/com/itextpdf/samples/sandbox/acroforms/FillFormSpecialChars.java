package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class FillFormSpecialChars {
    public static final String DEST = "./target/sandbox/acroforms/fill_form_special_chars.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    public static final String SRC = "./src/main/resources/pdfs/test.pdf";

    // ěščřžýáíé characters
    public static final String VALUE = "\u011b\u0161\u010d\u0159\u017e\u00fd\u00e1\u00ed\u00e9";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillFormSpecialChars().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Being set as true, this parameter is responsible to generate an appearance Stream
        // while flattening for all form fields that don't have one. Generating appearances will
        // slow down form flattening, but otherwise Acrobat might render the pdf on its own rules.
        form.setGenerateAppearance(true);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        form.getField("test").setValue(VALUE, font, 12f);
        form.getField("test2").setValue(VALUE, font, 12f);

        pdfDoc.close();
    }
}
