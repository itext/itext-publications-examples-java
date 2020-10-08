package com.itextpdf.samples.sandbox.pdfhtml.resource.retriever;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.resolver.resource.DefaultResourceRetriever;
import com.itextpdf.styledxmlparser.resolver.resource.IResourceRetriever;

import java.io.File;
import java.io.IOException;

public class FilterSizeByDefaultResourceRetriever {
    public static final String SRC = "./src/main/resources/pdfhtml/FilterSizeByDefaultResourceRetriever/";
    public static final String DEST = "./target/sandbox/pdfhtml/FilterSizeByDefaultResourceRetriever.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "FilterSizeByDefaultResourceRetriever.html";

        new FilterSizeByDefaultResourceRetriever().manipulatePdf(htmlSource, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws IOException {

        // Specify that resources exceeding 100kb will be filtered out, i.e. data will not be extracted from them.
        IResourceRetriever retriever = new DefaultResourceRetriever().setResourceSizeByteLimit(100_000);
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setResourceRetriever(retriever);

        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }
}
