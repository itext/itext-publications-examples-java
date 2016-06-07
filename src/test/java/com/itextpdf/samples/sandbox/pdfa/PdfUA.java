/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28222277/how-can-i-generate-a-pdf-ua-compatible-pdf-with-itext
 */
package com.itextpdf.samples.sandbox.pdfa;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class PdfUA extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/pdfa/pdf_ua.pdf";
    public static final String DOG = "./src/test/resources/img/dog.bmp";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";
    public static final String FOX = "./src/test/resources/img/fox.bmp";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUA().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_1_7)));
        Document document = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());
        //TAGGED PDF
        //Make document tagged
        pdfDoc.setTagged();
        //===============
        //PDF/UA
        //Set document metadata

        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("English pangram");
        //=====================

        Paragraph p = new Paragraph();
        //PDF/UA
        //Embed font
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        p.setFont(font);
        //==================
        Text c = new Text("The quick brown ");
        p.add(c);
        Image i = new Image(ImageDataFactory.create(FOX));
        //PDF/UA
        //Set alt text
        i.getAccessibilityProperties().setAlternateDescription("Fox");
        //==============
        p.add(i);
        p.add(" jumps over the lazy ");
        i = new Image(ImageDataFactory.create(DOG));
        //PDF/UA
        //Set alt text
        i.getAccessibilityProperties().setAlternateDescription("Dog");
        //==================
        p.add(i);
        document.add(p);
        p = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n").setFont(font).setFontSize(20);
        document.add(p);
        List list = new List();
        list.add((ListItem) new ListItem("quick").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("brown").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("fox").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("jumps").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("over").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("the").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("lazy").setFont(font).setFontSize(20));
        list.add((ListItem) new ListItem("dog").setFont(font).setFontSize(20));
        document.add(list);
        document.close();
    }
}
