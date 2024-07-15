package com.itextpdf.samples.sandbox.signatures.clients;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.commons.bouncycastle.IBouncyCastleFactory;
import com.itextpdf.commons.bouncycastle.asn1.x509.IExtension;
import com.itextpdf.commons.bouncycastle.cert.ocsp.ICertificateID;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IOCSPReqBuilder;
import com.itextpdf.kernel.pdf.PdfEncryption;
import com.itextpdf.samples.sandbox.signatures.utils.TestOcspResponseBuilder;
import com.itextpdf.signatures.IOcspClient;

import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestOcspClient implements IOcspClient {
    private static final IBouncyCastleFactory BOUNCY_CASTLE_FACTORY = BouncyCastleFactoryCreator.getFactory();

    private final Map<String, TestOcspResponseBuilder> certDNToResponseBuilder = new LinkedHashMap<>();

    public TestOcspClient addBuilderForCertificate(X509Certificate cert, PrivateKey privateKey)
            throws CertificateEncodingException {
        certDNToResponseBuilder.put(cert.getSubjectX500Principal().getName(), new TestOcspResponseBuilder(cert, privateKey));
        return this;
    }

    public TestOcspClient addBuilderForCertificate(X509Certificate cert, TestOcspResponseBuilder builder) {
        certDNToResponseBuilder.put(cert.getSubjectX500Principal().getName(), builder);
        return this;
    }

    @Override
    public byte[] getEncoded(X509Certificate checkCert, X509Certificate issuerCert, String url) {
        byte[] bytes = null;
        try {
            ICertificateID id = BOUNCY_CASTLE_FACTORY.createCertificateID(
                    BOUNCY_CASTLE_FACTORY.createJcaDigestCalculatorProviderBuilder().build().get(BOUNCY_CASTLE_FACTORY.createCertificateID().getHashSha1()),
                    BOUNCY_CASTLE_FACTORY.createJcaX509CertificateHolder(issuerCert), checkCert.getSerialNumber());
            TestOcspResponseBuilder builder = certDNToResponseBuilder.get(checkCert.getSubjectX500Principal().getName());
            if (builder == null) {
                return null;
            }
            IOCSPReqBuilder gen = BOUNCY_CASTLE_FACTORY.createOCSPReqBuilder();
            gen.addRequest(id);

            IExtension ext = BOUNCY_CASTLE_FACTORY.createExtension(
                    BOUNCY_CASTLE_FACTORY.createOCSPObjectIdentifiers().getIdPkixOcspNonce(), false,
                    BOUNCY_CASTLE_FACTORY.createDEROctetString(
                            BOUNCY_CASTLE_FACTORY.createDEROctetString(PdfEncryption.generateNewDocumentId()).getEncoded()));
            gen.setRequestExtensions(BOUNCY_CASTLE_FACTORY.createExtensions(ext));
            bytes = builder.makeOcspResponse(gen.build().getEncoded());
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
        }

        return bytes;
    }
}
