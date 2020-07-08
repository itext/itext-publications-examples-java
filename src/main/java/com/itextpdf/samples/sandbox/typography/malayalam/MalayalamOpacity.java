package com.itextpdf.samples.sandbox.typography.malayalam;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class MalayalamOpacity {

    public static final String DEST = "./target/sandbox/typography/MalayalamOpacity.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MalayalamOpacity().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // സാേങ്കതിക പദസൂചികഒരു സ്വതന്ത്ര
        Text text = new Text("\u0D38\u0D3E\u0D47\u0D19\u0D4D\u0D15\u0D24\u0D3F\u0D15\u0020\u0D2A\u0D26\u0D38\u0D42\u0D1A" +
                "\u0D3F\u0D15\u0D12\u0D30\u0D41\u0020\u0D38\u0D4D\u0D35\u0D24\u0D28\u0D4D\u0D24\u0D4D\u0D30");

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansMalayalam-Regular.ttf",
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
