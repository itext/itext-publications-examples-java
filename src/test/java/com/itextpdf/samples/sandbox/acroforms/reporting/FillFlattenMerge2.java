/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.acroforms.reporting;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.StringTokenizer;

@Category(SampleTest.class)
public class FillFlattenMerge2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/acroforms/reporting/fill_flatten_merge2.pdf";
    public static final String DATA = "./src/test/resources/data/united_states.csv";
    public static final String SRC = "./src/test/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FillFlattenMerge2().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfWriter writer = new PdfWriter(DEST);
        writer.setSmartMode(true);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.initializeOutlines();

        ByteArrayOutputStream baos;
        PdfReader reader;
        PdfDocument pdfInnerDoc;
        Map<String, PdfFormField> fields;
        PdfAcroForm form;
        StringTokenizer tokenizer;

        BufferedReader br = new BufferedReader(new FileReader(DATA));
        String line = br.readLine();
        while ((line = br.readLine()) != null) {
            // create a PDF in memory
            baos = new ByteArrayOutputStream();
            reader = new PdfReader(SRC);
            pdfInnerDoc = new PdfDocument(reader, new PdfWriter(baos));
            form = PdfAcroForm.getAcroForm(pdfInnerDoc, true);
            tokenizer = new StringTokenizer(line, ";");
            fields = form.getFormFields();
            fields.get("name").setValue(tokenizer.nextToken());
            fields.get("abbr").setValue(tokenizer.nextToken());
            fields.get("capital").setValue(tokenizer.nextToken());
            fields.get("city").setValue(tokenizer.nextToken());
            fields.get("population").setValue(tokenizer.nextToken());
            fields.get("surface").setValue(tokenizer.nextToken());
            fields.get("timezone1").setValue(tokenizer.nextToken());
            fields.get("timezone2").setValue(tokenizer.nextToken());
            fields.get("dst").setValue(tokenizer.nextToken());
            form.flattenFields();
            pdfInnerDoc.close();

            pdfInnerDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(baos.toByteArray())));
            pdfInnerDoc.copyPagesTo(1, pdfInnerDoc.getNumberOfPages(), pdfDoc, new PdfPageFormCopier());
            pdfInnerDoc.close();
        }
        br.close();

        pdfDoc.close();
    }
}
