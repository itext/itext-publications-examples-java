package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.samples.sandbox.signatures.utils.TestOcspResponseBuilder;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.IssuingCertificateRetriever;
import com.itextpdf.signatures.PdfPadesSigner;
import com.itextpdf.signatures.SignerProperties;
import com.itextpdf.signatures.validation.v1.CertificateChainValidator;
import com.itextpdf.signatures.validation.v1.SignatureValidationProperties;
import com.itextpdf.signatures.validation.v1.ValidatorChainBuilder;
import com.itextpdf.signatures.validation.v1.context.CertificateSource;
import com.itextpdf.signatures.validation.v1.context.TimeBasedContext;
import com.itextpdf.signatures.validation.v1.context.ValidationContext;
import com.itextpdf.signatures.validation.v1.context.ValidatorContext;
import com.itextpdf.signatures.validation.v1.report.ValidationReport;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;

/**
 * Basic example of the certificate chain validation before the document signing.
 */
public class ValidateChainBeforeSigningExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/signatures/validation/validateChainBeforeSigningExample.txt";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String ROOT = "./src/main/resources/cert/root.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    private Certificate[] certificateChain;
    private PrivateKey privateKey;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new ValidateChainBeforeSigningExample().validateChainAndSignSignature(SRC, DEST);
    }

    /**
     * Basic example of the certificate chain validation before the document signing.
     *
     * @param src  pdf source file
     * @param dest validation report destination file
     *
     * @throws Exception in case some exception occurred.
     */
    public void validateChainAndSignSignature(String src, String dest)
            throws Exception {
        Certificate[] certificateChain = getCertificateChain();
        X509Certificate signingCert = (X509Certificate) certificateChain[0];
        X509Certificate rootCert = (X509Certificate) certificateChain[1];

        // Set up the validator.
        SignatureValidationProperties properties = new SignatureValidationProperties()
                .addOcspClient(getOcspClient());
        IssuingCertificateRetriever certificateRetriever = new IssuingCertificateRetriever();
        ValidatorChainBuilder validatorChainBuilder = new ValidatorChainBuilder()
                .withIssuingCertificateRetrieverFactory(() -> certificateRetriever)
                .withSignatureValidationProperties(properties);

        CertificateChainValidator validator = validatorChainBuilder.buildCertificateChainValidator();
        certificateRetriever.setTrustedCertificates(Collections.singletonList(rootCert));

        ValidationContext baseContext = new ValidationContext(ValidatorContext.CERTIFICATE_CHAIN_VALIDATOR,
                CertificateSource.SIGNER_CERT, TimeBasedContext.PRESENT);

        // Validate the chain. ValidationReport will contain all the validation report messages.
        ValidationReport report =
                validator.validateCertificate(baseContext, signingCert, DateTimeUtil.getCurrentTimeDate());

        if (ValidationReport.ValidationResult.VALID == report.getValidationResult()) {
            signDocument(src);
        }

        // Write validation report to the file.
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(report.toString().getBytes());
        }
    }

    /**
     * Sign document with PaDES Baseline-B Profile.
     *
     * @param src source file
     *
     * @throws Exception in case some exception occurred.
     */
    protected void signDocument(String src) throws Exception {
        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(src)),
                new ByteArrayOutputStream());
        SignerProperties signerProperties = createSignerProperties();
        padesSigner.signWithBaselineBProfile(signerProperties, getCertificateChain(), getPrivateKey(SIGN));
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    protected Certificate[] getCertificateChain() {
        if (certificateChain == null) {
            try {
                certificateChain = PemFileHelper.readFirstChain(CHAIN);
            } catch (Exception e) {
                // Ignore.
            }
        }
        return certificateChain;
    }

    /**
     * Creates private key for the sample. This key shouldn't be used for the real signing.
     *
     * @param path path to the private key
     *
     * @return {@link PrivateKey} instance to be used for the main signing operation.
     */
    protected PrivateKey getPrivateKey(String path) {
        if (privateKey == null) {
            try {
                privateKey = PemFileHelper.readFirstKey(path, PASSWORD);
            } catch (Exception e) {
                // Ignore.
            }
        }
        return privateKey;
    }

    /**
     * Creates an OCSP client for the sample.
     *
     * <p>
     * NOTE: for the real signing you should use real revocation data clients
     * (such as {@link com.itextpdf.signatures.OcspClientBouncyCastle}).
     *
     * @return the OCSP client to be used for the signing operation.
     */
    protected IOcspClient getOcspClient() {
        try {
            Certificate[] certificates = getCertificateChain();
            TestOcspResponseBuilder builder = new TestOcspResponseBuilder((X509Certificate) certificates[1],
                    getPrivateKey(ROOT));
            Date currentDate = DateTimeUtil.getCurrentTimeDate();
            builder.setProducedAt(currentDate);
            builder.setThisUpdate(DateTimeUtil.getCalendar(currentDate));
            builder.setNextUpdate(DateTimeUtil.getCalendar(DateTimeUtil.addDaysToDate(currentDate, 10)));
            return new TestOcspClient().addBuilderForCertificate((X509Certificate) certificates[0], builder);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates properties to be used in signing operations.
     *
     * @return {@link SignerProperties} properties to be used for main signing operation.
     */
    protected SignerProperties createSignerProperties() {
        SignerProperties signerProperties = new SignerProperties()
                .setFieldName("Signature1");
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(signerProperties.getFieldName())
                .setContent("Approval test signature.\nCreated by iText.");
        signerProperties
                .setPageNumber(1)
                .setPageRect(new Rectangle(50, 650, 200, 100))
                .setSignatureAppearance(appearance)
                .setReason("Reason")
                .setLocation("Location");
        return signerProperties;
    }
}
