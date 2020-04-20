package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.FontMetrics;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class MultiLineFieldCount {
    public static final String DEST = "./target/sandbox/acroforms/multiLineFieldCount.pdf";
    public static final String SRC = "./src/main/resources/pdfs/multiline.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MultiLineFieldCount().manipulatePdf(SRC, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws Exception {
        PdfDocument pdfDoc =  new PdfDocument(new PdfReader(src), new PdfWriter(dest));
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDoc, false);

        passData(acroForm);

        pdfDoc.close();
    }

    private void passData(PdfAcroForm acroForm) {
        String character = " *";
        StringBuilder sb = new StringBuilder();

        for (String name : acroForm.getFormFields().keySet()) {
            for (int i = 0; i < getInfo(character, acroForm, name); i++) {
                sb.append(character);
            }

            String filler = sb.toString();
            PdfFormField formField = acroForm.getField(name);
            formField.setValue(name + filler);
        }
    }

    private int getInfo(String character, PdfAcroForm form, String name) {
        PdfFormField field = form.getField(name);
        PdfFont font = field.getFont();
        FontMetrics fontMetrics = font.getFontProgram().getFontMetrics();
        float fontSize = field.getFontSize();

        if (fontSize == 0) {
            return 1000;
        }

        Rectangle rectangle = field.getWidgets().get(0).getRectangle().toRectangle();

        // Factor here is a leading value. We calculate it by subtracting lower left corner value from
        // the upper right corner value of the glyph bounding box
        float factor = (fontMetrics.getBbox()[3] - fontMetrics.getBbox()[1]) / 1000f;

        int rows = Math.round(rectangle.getHeight() / (fontSize * factor) + 0.5f);
        int columns = Math.round(rectangle.getWidth() / font.getWidth(character, fontSize) + 0.5f);

        return rows * columns;
    }
}
