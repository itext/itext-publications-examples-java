/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
* This code sample was written in the context of the tutorial:
* ZUGFeRD: The future of Invoicing
*/
package com.itextpdf.samples.sandbox.zugferd.chapter02;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.pdfa.PdfADocument;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.experimental.categories.Category;

/**
 * Creates a PDF that conforms with PDF/A-3 Level A.
 */
@Category(SampleTest.class)
public class C2E4_PdfA3a extends GenericTest {
    public static final String ICC = "./src/test/resources/data/sRGB_CS_profile.icm";
    public static final String FONT = "./src/test/resources/font/FreeSans.ttf";

    public static final String FOX = "./src/test/resources/img/fox.bmp";
    public static final String DOG = "./src/test/resources/img/dog.bmp";

    public static final String DEST = "./target/test/resources/zugferd/chapter02/C2E4_PdfA3a.pdf";

    @Override
    public void manipulatePdf(String dest) throws IOException, XMPException, InterruptedException {
        //PDF/A-3a
        //Set output intents
        //Create PdfAWDocument with the required conformance level
        InputStream is = new FileInputStream(ICC);
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_3A,
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", is));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        //====================
        //TAGGED PDF
        //Make document tagged
        pdfDoc.setTagged();
        //===============
        //PDF/UA
        //Set document metadata
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        if (null == info) {
            info = new PdfDocumentInfo(pdfDoc);
        }
        info.setTitle("Some title");
        //=====================


        Paragraph p = new Paragraph();
        //PDF/A-3a
        //Embed font
        p.setFont(PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true)).setFontSize(20);
        Text text = new Text("The quick brown ");
        p.add(text);
        Image image = new Image(ImageDataFactory.create(FOX));
        //PDF/UA
        //Set alt text
        image.getAccessibilityProperties().setAlternateDescription("Fox");
        //==============
        p.add(image);
        text = new Text(" jumps over the lazy ");
        p.add(text);
        image = new Image(ImageDataFactory.create(DOG));
        //PDF/UA
        //Set alt text
        image.getAccessibilityProperties().setAlternateDescription("Dog");
        //==================
        p.add(image);
        doc.add(p);

        doc.close();
    }
}
