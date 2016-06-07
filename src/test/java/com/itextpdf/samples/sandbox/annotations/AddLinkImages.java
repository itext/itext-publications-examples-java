/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to
 * http://stackoverflow.com/questions/29388313/itext-how-to-associate-actions-with-graphical-object
 */
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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.net.MalformedURLException;

@Category(SampleTest.class)
public class AddLinkImages extends GenericTest {
    public static final String sourceFolder = "./src/test/resources/img/";
    public static final String BUTTERFLY = sourceFolder + "butterfly.wmf";
    public static final String DEST = "./target/test/resources/sandbox/annotations/add_link_images.pdf";
    public static final String DOG = sourceFolder + "dog.bmp";
    public static final String FOX = sourceFolder + "fox.bmp";
    public static final String INFO = sourceFolder + "info.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddLinkImages().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        Paragraph p = new Paragraph("Objects with links");
        p.add(createImage(INFO, "http://itextpdf.com/"));
        p.add(createImage(DOG, "http://pages.itextpdf.com/ebook-stackoverflow-questions.html"));
        p.add(createImage(FOX, "http://stackoverflow.com/q/29388313/1622493"));
        p.add(new Image(new PdfFormXObject(new WmfImageData(BUTTERFLY), pdfDoc)).
                setAction(PdfAction.createURI("http://stackoverflow.com/questions/tagged/itext*")));
        doc.add(p);
        doc.close();
    }

    public Image createImage(String src, String url) throws MalformedURLException {
        Image img = new Image(ImageDataFactory.create(src));
        img.setProperty(Property.ACTION, PdfAction.createURI(url));
        return img;
    }
}
