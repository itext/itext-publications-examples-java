package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;

public class AddOpenAction {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/stamper/add_open_action.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddOpenAction().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfPage page1 = pdfDoc.getPage(1);
        float page1Height = page1.getPageSize().getHeight();
        PdfDestination pdfDestination = PdfExplicitDestination.createXYZ(page1, 0, page1Height, 0.75f);
        pdfDoc.getCatalog().setOpenAction(pdfDestination);
        pdfDoc.close();
    }
}
