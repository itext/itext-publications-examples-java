/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/30233783/alignment-issue-with-multiline-text-form-fields-in-itextsharp
 */
package com.itextpdf.samples.sandbox.acroforms;

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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class MultiLineField extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/multi_line_field.pdf";
    public static final String SRC = "./src/test/resources/pdfs/multinewline.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MultiLineField().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory().createSource(createForm()),
                new ReaderProperties()), new PdfWriter(DEST));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        form.getField("text").setValue("A B C D E F\nG H I J K L M N\nO P Q R S T U\r\nV W X Y Z\n\nAlphabet street");
        form.flattenFields();
        pdfDoc.close();
    }

    public byte[] createForm() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));

        Rectangle rect = new Rectangle(36, 720, 108, 86);
        PdfTextFormField tf = PdfFormField.createText(pdfDoc, rect, "text", "text");
        tf.setMultiline(true);

        PdfAcroForm.getAcroForm(pdfDoc, true).addField(tf);

        pdfDoc.close();
        return baos.toByteArray();
    }
}
