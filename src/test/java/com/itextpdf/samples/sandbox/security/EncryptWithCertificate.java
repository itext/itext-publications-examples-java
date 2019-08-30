/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
/**
 * This example was written by Bruno Lowagie.
 */
package com.itextpdf.samples.sandbox.security;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.test.ITextTest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * The file created using this example can not be opened, unless
 * you import the private key stored in test.p12 in your certificate store.
 * The password for the p12 file is kspass.
 *
 * <br/>
 * Due to import control restrictions by the governments of a few countries,
 * the encryption libraries shipped by default with the Java SDK restrict the
 * length, and as a result the strength, of encryption keys. Be aware that in
 * this sample by using {@link ITextTest#removeCryptographyRestrictions()} we
 * remove cryptography restrictions via reflection for testing purposes.
 * <br/>
 * For more conventional way of solving this problem you need to replace the
 * default security JARs in your Java installation with the Java Cryptography
 * Extension (JCE) Unlimited Strength Jurisdiction Policy Files. These JARs
 * are available for download from http://java.oracle.com/ in eligible countries.
 */
public class EncryptWithCertificate {
    public static final String DEST
            = "./target/sandbox/security/encrypt_with_certificate.pdf";
    public static final String SRC
            = "./src/test/resources/pdfs/hello_encrypted.pdf";
    public static final String PUBLIC
            = "./src/test/resources/encryption/test.cer";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new EncryptWithCertificate().manipulatePdf(DEST);
    }

    public Certificate getPublicCertificate(String path) throws IOException, CertificateException {
        FileInputStream is = new FileInputStream(path);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }

    protected void manipulatePdf(String dest) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Certificate cert = getPublicCertificate(PUBLIC);
        PdfWriter writer = new PdfWriter(DEST, new WriterProperties()
                .setPublicKeyEncryption(
                        new Certificate[]{cert},
                        new int[]{EncryptionConstants.ALLOW_PRINTING},
                        EncryptionConstants.ENCRYPTION_AES_256));

        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("My secret hello"));
        doc.close();
    }
}
