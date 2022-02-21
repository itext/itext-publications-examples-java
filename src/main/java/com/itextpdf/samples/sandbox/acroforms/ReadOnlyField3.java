package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.fields.TextFormFieldBuilder;
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
import java.io.FileInputStream;
import java.io.IOException;

public class ReadOnlyField3 {
    public static final String DEST = "./target/sandbox/acroforms/read_only_field3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ReadOnlyField3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // createForm() method creates a temporary document in the memory,
        // which then will be used as a source while writing to a real document
        byte[] content = createForm();
        IRandomAccessSource source = new RandomAccessSourceFactory().createSource(content);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(source, new ReaderProperties()), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        //  Set a flag specifying whether to construct appearance streams and appearance dictionaries
        //  for all widget annotations in the document.
        form.setNeedAppearances(true);

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
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        PdfFont font = PdfFontFactory.createFont();

        Rectangle rect = new Rectangle(36, 770, 108, 36);
        PdfTextFormField textField1 = new TextFormFieldBuilder(pdfDoc, "text1")
                .setWidgetRectangle(rect).createText();
        textField1.setFont(font).setFontSize(18f).setValue("text1");

        // Being set as true, the field can contain multiple lines of text;
        // if false, the field's text is restricted to a single line.
        textField1.setMultiline(true);
        form.addField(textField1);

        rect = new Rectangle(148, 770, 108, 36);
        PdfTextFormField textField2 = new TextFormFieldBuilder(pdfDoc, "text2")
                .setWidgetRectangle(rect).createText();
        textField2.setFont(font).setFontSize(18f).setValue("text2");
        textField2.setMultiline(true);
        form.addField(textField2);

        rect = new Rectangle(36, 724, 108, 36);
        PdfTextFormField textField3 = new TextFormFieldBuilder(pdfDoc, "text3")
                .setWidgetRectangle(rect).createText();
        textField3.setFont(font).setFontSize(18f).setValue("text3");
        textField3.setMultiline(true);
        form.addField(textField3);

        rect = new Rectangle(148, 727, 108, 33);
        PdfTextFormField textField4 = new TextFormFieldBuilder(pdfDoc, "text4")
                .setWidgetRectangle(rect).createText();
        textField4.setFont(font).setFontSize(18f).setValue("text4");
        textField4.setMultiline(true);
        form.addField(textField4);

        pdfDoc.close();

        return baos.toByteArray();
    }
}
