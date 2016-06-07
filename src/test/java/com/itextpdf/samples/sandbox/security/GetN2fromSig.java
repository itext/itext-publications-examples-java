/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27867868/how-can-i-decrypt-a-pdf-document-with-the-owner-password
 */
package com.itextpdf.samples.sandbox.security;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Map;

@Category(SampleTest.class)
public class GetN2fromSig {
    public static String SRC = "./src/test/resources/pdfs/signature_n2.pdf";
    public static String CMP_RESULT = "BT\n" +
            "1 0 0 1 0 49.55 Tm\n" +
            "/F1 12 Tf\n" +
            "(This document was signed by Bruno)Tj\n" +
            "1 0 0 1 0 31.55 Tm\n" +
            "(Specimen.)Tj\n" +
            "ET";

    protected static String RESULT;

    @Before
    public void before() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Map<String, PdfFormField> fields = form.getFormFields();

        PdfFormField field = fields.get("Signature1");
        PdfDictionary widget = field.getWidgets().get(0).getPdfObject();
        PdfDictionary ap = widget.getAsDictionary(PdfName.AP);
        PdfStream normal = ap.getAsStream(PdfName.N);
        PdfDictionary resources = normal.getAsDictionary(PdfName.Resources);
        PdfDictionary xobject = resources.getAsDictionary(PdfName.XObject);
        PdfStream frm = xobject.getAsStream(new PdfName("FRM"));
        PdfDictionary res = frm.getAsDictionary(PdfName.Resources);
        PdfDictionary xobj = res.getAsDictionary(PdfName.XObject);
        PdfStream n2 = xobj.getAsStream(new PdfName("n2"));
        byte[] stream = n2.getBytes();
        RESULT = new String(stream);
        System.out.println(RESULT);
    }

    @Test
    public void compareResults() throws IOException {
        Assert.assertEquals(CMP_RESULT.trim(), RESULT.trim());
    }
}
