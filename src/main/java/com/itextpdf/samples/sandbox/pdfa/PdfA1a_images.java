package com.itextpdf.samples.sandbox.pdfa;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfA1a_images {
    public static final float MARGIN_OF_ONE_CM = 28.8f;

    public static final String DEST = "./target/sandbox/pdfa/pdf_a1a_images.pdf";

    public static final String FONT = "./src/main/resources/font/OpenSans-Regular.ttf";

    public static final String LOGO = "./src/main/resources/img/hero.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfA1a_images().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);

        InputStream inputStream = new FileInputStream("./src/main/resources/data/sRGB_CS_profile.icm");
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(dest), PdfAConformanceLevel.PDF_A_1A,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", inputStream));
        pdfDoc.getCatalog().setLang(new PdfString("nl-nl"));

        pdfDoc.setTagged();

        Document doc = new Document(pdfDoc);
        doc.setMargins(MARGIN_OF_ONE_CM, MARGIN_OF_ONE_CM, MARGIN_OF_ONE_CM, MARGIN_OF_ONE_CM);

        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info
                .setTitle("title")
                .setAuthor("Author")
                .setSubject("Subject")
                .setCreator("Creator")
                .setKeywords("Metadata, iText, PDF")
                .setCreator("My program using iText")
                .addCreationDate();

        Paragraph element = new Paragraph("Hello World").setFont(font).setFontSize(10);
        doc.add(element);

        Image logoImage = new Image(ImageDataFactory.create(LOGO));
        logoImage.getAccessibilityProperties().setAlternateDescription("Logo");
        doc.add(logoImage);

        doc.close();
    }
}
