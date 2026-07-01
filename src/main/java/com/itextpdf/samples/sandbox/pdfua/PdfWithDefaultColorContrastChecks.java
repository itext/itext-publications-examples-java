package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.contrast.ColorContrastChecker;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.validation.ValidationContainer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

/*
 * PdfWithDefaultColorContrastChecks.java
 *
 * Example showing default color contrast checks in a regular PdfDocument.
 */
public class PdfWithDefaultColorContrastChecks {

    public static final String DEST = "./target/sandbox/pdfua/pdf_default_color_contrast_checks.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfWithDefaultColorContrastChecks().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest))) {

            ValidationContainer container = new ValidationContainer();
            // Register color contrast checker.
            // The second parameter controls whether an exception is thrown on failure.
            // Set it to false to allow PDF creation even if contrast issues are found.
            container.addChecker(new ColorContrastChecker(true, false));
            pdfDocument.getDiContainer().register(ValidationContainer.class, container);

            try (Document document = new Document(pdfDocument)) {
                PdfFont font = PdfFontFactory.createFont(FONT);
                document.setFont(font);

                document.add(new Paragraph("This paragraph uses a clearly readable contrast.")
                        .setFontColor(ColorConstants.BLACK)
                        .setBackgroundColor(ColorConstants.WHITE)
                        .setFontSize(12));

                document.add(new Paragraph("This paragraph intentionally uses low contrast.")
                        .setFontColor(ColorConstants.LIGHT_GRAY)
                        .setBackgroundColor(ColorConstants.WHITE)
                        .setFontSize(12));
            }

        } catch (PdfException e) {
            System.out.println("Color contrast validation failed:");
            System.out.println(e.getMessage());
        }
    }
}