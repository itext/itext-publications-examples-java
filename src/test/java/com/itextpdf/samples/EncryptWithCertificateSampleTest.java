/*
This file is part of the iText (R) project.
Copyright (c) 1998-2022 iText Group NV
Authors: iText Software.

For more information, please contact iText Software at this address:
sales@itextpdf.com
*/
package com.itextpdf.samples;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.samples.sandbox.security.EncryptWithCertificate;
import com.itextpdf.test.ITextTest;
import com.itextpdf.test.RunnerSearchConfig;
import com.itextpdf.test.WrappedSamplesRunner;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

@Category(SampleTest.class)
public class EncryptWithCertificateSampleTest extends WrappedSamplesRunner {

    public static final String PRIVATE = "./src/main/resources/encryption/test.p12";

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> data() {
        RunnerSearchConfig searchConfig = new RunnerSearchConfig();
        searchConfig.addClassToRunnerSearchPath("com.itextpdf.samples.sandbox.security.EncryptWithCertificate");

        return generateTestsList(searchConfig);
    }

    @Test(timeout = 60000)
    public void test() throws Exception {
        ITextTest.removeCryptographyRestrictions();
        LicenseKey.unloadLicenses();
        runSamples();
        ITextTest.restoreCryptographyRestrictions();
    }

    @Override
    protected void comparePdf(String outPath, String dest, String cmp) throws Exception {
        CompareTool compareTool = new CompareTool();
        PrivateKey privateKey = getPrivateKey();

        compareTool.getOutReaderProperties().setPublicKeySecurityParams(
                getPublicCertificate(EncryptWithCertificate.PUBLIC),
                privateKey, "BC", null);
        compareTool.getCmpReaderProperties().setPublicKeySecurityParams(
                getPublicCertificate(EncryptWithCertificate.PUBLIC),
                privateKey, "BC", null);
        compareTool.enableEncryptionCompare();

        addError(compareTool.compareByContent(dest, cmp, outPath, "diff_"));
        addError(compareTool.compareDocumentInfo(dest, cmp));
    }

    public Certificate getPublicCertificate(String path) throws IOException, CertificateException {
        try (FileInputStream is = new FileInputStream(path)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            return cert;
        }
    }

    private PrivateKey getPrivateKey() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        try (FileInputStream is = new FileInputStream(PRIVATE)) {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(is, "kspass".toCharArray());
            return (PrivateKey) keystore.getKey("sandbox", "kspass".toCharArray());
        }
    }
}
