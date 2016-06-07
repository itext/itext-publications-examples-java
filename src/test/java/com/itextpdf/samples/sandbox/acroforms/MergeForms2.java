/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/26174675/copy-pdf-with-annotations-using-itext
 */
package com.itextpdf.samples.sandbox.acroforms;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class MergeForms2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/merge_forms2.pdf";
    public static final String SRC = "./src/test/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new MergeForms2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.initializeOutlines();
        for (int i = 0; i < 3; ) {
            PdfDocument readerDoc = new PdfDocument(new PdfReader(
                    new RandomAccessSourceFactory().createSource(renameFields(SRC, ++i)),
                    new ReaderProperties()));
            readerDoc.copyPagesTo(1, readerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
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
