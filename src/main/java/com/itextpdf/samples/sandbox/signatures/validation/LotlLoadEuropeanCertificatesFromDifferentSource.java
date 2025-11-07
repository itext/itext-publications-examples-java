package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.validation.SignatureValidator;
import com.itextpdf.signatures.validation.ValidatorChainBuilder;
import com.itextpdf.signatures.validation.lotl.EuropeanResourceFetcher;
import com.itextpdf.signatures.validation.lotl.LotlCountryCodeConstants;
import com.itextpdf.signatures.validation.lotl.LotlFetchingProperties;
import com.itextpdf.signatures.validation.lotl.LotlService;
import com.itextpdf.signatures.validation.lotl.RemoveOnFailingCountryData;
import com.itextpdf.signatures.validation.report.ValidationReport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LotlLoadEuropeanCertificatesFromDifferentSource {

    public static final String SRC = "./src/main/resources/pdfs"
            + "/super_official_document_signed.pdf";

    public static final String CERTS = "./src/main/resources/cert/european_certs/";

    public static final String DUMMY_PDF = "./src/main/resources/validation/pdf/dummy.pdf";
    public static final String DEST = "./target/sandbox/signatures/validation/somepdf.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Files.copy(Paths.get(DUMMY_PDF), Paths.get(DEST), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        new LotlLoadEuropeanCertificatesFromDifferentSource().loadEuropeanCertificatesFromPemFiles();

    }

    public void loadEuropeanCertificatesFromPemFiles() {
        ValidatorChainBuilder builder = new ValidatorChainBuilder();

        builder.trustEuropeanLotl(true);
        LotlFetchingProperties fetchingProperties = new LotlFetchingProperties(new RemoveOnFailingCountryData());
        fetchingProperties.setCountryNames(LotlCountryCodeConstants.PORTUGAL);
        try (LotlService lotlService = new LotlService(fetchingProperties)) {
            //You might not want to rely on our provided resources module and want to load the european trusted list
            // from
            // the pem files you have downloaded and verified yourself. In this case you can implement your own
            // EuropeanResourceFetcher
            // and provide it to the LotlService like shown below.
            lotlService.withEuropeanResourceFetcher(
                    new LoadEuropeanCertificatesFromPemFiles(CERTS));


            //Don't forget to initialize the custom cache before using the LotlService or you will get an exception
            lotlService.initializeCache();



            builder.withLotlService(() -> lotlService);
            try (PdfDocument document = new PdfDocument(new PdfReader(SRC))) {
                SignatureValidator validator = builder.buildSignatureValidator(document);
                ValidationReport r = validator.validateSignatures();
                //Here you have the validation report and can use it as you need
                System.out.println(r);
            } catch (IOException e) {
                System.out.println("Error during document processing: " + e.getMessage());
            }
        }
    }

    static class LoadEuropeanCertificatesFromPemFiles extends EuropeanResourceFetcher {

        private final String pathToPemFolderDirectory;

        private final List<String> pemFileName = Collections.unmodifiableList(Arrays.asList(
                "1.pem",
                "2.pem",
                "3.pem",
                "4.pem",
                "5.pem",
                "6.pem",
                "7.pem",
                "8.pem")
        );

        LoadEuropeanCertificatesFromPemFiles(String pathToPemFolderDirectory) {
            this.pathToPemFolderDirectory = pathToPemFolderDirectory;
        }

        @Override
        public Result getEUJournalCertificates() {
            Result r = new Result();
            List<Certificate> certs = new ArrayList<>();
            try {
                for (String pemFile : pemFileName) {
                    String fullPath = pathToPemFolderDirectory + "/" + pemFile;
                    Certificate c = PemFileHelper.readFirstChain(fullPath)[0];
                    certs.add(c);
                }
            } catch (IOException | CertificateException e) {
                throw new RuntimeException(e);
            }
            r.setCertificates(certs);
            return r;
        }
    }

}
