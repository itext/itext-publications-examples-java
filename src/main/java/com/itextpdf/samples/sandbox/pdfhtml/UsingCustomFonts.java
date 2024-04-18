package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UsingCustomFonts {
    public static final String SRC = "./src/main/resources/pdfhtml/FontExample/";
    public static final String DEST = "./target/sandbox/pdfhtml/FontExample.pdf";
    public static final String FONT_FOLDER = "./src/main/resources/pdfhtml/FontExample/font/";
    public static final String FONT1 = "./src/main/resources/font/New Walt Disney.ttf";
    public static final String FONT2 = "./src/main/resources/font/Greifswalder Tengwar.ttf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "FontExample.html";

        new UsingCustomFonts().manipulatePdf(htmlSource, DEST);
    }

    public void manipulatePdf(String htmlSource, String pdfDest) throws IOException {
        PdfWriter writer = new PdfWriter(pdfDest);
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Default provider will register standard fonts and free fonts shipped with iText, but not system fonts
        FontProvider provider = new DefaultFontProvider();

        // 1. Register all fonts in a directory
        provider.addDirectory(FONT_FOLDER);

        // 2. Register a single font by specifying path
        provider.addFont(FONT1);

        // 3. Use the raw bytes of the font file
        Path fontPathTwo = Paths.get(FONT2);
        byte[] fontBytes = Files.readAllBytes(fontPathTwo);
        provider.addFont(fontBytes);

        // Make sure the provider is used
        ConverterProperties converterProperties = new ConverterProperties()
                // Base URI is required to resolve the path to source files
                .setBaseUri(SRC)
                .setFontProvider(provider);

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDoc, converterProperties);

        pdfDoc.close();
    }
}
