package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;

import java.io.File;
import java.io.IOException;

public class ReadOnlyField {
    public static final String DEST = "./target/sandbox/acroforms/read_only_field.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReadOnlyField().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // createForm() method creates a temporary document in the memory,
        // which then will be used as a source while writing to a real document
        byte[] content = createForm();
        IRandomAccessSource source = new RandomAccessSourceFactory().createSource(content);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(source, new ReaderProperties()), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField("text")

                // Method sets the flag, specifying whether or not the field can be changed.
                .setReadOnly(true)
                .setValue("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");

        pdfDoc.close();
    }

    public byte[] createForm() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        PdfFont font = PdfFontFactory.createFont();
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Rectangle rect = new Rectangle(36, 770, 104, 36);
        PdfTextFormField textField = PdfFormField.createText(pdfDoc, rect, "text",
                "text", font, 20f);

        // Being set as true, the field can contain multiple lines of text;
        // if false, the field's text is restricted to a single line.
        textField.setMultiline(true);
        form.addField(textField);

        pdfDoc.close();

        return baos.toByteArray();
    }
}
