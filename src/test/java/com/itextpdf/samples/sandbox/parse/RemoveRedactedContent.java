/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24037282/any-way-to-create-redactions
 */
package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class RemoveRedactedContent extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/parse/remove_redacted_content.pdf";
    public static final String SRC = "./src/test/resources/pdfs/page229_redacted.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RemoveRedactedContent().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        //Load the license file to use cleanup features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-multiple-products.xml");

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, true);
        cleaner.cleanUp();

        pdfDoc.close();
    }

}
