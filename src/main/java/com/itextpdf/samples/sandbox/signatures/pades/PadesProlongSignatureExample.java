package com.itextpdf.samples.sandbox.signatures.pades;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestCrlClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestTsaClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.PdfPadesSigner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

/**
 * Basic example of document signature prolongation.
 */
public class PadesProlongSignatureExample {
    public static final String SRC = "./src/main/resources/pdfs/padesSignatureLTLevel.pdf";
    public static final String DEST = "./target/sandbox/signatures/pades/padesProlongSignature.pdf";

    private static final String ROOT = "./src/main/resources/cert/root.pem";
    private static final String TSA = "./src/main/resources/cert/tsaCert2.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new PadesProlongSignatureExample().prolongSignature(SRC, DEST);
    }

    /**
     * Basic example of document signature prolongation.
     *
     * @param src  source file
     * @param dest destination file
     *
     * @throws Exception in case some exception occurred.
     */
    public void prolongSignature(String src, String dest) throws Exception {
        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(src)),
                FileUtil.getFileOutputStream(dest));
        // Set clients in order to add revocation information for all the signatures in the provided document.
        padesSigner.setOcspClient(getOcspClient()).setCrlClient(getCrlClient());
        padesSigner.setTrustedCertificates(getTrustedStore());
        padesSigner.setTimestampSignatureName("TimestampSignature");
        padesSigner.prolongSignatures(getTsaClient());
    }

    /**
     * Creates timestamp client for the sample.
     *
     * <p>
     * NOTE: for the real signing you should use real revocation data clients
     * (such as {@link com.itextpdf.signatures.TSAClientBouncyCastle}).
     *
     * @return the TSA client to be used for the signing operation.
     */
    protected ITSAClient getTsaClient() {
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
    protected IOcspClient getOcspClient() {
        return new TestOcspClient();
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
    protected ICrlClient getCrlClient() {
        try {
            Certificate[] caCert = PemFileHelper.readFirstChain(ROOT);
            PrivateKey caPrivateKey = PemFileHelper.readFirstKey(ROOT, PASSWORD);
            return new TestCrlClient().addBuilderForCertIssuer((X509Certificate) caCert[0], caPrivateKey);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates trusted certificate store for the sample. This store shouldn't be used for the real signing.
     *
     * @return the trusted certificate store to be used for the missing CRL response issuer certificates retrieving
     * during the signing operation.
     */
    protected List<Certificate> getTrustedStore() {
        try {
            return Arrays.asList(PemFileHelper.readFirstChain(ROOT));
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }
}
