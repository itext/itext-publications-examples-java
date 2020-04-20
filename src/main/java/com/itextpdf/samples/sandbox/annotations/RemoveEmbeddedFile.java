package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfArray;

import java.io.File;

public class RemoveEmbeddedFile {
    public static final String DEST = "./target/sandbox/annotations/remove_embedded_file.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_with_attachment.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RemoveEmbeddedFile().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary names = root.getAsDictionary(PdfName.Names);
        PdfDictionary embeddedFiles = names.getAsDictionary(PdfName.EmbeddedFiles);
        PdfArray namesArray = embeddedFiles.getAsArray(PdfName.Names);

        // Remove the description of the embedded file
        namesArray.remove(0);

        // Remove the reference to the embedded file.
        namesArray.remove(0);

        pdfDoc.close();
    }
}
