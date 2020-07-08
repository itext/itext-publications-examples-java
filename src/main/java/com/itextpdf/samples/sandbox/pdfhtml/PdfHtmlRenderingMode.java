package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.ICssApplierFactory;
import com.itextpdf.html2pdf.css.apply.impl.BodyTagCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.RenderingMode;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfHtmlRenderingMode {
    public static final String SRC = "./src/main/resources/pdfhtml/PdfHtmlRenderingMode/";
    public static final String DEST = "./target/sandbox/pdfhtml/PdfHtmlRenderingMode.pdf";

    public static void main(String[] args) throws IOException {
        String currentSrc = SRC + "PdfHtmlRenderingMode.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfHtmlRenderingMode().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String freeFontsDirectory) throws IOException {
        // In pdfHTML the iText layouting mechanism now works in HTML rendering mode. In order
        // to switch to the iText default rendering mode, you should declare a custom ICssApplierFactory
        // in which to create a custom ICssApplier for the body node. Then set the default rendering mode
        // property for the property container.
        ICssApplierFactory customCssApplierFactory = new DefaultModeCssApplierFactory();

        // Starting from pdfHTML version 3.0.0 the GNU Free Fonts family (e.g. FreeSans) that were shipped together with the pdfHTML distribution
        // are now replaced by Noto fonts. If your HTML file contains characters which are not supported by standard PDF fonts (basically most of the
        // characters except latin script and some of the symbol characters, e.g. cyrillic characters like in this sample) and also if no other fonts
        // are specified explicitly by means of iText `FontProvider` class or CSS `@font-face` rule, then pdfHTML shipped fonts covering a wide range
        // of glyphs are used instead. In order to replicate old behavior, one needs to exclude from `FontProvider` the fonts shipped by default and
        // provide GNU Free Fonts instead. GNU Free Fonts can be found at https://www.gnu.org/software/freefont/.
        FontProvider fontProvider = new DefaultFontProvider(true, false, false);
        fontProvider.addDirectory(freeFontsDirectory);

        ConverterProperties converterProperties = new ConverterProperties()
                .setBaseUri(freeFontsDirectory)
                // Try removing registering of custom DefaultModeCssApplierFactory to compare legacy behavior
                // with the newly introduced. You would notice that now lines spacing algorithm is changed:
                // line spacing is considerably smaller and is closer compared to browsers rendering.
                // You would also notice that now single image in a line behaves according to HTML's "noQuirks" mode:
                // there's an additional "spacing" underneath the image which depends on element's `line-height` and
                // `font-size` CSS properties.
                .setCssApplierFactory(customCssApplierFactory)
                .setFontProvider(fontProvider);

        // When converting using the method #convertToElements to change the rendering mode you must also
        // use the flag Property.RENDERING_MODE. However it must be added to the elements from the
        // resulting list before adding these elements to the document. Then the elements will be
        // placed in the specified mode.
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
    }

    private static class DefaultModeCssApplierFactory extends DefaultCssApplierFactory {
        @Override
        public ICssApplier getCustomCssApplier(IElementNode tag) {
            if (TagConstants.BODY.equals(tag.name())) {
                return new DefaultModeBodyTagCssApplier();
            }
            return null;
        }
    }

    private static class DefaultModeBodyTagCssApplier extends BodyTagCssApplier {
        @Override
        public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
            super.apply(context, stylesContainer, tagWorker);
            IPropertyContainer container = tagWorker.getElementResult();
            // Enable default mode
            container.setProperty(Property.RENDERING_MODE, RenderingMode.DEFAULT_LAYOUT_MODE);
        }
    }

}
