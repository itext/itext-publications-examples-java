package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.licensekey.LicenseKey;

public class C07E13_Peace {

    /**
     * The path to a folder containing extra fonts.
     */
    public static final String FONTS = "./src/main/resources/htmlsamples/fonts/noto/";

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/peace.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/peace.html";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C07E13_Peace app = new C07E13_Peace();
        app.createPdf(SRC, FONTS, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src   the path to the source HTML file
     * @param fonts the path to a folder containing a series of fonts
     * @param dest  the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String fonts, String dest) throws IOException {
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(PageSize.A4.rotate());
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        fontProvider.addDirectory(fonts);
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new FileInputStream(src), pdf, properties);
    }
}
