package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.commons.utils.MessageFormatUtil;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.samples.sandbox.signatures.utils.TestOcspResponseBuilder;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.IssuingCertificateRetriever;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.validation.v1.CertificateChainValidator;
import com.itextpdf.signatures.validation.v1.SignatureValidationProperties;
import com.itextpdf.signatures.validation.v1.ValidatorChainBuilder;
import com.itextpdf.signatures.validation.v1.context.*;
import com.itextpdf.signatures.validation.v1.report.ReportItem;
import com.itextpdf.signatures.validation.v1.report.ValidationReport;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Basic example of the existing signature validation.
 */
public class ValidateSignatureExample {
    public static final String SRC = "./src/main/resources/pdfs/validDocWithTimestamp.pdf";
    public static final String DEST = "./target/sandbox/signatures/validation/validateSignatureExample.txt";

    private static final String SIGNATURE_VERIFICATION = "Signature verification check.";
    private static final String CANNOT_VERIFY_SIGNATURE = "Signature {0} cannot be mathematically verified.";
    private static final String DOCUMENT_IS_NOT_COVERED = "Signature {0} doesn't cover entire document.";
    private static final String TIMESTAMP_VERIFICATION = "Timestamp verification check.";
    private static final String CANNOT_VERIFY_TIMESTAMP = "Signature timestamp attribute cannot be verified";

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

        IssuingCertificateRetriever certificateRetriever = new IssuingCertificateRetriever();
        X509Certificate rootCert = (X509Certificate) getCertificateChain()[2];
        certificateRetriever.setTrustedCertificates(Collections.singletonList(rootCert));

        ValidatorChainBuilder validatorChainBuilder = new ValidatorChainBuilder()
                .withIssuingCertificateRetriever(certificateRetriever)
                .withSignatureValidationProperties(properties);

        ValidationReport report = new ValidationReport();
        try (PdfDocument document = new PdfDocument(new PdfReader(src))) {
            CertificateChainValidator validator = validatorChainBuilder.buildCertificateChainValidator();
            validator.addOcspClient(getOcspClient());

            // Validate the signature.
            SignatureUtil signatureUtil = new SignatureUtil(document);
            List<String> signatures = signatureUtil.getSignatureNames();
            String latestSignatureName = signatures.get(signatures.size() - 1);
            PdfPKCS7 pkcs7 = signatureUtil.readSignatureData(latestSignatureName);
            validateSignature(report, signatureUtil, latestSignatureName, pkcs7);

            Date signingDate = DateTimeUtil.getCurrentTimeDate();
            if (pkcs7.getTimeStampTokenInfo() != null) {
                // Validate timestamp.
                validateTimestamp(certificateRetriever, report, validator, pkcs7);
                signingDate = pkcs7.getTimeStampDate().getTime();
            }

            Certificate[] certificates = pkcs7.getCertificates();
            certificateRetriever.addKnownCertificates(Arrays.asList(certificates));
            X509Certificate signingCertificate = pkcs7.getSigningCertificate();

            // Set up validation context related to CertificateChainValidator for the historical validation.
            ValidationContext baseContext = new ValidationContext(ValidatorContext.CERTIFICATE_CHAIN_VALIDATOR,
                    CertificateSource.SIGNER_CERT, TimeBasedContext.HISTORICAL);

            // Validate the signing chain. ValidationReport will contain all the validation report messages.
            report = validator.validate(report, baseContext, signingCertificate, signingDate);
        }
        Assert.assertSame(report.getValidationResult(), ValidationReport.ValidationResult.VALID);

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
     * Check if the signature covers the entire document and verify signature integrity and authenticity.
     *
     * @param report              validation report
     * @param signatureUtil       signature util instance created for the signed document
     * @param latestSignatureName signature name
     * @param pkcs7               created {@link PdfPKCS7} instance
     */
    protected void validateSignature(ValidationReport report, SignatureUtil signatureUtil, String latestSignatureName,
                                     PdfPKCS7 pkcs7) {
        if (!signatureUtil.signatureCoversWholeDocument(latestSignatureName)) {
            report.addReportItem(new ReportItem(SIGNATURE_VERIFICATION, MessageFormatUtil.format(
                    DOCUMENT_IS_NOT_COVERED, latestSignatureName), ReportItem.ReportItemStatus.INVALID));
        }
        try {
            if (!pkcs7.verifySignatureIntegrityAndAuthenticity()) {
                report.addReportItem(new ReportItem(SIGNATURE_VERIFICATION, MessageFormatUtil.format(
                        CANNOT_VERIFY_SIGNATURE, latestSignatureName), ReportItem.ReportItemStatus.INVALID));
            }
        } catch (GeneralSecurityException e) {
            report.addReportItem(new ReportItem(SIGNATURE_VERIFICATION, MessageFormatUtil.format(
                    CANNOT_VERIFY_SIGNATURE, latestSignatureName), e, ReportItem.ReportItemStatus.INVALID));
        }
    }

    /**
     * Verify timestamp imprint and validate timestamp certificates chain.
     *
     * @param certificateRetriever certificate retriever
     * @param report               validation report
     * @param validator            certificate chain validator instance
     * @param pkcs7                created {@link PdfPKCS7} instance
     */
    protected void validateTimestamp(IssuingCertificateRetriever certificateRetriever, ValidationReport report,
                                     CertificateChainValidator validator, PdfPKCS7 pkcs7) {
        try {
            if (!pkcs7.verifyTimestampImprint()) {
                report.addReportItem(new ReportItem(TIMESTAMP_VERIFICATION, CANNOT_VERIFY_TIMESTAMP,
                        ReportItem.ReportItemStatus.INVALID));
            }
        } catch (GeneralSecurityException e) {
            report.addReportItem(new ReportItem(TIMESTAMP_VERIFICATION, CANNOT_VERIFY_TIMESTAMP, e,
                    ReportItem.ReportItemStatus.INVALID));
        }

        // Validate timestamp certificates chain.
        Certificate[] timestampCertificates = pkcs7.getTimestampCertificates();
        certificateRetriever.addKnownCertificates(Arrays.asList(timestampCertificates));
        ValidationContext baseContext = new ValidationContext(ValidatorContext.CERTIFICATE_CHAIN_VALIDATOR,
                CertificateSource.TIMESTAMP, TimeBasedContext.PRESENT);
        validator.validate(report, baseContext, (X509Certificate) timestampCertificates[0],
                DateTimeUtil.getCurrentTimeDate());
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
