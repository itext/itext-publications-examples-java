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
import com.itextpdf.commons.bouncycastle.cert.IX509CRLHolder;
import com.itextpdf.commons.bouncycastle.cert.IX509v2CRLBuilder;
import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.operator.IContentSigner;
import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.signatures.ICrlClient;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestCrlClient implements ICrlClient {

    private final List<TestCrlBuilder> crlBuilders;

    public TestCrlClient() {
        crlBuilders = new ArrayList<>();
    }

    public TestCrlClient addBuilderForCertIssuer(X509Certificate issuerCert, PrivateKey issuerPrivateKey)
            throws CertificateEncodingException {
        Date yesterday = DateTimeUtil.addDaysToDate(TestCrlBuilder.TEST_DATE_TIME, -1);
        crlBuilders.add(new TestCrlBuilder(issuerCert, issuerPrivateKey, yesterday));
        return this;
    }

    @Override
    public Collection<byte[]> getEncoded(X509Certificate checkCert, String url) {
        return crlBuilders.stream()
                .map(testCrlBuilder -> {
                    try {
                        return testCrlBuilder.makeCrl();
                    } catch (Exception e) {
                        throw new PdfException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private static class TestCrlBuilder {
        static final Date TEST_DATE_TIME = new Date(950537642000L); // Feb 14, 2000 14:14:02 UTC

        private static final IBouncyCastleFactory FACTORY = BouncyCastleFactoryCreator.getFactory();

        private static final String SIGN_ALG = "SHA256withRSA";

        private final PrivateKey issuerPrivateKey;
        private final IX509v2CRLBuilder crlBuilder;
        private final Date nextUpdate = DateTimeUtil.addDaysToDate(TEST_DATE_TIME, 30);

        public TestCrlBuilder(X509Certificate issuerCert, PrivateKey issuerPrivateKey, Date thisUpdate)
                throws CertificateEncodingException {
            this.crlBuilder = FACTORY.createX509v2CRLBuilder(FACTORY.createX500Name(issuerCert), thisUpdate);
            this.issuerPrivateKey = issuerPrivateKey;
        }

        public byte[] makeCrl() throws IOException, AbstractOperatorCreationException {
            IContentSigner signer =
                    FACTORY.createJcaContentSignerBuilder(SIGN_ALG).setProvider(FACTORY.getProviderName())
                            .build(issuerPrivateKey);
            crlBuilder.setNextUpdate(nextUpdate);
            IX509CRLHolder crl = crlBuilder.build(signer);
            return crl.getEncoded();
        }
    }
}
