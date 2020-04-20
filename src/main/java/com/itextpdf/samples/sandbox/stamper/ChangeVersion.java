package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.File;

public class ChangeVersion {
    public static final String DEST = "./target/sandbox/stamper/change_version.pdf";
    public static final String SRC = "./src/main/resources/pdfs/OCR.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeVersion().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC),

                // Please note that the default PdfVersion value is PDF 1.7
                new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_5)));

        pdfDoc.close();
    }
}
