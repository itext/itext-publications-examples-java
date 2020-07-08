package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.pdf.ReaderProperties;

import java.io.File;

public class MultiLineField {
    public static final String DEST = "./target/sandbox/acroforms/multi_line_field.pdf";

    public static final String FIELD_NAME = "text";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MultiLineField().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        
        // createForm() method creates a temporary document in the memory,
        // which then will be used as a source while writing to a real document
        byte[] content = createForm();
        IRandomAccessSource source = new RandomAccessSourceFactory().createSource(content);
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(source, new ReaderProperties()), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField(FIELD_NAME).setValue(
                "A B C D E F\nG H I J K L M N\nO P Q R S T U\r\nV W X Y Z\n\nAlphabet street");

        // If no fields have been explicitly included, then all fields are flattened.
        // Otherwise only the included fields are flattened.
        form.flattenFields();

        pdfDoc.close();
    }

    public byte[] createForm() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Rectangle rect = new Rectangle(36, 720, 108, 86);

        PdfTextFormField textFormField = PdfFormField.createText(pdfDoc, rect, FIELD_NAME, "text");

        // Being set as true, the field can contain multiple lines of text;
        // if false, the field's text is restricted to a single line.
        textFormField.setMultiline(true);
        PdfAcroForm.getAcroForm(pdfDoc, true).addField(textFormField);

        pdfDoc.close();

        return baos.toByteArray();
    }
}
