/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example is written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/39037594
 */

package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
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
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

@Category(SampleTest.class)
public class CreateTOC extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/events/create_toc.pdf";

    List<AbstractMap.SimpleEntry<String, Integer>> toc = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new CreateTOC().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws FileNotFoundException {
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
        for (AbstractMap.SimpleEntry<String, Integer> entry : toc) {
            p = new Paragraph(entry.getKey());
            p.addTabStops(new TabStop(750, TabAlignment.RIGHT, new DottedLine()));
            p.add(new Tab());
            p.add(String.valueOf(entry.getValue()));
            doc.add(p);
        }

        doc.close();
    }


    protected class TOCTextRenderer extends TextRenderer {
        public TOCTextRenderer(Text modelElement) {
            super(modelElement);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            return super.layout(layoutContext);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            toc.add(new AbstractMap.SimpleEntry(((Text) modelElement).getText(), drawContext.getDocument().getNumberOfPages()));
        }
    }
}

