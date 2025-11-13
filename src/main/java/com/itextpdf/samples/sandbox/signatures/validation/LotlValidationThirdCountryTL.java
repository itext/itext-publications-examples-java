package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.signatures.validation.EuropeanTrustedListConfigurationFactory;
import com.itextpdf.signatures.validation.SafeCalling;
import com.itextpdf.signatures.validation.TrustedCertificatesStore;
import com.itextpdf.signatures.validation.lotl.EuropeanResourceFetcher;
import com.itextpdf.signatures.validation.lotl.LotlFetchingProperties;
import com.itextpdf.signatures.validation.lotl.LotlService;
import com.itextpdf.signatures.validation.lotl.LotlValidator;
import com.itextpdf.signatures.validation.lotl.PivotFetcher;
import com.itextpdf.signatures.validation.lotl.RemoveOnFailingCountryData;
import com.itextpdf.signatures.validation.lotl.XmlSignatureValidator;
import com.itextpdf.signatures.validation.report.ReportItem;
import com.itextpdf.signatures.validation.report.ValidationReport;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class LotlValidationThirdCountryTL {

    private static final String TSL = "./src/main/resources/validation/tsl/jgoigecgmelgnadppbgklkndmkdgcjpm";
    public static final String DEST = "./target/sandbox/signatures/validation/third_country_tl_validation.txt";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        LotlValidationThirdCountryTL.validateThirdCountryTL();
    }

    public static void validateThirdCountryTL() throws IOException {
        EuropeanTrustedListConfigurationFactory ogFactory =
                EuropeanTrustedListConfigurationFactory.getFactory().get();
        try {

            EuropeanTrustedListConfigurationFactory.setFactory(() ->
                    new EuropeanTrustedListConfigurationFactoryForThirdCountries(ogFactory)
            );

            LotlFetchingProperties lotlFetchingProperties = new LotlFetchingProperties(
                    new RemoveOnFailingCountryData());
            lotlFetchingProperties.setCountryNames("UA", "MD");

            LotlValidator validator;
            try (LotlService lotlService = new LotlService(lotlFetchingProperties)) {
                lotlService.withEuropeanResourceFetcher(new ThirdCountriesResourceFetcher());
                lotlService.withPivotFetcher(new ThirdCountriesDoesNotContainPivots(lotlService));
                lotlService.initializeCache();
                validator = new LotlValidator(lotlService);
            }
            ValidationReport report = validator.validate();
            //Here you have the validation report and can use it as you need
            Files.write(Paths.get(DEST), report.toString().getBytes(StandardCharsets.UTF_8));
            System.out.println(report);
        } finally {
            EuropeanTrustedListConfigurationFactory.setFactory(() -> ogFactory);
        }
    }

    static class EuropeanTrustedListConfigurationFactoryForThirdCountries extends
            EuropeanTrustedListConfigurationFactory {

        private final EuropeanTrustedListConfigurationFactory originalFactory;


        EuropeanTrustedListConfigurationFactoryForThirdCountries(EuropeanTrustedListConfigurationFactory originalFactory) {
            this.originalFactory = originalFactory;
        }


        public String getTrustedListUri() {
            return "https://ec.europa.eu/tools/lotl/mra/ades-lotl.xml";
        }

        public String getCurrentlySupportedPublication() {
            return "";
        }

        /**
         * The third-countries LOTL (ades-lotl.xml) is also signed with the EU-published
         * Official Journal signing certificates. Reusing the original factory's certificates
         * ensures signature validation works for both the main LOTL and the third-countries LOTL.
         */
        public List<Certificate> getCertificates() {
            return originalFactory.getCertificates();
        }
    }

    static class ThirdCountriesResourceFetcher extends EuropeanResourceFetcher {
        @Override
        public Result getEUJournalCertificates() {
            Result result = new Result();
            SafeCalling.onExceptionLog(
                    () -> result.setCertificates(loadCertificatesFromPointersToOtherTSL(Paths.get(TSL))),
                    result.getLocalReport(),
                    e -> new ReportItem(LotlValidator.LOTL_VALIDATION, "JOURNAL_CERT_NOT_PARSABLE",
                            e, ReportItem.ReportItemStatus.INFO));
            return result;
        }

        private static List<Certificate> loadCertificatesFromPointersToOtherTSL(Path euXmlPath) throws Exception {
            try (InputStream in = Files.newInputStream(euXmlPath)) {
                return loadCertificatesFromPointersToOtherTSL(in);
            }
        }

        private static List<Certificate> loadCertificatesFromPointersToOtherTSL(InputStream xmlStream) throws Exception {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            Document doc = dbf.newDocumentBuilder().parse(xmlStream);

            // Use a namespace-agnostic XPath with local-name() to avoid issues with default/prefixed namespaces
            XPath xp = XPathFactory.newInstance().newXPath();
            String x509CertXPath =
                    "/*[local-name()='TrustServiceStatusList']" +
                    "/*[local-name()='SchemeInformation']" +
                    "/*[local-name()='PointersToOtherTSL']" +
                    "//*[local-name()='ServiceDigitalIdentities']" +
                    "/*[local-name()='ServiceDigitalIdentity']" +
                    "/*[local-name()='DigitalId']" +
                    "/*[local-name()='X509Certificate']";

            NodeList nodes = (NodeList) xp.evaluate(x509CertXPath, doc, XPathConstants.NODESET);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            List<Certificate> result = new ArrayList<>(nodes.getLength());

            for (int i = 0; i < nodes.getLength(); i++) {
                String b64 = nodes.item(i).getTextContent();
                if (b64 == null) continue;
                b64 = b64.trim();
                if (b64.isEmpty()) continue;

                byte[] der = Base64.getMimeDecoder().decode(b64);
                try (ByteArrayInputStream bin = new ByteArrayInputStream(der)) {
                    Certificate cert = cf.generateCertificate(bin);
                    result.add(cert);
                }
            }
            return result;
        }

    }

    static class ThirdCountriesDoesNotContainPivots extends PivotFetcher {
        public ThirdCountriesDoesNotContainPivots(LotlService service) {
            super(service);
        }

        @Override
        public void setCurrentJournalUri(String currentJournalUri) {
            super.setCurrentJournalUri(currentJournalUri);
        }

        @Override
        public Result downloadAndValidatePivotFiles(byte[] lotlXml, List<Certificate> certificates) {
            List<Certificate> trustedCertificates = certificates;
            Result result = new Result();
            TrustedCertificatesStore trustedCertificatesStore = new TrustedCertificatesStore();
            trustedCertificatesStore.addGenerallyTrustedCertificates(trustedCertificates);

            CustomXmlSignatureValidator xmlSignatureValidator = new CustomXmlSignatureValidator(trustedCertificatesStore);
            ValidationReport localReport = xmlSignatureValidator.publicValidate(new ByteArrayInputStream(lotlXml));
            if (localReport.getValidationResult() != ValidationReport.ValidationResult.VALID) {
                result.getLocalReport().addReportItem(new ReportItem(LotlValidator.LOTL_VALIDATION,
                        "LOTL_VALIDATION_UNSUCCESSFUL", ReportItem.ReportItemStatus.INVALID));
                result.getLocalReport().merge(localReport);
                return result;
            }
            return result;
        }

        static class CustomXmlSignatureValidator extends XmlSignatureValidator {

            /**
             * Creates {@link XmlSignatureValidator} instance. This constructor shall not be used directly.
             *
             * @param trustedCertificatesStore {@link TrustedCertificatesStore} which contains trusted certificates
             */
            protected CustomXmlSignatureValidator(TrustedCertificatesStore trustedCertificatesStore) {
                super(trustedCertificatesStore);
            }

            public ValidationReport publicValidate(InputStream xmlDocumentInputStream) {
                return super.validate(xmlDocumentInputStream);
            }
        }

    }
}
