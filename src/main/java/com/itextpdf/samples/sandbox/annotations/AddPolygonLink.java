package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;

import java.io.File;

public class AddPolygonLink {
    public static final String DEST = "./target/sandbox/annotations/add_polygon_link.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddPolygonLink().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        PdfPage firstPage = pdfDoc.getFirstPage();

        PdfCanvas canvas = new PdfCanvas(firstPage);
        canvas
                .moveTo(36, 700)
                .lineTo(72, 760)
                .lineTo(144, 720)
                .lineTo(72, 730)
                .closePathStroke();

        Rectangle linkLocation = new Rectangle(36, 700, 144, 760);

        // Make the link destination page fit to the display
        PdfExplicitDestination destination = PdfExplicitDestination.createFit(firstPage);
        PdfLinkAnnotation linkAnnotation = new PdfLinkAnnotation(linkLocation)

                // Set highlighting type which is enabled after a click on the annotation
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)

                // Create a link to the first page of the document.
                .setAction(PdfAction.createGoTo(destination));
        PdfArray arrayOfQuadPoints = new PdfArray(new int[]{72, 730, 144, 720, 72, 760, 36, 700});
        linkAnnotation.put(PdfName.QuadPoints, arrayOfQuadPoints);

        firstPage.addAnnotation(linkAnnotation);

        doc.close();
    }
}
