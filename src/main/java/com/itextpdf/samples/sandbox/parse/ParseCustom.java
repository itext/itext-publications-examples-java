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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.io.BufferedWriter;

public class ParseCustom {
    public static final String DEST = "./target/txt/parse_custom.txt";

    public static final String SRC = "./src/main/resources/pdfs/nameddestinations.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ParseCustom().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        Rectangle rect = new Rectangle(36, 750, 523, 56);
        CustomFontFilter fontFilter = new CustomFontFilter(rect);
        FilteredEventListener listener = new FilteredEventListener();

        // Create a text extraction renderer
        LocationTextExtractionStrategy extractionStrategy = listener
                .attachEventListener(new LocationTextExtractionStrategy(), fontFilter);

        // Note: If you want to re-use the PdfCanvasProcessor, you must call PdfCanvasProcessor.reset()
        PdfCanvasProcessor parser = new PdfCanvasProcessor(listener);
        parser.processPageContent(pdfDoc.getFirstPage());

        // Get the resultant text after applying the custom filter
        String actualText = extractionStrategy.getResultantText();

        pdfDoc.close();

        // See the resultant text in the console
        System.out.println(actualText);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest)))) {
            writer.write(actualText);
        }
    }

    /*
     * The custom filter filters only the text of which the font name ends with Bold or Oblique.
     */
    protected class CustomFontFilter extends TextRegionEventFilter {
        public CustomFontFilter(Rectangle filterRect) {
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
