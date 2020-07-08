package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;

public class AddAltTags {
    public static final String DEST = "./target/sandbox/tagging/add_alt_tags.pdf";

    public static final String SRC = "./src/main/resources/pdfs/no_alt_attribute.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddAltTags().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfDictionary catalog = pdfDoc.getCatalog().getPdfObject();

        // Gets the root dictionary
        PdfDictionary structTreeRoot = catalog.getAsDictionary(PdfName.StructTreeRoot);
        manipulate(structTreeRoot);

        pdfDoc.close();
    }

    public void manipulate(PdfDictionary element) {
        if (element == null) {
            return;
        }

        // If an element is a figure, adds an /Alt entry.
        if (PdfName.Figure.equals(element.get(PdfName.S))) {
            element.put(PdfName.Alt, new PdfString("Figure without an Alt description"));
        }

        PdfArray kids = element.getAsArray(PdfName.K);

        if (kids == null) {
            return;
        }

        // Loops over all the kids
        for (int i = 0; i < kids.size(); i++) {
            manipulate(kids.getAsDictionary(i));
        }
    }
}
