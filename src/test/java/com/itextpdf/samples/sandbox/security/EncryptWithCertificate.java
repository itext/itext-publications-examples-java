/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

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
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Ignore;
import org.junit.experimental.categories.Category;

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
 */
// TODO DEVSIX-576
@Ignore
@Category(SampleTest.class)
public class EncryptWithCertificate extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/security/encrypt_with_certificate.pdf";
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

    @Override
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
