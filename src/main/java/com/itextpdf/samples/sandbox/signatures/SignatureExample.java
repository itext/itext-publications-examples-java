package com.itextpdf.samples.sandbox.signatures;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.OCSPVerifier;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.CrlClientOnline;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.DigestAlgorithms;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class SignatureExample {
    public static final String DEST = "./target/sandbox/signatures/signExample.pdf";

    public static final String SRC = "./src/main/resources/pdfs/signExample.pdf";
    public static final String CERT_PATH = "./src/main/resources/cert/signCertRsa01.p12";
    public static final String IMAGE_PATH = "./src/main/resources/img/sign.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SignatureExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        ElectronicSignatureInfoDTO signatureInfo = new ElectronicSignatureInfoDTO();
        signatureInfo.setBottom(25);
        signatureInfo.setLeft(25);
        signatureInfo.setPageNumber(1);

        SignDocumentSignature(dest, signatureInfo);
    }

    protected void SignDocumentSignature(String filePath, ElectronicSignatureInfoDTO signatureInfo)
            throws Exception {
        PdfSigner pdfSigner = new PdfSigner(new PdfReader(SRC), new FileOutputStream(filePath),
                new StampingProperties());
        pdfSigner.setCertificationLevel(PdfSigner.CERTIFIED_NO_CHANGES_ALLOWED);

        // Set the name indicating the field to be signed.
        // The field can already be present in the document but shall not be signed
        pdfSigner.setFieldName("signature");

        ImageData clientSignatureImage = ImageDataFactory.create(IMAGE_PATH);

        // If you create new signature field (or use SetFieldName(System.String) with
        // the name that doesn't exist in the document or don't specify it at all) then
        // the signature is invisible by default.
        PdfSignatureAppearance signatureAppearance = pdfSigner.getSignatureAppearance();
        signatureAppearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
        signatureAppearance.setReason("");
        signatureAppearance.setLocationCaption("");
        signatureAppearance.setSignatureGraphic(clientSignatureImage);
        signatureAppearance.setPageNumber(signatureInfo.getPageNumber());
        signatureAppearance.setPageRect(new Rectangle(signatureInfo.getLeft(), signatureInfo.getBottom(),
                25, 25));

        char[] password = "testpass".toCharArray();
        IExternalSignature pks = getPrivateKeySignature(CERT_PATH, password);
        Certificate[] chain = getCertificateChain(CERT_PATH, password);
        OCSPVerifier ocspVerifier = new OCSPVerifier(null, null);
        OcspClientBouncyCastle ocspClient = new OcspClientBouncyCastle(ocspVerifier);
        List<ICrlClient> crlClients = Arrays.asList((ICrlClient) new CrlClientOnline());

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        // This method closes the underlying pdf document, so the instance
        // of PdfSigner cannot be used after this method call
        pdfSigner.signDetached(new BouncyCastleDigest(), pks, chain, crlClients, ocspClient, null,
                0, PdfSigner.CryptoStandard.CMS);
    }

    /**
     * Method reads pkcs12 file's first private key and returns {@link PrivateKeySignature} instance,
     * which uses SHA-512 hash algorithm.
     */
    private PrivateKeySignature getPrivateKeySignature(String certificatePath, char[] password) throws Exception {
        PrivateKey pk = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(certificatePath), password);

        Enumeration<String> aliases = p12.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (p12.isKeyEntry(alias)) {
                pk = (PrivateKey) p12.getKey(alias, password);
                break;
            }
        }

        Security.addProvider(new BouncyCastleProvider());
        return new PrivateKeySignature(pk, DigestAlgorithms.SHA512, BouncyCastleProvider.PROVIDER_NAME);
    }


    /**
     * Method reads first public certificate chain
     */
    private Certificate[] getCertificateChain(String certificatePath, char[] password) throws Exception {
        Certificate[] certChain = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(certificatePath), password);

        Enumeration<String> aliases = p12.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (p12.isKeyEntry(alias)) {
                certChain = p12.getCertificateChain(alias);
                break;
            }
        }
        return certChain;
    }

    protected class ElectronicSignatureInfoDTO {
        private int pageNumber;
        private float left;
        private float bottom;

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }
    }
}
