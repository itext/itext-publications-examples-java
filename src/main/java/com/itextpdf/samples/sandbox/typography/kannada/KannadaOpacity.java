package com.itextpdf.samples.sandbox.typography.kannada;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.licensing.base.LicenseKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class KannadaOpacity {

    public static final String DEST = "./target/sandbox/typography/KannadaOpacity.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        try (FileInputStream license = new FileInputStream(System.getenv("ITEXT7_LICENSEKEY")
                + "/itextkey-typography.json")) {
            LicenseKey.loadLicenseFile(license);
        }

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KannadaOpacity().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // ಅಶೋಕನ ಬ್ರಾಹ್ಮೀ ಲಿಪಿ ಉತ್ತರಕ್ಕೆ
        Text text = new Text("\u0C85\u0CB6\u0CCB\u0C95\u0CA8\u0020\u0CAC\u0CCD\u0CB0\u0CBE\u0CB9\u0CCD\u0CAE\u0CC0\u0020" +
                "\u0CB2\u0CBF\u0CAA\u0CBF\u0020\u0C89\u0CA4\u0CCD\u0CA4\u0CB0\u0C95\u0CCD\u0C95\u0CC6");

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansKannada-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        // Wrap the text with paragraphs of different opacity and add them to the document: at first with 0.1, then with
        // 0.5 and then with the default opacity
        document.add(new Paragraph(text).setOpacity(0.1f));
        document.add(new Paragraph(text).setOpacity(0.5f));
        document.add(new Paragraph(text));

        document.close();
    }
}
