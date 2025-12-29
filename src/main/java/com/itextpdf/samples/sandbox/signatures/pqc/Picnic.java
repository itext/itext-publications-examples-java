package com.itextpdf.samples.sandbox.signatures.pqc;

import com.itextpdf.bouncycastleconnector.BouncyCastleFactoryCreator;
import com.itextpdf.kernel.crypto.DigestAlgorithms;

import java.security.Provider;

/**
 * Example of document signing using Picnic3-L1 post-quantum algorithm.
 */
public class Picnic extends PqcSignatureExample {
    public static final String DEST = "./target/sandbox/signatures/pqc/signPicnic.pdf";

    private static final String SIGNATURE_ALGO = "Picnic3-L1";
    private static final String CERT_PATH = "./src/main/resources/cert/pqc/cert_" + SIGNATURE_ALGO + ".pem";
    private static final String DIGEST_ALGO = DigestAlgorithms.SHAKE256;

    public static void main(String[] args) throws Exception {
        new Picnic().manipulatePdf();
    }

    @Override
    public String getDestination() {
        return DEST;
    }

    /**
     * This certificate was generated using bouncycastle.
     *
     * @return Picnic3-L1 certificate
     */
    @Override
    public String getCertPath() {
        return CERT_PATH;
    }

    /**
     * For Picnic possible parameters are:
     * "Picnic3-L1", "Picnic3-L3", "Picnic3-L5",
     * "Picnic-L1-FS", "Picnic-L1-Full", "Picnic-L1-UR",
     * "Picnic-L3-FS", "Picnic-L3-Full", "Picnic-L3-UR",
     * "Picnic-L5-FS", "Picnic-L5-Full", "Picnic-L5-UR".
     * These can be used for certificate generation. For all of them signature algorithm will be "Picnic".
     *
     * @return "Picnic" signature algorithm.
     */
    @Override
    public String getSignatureAlgo() {
        return "Picnic";
    }

    @Override
    public String getSignatureAlgoOid() {
        return "1.3.6.1.4.1.22554.2.6.2.2";
    }

    @Override
    public String getDigestAlgo() {
        return DIGEST_ALGO;
    }

    @Override
    public int getEstimatedSize() {
        return 30000;
    }

    @Override
    public Provider getProvider() {
        return BouncyCastleFactoryCreator.getFactory().getPqcProvider();
    }
}
