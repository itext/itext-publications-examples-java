package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.wmf.WmfImageData;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;

import java.io.File;
import java.net.MalformedURLException;

public class AddLinkImages {
    public static final String DEST = "./target/sandbox/annotations/add_link_images.pdf";

    public static final String sourceFolder = "./src/main/resources/img/";
    public static final String BUTTERFLY = sourceFolder + "butterfly.wmf";
    public static final String DOG = sourceFolder + "dog.bmp";
    public static final String FOX = sourceFolder + "fox.bmp";
    public static final String INFO = sourceFolder + "info.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddLinkImages().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Paragraph p = new Paragraph("Objects with links");
        p.add(createImage(INFO, "https://itextpdf.com/"));
        p.add(createImage(DOG, "https://pages.itextpdf.com/ebook-stackoverflow-questions.html"));
        p.add(createImage(FOX, "https://stackoverflow.com/q/29388313/1622493"));

        // Create PdfFormXObject object to add .wmf format image to the document,
        // because the creation of an ImageData instance from .wmf format image isn't supported.
        PdfFormXObject wmfImage = new PdfFormXObject(new WmfImageData(BUTTERFLY), pdfDoc);
        p.add(new Image(wmfImage)
                .setAction(PdfAction.createURI("https://stackoverflow.com/questions/tagged/itext*")));
        doc.add(p);

        doc.close();
    }

    public Image createImage(String src, String url) throws MalformedURLException {
        Image img = new Image(ImageDataFactory.create(src));

        // Create the url in the image by setting action property directly
        img.setProperty(Property.ACTION, PdfAction.createURI(url));
        return img;
    }
}
