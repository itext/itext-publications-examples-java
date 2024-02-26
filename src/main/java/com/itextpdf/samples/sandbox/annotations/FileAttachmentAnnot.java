package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class FileAttachmentAnnot {
    public static final String DEST = "./target/sandbox/annotations/file_attachment_annot.pdf";

    public static final String IMG = "./src/main/resources/img/info.png";
    public static final String PATH = "./src/main/resources/txt/test.docx";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new FileAttachmentAnnot().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        Rectangle rect = new Rectangle(36, 700, 100, 100);
        String embeddedFileName = "test.docx";

        // the 3rd argument is the file description.
        // the 5th argument is the mime-type of the embedded file;
        // the 6th argument is the AFRelationship key value.
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, PATH, null, embeddedFileName, null, null);
        PdfAnnotation attachment = new PdfFileAttachmentAnnotation(rect, fileSpec);

        // This method sets the text that will be displayed for the annotation or the alternate description,
        // if this type of annotation does not display text.
        attachment.setContents("Click me");

        // Create XObject and draw it with the imported image on the canvas
        // to add XObject as normal appearance.
        PdfFormXObject xObject = new PdfFormXObject(rect);
        ImageData imageData = ImageDataFactory.create(IMG);
        PdfCanvas canvas = new PdfCanvas(xObject, pdfDoc);
        canvas.addImageFittedIntoRectangle(imageData, rect, true);
        attachment.setNormalAppearance(xObject.getPdfObject());

        pdfDoc.addNewPage().addAnnotation(attachment);

        pdfDoc.close();
    }
}
