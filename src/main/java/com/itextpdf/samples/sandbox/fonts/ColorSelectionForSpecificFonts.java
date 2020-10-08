package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TransparentColor;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.LineRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorSelectionForSpecificFonts {
    public static final String DEST = "./target/sandbox/fonts/color_selection_for_specific_fonts.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";
    public static final String TEXT = "Some arabic text in yellow - \u0644\u0640\u0647 \n"
            + "Some devanagari text in green - \u0915\u0941\u091B \n"
            + "Some latin text in blue - iText \n";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ColorSelectionForSpecificFonts().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        String timesRomanFont = StandardFonts.TIMES_ROMAN;
        String arabicFont = FONTS_FOLDER + "NotoNaskhArabic-Regular.ttf";
        String devanagariFont = FONTS_FOLDER + "NotoSansDevanagari-Regular.ttf";

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        FontProvider provider = new FontProvider();
        provider.addFont(timesRomanFont);
        provider.addFont(arabicFont);
        provider.addFont(devanagariFont);

        // Adding fonts to the font provider
        doc.setFontProvider(provider);

        Paragraph paragraph = new Paragraph(TEXT);

        // Setting font family to the particular element will trigger iText's font selection mechanism:
        // font for the element will be picked up from the provider's fonts
        paragraph.setFontFamily(timesRomanFont);

        Map<String, Color> fontColorMap = new HashMap<>();
        fontColorMap.put("NotoNaskhArabic", ColorConstants.YELLOW);
        fontColorMap.put("NotoSansDevanagari", ColorConstants.GREEN);
        fontColorMap.put("Times-Roman", ColorConstants.BLUE);

        // Set custom renderer, which will change the color of text written within specific font
        paragraph.setNextRenderer(new CustomParagraphRenderer(paragraph, fontColorMap));

        doc.add(paragraph);

        doc.close();
    }

    public static class CustomParagraphRenderer extends ParagraphRenderer {
        private Map<String, Color> fontColorMap;

        public CustomParagraphRenderer(Paragraph modelElement, Map<String, Color> fontColorMap) {
            super(modelElement);
            this.fontColorMap = new HashMap<>(fontColorMap);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);

            // During layout() execution iText parses the Paragraph and splits it into a number of Text objects,
            // each of which have glyphs to be processed by the same font.
            // Lines, which are the result of layout(), will be drawn to the pdf on draw().
            // In order to change the color of text written in specific font, we could just go through all the lines
            // and update the color of Text objects, which have this font being set.
            List<LineRenderer> lines = getLines();
            updateColor(lines);

            return result;
        }

        private void updateColor(List<LineRenderer> lines) {
            for (LineRenderer renderer : lines) {
                List<IRenderer> children = renderer.getChildRenderers();
                for (IRenderer child : children) {
                    if (child instanceof TextRenderer) {
                        PdfFont pdfFont = ((TextRenderer) child).getPropertyAsFont(Property.FONT);
                        if (null != pdfFont) {
                            Color updatedColor = fontColorMap
                                    .get(pdfFont.getFontProgram().getFontNames().getFontName());
                            if (null != updatedColor) {

                                // Although setting a property via setProperty might be useful,
                                // it's regarded as internal iText functionality. The properties are expected
                                // to be set on elements via specific setters (setFont, for example).
                                // iText doesn't guarantee that setProperty will work as you expect it,
                                // so please be careful while calling this method.
                                child.setProperty(Property.FONT_COLOR, new TransparentColor(updatedColor));
                            }
                        }
                    }
                }
            }
        }
    }
}
