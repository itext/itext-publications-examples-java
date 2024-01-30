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
import com.itextpdf.commons.bouncycastle.asn1.IASN1ObjectIdentifier;
import com.itextpdf.commons.bouncycastle.cms.ISignerInfoGenerator;
import com.itextpdf.commons.bouncycastle.operator.AbstractOperatorCreationException;
import com.itextpdf.commons.bouncycastle.operator.IContentSigner;
import com.itextpdf.commons.bouncycastle.operator.IDigestCalculator;
import com.itextpdf.commons.bouncycastle.operator.IDigestCalculatorProvider;
import com.itextpdf.commons.bouncycastle.tsp.*;
import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.commons.utils.SystemUtil;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.ITSAClient;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.*;

public class TestTsaClient implements ITSAClient {
    private static final IBouncyCastleFactory BOUNCY_CASTLE_FACTORY = BouncyCastleFactoryCreator.getFactory();

    private static final String DIGEST_ALG = "SHA256";
    private final PrivateKey tsaPrivateKey;
    private final List<Certificate> tsaCertificateChain;

    public TestTsaClient(List<Certificate> tsaCertificateChain, PrivateKey tsaPrivateKey) {
        this.tsaCertificateChain = tsaCertificateChain;
        this.tsaPrivateKey = tsaPrivateKey;
    }

    @Override
    public int getTokenSizeEstimate() {
        return 4096;
    }

    @Override
    public MessageDigest getMessageDigest() throws GeneralSecurityException {
        return new BouncyCastleDigest().getMessageDigest(DIGEST_ALG);
    }

    @Override
    public byte[] getTimeStampToken(byte[] imprint) throws Exception {
        ITimeStampRequestGenerator tsqGenerator = BOUNCY_CASTLE_FACTORY.createTimeStampRequestGenerator();
        tsqGenerator.setCertReq(true);
        BigInteger nonce = BigInteger.valueOf(SystemUtil.getTimeBasedSeed());
        ITimeStampRequest request = tsqGenerator.generate(BOUNCY_CASTLE_FACTORY.createASN1ObjectIdentifier(DigestAlgorithms.getAllowedDigest(DIGEST_ALG)), imprint, nonce);

        return new TestTimestampTokenBuilder(tsaCertificateChain, tsaPrivateKey).createTimeStampToken(request);
    }

    private static class TestTimestampTokenBuilder {
        private static final IBouncyCastleFactory FACTORY = BouncyCastleFactoryCreator.getFactory();

        private static final String SIGN_ALG = "SHA256withRSA";

        // just a more or less random oid of timestamp policy
        private static final String POLICY_OID = "1.3.6.1.4.1.45794.1.1";

        private final List<Certificate> tsaCertificateChain;
        private final PrivateKey tsaPrivateKey;

        public TestTimestampTokenBuilder(List<Certificate> tsaCertificateChain, PrivateKey tsaPrivateKey) {
            if (tsaCertificateChain.isEmpty()) {
                throw new IllegalArgumentException("tsaCertificateChain shall not be empty");
            }
            this.tsaCertificateChain = tsaCertificateChain;
            this.tsaPrivateKey = tsaPrivateKey;
        }

        public byte[] createTimeStampToken(ITimeStampRequest request)
                throws AbstractOperatorCreationException, AbstractTSPException, IOException, CertificateEncodingException {
            ITimeStampTokenGenerator tsTokGen = createTimeStampTokenGenerator(tsaPrivateKey,
                    tsaCertificateChain.get(0), SIGN_ALG, "SHA1", POLICY_OID);
            tsTokGen.setAccuracySeconds(1);

            tsTokGen.addCertificates(FACTORY.createJcaCertStore(tsaCertificateChain));

            // should be unique for every timestamp
            BigInteger serialNumber = new BigInteger(String.valueOf(SystemUtil.getTimeBasedSeed()));
            Date genTime = DateTimeUtil.getCurrentTimeDate();
            ITimeStampToken tsToken = tsTokGen.generate(request, serialNumber, genTime);
            return tsToken.getEncoded();
        }

        private static ITimeStampTokenGenerator createTimeStampTokenGenerator(PrivateKey pk, Certificate cert,
                                                                              String signatureAlgorithm,
                                                                              String allowedDigest, String policyOid)
                throws AbstractTSPException, AbstractOperatorCreationException, CertificateEncodingException {
            IContentSigner signer = FACTORY.createJcaContentSignerBuilder(signatureAlgorithm).build(pk);
            IDigestCalculatorProvider digestCalcProviderProvider = FACTORY.createJcaDigestCalculatorProviderBuilder()
                    .build();
            ISignerInfoGenerator siGen =
                    FACTORY.createJcaSignerInfoGeneratorBuilder(digestCalcProviderProvider)
                            .build(signer, (X509Certificate) cert);

            String digestForTsSigningCert = DigestAlgorithms.getAllowedDigest(allowedDigest);
            IDigestCalculator dgCalc = digestCalcProviderProvider.get(
                    FACTORY.createAlgorithmIdentifier(FACTORY.createASN1ObjectIdentifier(digestForTsSigningCert)));
            IASN1ObjectIdentifier policy = FACTORY.createASN1ObjectIdentifier(policyOid);
            return FACTORY.createTimeStampTokenGenerator(siGen, dgCalc, policy);
        }
    }
}
