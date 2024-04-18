package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.itextpdf.styledxmlparser.css.util.CssDimensionParsingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfHtmlResponsiveDesign {
    public static final String SRC = "./src/main/resources/pdfhtml/ResponsiveDesign/responsive/";
    public static final String DEST = "./target/sandbox/pdfhtml/<filename>";

    public static final PageSize[] pageSizes = {
            PageSize.A4.rotate(),
            new PageSize(720, PageSize.A4.getHeight()),
            new PageSize(PageSize.A5.getWidth(), PageSize.A4.getHeight())
    };

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        String htmlSource = SRC + "responsive.html";
        PdfHtmlResponsiveDesign runner = new PdfHtmlResponsiveDesign();

        // Create a pdf for each page size
        for (int i = 0; i < pageSizes.length; i++) {
            float width = CssDimensionParsingUtils.parseAbsoluteLength(Float.toString(pageSizes[i].getWidth()));
            String dest = DEST.replace("<filename>", "responsive_" + width + ".pdf");

            runner.manipulatePdf(htmlSource, dest, SRC, pageSizes[i], width);
        }
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc,
            PageSize pageSize, float screenWidth) throws IOException {
        PdfWriter writer = new PdfWriter(pdfDest);
        PdfDocument pdfDoc = new PdfDocument(writer);

        // Set the result to be tagged
        pdfDoc.setTagged();
        pdfDoc.setDefaultPageSize(pageSize);

        ConverterProperties converterProperties = new ConverterProperties();

        // Set media device description details
        MediaDeviceDescription mediaDescription = new MediaDeviceDescription(MediaType.SCREEN);
        mediaDescription.setWidth(screenWidth);
        converterProperties.setMediaDeviceDescription(mediaDescription);

        FontProvider fp = new DefaultFontProvider();

        // Register external font directory
        fp.addDirectory(resourceLoc);

        converterProperties.setFontProvider(fp);
        // Base URI is required to resolve the path to source files
        converterProperties.setBaseUri(resourceLoc);

        // Create acroforms from text and button input fields
        converterProperties.setCreateAcroForm(true);

        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDoc, converterProperties);

        pdfDoc.close();
    }
}
