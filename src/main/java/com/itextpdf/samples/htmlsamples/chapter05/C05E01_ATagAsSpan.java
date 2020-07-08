package com.itextpdf.samples.htmlsamples.chapter05;

import java.io.File;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.attach.impl.tags.SpanTagWorker;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * Creates a series of PDF files from HTML that uses some custom tags.
 */
public class C05E01_ATagAsSpan {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch05/no_link.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/2_inline_css.html";

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

        C05E01_ATagAsSpan app = new C05E01_ATagAsSpan();
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
        converterProperties.setTagWorkerFactory(
                new DefaultTagWorkerFactory() {
                    @Override
                    public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
                        if ("a".equalsIgnoreCase(tag.name())) {
                            return new SpanTagWorker(tag, context);
                        }
                        return null;
                    }
                }
        );
        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }
}