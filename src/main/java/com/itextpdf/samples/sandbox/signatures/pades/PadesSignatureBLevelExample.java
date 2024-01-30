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
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.PdfPadesSigner;
import com.itextpdf.signatures.SignerProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * Basic example of document signing with PaDES Baseline-B Profile.
 */
public class PadesSignatureBLevelExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/signatures/pades/padesSignatureLevelBTest.pdf";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new PadesSignatureBLevelExample().signSignatureWithBaselineBProfile(SRC, DEST);
    }

    /**
     * Basic example of document signing with PaDES Baseline-B Profile.
     *
     * @param src  source file
     * @param dest destination file
     *
     * @throws Exception in case some exception occurred.
     */
    public void signSignatureWithBaselineBProfile(String src, String dest)
            throws Exception {
        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(src)),
                FileUtil.getFileOutputStream(dest));
        // You could also set:
        // padesSigner.setTemporaryDirectoryPath(destinationFolder);
        // padesSigner.setExternalDigest()
        // padesSigner.setIssuingCertificateRetriever()
        // padesSigner.setTrustedCertificates()
        // padesSigner.setEstimatedSize()
        // padesSigner.setStampingProperties()

        SignerProperties signerProperties = createSignerProperties();

        // You could also pass IExternalSignature instance instead of privateKey.
        padesSigner.signWithBaselineBProfile(signerProperties, getCertificateChain(), getPrivateKey());
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
