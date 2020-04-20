package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfTextAnnotation;

import java.io.File;

public class AddAccepted {
    public static final String DEST = "./target/sandbox/annotations/add_accepted.pdf";

    public static final String SRC = "./src/main/resources/pdfs/hello_sticky_note.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddAccepted().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfPage page = pdfDoc.getFirstPage();

        PdfAnnotation sticky = page.getAnnotations().get(0);
        Rectangle stickyRectangle = sticky.getRectangle().toRectangle();
        PdfAnnotation replySticky = new PdfTextAnnotation(stickyRectangle)
                .setStateModel(new PdfString("Review"))
                .setState(new PdfString("Accepted"))
                .setIconName(new PdfName("Comment"))

                // This method sets an annotation to which the current annotation is "in reply".
                // Both annotations shall be on the same page of the document.
                .setInReplyTo(sticky)

                // This method sets the text label that will be displayed in the title bar of the annotation's pop-up window
                // when open and active. This entry shall identify the user who added the annotation.
                .setText(new PdfString("Bruno"))

                // This method sets the text that will be displayed for the annotation or the alternate description,
                // if this type of annotation does not display text.
                .setContents("Accepted by Bruno")

                // This method sets a complete set of enabled and disabled flags at once. If not set specifically
                // the default value is 0.
                // The argument is an integer interpreted as set of one-bit flags
                // specifying various characteristics of the annotation.
                .setFlags(sticky.getFlags() + PdfAnnotation.HIDDEN);
        pdfDoc.getFirstPage().addAnnotation(replySticky);

        pdfDoc.close();
    }
}
