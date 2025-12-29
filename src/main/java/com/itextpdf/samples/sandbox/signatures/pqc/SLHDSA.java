package com.itextpdf.samples.sandbox.signatures.pqc;

import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.crypto.OID;

/**
 * Example of document signing using SLH-DSA-SHA2-128F post-quantum algorithm.
 */
public class SLHDSA extends PqcSignatureExample {
    public static final String DEST = "./target/sandbox/signatures/pqc/signSLHDSA.pdf";

    private static final String SIGNATURE_ALGO = "slh-dsa-sha2-128f";
    private static final String CERT_PATH = "./src/main/resources/cert/pqc/cert_" + SIGNATURE_ALGO + ".pem";
    private static final String DIGEST_ALGO = DigestAlgorithms.SHA256;

    public static void main(String[] args) throws Exception {
        new SLHDSA().manipulatePdf();
    }

    @Override
    public String getDestination() {
        return DEST;
    }

    /**
     * This certificate was generated via openssl:
     * openssl req -x509 -newkey slh-dsa-sha2-128f -keyout key_slh-dsa-sha2-128f.pem -out cert_slh-dsa-sha2-128f.pem
     * -subj "/CN=iText Test slh-dsa-sha2-128f Certificate" -days 365
     *
     * @return SLH-DSA-SHA2-128F certificate
     */
    @Override
    public String getCertPath() {
        return CERT_PATH;
    }

    /**
     * Possible SLH-DSA algorithms values depending on the parameters are:
     * "slh-dsa-sha2-128s", "slh-dsa-sha2-128f", "slh-dsa-shake-128s", "slh-dsa-shake-128f",
     * "slh-dsa-sha2-192s", "slh-dsa-sha2-192f", "slh-dsa-shake-192s", "slh-dsa-shake-192f",
     * "slh-dsa-sha2-256s", "slh-dsa-sha2-256f", "slh-dsa-shake-256s", "slh-dsa-shake-256f".
     *
     * @return SLH-DSA-SHA2-128F signature algorithm
     */
    @Override
    public String getSignatureAlgo() {
        return SIGNATURE_ALGO;
    }

    /**
     * Possible SLH-DSA algorithms OID values are:
     * {@link OID#SLH_DSA_SHA2_128S}, {@link OID#SLH_DSA_SHA2_128F}, {@link OID#SLH_DSA_SHAKE_128S},
     * {@link OID#SLH_DSA_SHAKE_128F}, {@link OID#SLH_DSA_SHA2_192S}, {@link OID#SLH_DSA_SHA2_192F},
     * {@link OID#SLH_DSA_SHAKE_192S}, {@link OID#SLH_DSA_SHAKE_192F}, {@link OID#SLH_DSA_SHA2_256S},
     * {@link OID#SLH_DSA_SHA2_256F}, {@link OID#SLH_DSA_SHAKE_256S}, {@link OID#SLH_DSA_SHAKE_256F}.
     *
     * @return {@link OID#SLH_DSA_SHA2_128F} OID value.
     */
    @Override
    public String getSignatureAlgoOid() {
        return OID.SLH_DSA_SHA2_128F;
    }

    @Override
    public String getDigestAlgo() {
        return DIGEST_ALGO;
    }

    @Override
    public int getEstimatedSize() {
        return 50000;
    }
}
