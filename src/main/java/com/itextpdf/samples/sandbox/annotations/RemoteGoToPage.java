package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;

public class RemoteGoToPage {
    public static final String DEST = "./target/sandbox/annotations/";

    public static final String[] DEST_NAMES = {
            "remote_go_to_page.pdf",
            "subdir/xyz2.pdf",
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST + "subdir/");
        file.mkdirs();

        new RemoteGoToPage().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        createLinkPdf(dest + DEST_NAMES[0]);
        createDestinationPdf(dest + DEST_NAMES[1]);
    }

    // This method creates a link destination pdf file.
    private void createDestinationPdf(String src) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(src));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("page 1"));
        for (int i = 2; i < 8; i++) {
            doc.add(new AreaBreak());
            doc.add(new Paragraph("page " + i));
        }

        doc.close();
    }

    // This method creates a pdf file, which will contain a link
    // to the sixth page of another pdf file.
    private static void createLinkPdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Create a link action, which leads to the another pdf file's page.
        // The 1st argument is the relative destination pdf file's path;
        // the 2nd argument is the number of the page (in the destination pdf file),
        // to which the link will lead after a click on it.
        PdfAction action = PdfAction.createGoToR(DEST_NAMES[1], 6);
        Paragraph chunk = new Paragraph(new Link("Link", action));
        doc.add(chunk);

        doc.close();
    }
}
