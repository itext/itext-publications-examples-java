package com.itextpdf.samples.sandbox.typography.latin;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

public class LatinSignature {

    public static final String DEST = "./target/sandbox/typography/LatinSignature.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";
    public static final String RESOURCE_FOLDER = "./src/main/resources/pdfs/";
    public static final String CERTIFICATE_FOLDER = "./src/main/resources/cert/";
    private static final char[] PASSWORD = "testpass".toCharArray();

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LatinSignature().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException, GeneralSecurityException {
        String line1 = "Text # 1";
        String line2 = "Text # 2";
        String line3 = "Text # 3 / ";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "FreeSans.ttf", PdfEncodings.IDENTITY_H);

        // Define certificate
        String certFileName = CERTIFICATE_FOLDER + "signCertRsa01.p12";

        // Prerequisite for signing
        Security.addProvider(new BouncyCastleProvider());
        PrivateKey signPrivateKey = readFirstKey(certFileName, PASSWORD, PASSWORD);
        IExternalSignature pks = new PrivateKeySignature(signPrivateKey, DigestAlgorithms.SHA256,
                BouncyCastleProvider.PROVIDER_NAME);
        Certificate[] signChain = readFirstChain(certFileName, PASSWORD);

        PdfSigner signer = new PdfSigner(new PdfReader(RESOURCE_FOLDER + "simpleDocument.pdf"),
                new FileOutputStream(dest), new StampingProperties());

        Rectangle rect = new Rectangle(30, 500, 500, 100);

        // Set the name indicating the field to be signed
        signer.setFieldName("Field1");

        // Get Signature Appearance and set some of its properties
        signer.getSignatureAppearance()
                .setPageRect(rect)
                .setReason(line1)
                .setLocation(line2)
                .setReasonCaption(line3)
                .setLayer2Font(font);

        // Sign the document
        signer.signDetached(new BouncyCastleDigest(), pks, signChain, null, null, null, 0,
                PdfSigner.CryptoStandard.CADES);
    }

    // Read pkcs12 file first private key
    private static Certificate[] readFirstChain(String p12FileName, char[] ksPass) throws KeyStoreException, IOException,
            CertificateException, NoSuchAlgorithmException {
        Certificate[] certChain = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(p12FileName), ksPass);

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

    // Read first public certificate chain
    private static PrivateKey readFirstKey(String p12FileName, char[] ksPass, char[] keyPass) throws KeyStoreException,
            IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        PrivateKey pk = null;

        KeyStore p12 = KeyStore.getInstance("pkcs12");
        p12.load(new FileInputStream(p12FileName), ksPass);

        Enumeration<String> aliases = p12.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (p12.isKeyEntry(alias)) {
                pk = (PrivateKey) p12.getKey(alias, keyPass);
                break;
            }
        }
        return pk;
    }
}
