package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.contrast.ColorContrastChecker;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfUAConformance;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.validation.IValidationChecker;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
 * CustomizeColorContrastChecks.java
 *
 * Example showing how to replace the default ColorContrastChecker
 * with a custom one that throws an exception on failure and checks
 * WCAG AA instead of AAA.
 */
public class CustomizeColorContrastChecks {

    public static final String DEST = "./target/sandbox/pdfua/pdf_ua_customize_color_contrast_checks.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        try {
            new CustomizeColorContrastChecks().manipulatePdf(DEST);
        } catch (PdfException e) {
            System.out.println("Color contrast validation failed:");
            System.out.println(e.getMessage());
        }
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfUAConfig config = new PdfUAConfig(PdfUAConformance.PDF_UA_2, "Custom color contrast checks", "en-US");

        try (PdfUADocument pdfDocument =
                new CustomPdfUADocument(new PdfWriter(dest,
                        new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)), config);
                Document document = new Document(pdfDocument)) {

            PdfFont font = PdfFontFactory.createFont(FONT);
            document.setFont(font);

            document.add(new Paragraph("This paragraph intentionally uses low contrast.")
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setBackgroundColor(ColorConstants.WHITE)
                    .setFontSize(12));
        }
    }

    private static class CustomPdfUADocument extends PdfUADocument {
        public CustomPdfUADocument(PdfWriter writer, PdfUAConfig config) {
            super(writer, config);
        }

        @Override
        protected List<IValidationChecker> createCheckers(PdfUAConformance uaConformance) {
            List<IValidationChecker> checkers = super.createCheckers(uaConformance);

            checkers.removeIf(checker -> checker instanceof ColorContrastChecker);

            // Register color contrast checker.
            // The second parameter controls whether an exception is thrown on failure.
            // Set it to false to allow PDF creation even if contrast issues are found.
            ColorContrastChecker contrastChecker = new ColorContrastChecker(true, false)
                    .setCheckWcagAA(true)
                    .setCheckWcagAAA(false);

            checkers.add(contrastChecker);
            return checkers;
        }
    }
}