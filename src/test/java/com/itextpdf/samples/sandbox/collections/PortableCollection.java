/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27063677/use-of-relative-path-for-anchor-method-using-itext-for-pdf-generation
 */
package com.itextpdf.samples.sandbox.collections;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class PortableCollection extends GenericTest {
    public static final String DATA
            = "./src/test/resources/data/united_states.csv";
    public static final String DEST
            = "./target/test/resources/sandbox/collections/portable_collection.pdf";
    public static final String HELLO
            = "./src/test/resources/pdfs/hello.pdf";
    public static final String IMG =
            "./src/test/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PortableCollection().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("Portable collection"));
        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.TILE);
        pdfDoc.getCatalog().setCollection(collection);
        pdfDoc.addFileAttachment("united_states.csv",
                PdfFileSpec.createEmbeddedFileSpec(pdfDoc, DATA, "united_states.csv", "united_states.csv",
                        null, null));
        pdfDoc.addFileAttachment("hello.pdf",
                PdfFileSpec.createEmbeddedFileSpec(pdfDoc, HELLO, "hello.pdf", "hello.pdf",
                        null, null));
        pdfDoc.addFileAttachment("berlin2013.jpg",
                PdfFileSpec.createEmbeddedFileSpec(pdfDoc, IMG, "berlin2013.jpg", "berlin2013.jpg",
                        null, null));
        doc.close();
    }
}
