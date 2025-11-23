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

/**
 * Sample: validation of the List of Trusted Lists (LOTL) for third countries.
 *
 * <p>What this sample does:
 * <br>
 * - Points the {@link LotlValidator} to the European Commission third-country LOTL
 * (also known as MRA/AdES LOTL) instead of the default EU LOTL URL.
 * <br>
 * - Customizes fetching of Official Journal signing certificates used to sign the third-country LOTL
 * by extracting X.509 certificates from the pointers section of a local TSL resource.
 * <br>
 * - Overrides pivot handling because the third-country LOTL does not publish pivot files the same way
 * as the EU LOTL. The custom pivot fetcher directly verifies the LOTL XML signature instead.
 * <br>
 * - Initializes a {@link LotlService} with tailored {@link LotlFetchingProperties} that limit countries
 * to those relevant for the scenario (e.g., UA, MD) and remove failed country data from use.
 * <br>
 * - Produces a {@link ValidationReport} describing the LOTL validation outcome.
 * <br>
 * <p>When to use this approach:
 * <br>
 * - When you need to validate the LOTL for third countries.
 * <br>
 * - When you need to adapt resource/pivot fetching logic to the structure of the third-country LOTL.
 * <br>
 * <p>Key customizations in this file:
 * <br>
 * - {@link EuropeanTrustedListConfigurationFactoryForThirdCountries}: supplies the third‑country LOTL URI
 * while reusing the Official Journal signing certificates from the default configuration.
 * <br>
 * - {@link ThirdCountriesResourceFetcher}: provides Official Journal certificates by parsing them from
 * PointersToOtherTSL data of a local TSL resource.
 * <br>
 * - {@link ThirdCountriesDoesNotContainPivots}: bypasses pivot downloads and validates the LOTL XML
 * signature directly with trusted Official Journal certificates.
 */

public class LotlValidationThirdCountryTL {

    /**
     * Path to a local TSL XML used only to extract Official Journal certificates from
     * the PointersToOtherTSL section. This is a convenience source for the certificates
     * that sign the third-country LOTL.
     */
    private static final String TSL = "./src/main/resources/validation/tsl/jgoigecgmelgnadppbgklkndmkdgcjpm";

    /**
     * Path where the textual {@link ValidationReport} will be written.
     */
    public static final String DEST = "./target/sandbox/signatures/validation/third_country_tl_validation.txt";

    /**
     * Entry point that ensures the output folder exists and triggers third-country LOTL validation.
     *
     * @param args CLI arguments (not used)
     * @throws IOException if report file creation fails
     */

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        LotlValidationThirdCountryTL.validateThirdCountryTL();
    }

    /**
     * Configures a {@link LotlService} and {@link LotlValidator} to work with the third-country LOTL:
     * <br>
     * - Temporarily swaps the global {@link EuropeanTrustedListConfigurationFactory} with a variant that
     * returns the third-country LOTL URI.
     * <br>
     * - Sets {@link LotlFetchingProperties} to remove failing country data and to focus on specific
     * country codes (e.g., UA, MD).
     * <br>
     * - Installs {@link ThirdCountriesResourceFetcher} to supply Official Journal certificates and
     * {@link ThirdCountriesDoesNotContainPivots} to validate without pivots.
     * <br>
     * - Initializes cache, validates the LOTL, and writes the result to {@link #DEST}.
     * <br>
     * <p>The original factory is restored in a finally block to avoid side effects.
     *
     * @throws IOException if writing the report fails
     */

    public static void validateThirdCountryTL() throws IOException {
        EuropeanTrustedListConfigurationFactory ogFactory =
                EuropeanTrustedListConfigurationFactory.getFactory().get();
        try {

            EuropeanTrustedListConfigurationFactory.setFactory(() ->
                    new EuropeanTrustedListConfigurationFactoryForThirdCountries(ogFactory)
            );

            // We need to configure some additional properties for LOTL fetching
            // We want to remove country data if something goes wrong during fetching
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

    /**
     * Custom {@link EuropeanTrustedListConfigurationFactory} that targets the third‑country LOTL.
     *
     * <p>Why it is needed:
     * <br>
     * - The third-country LOTL is published at a different URI than the main EU LOTL; this factory
     * points the validator to that URI.
     * <br>
     * - The third-country LOTL is signed with EU Official Journal certificates, so we reuse the
     * certificates provided by the original factory.
     */
    static class EuropeanTrustedListConfigurationFactoryForThirdCountries extends
            EuropeanTrustedListConfigurationFactory {

        private final EuropeanTrustedListConfigurationFactory originalFactory;

        /**
         * Wraps the original factory to delegate certificate retrieval while overriding the LOTL URI.
         *
         * @param originalFactory the default factory used to obtain Official Journal certificates
         */
        EuropeanTrustedListConfigurationFactoryForThirdCountries(EuropeanTrustedListConfigurationFactory originalFactory) {
            this.originalFactory = originalFactory;
        }

        /**
         * Returns the URI of the third‑country (MRA/AdES) LOTL.
         */
        public String getTrustedListUri() {
            return "https://ec.europa.eu/tools/lotl/mra/ades-lotl.xml";
        }

        /**
         * Third‑country LOTL does not rely on the same publication identifier; empty string is returned.
         */
        public String getCurrentlySupportedPublication() {
            return "";
        }

        /**
         * Reuses Official Journal signing certificates from the original factory so the XML signature
         * of the third-country LOTL can be validated with the same trust anchors.
         *
         * @return Official Journal signing certificates
         */
        public List<Certificate> getCertificates() {
            return originalFactory.getCertificates();
        }
    }

    /**
     * Resource fetcher tailored for the third‑country LOTL.
     *
     * <p>What it does:
     * <br>
     * - Provides EU Official Journal certificates by parsing them from the PointersToOtherTSL section
     * of a local TSL XML resource.
     * <br>
     * - Ensures the validator can verify the signature of the third‑country LOTL without depending
     * on the default resource locations.
     */
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

        /**
         * Loads Official Journal certificates from a local TSL XML file by reading the
         * PointersToOtherTSL → ServiceDigitalIdentities → X509Certificate entries.
         *
         * @param euXmlPath path to a local TSL XML
         * @return list of parsed certificates
         * @throws Exception if XML parsing or certificate creation fails
         */

        private static List<Certificate> loadCertificatesFromPointersToOtherTSL(Path euXmlPath) throws Exception {
            try (InputStream in = Files.newInputStream(euXmlPath)) {
                return loadCertificatesFromPointersToOtherTSL(in);
            }
        }

        /**
         * Parses X.509 certificates from the given TSL XML stream using a namespace‑agnostic XPath.
         *
         * @param xmlStream input stream containing TSL XML
         * @return list of X.509 certificates extracted from the XML
         * @throws Exception if parsing or decoding fails
         */
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

    /**
     * Pivot handler for the third‑country LOTL which does not rely on pivot files.
     *
     * <p>Why it is needed:
     * <br>
     * - The third‑country LOTL does not publish or require pivot files in the same manner as the EU LOTL.
     * <br>
     * - This fetcher short‑circuits pivot retrieval and directly validates the LOTL XML signature
     * using trusted Official Journal certificates.
     */
    static class ThirdCountriesDoesNotContainPivots extends PivotFetcher {
        /**
         * Creates the pivot fetcher bound to the provided {@link LotlService}.
         *
         * @param service current LOTL service instance
         */
        public ThirdCountriesDoesNotContainPivots(LotlService service) {
            super(service);
        }

        /**
         * Retains the current journal URI if set by upstream logic.
         *
         * @param currentJournalUri current journal URI
         */
        @Override
        public void setCurrentJournalUri(String currentJournalUri) {
            super.setCurrentJournalUri(currentJournalUri);
        }

        /**
         * Validates the LOTL XML directly instead of downloading pivot files.
         *
         * <p>Implementation details:
         * <br>
         * - Builds a {@link TrustedCertificatesStore} from the provided Official Journal certificates.
         * <br>
         * - Uses a {@link CustomXmlSignatureValidator} to validate the LOTL XML signature.
         * <br>
         * - On failure, merges the detailed validation report and marks the operation as invalid.
         *
         * @param lotlXml      the LOTL XML bytes to validate
         * @param certificates trusted Official Journal certificates
         * @return a result whose {@code localReport} contains signature validation details
         */
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

        /**
         * Small adapter around {@link XmlSignatureValidator} to expose its validate method
         * for direct use in this sample.
         */
        static class CustomXmlSignatureValidator extends XmlSignatureValidator {

            /**
             * Creates a validator using the provided trust store containing Official Journal certificates.
             *
             * @param trustedCertificatesStore {@link TrustedCertificatesStore} holding trusted certificates
             */
            protected CustomXmlSignatureValidator(TrustedCertificatesStore trustedCertificatesStore) {
                super(trustedCertificatesStore);
            }

            /**
             * Exposes {@link #validate(InputStream)} for use by the enclosing pivot fetcher.
             *
             * @param xmlDocumentInputStream input stream of the LOTL XML
             * @return a {@link ValidationReport} with the signature validation outcome
             */
            public ValidationReport publicValidate(InputStream xmlDocumentInputStream) {
                return super.validate(xmlDocumentInputStream);
            }
        }

    }
}
