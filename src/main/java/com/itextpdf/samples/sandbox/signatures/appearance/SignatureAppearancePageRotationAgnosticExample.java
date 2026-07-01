package com.itextpdf.samples.sandbox.signatures.appearance;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.signatures.SignerProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

/*
 * SignatureAppearancePageRotationAgnosticExample.java
 *
 * Creates PDF signatures using custom appearance layers and rotated input page.
 * Content is placed as if page is not rotated and real rectangle is recalculated based on the rotation.
 */
public class SignatureAppearancePageRotationAgnosticExample {

    public static final String DEST = "./target/sandbox/signatures/appearance/signatureAppearancePageRotationAgnosticExample.pdf";
    public static final String ROTATED_DOC = "./target/sandbox/signatures/appearance/rotatedSource.pdf";
    public static final String CERT_PATH = "./src/main/resources/cert/sign.pem";

    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(ROTATED_DOC))) {
            pdfDocument.addNewPage().setRotation(90);
            try (Document document = new Document(pdfDocument)) {
                document.add(new Paragraph("Rotated 90 degrees"));
            }
        }

        new SignatureAppearancePageRotationAgnosticExample().signDocumentSignature(DEST);
    }

    /**
     * Signs the rotated document using custom appearance layers and
     * additional logic to recalculate field rectangle and rotation based on page rotation.
     *
     * @param filePath output path as {@link String}
     *
     * @throws Exception in case of any exceptions
     */
    protected void signDocumentSignature(String filePath) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PrivateKey privateKey = getPrivateKey();
        Certificate[] chain = getCertificateChain();

        PdfSigner signer = new PdfSigner(new PdfReader(ROTATED_DOC), FileUtil.getFileOutputStream(filePath), new StampingProperties());
        // Specify page number, at which signature is supposed to be drawn.
        int pageNumber = 1;
        PdfPage page = signer.getDocument().getPage(pageNumber);
        // We need to get page rotation to modify signature rectangle and set proper rotation.
        int pageRotation = page.getRotation();

        // Here you want to specify the rectangle as if page is not rotated. So coordinates begin in left bottom corner.
        Rectangle originalSignatureRectangle = new Rectangle(250, 100, 100, 50);

        // With following transformations signature rectangle will look the same agnostic to page rotation.
        Rectangle transformedRectangle = getTransformedRectangle(originalSignatureRectangle, page, pageRotation);

        SignerProperties signerProperties = new SignerProperties()
                .setFieldName("signature")
                .setPageRect(transformedRectangle)
                .setReason("Test 1")
                .setLocation("TestCity")
                .setPageNumber(pageNumber);
        signer.setSignerProperties(signerProperties);

        Rectangle rectangle = new Rectangle(0, 0, 100, 50);

        PdfFormXObject layer0 = new PdfFormXObject(rectangle);
        new PdfCanvas(layer0, signer.getDocument())
                .saveState()
                .setFillColor(ColorConstants.PINK)
                .rectangle(0, 0, 100, 50)
                .fillStroke()
                .restoreState();

        PdfFormXObject layer2 = new PdfFormXObject(rectangle);
        new PdfCanvas(layer2, signer.getDocument())
                .saveState()
                .beginText()
                .moveText(10, 30)
                .setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 8)
                .showText("Signature Appearance")
                .endText()
                .restoreState();

        signer.getSignatureField().setBackgroundLayer(layer0).setSignatureAppearanceLayer(layer2);
        signer.getSignatureField().getFirstFormAnnotation().setBorderWidth(5);
        // We need to set ignore page rotation to false first to draw signature as is on a page.
        signer.getSignatureField().setIgnorePageRotation(false);
        // Depending on page rotation, we need to rotate signature form field so that it always looks the same.
        // Field is rotated to the opposite direction, so we just need to set page rotation to field as is.
        signer.getSignatureField().getFirstFormAnnotation().setRotation(pageRotation);

        // Sign the document using the detached mode, CMS or CAdES equivalent.
        // This method closes the underlying pdf document, so the instance
        // of PdfSigner cannot be used after this method call
        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256,
                BouncyCastleProvider.PROVIDER_NAME);
        signer.signDetached(new BouncyCastleDigest(), pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
    }

    /**
     * Calculates transformed signature rectangle based on page rotation.
     *
     * @param originalSignatureRectangle original signature rectangle specified by a user
     * @param page {@link PdfPage} on which signature is supposed to be drawn
     * @param pageRotation rotation of the page
     *
     * @return transformed signature rectangle
     */
    protected Rectangle getTransformedRectangle(Rectangle originalSignatureRectangle, PdfPage page, int pageRotation) {
        switch (pageRotation) {
            case 90:
                return new Rectangle(
                        page.getPageSize().getWidth() - originalSignatureRectangle.getY() - originalSignatureRectangle.getHeight(),
                        originalSignatureRectangle.getX(),
                        originalSignatureRectangle.getHeight(),
                        originalSignatureRectangle.getWidth());
            case 180:
                return new Rectangle(
                        page.getPageSize().getWidth() - originalSignatureRectangle.getX() - originalSignatureRectangle.getWidth(),
                        page.getPageSize().getHeight() - originalSignatureRectangle.getY() - originalSignatureRectangle.getHeight(),
                        originalSignatureRectangle.getWidth(),
                        originalSignatureRectangle.getHeight());
            case 270:
                return new Rectangle(
                        originalSignatureRectangle.getY(),
                        page.getPageSize().getHeight() - originalSignatureRectangle.getX() - originalSignatureRectangle.getWidth(),
                        originalSignatureRectangle.getHeight(),
                        originalSignatureRectangle.getWidth());
            default:
                return originalSignatureRectangle;
        }
    }

    /**
     * Creates signing chain for the sample. This chain shouldn't be used for the real signing.
     *
     * @return the chain of certificates to be used for the signing operation.
     */
    protected Certificate[] getCertificateChain() {
        try {
            return PemFileHelper.readFirstChain(CERT_PATH);
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
            return PemFileHelper.readFirstKey(CERT_PATH, PASSWORD);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }
}
