/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20233630/itextsharp-how-to-add-a-full-line-break
 * <p>
 * We create a Chunk and add a background color.
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class FullDottedLine extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/full_dotted_line.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FullDottedLine().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Before dotted line"));
        doc.add(new LineSeparator(new CustomDottedLine(pdfDoc.getDefaultPageSize())));
        doc.add(new Paragraph("After dotted line"));

        doc.close();
    }


    class CustomDottedLine extends DottedLine {
        protected Rectangle pageSize;

        public CustomDottedLine(Rectangle pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public void draw(PdfCanvas canvas, Rectangle drawArea) {
            super.draw(canvas, new Rectangle(pageSize.getLeft(), drawArea.getBottom(), pageSize.getWidth(), drawArea.getHeight()));
        }
    }
}
