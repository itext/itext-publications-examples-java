/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20401125/overlapping-characters-in-text-field-itext-pdf
 * <p/>
 * Sometimes you need to change the font of a field.
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;


@Category(SampleTest.class)
public class FillFormSpecialChars extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/fill_form_special_chars.pdf";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";
    public static final String SRC = "./src/test/resources/pdfs/test.pdf";
    public static final String VALUE = "\u011b\u0161\u010d\u0159\u017e\u00fd\u00e1\u00ed\u00e9";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFormSpecialChars().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.setGenerateAppearance(true);

        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        form.getField("test").setValue(VALUE, font, 12f);
        form.getField("test2").setValue(VALUE, font, 12f);

        pdfDoc.close();
    }
}
