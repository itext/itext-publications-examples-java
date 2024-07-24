package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.samples.sandbox.signatures.utils.TestOcspResponseBuilder;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.IssuingCertificateRetriever;
import com.itextpdf.signatures.validation.v1.SignatureValidationProperties;
import com.itextpdf.signatures.validation.v1.SignatureValidator;
import com.itextpdf.signatures.validation.v1.ValidatorChainBuilder;
import com.itextpdf.signatures.validation.v1.context.CertificateSource;
import com.itextpdf.signatures.validation.v1.context.CertificateSources;
import com.itextpdf.signatures.validation.v1.context.TimeBasedContext;
import com.itextpdf.signatures.validation.v1.context.TimeBasedContexts;
import com.itextpdf.signatures.validation.v1.context.ValidatorContext;
import com.itextpdf.signatures.validation.v1.context.ValidatorContexts;
import com.itextpdf.signatures.validation.v1.report.ValidationReport;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;

/**
 * Basic example of the existing signature validation.
 */
public class ValidateSignatureExample {
    public static final String SRC = "./src/main/resources/pdfs/validDocWithTimestamp.pdf";
    public static final String DEST = "./target/sandbox/signatures/validation/validateSignatureExample.txt";

    private Certificate[] certificateChain;
    private PrivateKey privateKey;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new ValidateSignatureExample().validateExistingSignature(SRC, DEST);
    }

    /**
     * Basic example of the existing signature validation.
     *
     * @param src  path to the signed document
     * @param dest path to the parsed validation report
     *
     * @throws Exception in case some exception occurred.
     */
    public void validateExistingSignature(String src, String dest) throws Exception {
        // Set up the validator.
        SignatureValidationProperties properties = getSignatureValidationProperties();
        properties.addOcspClient(getOcspClient());

        IssuingCertificateRetriever certificateRetriever = new IssuingCertificateRetriever();
        X509Certificate rootCert = (X509Certificate) getCertificateChain()[2];
        certificateRetriever.setTrustedCertificates(Collections.singletonList(rootCert));

        ValidatorChainBuilder validatorChainBuilder = new ValidatorChainBuilder()
                .withIssuingCertificateRetrieverFactory(() -> certificateRetriever)
                .withSignatureValidationProperties(properties);

        ValidationReport report;
        try (PdfDocument document = new PdfDocument(new PdfReader(src))) {
            SignatureValidator validator = validatorChainBuilder.buildSignatureValidator(document);

            // Validate all signatures in the document.
            report = validator.validateSignatures();
        }
        Assertions.assertSame(report.getValidationResult(), ValidationReport.ValidationResult.VALID);

        // Write validation report to the file.
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(report.toString().getBytes());
        }
    }

    /**
     * Use {@link SignatureValidationProperties} to configure different properties for the validators, e.g. whether to
     * continue validation after failure, whether to allow online revocation data fetching, set freshness value for the
     * revocation data. It is possible to specify properties for any {@link ValidatorContexts},
     * {@link CertificateSources} and {@link TimeBasedContext}.
     *
     * @return created {@link SignatureValidationProperties} instance.
     */
    protected SignatureValidationProperties getSignatureValidationProperties() {
        SignatureValidationProperties properties = new SignatureValidationProperties();
        properties.setRevocationOnlineFetching(ValidatorContexts.all(), CertificateSources.all(),
                TimeBasedContexts.all(), SignatureValidationProperties.OnlineFetching.NEVER_FETCH);
        properties.setFreshness(ValidatorContexts.all(), CertificateSources.all(),
                TimeBasedContexts.of(TimeBasedContext.HISTORICAL), Duration.ofDays(-5));
        properties.setContinueAfterFailure(
                ValidatorContexts.of(ValidatorContext.OCSP_VALIDATOR, ValidatorContext.CRL_VALIDATOR),
                CertificateSources.of(CertificateSource.CRL_ISSUER, CertificateSource.OCSP_ISSUER,
                        CertificateSource.CERT_ISSUER), false);
        return properties;
    }

    /**
     * Creates an OCSP client for the sample.
     *
     * <p>
     * NOTE: for the real validation you should use real revocation data clients
     * (such as {@link com.itextpdf.signatures.OcspClientBouncyCastle}).
     *
     * @return the OCSP client to be used for the validation.
     */
    protected IOcspClient getOcspClient() {
        try {
            Certificate[] certificateChain = getCertificateChain();
            X509Certificate signCert = (X509Certificate) certificateChain[0];
            X509Certificate intermediateCert = (X509Certificate) certificateChain[1];
            X509Certificate rootCert = (X509Certificate) certificateChain[2];
            String timestamp = "./src/main/resources/cert/timestamp.pem";
            X509Certificate timestampCert = (X509Certificate) PemFileHelper.readFirstChain(timestamp)[0];
            PrivateKey rootPrivateKey = getPrivateKey();

            Date currentDate = DateTimeUtil.getCurrentTimeDate();
            TestOcspResponseBuilder builder1 = new TestOcspResponseBuilder(rootCert, rootPrivateKey);
            builder1.setProducedAt(currentDate);
            builder1.setThisUpdate(DateTimeUtil.getCalendar(currentDate));
            builder1.setNextUpdate(DateTimeUtil.getCalendar(DateTimeUtil.addDaysToDate(currentDate, 30)));
            TestOcspResponseBuilder builder2 = new TestOcspResponseBuilder(rootCert, rootPrivateKey);
            builder2.setProducedAt(currentDate);
            builder2.setThisUpdate(DateTimeUtil.getCalendar(currentDate));
            builder2.setNextUpdate(DateTimeUtil.getCalendar(DateTimeUtil.addDaysToDate(currentDate, 30)));
            TestOcspResponseBuilder builder3 = new TestOcspResponseBuilder(rootCert, rootPrivateKey);
            builder3.setProducedAt(currentDate);
            builder3.setThisUpdate(DateTimeUtil.getCalendar(currentDate));
            builder3.setNextUpdate(DateTimeUtil.getCalendar(DateTimeUtil.addDaysToDate(currentDate, 30)));
            return new TestOcspClient().addBuilderForCertificate(intermediateCert, builder1)
                    .addBuilderForCertificate(signCert, builder2)
                    .addBuilderForCertificate(timestampCert, builder3);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates signing chain for the revocation data clients. This chain shouldn't be used for the real validation.
     *
     * @return the chain of certificates to be used for the validation.
     */
    private Certificate[] getCertificateChain() {
        if (certificateChain == null) {
            try {
                String CHAIN = "./src/main/resources/cert/validCertsChain.pem";
                certificateChain = PemFileHelper.readFirstChain(CHAIN);
            } catch (Exception e) {
                // Ignore.
            }
        }
        return certificateChain;
    }

    /**
     * Creates private key for the sample. This key shouldn't be used for the real validation.
     *
     * @return {@link PrivateKey} instance to be used for the validation.
     */
    private PrivateKey getPrivateKey() {
        if (privateKey == null) {
            try {
                final String rootKey = "./src/main/resources/cert/rootCertKey.pem";
                final char[] password = "testpassphrase".toCharArray();
                privateKey = PemFileHelper.readFirstKey(rootKey, password);
            } catch (Exception e) {
                // Ignore.
            }
        }
        return privateKey;
    }
}
