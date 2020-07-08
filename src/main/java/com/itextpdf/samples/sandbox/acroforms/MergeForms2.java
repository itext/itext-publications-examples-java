package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.IRandomAccessSource;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;

import java.io.File;
import java.io.IOException;

public class MergeForms2 {
    public static final String DEST = "./target/sandbox/acroforms/merge_forms2.pdf";

    public static final String SRC = "./src/main/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MergeForms2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // This method initializes an outline tree of the document and sets outline mode to true.
        pdfDoc.initializeOutlines();

        // Copier contains the logic to copy only acroform fields to a new page.
        // PdfPageFormCopier uses some caching logic which can potentially improve performance
        // in case of the reusing of the same instance.
        PdfPageFormCopier formCopier = new PdfPageFormCopier();

        for (int i = 0; i < 3; ) {

            // This method reads source pdf and renames form fields,
            // because the same source pdf with the same form fields will be copied.
            byte[] content = renameFields(SRC, ++i);
            IRandomAccessSource source  = new RandomAccessSourceFactory().createSource(content);
            PdfDocument readerDoc = new PdfDocument(new PdfReader(source, new ReaderProperties()));
            readerDoc.copyPagesTo(1, readerDoc.getNumberOfPages(), pdfDoc, formCopier);
            readerDoc.close();
        }

        pdfDoc.close();
    }

    protected byte[] renameFields(String src, int i) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(baos));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        for (PdfFormField field : form.getFormFields().values()) {
            field.setFieldName(String.format("%s_%d", field.getFieldName().toString(), i));
        }

        pdfDoc.close();

        return baos.toByteArray();
    }
}
