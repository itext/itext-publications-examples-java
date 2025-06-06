package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.Property;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RemoteGoto {
    public static final String DEST = "./target/sandbox/annotations/";

    public static final String[] DEST_NAMES = {
            "remote_goto.pdf",
            "subdir/xyz.pdf"
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST + "subdir/");
        file.mkdirs();

        new RemoteGoto().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        createLinkPdf(dest + DEST_NAMES[0]);
        createDestinationPdf(dest + DEST_NAMES[1]);
    }

    // This method creates a link destination pdf file.
    private void createDestinationPdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph anchor = new Paragraph("This is a destination");

        // Set string destination, to which the created in the another pdf file link will lead.
        Set<Object> destinations = new HashSet<>();
        destinations.add("dest");
        anchor.setProperty(Property.DESTINATION, destinations);
        doc.add(anchor);

        doc.close();
    }

    // This method creates a pdf file, which will contain a link
    // to the page with set string destination of another pdf file.
    private static void createLinkPdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Create a link action, which leads to the another pdf file's page.
        // The 1st argument is the relative destination pdf file's path;
        // the 2nd argument is the string destination in the destination pdf file,
        // to which the link will lead after a click on it.
        PdfAction action = PdfAction.createGoToR(DEST_NAMES[1], "dest");
        Paragraph chunk = new Paragraph(new Link("Link", action));
        doc.add(chunk);

        doc.close();
    }
}
