/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.signatures.clients;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.commons.bouncycastle.IBouncyCastleFactory;
import com.itextpdf.commons.bouncycastle.asn1.x500.IX500Name;
import com.itextpdf.commons.bouncycastle.asn1.x509.IExtension;
import com.itextpdf.commons.bouncycastle.cert.IX509CertificateHolder;
import com.itextpdf.commons.bouncycastle.cert.ocsp.*;
import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.operator.IContentSigner;
import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.kernel.pdf.PdfEncryption;
import com.itextpdf.signatures.IOcspClient;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
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

    private static class TestOcspResponseBuilder {
        private static final IBouncyCastleFactory FACTORY = BouncyCastleFactoryCreator.getFactory();

        private static final String SIGN_ALG = "SHA256withRSA";
        private static final Date TEST_DATE_TIME = new Date(950537642000L); // Feb 14, 2000 14:14:02 UTC

        private final IBasicOCSPRespBuilder responseBuilder;
        private final X509Certificate issuerCert;
        private final PrivateKey issuerPrivateKey;
        private final ICertificateStatus certificateStatus;
        private final Calendar thisUpdate = DateTimeUtil.getCalendar(TEST_DATE_TIME);
        private final Calendar nextUpdate = DateTimeUtil.getCalendar(TEST_DATE_TIME);

        public TestOcspResponseBuilder(X509Certificate issuerCert, PrivateKey issuerPrivateKey,
                                       ICertificateStatus certificateStatus) throws CertificateEncodingException {
            this.issuerCert = issuerCert;
            this.issuerPrivateKey = issuerPrivateKey;
            this.certificateStatus = certificateStatus;
            IX500Name subjectDN = FACTORY.createX500Name(issuerCert);
            DateTimeUtil.addDaysToCalendar(thisUpdate, -1);
            DateTimeUtil.addDaysToCalendar(nextUpdate, 30);
            responseBuilder = FACTORY.createBasicOCSPRespBuilder(FACTORY.createRespID(subjectDN));
        }

        public TestOcspResponseBuilder(X509Certificate issuerCert, PrivateKey issuerPrivateKey)
                throws CertificateEncodingException {
            this(issuerCert, issuerPrivateKey, FACTORY.createCertificateStatus().getGood());
        }

        public byte[] makeOcspResponse(byte[] requestBytes) throws IOException, CertificateException,
                AbstractOperatorCreationException, AbstractOCSPException {
            IBasicOCSPResp ocspResponse = makeOcspResponseObject(requestBytes);
            return ocspResponse.getEncoded();
        }

        public IBasicOCSPResp makeOcspResponseObject(byte[] requestBytes) throws CertificateException,
                AbstractOperatorCreationException, AbstractOCSPException, IOException {
            IOCSPReq ocspRequest = FACTORY.createOCSPReq(requestBytes);
            IReq[] requestList = ocspRequest.getRequestList();

            IExtension extNonce = ocspRequest.getExtension(FACTORY.createOCSPObjectIdentifiers().getIdPkixOcspNonce());
            if (!FACTORY.isNullExtension(extNonce)) {
                responseBuilder.setResponseExtensions(FACTORY.createExtensions(extNonce));
            }

            for (IReq req : requestList) {
                responseBuilder.addResponse(req.getCertID(), certificateStatus, thisUpdate.getTime(),
                        nextUpdate.getTime(), FACTORY.createNullExtensions());
            }

            IX509CertificateHolder[] chain = {FACTORY.createJcaX509CertificateHolder(issuerCert)};
            IContentSigner signer = FACTORY.createJcaContentSignerBuilder(SIGN_ALG).setProvider(FACTORY.getProviderName())
                    .build(issuerPrivateKey);
            return responseBuilder.build(signer, chain, TEST_DATE_TIME);
        }
    }
}
