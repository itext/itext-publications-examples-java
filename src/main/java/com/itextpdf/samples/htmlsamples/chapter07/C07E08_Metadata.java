package com.itextpdf.samples.htmlsamples.chapter07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.licensekey.LicenseKey;

public class C07E08_Metadata {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch07/metadata.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/metadata.html";

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

        C07E08_Metadata app = new C07E08_Metadata();
        app.createPdf(SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src  the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String dest) throws IOException {
        PdfWriter writer = new PdfWriter(dest, new WriterProperties().addXmpMetadata());
        HtmlConverter.convertToPdf(new FileInputStream(src), writer);
    }

}
