package com.itextpdf.samples.sandbox.signatures.appearance;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
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
 * Basic example of creating the signature field via signature appearance layout element.
 */
public class AddSignatureUsingAppearanceInstanceExample {
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";
    public static final String DOC_TO_SIGN = "./target/sandbox/signatures/appearance/signatureAddedUsingAppearance.pdf";
    public static final String DEST = "./target/sandbox/signatures/appearance/signSignatureAddedUsingAppearance.pdf";

    private static final String SIGNATURE_NAME = "Signature1";
    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new AddSignatureUsingAppearanceInstanceExample().createAndSignSignature(SRC, DOC_TO_SIGN, DEST, SIGNATURE_NAME);
    }

    /**
     * Basic example of creating the signature field via signature appearance layout element and signing it.
     *
     * @param src           source file
     * @param doc           destination for the file with added signature field
     * @param dest          final destination file
     * @param signatureName the name of the signature field to be added and signed
     *
     * @throws Exception in case some exception occurred.
     */
    public void createAndSignSignature(String src, String doc, String dest, String signatureName) throws Exception {
        addSignatureFieldAppearanceToTheDocument(src, doc, signatureName);

        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(doc)),
                FileUtil.getFileOutputStream(dest));
        // We can pass the appearance through the signer properties.
        SignerProperties signerProperties = createSignerProperties(signatureName);
        padesSigner.signWithBaselineBProfile(signerProperties, getCertificateChain(), getPrivateKey());
    }

    /**
     * Basic example of creating the signature field via signature appearance layout element.
     *
     * @param src           source file
     * @param dest          destination for the file with added signature field
     * @param signatureName the name of the signature field to be added
     *
     * @throws IOException in case an I/O error occurs.
     */
    protected void addSignatureFieldAppearanceToTheDocument(String src, String dest, String signatureName)
            throws IOException {
        try (Document document = new Document(new PdfDocument(new PdfReader(src), new PdfWriter(dest)))) {
            Table table = new Table(2);
            Cell cell = new Cell(0, 2).add(new Paragraph("Test signature").setFontColor(ColorConstants.WHITE));
            cell.setBackgroundColor(ColorConstants.GREEN);
            table.addCell(cell);
            cell = new Cell().add(new Paragraph("Signer"));
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            table.addCell(cell);

            // Add signature field to the table.
            cell = new Cell().add(
                    new SignatureFieldAppearance(signatureName)
                            .setContent("Sign here")
                            .setHeight(50)
                            .setWidth(100)
                            .setInteractive(true));
            table.addCell(cell);

            document.add(table);
        }
    }

    /**
     * Creates properties to be used in signing operations. Also creates the appearance that will be passed to the
     * PDF signer through the signer properties.
     *
     * @param signatureName the name of the signature field to be signed
     *
     * @return {@link SignerProperties} properties to be used for main signing operation.
     */
    protected SignerProperties createSignerProperties(String signatureName) {
        SignerProperties signerProperties = new SignerProperties().setFieldName(signatureName);

        // Create the appearance instance and set the signature content to be shown and different appearance properties.
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(SignerProperties.IGNORED_ID)
                .setContent("Signer", "Signature description. " +
                        "Signer is replaced to the one from the certificate.")
                .setBackgroundColor(ColorConstants.YELLOW);

        // Set created signature appearance and other signer properties.
        signerProperties
                .setSignatureAppearance(appearance)
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
