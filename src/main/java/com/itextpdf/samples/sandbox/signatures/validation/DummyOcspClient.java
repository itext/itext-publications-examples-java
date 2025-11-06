package com.itextpdf.samples.sandbox.signatures.validation;

import com.itextpdf.commons.bouncycastle.asn1.IASN1Encodable;
import com.itextpdf.commons.bouncycastle.asn1.IASN1ObjectIdentifier;
import com.itextpdf.commons.bouncycastle.cert.IX509CertificateHolder;
import com.itextpdf.commons.bouncycastle.cert.ocsp.AbstractOCSPException;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IBasicOCSPResp;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IRespID;
import com.itextpdf.commons.bouncycastle.cert.ocsp.ISingleResp;
import com.itextpdf.commons.bouncycastle.operator.IContentVerifierProvider;
import com.itextpdf.signatures.OcspClientBouncyCastle;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class DummyOcspClient extends OcspClientBouncyCastle {

    public DummyOcspClient() {
        super();
    }

    @Override
    public IBasicOCSPResp getBasicOCSPResp(X509Certificate checkCert, X509Certificate rootCert, String url) {
        return new IBasicOCSPResp() {
            @Override
            public ISingleResp[] getResponses() {
                return new ISingleResp[0];
            }

            @Override
            public boolean isSignatureValid(IContentVerifierProvider provider) throws AbstractOCSPException {
                return false;
            }

            @Override
            public IX509CertificateHolder[] getCerts() {
                return new IX509CertificateHolder[0];
            }

            @Override
            public byte[] getEncoded() throws IOException {
                return new byte[0];
            }

            @Override
            public Date getProducedAt() {
                return null;
            }

            @Override
            public IASN1Encodable getExtensionParsedValue(IASN1ObjectIdentifier objectIdentifier) {
                return null;
            }

            @Override
            public IRespID getResponderId() {
                return null;
            }
        };
    }
}
