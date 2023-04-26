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

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FillFormFieldOrder {
    public static final String DEST = "./target/sandbox/acroforms/fill_form_field_order.pdf";

    public static final String SRC = "./src/main/resources/pdfs/calendar_example.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FillFormFieldOrder().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {

        // Partially flattened form's byte array with content, which should placed beneath the other content.
        byte[] tempForm = createPartiallyFlattenedForm();
        createResultantPdf(dest, tempForm);
    }

    public byte[] createPartiallyFlattenedForm() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);

        Map<String, PdfFormField> fields = form.getAllFormFields();
        fields.get("sunday_1").setValue("1");
        fields.get("sunday_2").setValue("2");
        fields.get("sunday_3").setValue("3");
        fields.get("sunday_4").setValue("4");
        fields.get("sunday_5").setValue("5");
        fields.get("sunday_6").setValue("6");

        // Add the field, identified by name, to the list of fields to be flattened
        form.partialFormFlattening("sunday_1");
        form.partialFormFlattening("sunday_2");
        form.partialFormFlattening("sunday_3");
        form.partialFormFlattening("sunday_4");
        form.partialFormFlattening("sunday_5");
        form.partialFormFlattening("sunday_6");

        // Only the included above fields are flattened.
        // If no fields have been explicitly included, then all fields are flattened.
        form.flattenFields();

        doc.close();

        return baos.toByteArray();
    }

    public void createResultantPdf(String dest, byte[] src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new RandomAccessSourceFactory().createSource(src),
                new ReaderProperties()), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Map<String, PdfFormField> fields = form.getAllFormFields();
        fields.get("sunday_1_notes").setValue("It's Sunday today, let's go to the sea")
                .getFirstFormAnnotation().setBorderWidth(0);
        fields.get("sunday_2_notes").setValue("It's Sunday today, let's go to the park")
                .getFirstFormAnnotation().setBorderWidth(0);
        fields.get("sunday_3_notes").setValue("It's Sunday today, let's go to the beach")
                .getFirstFormAnnotation().setBorderWidth(0);
        fields.get("sunday_4_notes").setValue("It's Sunday today, let's go to the woods")
                .getFirstFormAnnotation().setBorderWidth(0);
        fields.get("sunday_5_notes").setValue("It's Sunday today, let's go to the lake")
                .getFirstFormAnnotation().setBorderWidth(0);
        fields.get("sunday_6_notes").setValue("It's Sunday today, let's go to the river")
                .getFirstFormAnnotation().setBorderWidth(0);

        // All fields will be flattened, because no fields have been explicitly included
        form.flattenFields();

        pdfDoc.close();
    }
}
