/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/37110820/itext-setting-creation-date-modified-date-in-sandbox-stamper-superimpose-java
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@Category(SampleTest.class)
public class ChangeMetadata extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/change_meta_data.pdf";
    public static final String SRC = "./src/test/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeMetadata().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        // the last parameter in order to
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST, new WriterProperties().addXmpMetadata()));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("New title");
        info.addCreationDate();
        pdfDoc.close();
    }
}
