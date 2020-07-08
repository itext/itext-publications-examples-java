package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptWithCertificate {
    public static final String DEST
            = "./target/sandbox/security/encrypt_with_certificate.pdf";
    public static final String SRC
            = "./src/main/resources/pdfs/hello.pdf";
    public static final String PUBLIC
            = "./src/main/resources/encryption/test.cer";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new EncryptWithCertificate().manipulatePdf(DEST);
    }

    public Certificate getPublicCertificate(String path) throws IOException, CertificateException {
        try (FileInputStream is = new FileInputStream(path)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            return cert;
        }
    }

    protected void manipulatePdf(String dest) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // The file created by this example can not be opened, unless
        // you import the private key stored in test.p12 in your certificate store.
        // The password for the p12 file is kspass.
        Certificate cert = getPublicCertificate(PUBLIC);

        PdfDocument pdfDoc = new PdfDocument(
                new PdfReader(SRC),
                new PdfWriter(dest, new WriterProperties().setPublicKeyEncryption(
                        new Certificate[] {cert},
                        new int[] {EncryptionConstants.ALLOW_PRINTING},
                        // Due to import control restrictions by the governments of a few countries,
                        // the encryption libraries shipped by default with the Java SDK restrict
                        // the length, and as a result the strength, of encryption keys. Be aware
                        // that in this sample you need to replace the default security JARs in your
                        // Java installation with the Java Cryptography Extension (JCE) Unlimited
                        // Strength Jurisdiction Policy Files. These JARs are available for download
                        // from http://java.oracle.com/ in eligible countries.
                        EncryptionConstants.ENCRYPTION_AES_256))
        );
        pdfDoc.close();

    }
}
