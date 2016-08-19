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
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertNull;

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
@Category(SampleTest.class)
public class EncryptWithCertificate extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/security/encrypt_with_certificate.pdf";
    public static final String SRC
            = "./src/test/resources/pdfs/hello_encrypted.pdf";
    public static final String PUBLIC
            = "./src/test/resources/encryption/test.cer";
    public static final String PRIVATE
            = "./src/test/resources/encryption/test.p12";

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

    @Override
    protected void beforeManipulatePdf() {
        super.beforeManipulatePdf();
        ITextTest.removeCryptographyRestrictions();
    }

    @Override
    protected void afterManipulatePdf() {
        super.afterManipulatePdf();
        ITextTest.restoreCryptographyRestrictions();
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        if (cmp == null || cmp.length() == 0) return;
        CompareTool compareTool = new CompareTool();
        PrivateKey privateKey = getPrivateKey();
        compareTool.getOutReaderProperties().setPublicKeySecurityParams(getPublicCertificate(PUBLIC), privateKey, "BC", null);
        compareTool.getCmpReaderProperties().setPublicKeySecurityParams(getPublicCertificate(PUBLIC), privateKey, "BC", null);
        compareTool.enableEncryptionCompare();
        String outPath = new File(dest).getParent();
        new File(outPath).mkdirs();
        if (compareXml) {
            if (!compareTool.compareXmls(dest, cmp)) {
                addError("The XML structures are different.");
            }
        } else {
            if (compareRenders) {
                addError(compareTool.compareVisually(dest, cmp, outPath, differenceImagePrefix));
                addError(compareTool.compareLinkAnnotations(dest, cmp));
            } else {
                addError(compareTool.compareByContent(dest, cmp, outPath, differenceImagePrefix));
            }
            addError(compareTool.compareDocumentInfo(dest, cmp));
        }

        assertNull(errorMessage);
    }

    private PrivateKey getPrivateKey() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(PRIVATE), "kspass".toCharArray());
        return (PrivateKey) keystore.getKey("sandbox", "kspass".toCharArray());
    }
}
