package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.File;

public class ChangeMetadata {
    public static final String DEST = "./target/sandbox/stamper/change_meta_data.pdf";
    public static final String SRC = "./src/main/resources/pdfs/state.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ChangeMetadata().manipulatePdf();
    }

    protected void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC),
                new PdfWriter(DEST, new WriterProperties().addXmpMetadata()));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("New title");
        info.addCreationDate();

        pdfDoc.close();
    }
}
