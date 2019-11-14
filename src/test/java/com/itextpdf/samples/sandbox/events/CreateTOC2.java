/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example is written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/39037594
 */

package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class CreateTOC2 {
    public static final String DEST = "./target/sandbox/events/create_toc2.pdf";
    protected int counter = 0;

    List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>>> toc = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateTOC2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        for (int i = 0; i < 10; i++) {
            String title = "This is title " + i;
            Text text = new Text(title).setFontSize(16);
            text.setNextRenderer(new TOCTextRenderer(text));
            doc.add(new Paragraph(text));
            for (int j = 0; j < 50; j++) {
                doc.add(new Paragraph("Line " + j + " of title " + i));
            }
        }
        doc.add(new AreaBreak());

        doc.add(new Paragraph("Table of Contents").setFontSize(16));
        Paragraph p;
        for (AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>> entry : toc) {
            Text text = new Text(entry.getKey());
            AbstractMap.SimpleEntry<String, Integer> value = entry.getValue();

            text.setAction(PdfAction.createGoTo(value.getKey()));
            p = new Paragraph(text);

            p.addTabStops(new TabStop(750, TabAlignment.RIGHT, new DottedLine()));
            p.add(new Tab());

            text = new Text(String.valueOf(value.getValue()));
            text.setAction(PdfAction.createGoTo(value.getKey()));
            p.add(text);

            doc.add(p);
        }

        doc.close();
    }


    protected class TOCTextRenderer extends TextRenderer {
        public TOCTextRenderer(Text modelElement) {
            super(modelElement);
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overriden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TOCTextRenderer((Text) modelElement);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            return super.layout(layoutContext);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            String name = "dest" + (counter++);
            int page = drawContext.getDocument().getNumberOfPages();
            toc.add(new AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>>(((Text) modelElement).getText(),
                    new AbstractMap.SimpleEntry<String, Integer>(name, page)));
            drawContext.getDocument().addNamedDestination(name,
                    PdfExplicitDestination.createFitH(drawContext.getDocument().getPage(page), drawContext.getDocument().getLastPage().getPageSize().getTop()).getPdfObject());
        }
    }
}
