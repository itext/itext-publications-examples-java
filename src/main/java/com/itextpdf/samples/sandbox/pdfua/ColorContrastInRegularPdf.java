package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.contrast.ColorContrastChecker;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.exceptions.PdfException;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.validation.ValidationContainer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

/*
 * This example shows how to enable color contrast validation
 * for a regular PdfDocument using ValidationContainer.
 */
public class ColorContrastInRegularPdf {

    public static final String DEST = "./target/sandbox/pdfua/color_contrast_in_regular_pdf.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ColorContrastInRegularPdf().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest))) {
            ValidationContainer validationContainer = new ValidationContainer();

            // Register color contrast checker.
            // The second parameter controls whether an exception is thrown on failure.
            // Set it to false to allow PDF creation even if contrast issues are found.
            ColorContrastChecker checker = new ColorContrastChecker(true, false);
            checker.setCheckWcagAA(true);
            checker.setCheckWcagAAA(false);

            validationContainer.addChecker(checker);
            pdfDocument.getDiContainer().register(ValidationContainer.class, validationContainer);

            try (Document document = new Document(pdfDocument)) {
                document.add(new Paragraph("This paragraph has sufficient contrast.")
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