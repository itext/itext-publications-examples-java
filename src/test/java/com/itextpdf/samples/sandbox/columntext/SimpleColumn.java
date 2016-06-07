/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/34445641
 */
package com.itextpdf.samples.sandbox.columntext;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class SimpleColumn extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/columntext/simple_column.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleColumn().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(100, 120));

        Paragraph paragraph = new Paragraph("REALLLLLLLLLLY LONGGGGGGGGGG text").setFontSize(4.5f);
        paragraph.setNextRenderer(new ParagraphRenderer(paragraph) {
            @Override
            public List<Rectangle> initElementAreas(LayoutArea area) {
                List<Rectangle> list = new ArrayList<Rectangle>();
                list.add(new Rectangle(9, 70, 61, 25));
                return list;
            }
        });
        doc.add(paragraph);

        doc.close();
    }
}