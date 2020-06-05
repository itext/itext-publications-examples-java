package com.itextpdf.samples.sandbox.pdfhtml.resource.retriever;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.styledxmlparser.resolver.resource.IResourceRetriever;
import com.itextpdf.styledxmlparser.resolver.resource.UriResolver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AddDefaultImagesCustomRetriever {
    public static final String SRC = "./src/main/resources/pdfhtml/";
    public static final String DEST = "./target/sandbox/pdfhtml/AddDefaultImagesCustomRetriever.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "AddDefaultImagesCustomRetriever/AddDefaultImagesCustomRetriever.html";

        new AddDefaultImagesCustomRetriever().manipulatePdf(htmlSource, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws IOException {
        IResourceRetriever retriever = new CustomResourceRetriever(SRC);
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setResourceRetriever(retriever);

        HtmlConverter.convertToPdf(new File(src), new File(dest), converterProperties);
    }

    private static final class CustomResourceRetriever implements IResourceRetriever {
        private String baseUri;

        public CustomResourceRetriever(String baseUri) {
            this.baseUri = baseUri;
        }

        @Override
        public InputStream getInputStreamByUrl(URL url) throws IOException {

            // The image with name 'imageToReplace.png' will be replaced by the default image.
            if (url.toString().contains("imageToReplace.png")) {
                url = new UriResolver(this.baseUri).resolveAgainstBaseUri("images/defaultImage.png");
            }

            return url.openStream();
        }

        @Override
        public byte[] getByteArrayByUrl(URL url) throws IOException {
            byte[] result = null;
            try (InputStream stream = getInputStreamByUrl(url)) {
                if (stream == null) {
                    return null;
                }

                result = inputStreamToArray(stream);
            }

            return result;
        }

        private static byte[] inputStreamToArray(InputStream stream) throws java.io.IOException {
            byte[] b = new byte[8192];
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while (true) {
                int read = stream.read(b);
                if (read < 1) {
                    break;
                }
                output.write(b, 0, read);
            }

            output.close();
            return output.toByteArray();
        }
    }
}
