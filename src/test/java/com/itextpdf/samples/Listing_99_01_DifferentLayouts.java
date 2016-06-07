/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

/**
 * Example demonstrates how to add paragraphs using floating and fixed layouts
 */
@Category(SampleTest.class)
public class Listing_99_01_DifferentLayouts extends GenericTest {

    public static final String DEST = "./target/test/resources/Listing_99_01_DifferentLayouts/Listing_99_01_DifferentLayouts.pdf";

    public static void main(String args[]) throws IOException {
        new Listing_99_01_DifferentLayouts().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        //Initialize writer
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        //Add floating paragraph
        doc.add(new Paragraph("Flowing paragraph"));

        //Add fixed paragraph
        Paragraph p = new Paragraph("Fixed paragraph").setFixedPosition(1, 100, 100, 200).setHeight(200).setBackgroundColor(Color.GREEN);
        doc.add(p);

        //Close document
        doc.close();
    }

}
