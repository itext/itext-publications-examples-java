/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/28502315/divide-page-in-2-parts-so-we-can-fill-each-with-different-source
 */
package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.*;

@Category(SampleTest.class)
public class ThreeParts extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/three_parts.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ThreeParts().manipulatePdf(DEST);
    }

    public Paragraph createParagraph(String path) throws IOException {
        Paragraph p = new Paragraph();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "UTF-8"));
        StringBuffer buffer = new StringBuffer();
        String line = in.readLine();
        while (null != line) {
            buffer.append(line);
            line = in.readLine();
        }
        in.close();
        p.add(buffer.toString());
        return p;
    }

    public void addSection(PdfDocument pdfDoc, Paragraph paragraph, int pageNumber, int sectionNumber) throws IOException {
        LayoutResult layoutResult;
        ParagraphRenderer renderer = (ParagraphRenderer) paragraph.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(new Document(pdfDoc)));
        while (((layoutResult = renderer.layout(new LayoutContext(new LayoutArea(pageNumber, new Rectangle(36, 36 + ((842 - 72) / 3) * sectionNumber, 523, (842 - 72) / 3)))))).getStatus() != LayoutResult.FULL) {
            if (pdfDoc.getNumberOfPages() < pageNumber) {
                pdfDoc.addNewPage();
            }
            layoutResult.getSplitRenderer().draw(new DrawContext(pdfDoc, new PdfCanvas(pdfDoc.getPage(pageNumber++)), false));
            renderer = (ParagraphRenderer) layoutResult.getOverflowRenderer();
        }
        if (pdfDoc.getNumberOfPages() < pageNumber) {
            pdfDoc.addNewPage();
        }
        renderer.draw(new DrawContext(pdfDoc, new PdfCanvas(pdfDoc.getPage(pageNumber)), false));
    }


    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        int firstPageNumber = 1;

        for (int section = 0; section < 3; section++) {
            // latin
            addSection(pdfDoc, createParagraph(String.format("./src/test/resources/txt/liber1_%s_la.txt", section + 1)), firstPageNumber, 2);
            // english
            addSection(pdfDoc, createParagraph(String.format("./src/test/resources/txt/liber1_%s_en.txt", section + 1)), firstPageNumber, 1);
            // french
            addSection(pdfDoc, createParagraph(String.format("./src/test/resources/txt/liber1_%s_fr.txt", section + 1)), firstPageNumber, 0);

            firstPageNumber = pdfDoc.getNumberOfPages() + 1;
        }

        pdfDoc.close();
    }
}
