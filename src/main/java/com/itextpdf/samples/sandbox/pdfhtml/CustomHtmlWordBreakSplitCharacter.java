package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.ICssApplierFactory;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.io.font.otf.Glyph;
import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.splitting.BreakAllSplitCharacters;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.io.File;
import java.io.IOException;

public class CustomHtmlWordBreakSplitCharacter {
    public static final String SRC = "./src/main/resources/pdfhtml/CustomHtmlWordBreakSplitCharacter/CustomHtmlWordBreakSplitCharacter.html";
    public static final String DEST = "./target/sandbox/pdfhtml/CustomHtmlWordBreakSplitCharacter.pdf";

    public static void main(String[] args) throws IOException, InterruptedException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CustomHtmlWordBreakSplitCharacter().manipulatePdf(SRC, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws InterruptedException, IOException {
        ConverterProperties converterProperties = new ConverterProperties();

        // Set custom css applier factory instance, that provides a functionality
        // to use custom word break split character setting in every default CSS applier.
        converterProperties.setCssApplierFactory(new CustomCssApplierFactory());

        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }

    private static class CustomCssApplierFactory implements ICssApplierFactory {
        private ICssApplierFactory defaultCssApplierFactory = new DefaultCssApplierFactory();

        @Override
        public ICssApplier getCssApplier(IElementNode tag) {
            ICssApplier defaultCssApplier = defaultCssApplierFactory.getCssApplier(tag);

            return defaultCssApplier != null ? new CustomCssApplier(defaultCssApplier) : null;
        }
    }

    private static class CustomCssApplier implements ICssApplier {
        private final ICssApplier defaultCssApplier;

        public CustomCssApplier(ICssApplier defaultCssApplier) {
            this.defaultCssApplier = defaultCssApplier;
        }

        @Override
        public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
            defaultCssApplier.apply(context, stylesContainer, tagWorker);
            IPropertyContainer elementResult = tagWorker.getElementResult();
            if (elementResult != null) {
                setCustomSplitCharacter(elementResult);
            } else if (tagWorker instanceof SpanTagWorker) {

                // If current element is span, then set the custom split character to nested elements
                for (IPropertyContainer ownLeafElement : ((SpanTagWorker) tagWorker).getOwnLeafElements()) {
                    setCustomSplitCharacter(ownLeafElement);
                }
            }
        }

        private void setCustomSplitCharacter(IPropertyContainer elementResult) {

            // If Property.SPLIT_CHARACTERS was null then DefaultSplitCharacters class would be used during layout.
            Object property = elementResult.getProperty(Property.SPLIT_CHARACTERS);

            //  BreakAllSplitCharacters and KeepAllSplitCharacters instances can be set
            //  if CSS word-break property was applied.
            if (!(property instanceof BreakAllSplitCharacters)) {
                elementResult.setProperty(Property.SPLIT_CHARACTERS, new CustomSlashSplitCharacters());
            }
        }
    }

    private static class CustomSlashSplitCharacters extends DefaultSplitCharacters {
        private static final String SPLIT_CHARACTER = "/";

        @Override
        public boolean isSplitCharacter(GlyphLine text, int glyphPos) {
            Glyph glyph = text.get(glyphPos);
            return glyph.hasValidUnicode() && SPLIT_CHARACTER.equals(glyph.getUnicodeString())
                    || super.isSplitCharacter(text, glyphPos);
        }
    }
}
