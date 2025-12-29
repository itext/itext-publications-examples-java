package com.itextpdf.samples.sandbox.signatures.pqc;

import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.crypto.OID;

/**
 * Example of document signing using ML-DSA-44 post-quantum algorithm.
 */
public class MLDSA extends PqcSignatureExample {
    public static final String DEST = "./target/sandbox/signatures/pqc/signMLDSA.pdf";

    private static final String SIGNATURE_ALGO = "ML-DSA-44";
    private static final String CERT_PATH = "./src/main/resources/cert/pqc/cert_" + SIGNATURE_ALGO + ".pem";
    private static final String DIGEST_ALGO = DigestAlgorithms.SHA3_256;

    public static void main(String[] args) throws Exception {
        new MLDSA().manipulatePdf();
    }

    @Override
    public String getDestination() {
        return DEST;
    }

    /**
     * This certificate was generated via openssl:
     * openssl req -x509 -newkey mldsa44 -keyout key_ML-DSA-44.pem -out cert_ML-DSA-44.pem
     * -subj "/CN=iText Test ML-DSA-44 Certificate" -days 365
     *
     * @return ML-DSA-44 certificate
     */
    @Override
    public String getCertPath() {
        return CERT_PATH;
    }

    /**
     * Possible ML-DSA algorithms values are ML-DSA-44, ML-DSA-65 and ML-DSA-87.
     *
     * @return ML-DSA-44 signature algorithm
     */
    @Override
    public String getSignatureAlgo() {
        return SIGNATURE_ALGO;
    }

    /**
     * Possible ML-DSA algorithms OID values are {@link OID#ML_DSA_44}, {@link OID#ML_DSA_65} and {@link OID#ML_DSA_87}.
     *
     * @return {@link OID#ML_DSA_44} OID value.
     */
    @Override
    public String getSignatureAlgoOid() {
        return OID.ML_DSA_44;
    }

    /**
     * ML-DSA-44 with DigestAlgorithms.SHA3_256,
     * ML-DSA-65 with DigestAlgorithms.SHA3_384,
     * ML-DSA-87 with DigestAlgorithms.SHA3_512.
     *
     * @return SHA3-256 digest algorithm
     */
    @Override
    public String getDigestAlgo() {
        return DIGEST_ALGO;
    }

    @Override
    public int getEstimatedSize() {
        return 10000;
    }
}
