package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.io.resolver.resource.DefaultResourceRetriever;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.validation.SignatureValidator;
import com.itextpdf.signatures.validation.ValidatorChainBuilder;
import com.itextpdf.signatures.validation.lotl.LotlCountryCodeConstants;
import com.itextpdf.signatures.validation.lotl.LotlFetchingProperties;
import com.itextpdf.signatures.validation.lotl.LotlService;
import com.itextpdf.signatures.validation.lotl.RemoveOnFailingCountryData;
import com.itextpdf.signatures.validation.report.ValidationReport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * LotlValidationWithLocalFirstFileAccess.java
 *
 * Validates signatures using List of Trusted Lists (LOTL) with local file caching.
 */

public class LotlValidationWithLocalFirstFileAccess {


    public static final String SRC = "./src/main/resources/pdfs"
            + "/super_official_document_signed.pdf";

    public static final String XMLS = "./src/main/resources/validation/xml/";
    public static final String DUMMY_PDF = "./src/main/resources/validation/pdf/dummy.pdf";
    public static final String DEST = "./target/sandbox/signatures/validation/somepdf.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Files.copy(Paths.get(DUMMY_PDF), Paths.get(DEST), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        new LotlValidationWithLocalFirstFileAccess().useLocalFirstAccess();
    }

    public void useLocalFirstAccess() {
        ValidatorChainBuilder builder = new ValidatorChainBuilder();
        // We want to use LOTL as a source of trusted certificates
        builder.trustEuropeanLotl(true);

        LotlFetchingProperties fetchingProperties = new LotlFetchingProperties(new RemoveOnFailingCountryData());
        fetchingProperties.setCountryNames(LotlCountryCodeConstants.PORTUGAL);

        try (LotlService lotlService = new LotlService(fetchingProperties)) {
            lotlService.withCustomResourceRetriever(new FromFileAccess(XMLS));

            lotlService.initializeCache();
            builder.withLotlService(() -> lotlService);

            try (PdfDocument document = new PdfDocument(new PdfReader(SRC))) {
                SignatureValidator validator = builder.buildSignatureValidator(document);
                ValidationReport r = validator.validateSignatures();
                //Here you have the validation report and can use it as you need
                System.out.println(r);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class FromFileAccess extends DefaultResourceRetriever {

        private final String resourcePath;

        FromFileAccess(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public byte[] getByteArrayByUrl(URL url) throws IOException {
            String fileName = url.toString().replaceAll("[^a-zA-Z0-9]", "_");
            String filePath = resourcePath + fileName;

            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                //here we can implement timers to force refreshing the files if needed
                // for example force refresh if the file is older than 1 day etc.
                //This can be done by checking the file attributes for the last modified time
                //This is left as an exercise to the reader :)

                return Files.readAllBytes(path);
            }
            byte[] data = null;
            try {
                data = super.getByteArrayByUrl(url);

            } catch (Exception e) {
                //Super naive retry mechanism in case of network issues
                waitABit();
                try {
                    data = super.getByteArrayByUrl(url);
                } catch (Exception ex) {
                    //nothing we can do if it fails again
                }
            }
            if (data != null) {
                Files.write(path, data);
            }
            return data;
        }

        private void waitABit() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
