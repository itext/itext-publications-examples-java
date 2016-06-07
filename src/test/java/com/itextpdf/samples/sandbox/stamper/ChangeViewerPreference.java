/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class ChangeViewerPreference extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/stamper/change_viewer_preference.pdf";
    public static final String SRC = "./src/test/resources/pdfs/OCR.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ChangeViewerPreference().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
        PdfViewerPreferences viewerPreferences = pdfDoc.getCatalog().getViewerPreferences();
        if (viewerPreferences == null) {
            viewerPreferences = new PdfViewerPreferences();
            pdfDoc.getCatalog().setViewerPreferences(viewerPreferences);
        }
        viewerPreferences.setDuplex(PdfViewerPreferences.PdfViewerPreferencesConstants.DUPLEX_FLIP_LONG_EDGE);
        pdfDoc.close();
    }
}
