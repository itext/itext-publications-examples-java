package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;

public class ChangeViewerPreference {
    public static final String DEST = "./target/sandbox/stamper/change_viewer_preference.pdf";
    public static final String SRC = "./src/main/resources/pdfs/united_states.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeViewerPreference().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfViewerPreferences viewerPreferences = pdfDoc.getCatalog().getViewerPreferences();
        if (viewerPreferences == null) {
            viewerPreferences = new PdfViewerPreferences();
            pdfDoc.getCatalog().setViewerPreferences(viewerPreferences);
        }

        // Setting printing mode on the both sides of the pdf document (duplex mode) along with "flip on long edge" mode
        viewerPreferences.setDuplex(PdfViewerPreferences.PdfViewerPreferencesConstants.DUPLEX_FLIP_LONG_EDGE);

        pdfDoc.close();
    }
}
