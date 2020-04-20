package com.itextpdf.samples.sandbox.layout;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Example demonstrates how to add paragraphs using floating and fixed layouts
 */
public class DifferentLayouts {

    public static final String DEST = "./target/sandbox/layout/differentLayouts.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new DifferentLayouts().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Add a flowing paragraph
        doc.add(new Paragraph("Flowing paragraph"));

        // Add a fixed paragraph
        Paragraph p = new Paragraph("Fixed paragraph")
                .setHeight(200)
                .setWidth(200)
                .setBackgroundColor(ColorConstants.GREEN);
        doc.showTextAligned(p, 100, 100, TextAlignment.LEFT);

        doc.close();
    }

}
