package com.itextpdf.samples.sandbox.signatures.pqc;

import com.itextpdf.kernel.crypto.DigestAlgorithms;

/**
 * Example of document signing using FN-DSA-512 post-quantum algorithm.
 */
public class FNDSA extends PqcSignatureExample {
    public static final String DEST = "./target/sandbox/signatures/pqc/signFNDSA.pdf";

    private static final String SIGNATURE_ALGO = "Falcon-512";
    private static final String CERT_PATH = "./src/main/resources/cert/pqc/cert_" + SIGNATURE_ALGO + ".pem";
    private static final String DIGEST_ALGO = DigestAlgorithms.SHAKE256;

    public static void main(String[] args) throws Exception {
        new FNDSA().manipulatePdf();
    }

    @Override
    public String getDestination() {
        return DEST;
    }

    /**
     * This certificate was generated using bouncycastle.
     *
     * @return FN-DSA-512 certificate
     */
    @Override
    public String getCertPath() {
        return CERT_PATH;
    }

    /**
     * Possible FN-DSA algorithms values are Falcon-512 and Falcon-1024 for now.
     *
     * @return Falcon-512 signature algorithm
     */
    @Override
    public String getSignatureAlgo() {
        return SIGNATURE_ALGO;
    }

    /**
     * This sample uses "1.3.9999.3.11" which is Falcon-512 OID, use "1.3.9999.3.14" for Falcon-1024.
     *
     * @return "1.3.9999.3.11" Falcon-512 signature algorithm OID
     */
    @Override
    public String getSignatureAlgoOid() {
        return "1.3.9999.3.11";
    }

    @Override
    public String getDigestAlgo() {
        return DIGEST_ALGO;
    }

    @Override
    public int getEstimatedSize() {
        return 5000;
    }
}
