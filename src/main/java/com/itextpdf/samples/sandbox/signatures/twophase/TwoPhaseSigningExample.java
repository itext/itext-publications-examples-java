package com.itextpdf.samples.sandbox.signatures.twophase;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.commons.bouncycastle.IBouncyCastleFactory;
import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.pkcs.AbstractPKCSException;
import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.PdfSignature;
import com.itextpdf.signatures.PdfTwoPhaseSigner;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.SignerProperties;
import com.itextpdf.signatures.cms.AlgorithmIdentifier;
import com.itextpdf.signatures.cms.CMSContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Basic example of document two-phase signing.
 */
public class TwoPhaseSigningExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/signatures/twophase/2phaseSigned.pdf";
    public static final String PREPPED = "./target/sandbox/signatures/twophase/2phasePrepared.tmp";

    private static final IBouncyCastleFactory BC_FACTORY = BouncyCastleFactoryCreator.getFactory();

    /**
     * Signature algorithm names,
     * see <a href="https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#signature-algorithms">...</a>
     */
    private static final String SIGNATURE_ALGORITHM = "SHA512withRSA";
    // Digest oid
    private static final String SHA512 = "2.16.840.1.101.3.4.2.3";

    private static final String FIELD_NAME = "TEST_SIGNATURE";

    /**
     * In this example the certificate chain and private key are contained in one PEM file
     * this can be split in multiple ways.
     * <p>
     * Merging PEM files is easy, and can be done in a text editor if needed.
     */
    private static final String CHAIN = "./src/main/resources/cert/rsachain.pem";

    /**
     * The example PEM with key has no password.
     */
    private static final char[] PASSWORD = null;

    public static void main(String[] args)
            throws GeneralSecurityException, IOException, AbstractPKCSException, AbstractOperatorCreationException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        // Register BouncyCastle as a security provider.
        Security.addProvider(new BouncyCastleProvider());

        TwoPhaseSigningExample twoPhaseSigner = new TwoPhaseSigningExample();

        // Set the signature field name to a specific value, we need this to complete the signature later on.
        SignerProperties signerProperties = new SignerProperties().setFieldName(FIELD_NAME);

        // Prepare the document, add the field, create a CMS container and return the data to sign.
        byte[] dataToSign = twoPhaseSigner.prepareDocument(SRC, PREPPED, CHAIN, SHA512, signerProperties);

        // Sign the data to sign, in a two phase scenario this typically happens off device.
        byte[] signature = twoPhaseSigner.getSignatureForSignedAttributes(dataToSign, CHAIN, PASSWORD, SIGNATURE_ALGORITHM);

        // Add the signature to the document.
        twoPhaseSigner.completePreparedDocument(PREPPED, DEST, signerProperties.getFieldName(), signature);
    }

    /**
     * Prepares a pdf document to be signed externally.
     *
     * @param sourcePath         the path to the document to be signed
     * @param targetPath         the path to the prepared document
     * @param certificatePath    the path to a pem file containing the certificate chain
     * @param digestAlgorithmOid the Oid off the digest to use (a list of constants for this is added tyo this example)
     * @param signerProperties   the signer properties to use to prepare the document
     *
     * @return the data to be signed (not yet digested).
     *
     * @throws IOException              when one of the files can't be read.
     * @throws GeneralSecurityException when there is an issue with the certificates.
     */
    public byte[] prepareDocument(String sourcePath, String targetPath, String certificatePath,
                                  String digestAlgorithmOid, SignerProperties signerProperties)
            throws IOException, GeneralSecurityException {
        CMSContainer cmsContainer;
        try (PdfReader reader = new PdfReader(FileUtil.getInputStreamForFile(sourcePath));
             FileOutputStream outputStream = new FileOutputStream(targetPath + ".temp")) {

            X509Certificate[] certificateChain = loadCertificatesFromFile(certificatePath);

            String digestAlgorithm = DigestAlgorithms.getDigest(digestAlgorithmOid);

            // 1. Preparing the container to get a size estimate
            cmsContainer = new CMSContainer();
            cmsContainer.addCertificates(certificateChain);
            cmsContainer.getSignerInfo().setSigningCertificateAndAddToSignedAttributes(certificateChain[0],
                    digestAlgorithmOid);
            // In a later version the default algorithm is extracted from the certificate
            cmsContainer.getSignerInfo().setSignatureAlgorithm(getAlgorithmOidFromCertificate(certificateChain[0]));
            cmsContainer.getSignerInfo().setDigestAlgorithm(new AlgorithmIdentifier(digestAlgorithmOid));

            // Next to these required fields, we can add validation data and other signed or unsigned attributes with
            // the following methods:
            // cmsContainer.getSignerInfo().setCrlResponses();
            // cmsContainer.getSignerInfo().setOcspResponses();
            // cmsContainer.getSignerInfo().addSignedAttribute();
            // cmsContainer.getSignerInfo().addUnSignedAttribute();

            // Get the estimated size
            long estimatedSize = cmsContainer.getSizeEstimation();
            // Add enough space for the digest
            estimatedSize += MessageDigest.getInstance(digestAlgorithm).getDigestLength() * 2L + 2;
            // Duplicate the size for conversion to hex
            estimatedSize *= 2;

            PdfTwoPhaseSigner signer = new PdfTwoPhaseSigner(reader, outputStream);
            signer.setStampingProperties(new StampingProperties().useAppendMode());

            // 2. Prepare the document by adding the signature field and getting the digest in return
            byte[] documentDigest = signer.prepareDocumentForSignature(signerProperties, digestAlgorithm,
                    PdfName.Adobe_PPKLite, PdfName.Adbe_pkcs7_detached, (int) estimatedSize, false);

            // 3. Add the digest to the CMS container, because this will be part of the items to be signed
            cmsContainer.getSignerInfo().setMessageDigest(documentDigest);
        }
        // 4. This step is completely optional. Add the CMS container to the document
        // to avoid having to build it again, or storing it separately from the document
        try (PdfReader reader = new PdfReader(targetPath + ".temp");
             FileOutputStream outputStream = new FileOutputStream(targetPath)) {
            PdfTwoPhaseSigner.addSignatureToPreparedDocument(reader, signerProperties.getFieldName(), outputStream,
                    cmsContainer.serialize());
        }

        // 5. The serialized signed attributes is what actually needs to be signed
        // sometimes we have to create a digest from it, sometimes this needs to be sent as is.
        return cmsContainer.getSerializedSignedAttributes();
    }

    /**
     * This method simulates signing outside this application.
     *
     * @param signedAttributes     the data to be signed
     * @param pathToKeyFile        the path to the file containing the private key
     * @param keyPass              the password for the key file if any
     * @param signingAlgorithmName the signing algorithm name, see  <a href="https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#signature-algorithms">...</a>
     *
     * @return the signature of the data.
     *
     * @throws NoSuchAlgorithmException          when the signing algorithm is unknown.
     * @throws IOException                       when there is a problem opening the key file.
     * @throws InvalidKeyException               when the private key mismatches the algorithm.
     * @throws AbstractPKCSException             if any issues occur during decryption of the private key.
     * @throws SignatureException                if this Signature object is not initialized properly or if this
     *                                           signature algorithm is unable to process the input data provided.
     * @throws AbstractOperatorCreationException when the algorithm with which the key file is signed is not available.
     */
    public byte[] getSignatureForSignedAttributes(byte[] signedAttributes, String pathToKeyFile, char[] keyPass,
                                                  String signingAlgorithmName)
            throws NoSuchAlgorithmException, IOException,
            InvalidKeyException, AbstractPKCSException, SignatureException, AbstractOperatorCreationException {
        PrivateKey pk = getKeyFromPemFile(pathToKeyFile, keyPass);

        Signature signature = Signature.getInstance(signingAlgorithmName);
        signature.initSign(pk);
        signature.update(signedAttributes);
        return signature.sign();
    }

    /**
     * Adds an existing signature to a PDF where space was already reserved.
     *
     * @param preparedDocumentPath  the original prepared PDF
     * @param targetPath            the output PDF
     * @param fieldName             the name of the signature field to sign
     * @param signature             the signed bytes for the signed data
     *
     * @throws IOException              when there is some I/O problem.
     * @throws GeneralSecurityException when there is an issue with the certificates.
     */
    public void completePreparedDocument(String preparedDocumentPath, String targetPath, String fieldName,
                                         byte[] signature) throws IOException, GeneralSecurityException {
        try (PdfReader reader = new PdfReader(preparedDocumentPath);
             PdfDocument document = new PdfDocument(new PdfReader(preparedDocumentPath));
             FileOutputStream outputStream = new FileOutputStream(targetPath)) {
            // 1. Read the documents CMS container
            SignatureUtil su = new SignatureUtil(document);
            PdfSignature sig = su.getSignature(fieldName);
            PdfString encodedCMS = sig.getContents();
            byte[] encodedCMSdata = encodedCMS.getValueBytes();
            CMSContainer cmsContainer = new CMSContainer(encodedCMSdata);

            // 2. Add the signatureValue to the CMS
            cmsContainer.getSignerInfo().setSignature(signature);

            PdfTwoPhaseSigner.addSignatureToPreparedDocument(reader, fieldName, outputStream,
                    cmsContainer.serialize());
        }
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @param certificatePath path to the file with certificate chain
     *
     * @return the chain of certificates to be used for the signing operation.
     *
     * @throws IOException          when there is some I/O problem.
     * @throws CertificateException indicates certificate problems.
     */
    protected X509Certificate[] loadCertificatesFromFile(String certificatePath)
            throws IOException, CertificateException {
        return Arrays.asList(PemFileHelper.readFirstChain(certificatePath)).toArray(new X509Certificate[0]);
    }

    /**
     * Creates private key for the sample. This key shouldn't be used for the real signing.
     *
     * @param pathToKeyFile path to the file with private key
     * @param keyPass       key password
     *
     * @return {@link PrivateKey} instance to be used for the main signing operation.
     *
     * @throws IOException                          when there is some I/O problem.
     * @throws AbstractPKCSException                if any issues occur during decryption of the private key.
     * @throws AbstractOperatorCreationException    when the algorithm with which the key is signed isn't available.
     */
    protected PrivateKey getKeyFromPemFile(String pathToKeyFile, char[] keyPass)
            throws IOException, AbstractPKCSException, AbstractOperatorCreationException {
        return PemFileHelper.readFirstKey(pathToKeyFile, keyPass);
    }

    private AlgorithmIdentifier getAlgorithmOidFromCertificate(X509Certificate x509Certificate)
            throws CertificateEncodingException, IOException {
        JcaX509CertificateHolder jcaCert = new JcaX509CertificateHolder(x509Certificate);
        if (jcaCert.getSubjectPublicKeyInfo().getAlgorithm().getParameters() != null) {
            return new AlgorithmIdentifier(jcaCert.getSubjectPublicKeyInfo().getAlgorithm().getAlgorithm().getId(),
                    BC_FACTORY.createASN1Primitive(jcaCert.getSubjectPublicKeyInfo().getAlgorithm()
                            .getParameters().toASN1Primitive().getEncoded()));
        }
        return new AlgorithmIdentifier(jcaCert.getSubjectPublicKeyInfo().getAlgorithm().getAlgorithm().getId());
    }
}
