/*
 * Copyright 2016-2017, iText Group NV.
 * This example was created by Bruno Lowagie.
 * It was written in the context of the following book:
 * https://leanpub.com/itext7_pdfHTML
 * Go to http://developers.itextpdf.com for more info.
 */
package com.itextpdf.samples.htmlsamples.chapter05;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.CssConstants;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.css.apply.util.BackgroundApplierUtil;
import com.itextpdf.html2pdf.html.TagConstants;
import com.itextpdf.html2pdf.html.node.IElementNode;
import com.itextpdf.html2pdf.html.node.IStylesContainer;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.licensekey.LicenseKey;

/**
 * Converts an HTML file to a PDF document overriding the CSS of that HTML.
 */
public class C05E05_GrayBackground {

	/** The Base URI of the HTML page. */
	public static final String BASEURI = "src/main/resources/html/";
	/** The path to the source HTML file. */
	public static final String SRC = String.format("%ssxsw.html", BASEURI);
	/** The target folder for the result. */
	public static final String TARGET = "target/results/ch05/";
	/** The path to the resulting PDF file. */
	public static final String DEST = String.format("%ssxsw_grayBackground.pdf", TARGET);

	/**
	 * The main method of this example.
	 *
	 * @param args no arguments are needed to run this example.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");        
    	File file = new File(TARGET);
    	file.mkdirs();
        C05E05_GrayBackground app = new C05E05_GrayBackground();
        app.createPdf(SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String dest) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setCssApplierFactory(new DefaultCssApplierFactory() {
        	ICssApplier grayBackground = new GrayBackgroundBlockCssApplier();
            @Override
            public ICssApplier getCustomCssApplier(IElementNode tag) {
            	if(tag.name().equals(TagConstants.DIV)){
            		return grayBackground;
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
    class GrayBackgroundBlockCssApplier implements ICssApplier {
        
        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.css.apply.ICssApplier#apply(com.itextpdf.html2pdf.attach.ProcessorContext, com.itextpdf.html2pdf.html.node.IStylesContainer, com.itextpdf.html2pdf.attach.ITagWorker)
         */
        public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker){
            Map<String, String> cssProps = stylesContainer.getStyles();
            IPropertyContainer container = tagWorker.getElementResult();
            if (container != null && cssProps.containsKey(CssConstants.BACKGROUND_COLOR)) {
                cssProps.put(CssConstants.BACKGROUND_COLOR, "#dddddd");
                BackgroundApplierUtil.applyBackground(cssProps, context, container);
            }
        }
    }
}
