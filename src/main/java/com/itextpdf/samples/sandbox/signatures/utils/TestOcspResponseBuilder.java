package com.itextpdf.samples.sandbox.signatures.utils;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.commons.bouncycastle.IBouncyCastleFactory;
import com.itextpdf.commons.bouncycastle.asn1.x500.IX500Name;
import com.itextpdf.commons.bouncycastle.asn1.x509.IExtension;
import com.itextpdf.commons.bouncycastle.cert.IX509CertificateHolder;
import com.itextpdf.commons.bouncycastle.cert.ocsp.AbstractOCSPException;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IBasicOCSPResp;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IBasicOCSPRespBuilder;
import com.itextpdf.commons.bouncycastle.cert.ocsp.ICertificateStatus;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IOCSPReq;
import com.itextpdf.commons.bouncycastle.cert.ocsp.IReq;
import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.operator.IContentSigner;
import com.itextpdf.commons.utils.DateTimeUtil;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

public class TestOcspResponseBuilder {
    private static final IBouncyCastleFactory FACTORY = BouncyCastleFactoryCreator.getFactory();

    private static final String SIGN_ALG = "SHA256withRSA";
    private static final Date TEST_DATE_TIME = new Date(950537642000L); // Feb 14, 2000 14:14:02 UTC

    private final IBasicOCSPRespBuilder responseBuilder;
    private final X509Certificate issuerCert;
    private final PrivateKey issuerPrivateKey;
    private final ICertificateStatus certificateStatus;
    private Calendar thisUpdate = DateTimeUtil.getCalendar(TEST_DATE_TIME);
    private Calendar nextUpdate = DateTimeUtil.getCalendar(TEST_DATE_TIME);
    private Date producedAt = TEST_DATE_TIME;

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

    public void setThisUpdate(Calendar thisUpdate) {
        this.thisUpdate = thisUpdate;
    }

    public void setProducedAt(Date producedAt) {
        this.producedAt = producedAt;
    }

    public void setNextUpdate(Calendar nextUpdate) {
        this.nextUpdate = nextUpdate;
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
        return responseBuilder.build(signer, chain, producedAt);
    }
}
