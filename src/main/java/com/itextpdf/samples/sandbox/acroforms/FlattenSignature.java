package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;

import java.io.File;

public class FlattenSignature {
    public static final String DEST = "./target/sandbox/acroforms/flatten_signature.pdf";
    public static final String SRC = "./src/main/resources/pdfs/input_signed.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FlattenSignature().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        // If no fields have been explicitly included, then all fields are flattened.
        // Otherwise only the included fields are flattened.
        form.flattenFields();

        pdfDoc.close();
    }
}
