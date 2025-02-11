package com.itextpdf.samples.sandbox.signatures.signaturetag;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.PdfPadesSigner;
import com.itextpdf.signatures.SignerProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class ConvertToPdfAndSign {

    public static final String SRC = "./src/main/resources/signatures/signaturetag/ConvertToPdfAndSign/htmlWithSignatureTag.html";
    public static final String DEST = "./target/sandbox/signatures/signaturetag/ConvertToPdfAndSign/signedPdfAfterConvert.pdf";

    private static final String PDF_TO_SIGN = "./target/sandbox/signatures/signaturetag/ConvertToPdfAndSign/htmlToSign.pdf";

    private static final String CHAIN = "./src/main/resources/cert/chain.pem";
    private static final String SIGN = "./src/main/resources/cert/sign.pem";
    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        Security.addProvider(new BouncyCastleProvider());

        new ConvertToPdfAndSign().convertHtmlToPdfAndSign(PDF_TO_SIGN, SRC);
    }

    private void convertHtmlToPdfAndSign(String dest, String src) throws IOException, GeneralSecurityException {
        ConverterProperties converterProperties = new ConverterProperties();
        DefaultTagWorkerFactory tagWorkerFactory = new SignatureTagWorkerFactory();
        converterProperties.setTagWorkerFactory(tagWorkerFactory);

        PdfWriter pdfWriter = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        HtmlConverter.convertToPdf(new FileInputStream(src), pdfDocument, converterProperties);

        PdfPadesSigner padesSigner = new PdfPadesSigner(new PdfReader(FileUtil.getInputStreamForFile(dest)),
                FileUtil.getFileOutputStream(DEST));
        // We can pass the appearance through the signer properties.
        SignerProperties signerProperties = createSignerProperties("signature_id");
        padesSigner.signWithBaselineBProfile(signerProperties, getCertificateChain(), getPrivateKey());
    }

    private static SignerProperties createSignerProperties(String signatureName) {
        SignerProperties signerProperties = new SignerProperties().setFieldName(signatureName);

        // Create the appearance instance and set the signature content to be shown and different appearance properties.
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(signatureName)
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

    private static Certificate[] getCertificateChain() {
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
    private static PrivateKey getPrivateKey() {
        try {
            return PemFileHelper.readFirstKey(SIGN, PASSWORD);
        } catch (Exception e) {
            throw new PdfException(e);
        }
    }
}
