package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPMetaFactory;

import java.io.File;

public class AddXmpToPage {
    public static final String DEST = "./target/sandbox/stamper/add_xmp_to_page.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddXmpToPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));

        PdfPage page = pdfDoc.getFirstPage();
        page.setXmpMetadata(XMPMetaFactory.create());

        pdfDoc.close();
    }
}
