package com.itextpdf.samples.sandbox.signatures.appearance;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.AccessPermissions;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.SignerProperties;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SignatureReuseAppearanceExample {
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static final String DEST = "./target/sandbox/signatures/appearance/signatureReuseAppearanceExample.pdf";

    public static final String SRC = "./src/main/resources/pdfs/unsignedSignatureField.pdf";
    public static final String CERT_PATH = "./src/main/resources/cert/sign.pem";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SignatureReuseAppearanceExample().signDocument(DEST);
    }

    protected void signDocument(String dest) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PrivateKey privateKey = getPrivateKey();
        Certificate[] chain = getCertificateChain();

        PdfSigner signer = new PdfSigner(new PdfReader(SRC), FileUtil.getFileOutputStream(dest), new StampingProperties());

        //      Set signer properties
        SignerProperties signerProperties = new SignerProperties()
                .setCertificationLevel(AccessPermissions.UNSPECIFIED)
                .setFieldName("Signature1")
                .setReason("Test 1")
                .setLocation("TestCity")
                .setSignatureAppearance(new SignatureFieldAppearance(SignerProperties.IGNORED_ID)
                        .setContent("New appearance").setFontColor(ColorConstants.GREEN));

        signer.setSignerProperties(signerProperties);

        signer.getSignatureField().setReuseAppearance(true);

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        // This method closes the underlying pdf document, so the instance
        // of PdfSigner cannot be used after this method call
        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256,
                BouncyCastleProvider.PROVIDER_NAME);
        signer.signDetached(new BouncyCastleDigest(), pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    protected Certificate[] getCertificateChain() {
        try {
            return PemFileHelper.readFirstChain(CERT_PATH);
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
            return PemFileHelper.readFirstKey(CERT_PATH, PASSWORD);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }
}
