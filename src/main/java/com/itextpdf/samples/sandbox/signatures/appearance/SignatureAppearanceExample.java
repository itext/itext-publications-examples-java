package com.itextpdf.samples.sandbox.signatures.appearance;

import com.itextpdf.commons.utils.DateTimeUtil;
import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.element.SignatureFieldAppearance;
import com.itextpdf.io.font.constants.StandardFontFamilies;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.crypto.DigestAlgorithms;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.*;
import com.itextpdf.samples.sandbox.signatures.utils.PemFileHelper;
import com.itextpdf.signatures.*;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class SignatureAppearanceExample {
    public static final String DEST = "./target/sandbox/signatures/appearance/signatureAppearanceExample.pdf";

    public static final String SRC = "./src/main/resources/pdfs/signExample.pdf";
    public static final String CERT_PATH = "./src/main/resources/cert/sign.pem";
    public static final String IMAGE_PATH = "./src/main/resources/img/itext.png";
    public static final String BACKGROUND_PATH = "./src/main/resources/img/sign.jpg";

    private static final char[] PASSWORD = "testpassphrase".toCharArray();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SignatureAppearanceExample().signDocument();
    }

    protected void signDocument() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        PrivateKey privateKey = PemFileHelper.readFirstKey(CERT_PATH, PASSWORD);
        Certificate[] chain = PemFileHelper.readFirstChain(CERT_PATH);

        PdfSigner signer = new PdfSigner(new PdfReader(SRC),
                FileUtil.getFileOutputStream(SignatureAppearanceExample.DEST),
                new StampingProperties());

        // Set up the appearance
        ImageData clientSignatureImage = ImageDataFactory.create(IMAGE_PATH);
        ImageData backgroundImage = ImageDataFactory.create(BACKGROUND_PATH);
        SignatureFieldAppearance appearance = new SignatureFieldAppearance(SignerProperties.IGNORED_ID)
                // Use any of the setContent overloads to specify the content. If new SignedAppearanceText() is used,
                // it will be filled in automatically during the signing, but could be also filled in manually using
                // setters, e.g. new SignedAppearanceText().setReasonLine("[Reason]: TestReason").setLocationLine(null)
                .setContent("Signature appearance description", clientSignatureImage)
                .setBackgroundImage(applyBackgroundImage(backgroundImage, -1))
                .setBorder(new SolidBorder(new DeviceRgb(1, 92, 135), 3))
                .setFontColor(new DeviceRgb(252, 173, 47))
                .setFontSize(30);
        // Use setFont in order to specify the font, e.g. appearance.setFont(PdfFontFactory.createFont());
        appearance.setFontFamily(StandardFontFamilies.HELVETICA)
                .setProperty(Property.FONT_PROVIDER, new BasicFontProvider());

        // Set signer properties
        SignerProperties signerProperties = new SignerProperties()
                .setFieldName("signature")
                .setCertificationLevel(AccessPermissions.NO_CHANGES_PERMITTED)
                .setClaimedSignDate(DateTimeUtil.getCurrentTimeCalendar())
                .setPageNumber(1)
                .setPageRect(new Rectangle(50, 450, 200, 200))
                .setContact("Contact")
                .setSignatureCreator("Creator")
                .setLocation("TestCity")
                .setReason("TestReason")
                .setSignatureAppearance(appearance);

        signer.setSignerProperties(signerProperties);

        IExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA256,
                BouncyCastleProvider.PROVIDER_NAME);
        signer.signDetached(new BouncyCastleDigest(), pks, chain, null, null, null, 0, PdfSigner.CryptoStandard.CMS);
    }

    private BackgroundImage applyBackgroundImage(ImageData image, float imageScale) {
        BackgroundRepeat repeat = new BackgroundRepeat(BackgroundRepeat.BackgroundRepeatValue.NO_REPEAT);
        BackgroundPosition position = new BackgroundPosition()
                .setPositionX(BackgroundPosition.PositionX.CENTER)
                .setPositionY(BackgroundPosition.PositionY.CENTER);
        BackgroundSize size = new BackgroundSize();
        final float EPS = 1e-5f;
        if (Math.abs(imageScale) < EPS) {
            size.setBackgroundSizeToValues(UnitValue.createPercentValue(100),
                    UnitValue.createPercentValue(100));
        } else {
            if (imageScale < 0) {
                size.setBackgroundSizeToContain();
            } else {
                size.setBackgroundSizeToValues(
                        UnitValue.createPointValue(imageScale * image.getWidth()),
                        UnitValue.createPointValue(imageScale * image.getHeight()));
            }
        }
        return new BackgroundImage.Builder()
                .setImage(new PdfImageXObject(image))
                .setBackgroundSize(size)
                .setBackgroundRepeat(repeat)
                .setBackgroundPosition(position)
                .build();
    }
}
