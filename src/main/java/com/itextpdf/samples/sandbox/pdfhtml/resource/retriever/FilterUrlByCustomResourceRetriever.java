package com.itextpdf.samples.sandbox.pdfhtml.resource.retriever;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.resolver.resource.DefaultResourceRetriever;
import com.itextpdf.styledxmlparser.resolver.resource.IResourceRetriever;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FilterUrlByCustomResourceRetriever {
    public static final String SRC = "./src/main/resources/pdfhtml/FilterUrlByCustomResourceRetriever/";
    public static final String DEST = "./target/sandbox/pdfhtml/FilterUrlByCustomResourceRetriever.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "FilterUrlByCustomResourceRetriever.html";

        new FilterUrlByCustomResourceRetriever().manipulatePdf(htmlSource, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws IOException {
        IResourceRetriever resourceRetriever = new FilterResourceRetriever();
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setResourceRetriever(resourceRetriever);

        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }

    private static final class FilterResourceRetriever extends DefaultResourceRetriever {
        @Override
        protected boolean urlFilter(URL url) {

            // Specify that only urls, that are containing '/imagePath' text in the path, are allowed to handle
            return url.getPath().contains("/imagePath");
        }
    }
}
