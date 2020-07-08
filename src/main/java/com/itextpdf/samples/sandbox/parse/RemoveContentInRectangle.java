package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.pdfcleanup.PdfCleanUpLocation;
import com.itextpdf.pdfcleanup.PdfCleanUpTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RemoveContentInRectangle {
    public static final String DEST = "./target/sandbox/parse/remove_content_in_rectangle.pdf";

    public static final String SRC = "./src/main/resources/pdfs/page229.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RemoveContentInRectangle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        List<PdfCleanUpLocation> cleanUpLocations = new ArrayList<PdfCleanUpLocation>();

        // The arguments of the PdfCleanUpLocation constructor: the number of page to be cleaned up,
        // a Rectangle defining the area on the page we want to clean up,
        // a color which will be used while filling the cleaned area.
        PdfCleanUpLocation location = new PdfCleanUpLocation(1, new Rectangle(97, 405, 383, 40),
                ColorConstants.GRAY);
        cleanUpLocations.add(location);

        PdfCleanUpTool cleaner = new PdfCleanUpTool(pdfDoc, cleanUpLocations);
        cleaner.cleanUp();

        pdfDoc.close();
    }
}
