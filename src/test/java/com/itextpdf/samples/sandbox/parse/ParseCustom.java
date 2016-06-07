/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/24506830/can-we-use-text-extraction-strategy-after-applying-location-extraction-strategy
 */
package com.itextpdf.samples.sandbox.parse;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class ParseCustom {
    public static final String SRC = "./src/test/resources/pdfs/nameddestinations.pdf";
    public static final String EXPECTED_TEXT = "Country List\n" +
            "Internet Movie Database";


    @BeforeClass
    public static void beforeClass() throws IOException {
        File file = new File(SRC);
        file.getParentFile().mkdirs();
    }

    @Test
    public void manipulatePdf() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle rect = new Rectangle(36, 750, 523, 56);

        FontFilter fontFilter = new FontFilter(rect);
        FilteredEventListener listener = new FilteredEventListener();
        LocationTextExtractionStrategy extractionStrategy = listener.attachEventListener(new LocationTextExtractionStrategy(), fontFilter);
        new PdfCanvasProcessor(listener).processPageContent(pdfDoc.getFirstPage());

        String actualText = extractionStrategy.getResultantText();
        System.out.println(actualText);

        pdfDoc.close();

        Assert.assertEquals(EXPECTED_TEXT, actualText);
    }


    class FontFilter extends TextRegionEventFilter {
        public FontFilter(Rectangle filterRect) {
            super(filterRect);
        }

        @Override
        public boolean accept(IEventData data, EventType type) {
            if (type.equals(EventType.RENDER_TEXT)) {
                TextRenderInfo renderInfo = (TextRenderInfo) data;

                PdfFont font = renderInfo.getFont();
                if (null != font) {
                    String fontName = font.getFontProgram().getFontNames().getFontName();
                    return fontName.endsWith("Bold") || fontName.endsWith("Oblique");
                }
            }
            return false;
        }
    }
}
