/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/19976343/how-to-set-the-paragraph-of-itext-pdf-file-as-rectangle-with-background-color-in
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.RootLayoutArea;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.io.IOException;

public class ColumnTextParagraphs {
    public static final String DEST = "./target/sandbox/objects/column_text_paragraphs.pdf";
    public static final String TEXT = "This is some long paragraph " +
            "that will be added over and over again to prove a point.";
    public static final Rectangle[] COLUMNS = {
            new Rectangle(36, 36, 254, 770),
            new Rectangle(305, 36, 254, 770)
    };

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextParagraphs().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.setRenderer(new CustomDocumentRenderer(doc));

        int pCounter = 0;
        while (pCounter < 30) {
            doc.add(new Paragraph(String.format("Paragraph %s: %s", ++pCounter, TEXT)));
        }
        doc.close();
    }

    protected class CustomDocumentRenderer extends DocumentRenderer {
        int nextAreaNumber = 0;
        int currentPageNumber;

        public CustomDocumentRenderer(Document document) {
            super(document);
        }

        // If renderer overflows on the next area iText will use default getNextRender() method with default renderer
        // parameters. So the method should be overridden with the parameters from the initial renderer
        @Override
        public IRenderer getNextRenderer() {
            return new DocumentRenderer(document);
        }

        @Override
        public LayoutArea updateCurrentArea(LayoutResult overflowResult) {
            if (nextAreaNumber % 2 == 0) {
                currentPageNumber = super.updateCurrentArea(overflowResult).getPageNumber();
            } else {
                new PdfCanvas(document.getPdfDocument(), document.getPdfDocument().getNumberOfPages())
                        .moveTo(297.5f, 36)
                        .lineTo(297.5f, 806)
                        .stroke();
            }
            currentArea = new RootLayoutArea(currentPageNumber, COLUMNS[nextAreaNumber % 2].clone());
            nextAreaNumber++;
            return currentArea;
        }
    }
}
