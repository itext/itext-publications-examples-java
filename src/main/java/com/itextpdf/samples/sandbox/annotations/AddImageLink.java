package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;

import java.io.File;

public class AddImageLink {
    public static final String DEST = "./target/sandbox/annotations/add_image_link.pdf";

    public static final String IMG = "./src/main/resources/img/info.png";
    public static final String SRC = "./src/main/resources/pdfs/primes.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddImageLink().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        ImageData img = ImageDataFactory.create(IMG);
        float x = 10;
        float y = 650;
        float width = img.getWidth();
        float height = img.getHeight();
        PdfPage firstPage = pdfDoc.getFirstPage();

        PdfCanvas canvas = new PdfCanvas(firstPage);
        canvas.addImageAt(img, x, y, false);

        Rectangle linkLocation = new Rectangle(x, y, width, height);

        // Make the link destination page fit to the display
        PdfExplicitDestination destination = PdfExplicitDestination.createFit(pdfDoc.getLastPage());
        PdfAnnotation annotation = new PdfLinkAnnotation(linkLocation)

                // Set highlighting type which is enabled after a click on the annotation
                .setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)

                // Create a link to the last page of the document.
                .setAction(PdfAction.createGoTo(destination))
                .setBorder(new PdfArray(new float[] {0, 0, 0}));
        firstPage.addAnnotation(annotation);

        pdfDoc.close();
    }
}
