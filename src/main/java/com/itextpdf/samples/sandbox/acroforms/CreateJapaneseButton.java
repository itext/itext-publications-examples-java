package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class CreateJapaneseButton {
    public static final String DEST = "./target/sandbox/acroforms/create_japanese_button.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    // あ き ら characters
    public static final String JAPANESE_TEXT = "\u3042\u304d\u3089";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateJapaneseButton().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // Define the position of a button that measures 108 by 26
        Rectangle rect = new Rectangle(36, 780, 108, 26);
        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDoc, rect, "japanese",
                JAPANESE_TEXT, font, 12f);
        form.addField(pushButton);

        pdfDoc.close();
    }
}
