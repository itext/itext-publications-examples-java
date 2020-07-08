package com.itextpdf.samples.sandbox.typography.latin;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.Property;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.typography.config.LatinScriptConfig;
import com.itextpdf.typography.config.TypographyConfigurator;

import java.io.File;
import java.io.IOException;

public class LatinKerning {

    public static final String DEST = "./target/sandbox/typography/LatinKerning.pdf";
    public static final String FONTS_FOLDER = "./src/main/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LatinKerning().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "FoglihtenNo07.otf", PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document.setFont(font);

        // Add a new paragraph with the default kerning feature
        Paragraph p = new Paragraph("FoglihtenNo07.otf: Paragraph with the default (off) kerning feature in layout: " +
                "\nAtAVAWAwAYAvLTLWPA" +
                "\nWAWeYaWaWAWAWA7447// // 4");
        document.add(p);

        // Add a new paragraph with switched off kerning feature
        Paragraph pKernOff = new Paragraph("FoglihtenNo07.otf: Paragraph with switched OFF kerning feature in layout: " +
                "\nAtAVAWAwAYAvLTLWPA" +
                "\nWAWeYaWaWAWAWA7447// // 4");
        pKernOff.setProperty(Property.TYPOGRAPHY_CONFIG, new TypographyConfigurator()
                .addFeatureConfig(
                        new LatinScriptConfig()
                                .setKerningFeature(false)));
        document.add(pKernOff);

        // Add a new paragraph with switched on kerning feature
        Paragraph pKernOn = new Paragraph("FoglihtenNo07.otf: Paragraph with switched ON kerning feature in layout: " +
                "\nAtAVAWAwAYAvLTLWPA" +
                "\nWAWeYaWaWAWAWA7447// // 4");
        pKernOn.setProperty(Property.TYPOGRAPHY_CONFIG, new TypographyConfigurator()
                .addFeatureConfig(
                        new LatinScriptConfig()
                                .setKerningFeature(true)));
        document.add(pKernOn);

        // Add a new paragraph with switched on kerning and ligature features
        Paragraph pKernOnLigaOn = new Paragraph("FoglihtenNo07.otf: Paragraph with switched ON kerning and ON ligature feature in layout: " +
                "\nAtAVAWAwAYAvLTLWPA" +
                "\nWAWeYaWaWAWAWA7447// // 4");
        pKernOnLigaOn.setProperty(Property.TYPOGRAPHY_CONFIG, new TypographyConfigurator()
                .addFeatureConfig(
                        new LatinScriptConfig()
                                .setLigaturesApplying(true)
                                .setKerningFeature(true)));
        document.add(pKernOnLigaOn);

        document.close();
    }
}
