package com.itextpdf.samples.sandbox.signatures.pqc;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.commons.bouncycastle.IBouncyCastleFactory;
import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfPKCS7;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.SignerProperties;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.Certificate;

/**
 * Example of document signing using specified post-quantum algorithms.
 *
 * <p>
 * See inherited classed for specific PQC algorithms samples. This class uses abstract methods as placeholders.
 */
public abstract class PqcSignatureExample {
    public static final String SRC = "./src/main/resources/pdfs/signExample.pdf";

    private static final IBouncyCastleFactory BOUNCY_CASTLE_FACTORY = BouncyCastleFactoryCreator.getFactory();
    private static final String SIGNATURE_FIELD = "Signature";
    private static final char[] KEY_PASSPHRASE = "testpassphrase".toCharArray();

    protected void manipulatePdf() throws Exception {
        String dest = getDestination();
        File file = new File(dest);
        file.getParentFile().mkdirs();
        doSign(dest);
        doVerify(dest, getSignatureAlgoOid());
    }

    /**
     * Performs PDF document signing using provided certificate {@link #getCertPath()},
     * signature algorithm name {@link #getSignatureAlgo()} and
     * digest algorithm name {@link #getDigestAlgo()}. Also {@link #getEstimatedSize()} should be specified.
     *
     * <p>
     * Note: for experimental (not standardised) algorithms BCPQC provider is required, see {@link #getProvider()}.
     *
     * @param outFile output PDF path
     *
     * @throws Exception in case of any error
     */
    private void doSign(String outFile) throws Exception {
        Certificate[] signChain = PemFileHelper.readFirstChain(getCertPath());
        PrivateKey signPrivateKey = PemFileHelper.readFirstKey(getCertPath(), KEY_PASSPHRASE, getProvider());
        IExternalSignature pks = new PrivateKeySignature(signPrivateKey, getDigestAlgo(), getSignatureAlgo(),
                getProvider().getName(), null);

        try (OutputStream out = FileUtil.getFileOutputStream(outFile)) {
            PdfSigner signer = new PdfSigner(new PdfReader(SRC), out, new StampingProperties());
            SignerProperties signerProperties = getSignerProperties();
            signer.setSignerProperties(signerProperties);

            signer.signDetached(new BouncyCastleDigest(), pks, signChain, null, null, null, getEstimatedSize(),
                    PdfSigner.CryptoStandard.CMS);
        }
    }

    private static void doVerify(String fileName, String expectedSigAlgoIdentifier) throws Exception {
        try (PdfReader r = new PdfReader(fileName); PdfDocument pdfDoc = new PdfDocument(r)) {
            SignatureUtil u = new SignatureUtil(pdfDoc);
            PdfPKCS7 data = u.readSignatureData(SIGNATURE_FIELD, BOUNCY_CASTLE_FACTORY.getProvider().getName());
            Assertions.assertTrue(data.verifySignatureIntegrityAndAuthenticity());
            if (expectedSigAlgoIdentifier != null) {
                Assertions.assertEquals(expectedSigAlgoIdentifier, data.getSignatureMechanismOid());
            }
        }
    }

    private static SignerProperties getSignerProperties() {
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(SignerProperties.IGNORED_ID)
                .setContent("Approval test signature.\nCreated by iText.");
        return new SignerProperties()
                .setFieldName(SIGNATURE_FIELD)
                .setPageRect(new Rectangle(50, 650, 200, 100))
                .setReason("Test")
                .setLocation("TestCity")
                .setSignatureAppearance(appearance);
    }

    public Provider getProvider() {
        return BOUNCY_CASTLE_FACTORY.getProvider();
    }

    public abstract String getDestination();

    public abstract String getSignatureAlgoOid();

    public abstract String getCertPath();

    public abstract String getSignatureAlgo();

    public abstract String getDigestAlgo();

    public abstract int getEstimatedSize();
}
