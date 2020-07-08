package com.itextpdf.samples.sandbox.interactive;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.IOException;

public class BaseURL3 {
    public static final String DEST = "./target/sandbox/interactive/base_url3.pdf";

    public static final String SRC = "./src/main/resources/pdfs/base_url.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new BaseURL3().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfDictionary uri = new PdfDictionary();
        uri.put(PdfName.Type, PdfName.URI);
        uri.put(new PdfName("Base"), new PdfString("http://itextpdf.com/"));
        pdfDoc.getCatalog().put(PdfName.URI, uri);

        pdfDoc.close();
    }
}
