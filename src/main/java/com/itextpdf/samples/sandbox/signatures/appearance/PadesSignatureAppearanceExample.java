package com.itextpdf.samples.sandbox.signatures.appearance;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.fields.properties.SignedAppearanceText;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.PdfPadesSigner;
import com.itextpdf.signatures.SignerProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * Basic example of the signature appearance customizing during the document signing.
 */
public class PadesSignatureAppearanceExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DEST = "./target/sandbox/signatures/appearance/padesSignatureAppearanceExample.pdf";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static final String IMAGE_PATH = "./src/main/resources/img/sign.jpg";
    public static final String BOLD = "./src/main/resources/font/OpenSans-Bold.ttf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new PadesSignatureAppearanceExample().signSignature(SRC, DEST);
    }

    /**
     * Basic example of the signature appearance customizing during the document signing.
     *
     * @param src  source file
     * @param dest destination file
     *
     * @throws Exception in case some exception occurred.
     */
    public void signSignature(String src, String dest) throws Exception {
        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(src)),
                FileUtil.getFileOutputStream(dest));
        // We can pass the appearance through the signer properties.
        SignerProperties signerProperties = createSignerProperties();
        padesSigner.signWithBaselineBProfile(signerProperties, getCertificateChain(), getPrivateKey());
    }

    /**
     * Creates properties to be used in signing operations. Also creates the appearance that will be passed to the
     * PDF signer through the signer properties.
     *
     * @return {@link SignerProperties} properties to be used for main signing operation.
     *
     * @throws IOException in case an I/O error occurs when reading the file.
     */
    protected SignerProperties createSignerProperties() throws IOException {
        SignerProperties signerProperties = new SignerProperties().setFieldName("Signature1");

        // Create the appearance instance and set the signature content to be shown and different appearance properties.
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(signerProperties.getFieldName())
                .setContent(new SignedAppearanceText().setReasonLine("Customized reason: Reason")
                        .setLocationLine("Customized location: Location"), ImageDataFactory.create(IMAGE_PATH))
                .setBorder(new SolidBorder(ColorConstants.DARK_GRAY, 2))
                .setFont(PdfFontFactory.createFont(BOLD, PdfEncodings.IDENTITY_H));

        // Note that if SignedAppearanceText is set as a content, description text will be generated automatically, but
        // any `visual` values can be changed by using the appropriate setters. This won't affect the signature dictionary.

        // Set created signature appearance and other signer properties.
        signerProperties
                .setSignatureAppearance(appearance)
                .setPageNumber(1)
                .setPageRect(new Rectangle(50, 650, 200, 100))
                .setReason("Reason")
                .setLocation("Location");
        return signerProperties;
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    protected Certificate[] getCertificateChain() {
        try {
            return PemFileHelper.readFirstChain(CHAIN);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }

    /**
     * Creates private key for the sample. This key shouldn't be used for the real signing.
     *
     * @return {@link PrivateKey} instance to be used for the main signing operation.
     */
    protected PrivateKey getPrivateKey() {
        try {
            return PemFileHelper.readFirstKey(SIGN, PASSWORD);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }
}
