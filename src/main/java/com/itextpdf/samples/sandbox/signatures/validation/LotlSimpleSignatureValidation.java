package com.itextpdf.samples.sandbox.signatures.validation;

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
import java.nio.file.Files;
import java.nio.file.Paths;

public class LotlSimpleSignatureValidation {

    public static final String SRC = "./src/main/resources/pdfs"
            + "/super_official_document_signed.pdf";

    public static final String DEST = "./target/sandbox/signatures/validation/somepdf.pdf";
    public static final String DUMMY_PDF = "./src/main/resources/validation/pdf/dummy.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Files.copy(Paths.get(DUMMY_PDF), Paths.get(DEST), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        new LotlSimpleSignatureValidation().showCaseCacheInitializationAndSimpleUsage();
    }

    public void showCaseCacheInitializationAndSimpleUsage() {
        ValidatorChainBuilder builder = new ValidatorChainBuilder();
        // We want to use LOTL as a source of trusted certificates
        builder.withOcspClient(() -> new DummyOcspClient());
        builder.trustEuropeanLotl(true);
        //We need to configure some additional properties for LOTL fetching
        // First of all we want to remove country data if something goes wrong during fetching,
        // here we choose to just not use those certificates, but other strategies are possible like if you
        //really need the certificates you can use new ThrowExceptionOnFailingCountryData() and handle the exception
        // in your code. (maybe try again later etc.)
        LotlFetchingProperties fetchingProperties = new LotlFetchingProperties(new RemoveOnFailingCountryData());
        //Our pdf is signed by a portuguese certificate, so we need to fetch the portuguese country lotl data.
        //If this is not set, the default behaviour is to fetch all countries in the lotl list (all european
        // countries + uk).
        fetchingProperties.setCountryNames(LotlCountryCodeConstants.PORTUGAL);
        LotlService.initializeGlobalCache(fetchingProperties);
        //If we want all countries except from a few we can use following api:
        //fetchingProperties
        // .setCountryNamesToIgnore(LotlCountryCodeConstants.ITALY, LotlCountryCodeConstants.UNITED_KINGDOM);

        //By default the cache is considered valid for 24 hours, if you want to change this you can use method
        //fetchingProperties.setCacheStalenessInMilliseconds
        //We highly recommend to not set this value too low as fetching the lotl data is network intensive,
        //and we want to avoid fetching it too often if not really needed.
        fetchingProperties.setCacheStalenessInMilliseconds(24 * 60 * 60 * 1000 * 2); //2 day

        // Behind the scenes we will refresh the certificates based on the
        fetchingProperties.getRefreshIntervalCalculator();
        // By default,  we will try 4 times per cache staleness period, So if the cache is valid for 24 hours
        // we will try to refresh every 6 hours, if you want to change this you can use
        fetchingProperties.setRefreshIntervalCalculator(
                (cacheStalenessInMilliseconds) -> cacheStalenessInMilliseconds / 8); //try every 3 hours
        //If you want to disable the automatic refresh you can use INT.MAX_VALUE as the refresh interval
        // fetchingProperties.setRefreshIntervalCalculator((cacheStalenessInMilliseconds) -> Integer.MAX_VALUE);

        //If you ran it without adding to the dependencies
        // <dependency>
        //   <groupId>com.itextpdf</groupId>
        //   <artifactId>eu-trusted-lists-resources</artifactId>
        //   <version>1.0.0</version>
        // </dependency>
        // you would get an exception here as the resources are not found. Something along the lines of:
        //Exception in thread "main" com.itextpdf.kernel.exceptions.PdfException: European Trusted List resources are
        // not available. Please ensure that the itextpdf-eutrustedlistsresources module is included in your project.
        // Alternatively,  you can use the EuropeanTrustedListConfigurationFactory to load the resources from a
        // custom location.
        try (PdfDocument document = new PdfDocument(new PdfReader(SRC))) {
            SignatureValidator validator = builder.buildSignatureValidator(document);
            ValidationReport r = validator.validateSignatures();
            //Here you have the vaidation report and can use it as you need
            System.out.println(r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
