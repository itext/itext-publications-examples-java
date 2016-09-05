/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/29633035/change-acrofields-order-in-existing-pdf-with-itext
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Category(SampleTest.class)
public class FillFormFieldOrder extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/fill_form_field_order.pdf";
    public static final String SRC = "./src/test/resources/pdfs/calendar_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFormFieldOrder().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        go2(go1());
    }

    public byte[] go1() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(baos));
        Document doc = new Document(pdfDoc);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
        Map<String, PdfFormField> fields = form.getFormFields();

        fields.get("sunday_1").setValue("1");
        fields.get("sunday_2").setValue("2");
        fields.get("sunday_3").setValue("3");
        fields.get("sunday_4").setValue("4");
        fields.get("sunday_5").setValue("5");
        fields.get("sunday_6").setValue("6");

        form.partialFormFlattening("sunday_1");
        form.partialFormFlattening("sunday_2");
        form.partialFormFlattening("sunday_3");
        form.partialFormFlattening("sunday_4");
        form.partialFormFlattening("sunday_5");
        form.partialFormFlattening("sunday_6");

        form.flattenFields();
        doc.close();

        return baos.toByteArray();
    }

    public void go2(byte[] src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory().createSource(src),
                new ReaderProperties()), new PdfWriter(DEST));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Map<String, PdfFormField> fields = form.getFormFields();
        fields.get("sunday_1_notes").setValue("It's Sunday today, let's go to the sea").setBorderWidth(0);
        fields.get("sunday_2_notes").setValue("It's Sunday today, let's go to the park").setBorderWidth(0);
        fields.get("sunday_3_notes").setValue("It's Sunday today, let's go to the beach").setBorderWidth(0);
        fields.get("sunday_4_notes").setValue("It's Sunday today, let's go to the woods").setBorderWidth(0);
        fields.get("sunday_5_notes").setValue("It's Sunday today, let's go to the lake").setBorderWidth(0);
        fields.get("sunday_6_notes").setValue("It's Sunday today, let's go to the river").setBorderWidth(0);

        form.flattenFields();
        pdfDoc.close();
    }
}
