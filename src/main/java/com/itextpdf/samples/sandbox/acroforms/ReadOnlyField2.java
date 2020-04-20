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

public class ReadOnlyField2 {
    public static final String DEST = "./target/sandbox/acroforms/read_only_field2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReadOnlyField2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // createForm() method creates a temporary document in the memory,
        // which then will be used as a source while writing to a real document
        byte[] content = createForm();
        IRandomAccessSource source = new RandomAccessSourceFactory().createSource(content);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(source, new ReaderProperties()), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField("text1")

                // Method sets the flag, specifying whether or not the field can be changed.
                .setReadOnly(true)
                .setValue("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");

        form.getField("text2")
                .setReadOnly(true)
                .setValue("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");

        form.getField("text3")
                .setReadOnly(true)
                .setValue("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");

        form.getField("text4")
                .setReadOnly(true)
                .setValue("A B C D E F G H I J K L M N O P Q R S T U V W X Y Z");

        pdfDoc.close();
    }

    public byte[] createForm() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        PdfFont font = PdfFontFactory.createFont();
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Rectangle rect = new Rectangle(36, 770, 108, 36);
        PdfTextFormField textField1 = PdfFormField.createText(pdfDoc, rect, "text1",
                "text1", font, 18f);

        // Being set as true, the field can contain multiple lines of text;
        // if false, the field's text is restricted to a single line.
        textField1.setMultiline(true);
        form.addField(textField1);

        rect = new Rectangle(148, 770, 108, 36);
        PdfTextFormField textField2 = PdfFormField.createText(pdfDoc, rect, "text2",
                "text2", font, 18f);
        textField2.setMultiline(true);
        form.addField(textField2);

        rect = new Rectangle(36, 724, 108, 36);
        PdfTextFormField textField3 = PdfFormField.createText(pdfDoc, rect, "text3",
                "text3", font, 18f);
        textField3.setMultiline(true);
        form.addField(textField3);

        rect = new Rectangle(148, 727, 108, 33);
        PdfTextFormField textField4 = PdfFormField.createText(pdfDoc, rect, "text4",
                "text4", font, 18f);
        textField4.setMultiline(true);
        form.addField(textField4);

        pdfDoc.close();

        return baos.toByteArray();
    }
}
