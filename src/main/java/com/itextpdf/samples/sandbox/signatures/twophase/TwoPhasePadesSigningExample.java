package com.itextpdf.samples.sandbox.signatures.twophase;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestCrlClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestTsaClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.ISignatureMechanismParams;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.PadesTwoPhaseSigningHelper;
import com.itextpdf.signatures.PdfSignature;
import com.itextpdf.signatures.PdfTwoPhaseSigner;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.SignerProperties;
import com.itextpdf.signatures.cms.CMSContainer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

public class TwoPhasePadesSigningExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String PREP = "./target/sandbox/signatures/twophase/twoPhasePadesSignatureLevelLT.temp";
    public static final String DEST = "./target/sandbox/signatures/twophase/twoPhasePadesSignatureLevelLT.pdf";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String ROOT = "./src/main/resources/cert/root.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final String TSA = "./src/main/resources/cert/tsaCert.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    /**
     * Basic example of two phase document signing with PaDES Baseline-LT Profile.
     *
     * @param args no arguments are needed to run this example.
     *
     * @throws Exception some error during file creation/accessing or during signing
     */
    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        SignerProperties signerProperties = createSignerProperties("PADES_SIGNATURE");
        // prepare the document
        CMSContainer cmsContainer = new TwoPhasePadesSigningExample().prepareDocument(SRC, PREP, getCertificateChain(),
                "SHA256", signerProperties);

        // simulate getting the signature
        byte[] signature = getSignatureForSignedAttributes(cmsContainer.getSerializedSignedAttributes(),
                getPrivateKey(), "sha256WithRSA");

        // add the signature and needed info for a valid pades signature
        new TwoPhasePadesSigningExample().signSignatureWithBaselineLTProfile(PREP, DEST, signerProperties, signature,
                "SHA256", "sha256WithRSA", null);
    }

    /**
     * Complete the signing process.
     *
     * @param src              the path to the prepared document
     * @param dest             the path to the signed document
     * @param signerProperties the signerproperties used to sign
     * @param signature        the signature
     * @param digestAlgorithm  the used digest algorithm name
     * @param signingAlgoritm  the used signing algorithm
     * @param signingParams    the used signong algorithm properties
     *
     * @throws Exception if some exception occur.
     */
    public void signSignatureWithBaselineLTProfile(String src, String dest, SignerProperties signerProperties,
            byte[] signature, String digestAlgorithm, String signingAlgoritm,
            ISignatureMechanismParams signingParams)
            throws Exception {

        // Extract the prepared CMS container from the prepared document.
        CMSContainer cmsContainer;
        try (PdfReader reader = new PdfReader(src);
                PdfDocument doc = new PdfDocument(reader)) {
            SignatureUtil su = new SignatureUtil(doc);
            PdfSignature preparedSignature = su.getSignature(signerProperties.getFieldName());
            cmsContainer = new CMSContainer(preparedSignature.getContents().getValueBytes());
        }

        // Second phase of signing.
        PadesTwoPhaseSigningHelper helper = new PadesTwoPhaseSigningHelper();
        helper.setTrustedCertificates(getTrustedStore());
        helper.setTSAClient(getTsaClient());
        helper.setOcspClient(getOcspClient());
        helper.setCrlClient(getCrlClient());

        try (PdfReader reader = new PdfReader(src);
                OutputStream outputStream = new FileOutputStream(dest)) {
            // An external signature implementation that starts from an existing signature.
            IExternalSignature externalSignature = new SignatureProvider(signature, digestAlgorithm, signingAlgoritm,
                    signingParams);

            helper.signCMSContainerWithBaselineTProfile(externalSignature, reader, outputStream,
                    signerProperties.getFieldName(), cmsContainer);
        }
    }

    /**
     * This method simulates signing outside this application.
     *
     * @param signedAttributes     the data to be signed
     * @param pk                   the private key
     * @param signingAlgorithmName the signing algorithm name, see  <a
     *                             href="https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names
     *                             .html#signature-algorithms">...</a>
     *
     * @return the signature of the data.
     *
     * @throws NoSuchAlgorithmException when the signing algorithm is unknown.
     * @throws InvalidKeyException      when the private key mismatches the algorithm.
     * @throws SignatureException       if this Signature object is not initialized properly or if this
     *                                  signature algorithm is unable to process the input data provided.
     */
    protected static byte[] getSignatureForSignedAttributes(byte[] signedAttributes, PrivateKey pk,
            String signingAlgorithmName) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance(signingAlgorithmName);
        signature.initSign(pk);
        signature.update(signedAttributes);
        return signature.sign();
    }

    /**
     * Prepare the document and put the prepared cms container in it for later reuse.
     *
     * @param src              the path to the document to be signed
     * @param prep             the path to the prepared document
     * @param certificateChain the certificate chain
     * @param digestAlgoritm   the name of the digest algorithm
     * @param signerProperties the signer properties to use to prepare the document
     *
     * @return A prepared CMS container.
     *
     * @throws IOException              when a problem occurs opening or creating the source or prepared file.
     * @throws GeneralSecurityException when a problem occurs processing the certificate chain.
     */
    private CMSContainer prepareDocument(String src, String prep, Certificate[] certificateChain, String digestAlgoritm,
            SignerProperties signerProperties)
            throws IOException, GeneralSecurityException {
        CMSContainer cmsContainer;

        try (PdfReader reader = new PdfReader(src);
                OutputStream outputStream = new FileOutputStream(prep + ".working")) {
            PadesTwoPhaseSigningHelper helper = new PadesTwoPhaseSigningHelper();
            helper.setTrustedCertificates(getTrustedStore());
            cmsContainer = helper.createCMSContainerWithoutSignature(certificateChain, digestAlgoritm, reader,
                    outputStream, signerProperties);
        }
        // This is optional, the CMS container can be stored in any way.
        // Adding the prepared CMS container to the prepared document
        try (PdfReader reader = new PdfReader(prep + ".working");
                OutputStream outputStream = new FileOutputStream(prep)) {
            PdfTwoPhaseSigner.addSignatureToPreparedDocument(reader, signerProperties.getFieldName(), outputStream,
                    cmsContainer);
        }
        FileUtil.deleteFile(new File(prep + ".working"));
        return cmsContainer;
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    private static Certificate[] getCertificateChain() {
        try {
            return PemFileHelper.readFirstChain(CHAIN);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates trusted certificate store for the sample. This store shouldn't be used for the real signing.
     *
     * @return the trusted certificate store to be used for the missing certificates in chain an CRL response
     * issuer certificates retrieving during the signing operation.
     */
    private static List<Certificate> getTrustedStore() {
        try {
            return Arrays.asList(PemFileHelper.readFirstChain(ROOT));
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates private key for the sample. This key shouldn't be used for the real signing.
     *
     * @return {@link PrivateKey} instance to be used for the main signing operation.
     */
    private static PrivateKey getPrivateKey() {
        try {
            return PemFileHelper.readFirstKey(SIGN, PASSWORD);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates timestamp client for the sample which will be used for the timestamp creation.
     *
     * <p>
     * NOTE: for the real signing you should use real revocation data clients
     * (such as {@link com.itextpdf.signatures.TSAClientBouncyCastle}).
     *
     * @return the TSA client to be used for the signing operation.
     */
    private static ITSAClient getTsaClient() {
        try {
            Certificate[] tsaChain = PemFileHelper.readFirstChain(TSA);
            PrivateKey tsaPrivateKey = PemFileHelper.readFirstKey(TSA, PASSWORD);
            return new TestTsaClient(Arrays.asList(tsaChain), tsaPrivateKey);
        } catch (Exception e) {
            throw new PdfException(e);
        }
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
    private static IOcspClient getOcspClient() {
        try {
            Certificate[] caCert = PemFileHelper.readFirstChain(ROOT);
            PrivateKey caPrivateKey = PemFileHelper.readFirstKey(ROOT, PASSWORD);
            return new TestOcspClient().addBuilderForCertificate((X509Certificate) caCert[0], caPrivateKey);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates an CRL client for the sample.
     *
     * <p>
     * NOTE: for the real signing you should use real revocation data clients
     * (such as {@link com.itextpdf.signatures.CrlClientOnline}).
     *
     * @return the CRL client to be used for the signing operation.
     */
    private static ICrlClient getCrlClient() {
        try {
            Certificate[] signCert = PemFileHelper.readFirstChain(SIGN);
            PrivateKey privateKey = PemFileHelper.readFirstKey(SIGN, PASSWORD);
            return new TestCrlClient().addBuilderForCertIssuer((X509Certificate) signCert[0], privateKey);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates properties to be used in signing operations.
     *
     * @return {@link SignerProperties} properties to be used for main signing operation.
     */
    private static SignerProperties createSignerProperties(String signatureFieldName) {
        SignerProperties signerProperties = new SignerProperties().setFieldName(signatureFieldName);
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(SignerProperties.IGNORED_ID)
                .setContent("Approval test signature.\nCreated by iText.");
        signerProperties
                .setPageNumber(1)
                .setPageRect(new Rectangle(50, 650, 200, 100))
                .setSignatureAppearance(appearance)
                .setReason("Reason")
                .setLocation("Location");
        return signerProperties;
    }

    /**
     * An IExternalSignature implementation that adds a previously created signature.
     */
    private static class SignatureProvider implements IExternalSignature {
        private final byte[] signature;
        private String digestAlgorithm;
        private String signatureAlgorithm;
        private ISignatureMechanismParams signatureMechanismParameters;

        public SignatureProvider(byte[] signature, String digestAlgorithm, String signatureAlgorithm,
                ISignatureMechanismParams signatureMechanismParameters) {
            this.signature = Arrays.copyOf(signature, signature.length);
            this.digestAlgorithm = digestAlgorithm;
            this.signatureAlgorithm = signatureAlgorithm;
            this.signatureMechanismParameters = signatureMechanismParameters;
        }

        @Override
        public String getDigestAlgorithmName() {
            return digestAlgorithm;
        }

        @Override
        public String getSignatureAlgorithmName() {
            return signatureAlgorithm;
        }

        @Override
        public ISignatureMechanismParams getSignatureMechanismParameters() {
            return signatureMechanismParameters;
        }

        @Override
        public byte[] sign(byte[] message) throws GeneralSecurityException {
            return signature;
        }
    }
}
