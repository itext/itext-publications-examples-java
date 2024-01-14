/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.signatures.pades;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.samples.sandbox.signatures.clients.TestCrlClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestOcspClient;
import com.itextpdf.samples.sandbox.signatures.clients.TestTsaClient;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

/**
 * Basic example of document signing with PaDES Baseline-LT Profile.
 */
public class PadesSignatureLTLevelExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/signatures/pades/padesSignatureLevelLTTest.pdf";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String ROOT = "./src/main/resources/cert/root.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final String TSA = "./src/main/resources/cert/tsaCert.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new PadesSignatureLTLevelExample().signSignatureWithBaselineLTProfile(SRC, DEST);
    }

    /**
     * Basic example of document signing with PaDES Baseline-LT Profile.
     *
     * @param src  source file
     * @param dest destination file
     *
     * @throws Exception in case some exception occurred.
     */
    public void signSignatureWithBaselineLTProfile(String src, String dest)
            throws Exception {
        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(src)),
                FileUtil.getFileOutputStream(dest));
        SignerProperties signerProperties = createSignerProperties();

        padesSigner.setTrustedCertificates(getTrustedStore());

        // Set revocation info clients to be used for LTV Verification.
        padesSigner.setOcspClient(getOcspClient()).setCrlClient(getCrlClient());

        padesSigner.signWithBaselineLTProfile(signerProperties, getCertificateChain(), getPrivateKey(),
                getTsaClient());
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    protected Certificate[] getCertificateChain() {
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
    protected List<Certificate> getTrustedStore() {
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
    protected PrivateKey getPrivateKey() {
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
    protected ICrlClient getCrlClient() {
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
    protected SignerProperties createSignerProperties() {
        SignerProperties signerProperties = new SignerProperties().setFieldName("Signature1");
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
