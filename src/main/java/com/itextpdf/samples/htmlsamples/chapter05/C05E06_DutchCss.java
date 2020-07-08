package com.itextpdf.samples.htmlsamples.chapter05;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

/**
 * Converts an HTML file to a PDF document overriding the CSS of that HTML.
 */
public class C05E06_DutchCss {

    public static final Map<String, String> KLEUR = new HashMap<String, String>();

    static {
        KLEUR.put("wit", "white");
        KLEUR.put("zwart", "black");
        KLEUR.put("rood", "red");
        KLEUR.put("groen", "green");
        KLEUR.put("blauw", "blue");
    }

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch05/dutch_css.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/dutch_css.html";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C05E06_DutchCss app = new C05E06_DutchCss();
        app.createPdf(SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src  the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String dest) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setCssApplierFactory(new DefaultCssApplierFactory() {
            ICssApplier dutchCssColor = new DutchColorCssApplier();

            @Override
            public ICssApplier getCustomCssApplier(IElementNode tag) {
                if (tag.name().equals(TagConstants.H1)
                        || tag.name().equals(TagConstants.DIV)) {
                    return dutchCssColor;
                }
                return null;
            }
        });
        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }

    /**
     * A custom implementation of the BlockCssApplier that will change the background color
     * to gray, no matter which color was defined in the CSS of the HTML file.
     */
    class DutchColorCssApplier extends BlockCssApplier {

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier#apply(com.itextpdf.html2pdf.attach.ProcessorContext, com.itextpdf.html2pdf.html.node.IStylesContainer, com.itextpdf.html2pdf.attach.ITagWorker)
         */
        @Override
        public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
            Map<String, String> cssStyles = stylesContainer.getStyles();
            if (cssStyles.containsKey("kleur")) {
                cssStyles.put(CssConstants.COLOR, KLEUR.get(cssStyles.get("kleur")));
                stylesContainer.setStyles(cssStyles);
            }
            if (cssStyles.containsKey("achtergrond")) {
                cssStyles.put(CssConstants.BACKGROUND_COLOR, KLEUR.get(cssStyles.get("achtergrond")));
                stylesContainer.setStyles(cssStyles);
            }
            super.apply(context, stylesContainer, tagWorker);
        }
    }
}
