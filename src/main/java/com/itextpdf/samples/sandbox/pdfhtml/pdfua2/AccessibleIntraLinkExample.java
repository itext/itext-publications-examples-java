package com.itextpdf.samples.sandbox.pdfhtml.pdfua2;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.resolver.font.BasicFontProvider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AccessibleIntraLinkExample {

    public static final String DEST = "./target/sandbox/pdfua2/pdf_ua_links.pdf";
    public static final String SRC = "./src/main/resources/pdfhtml/pdfua2/";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    public static final String UA_XMP = "./src/main/resources/xml/pdf_ua_xmp.xmp";

    public static void main(String[] args) throws IOException, XMPException {
        String currentSrc = SRC + "simpleLinks.html";
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AccessibleIntraLinkExample().manipulatePdf(currentSrc, DEST, SRC);
    }

    public void manipulatePdf(String htmlSource, String pdfDest, String resourceLoc) throws IOException, XMPException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfDest, new WriterProperties().setPdfVersion(
                PdfVersion.PDF_2_0)));
        // Create pdf/ua-2 document in which content will be placed
        createSimplePdfUA2Document(pdfDocument);
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new BasicFontProvider(false, true, false);
        // Base URI is required to resolve the path to source files, setting font provider which provides only embeddable fonts
        converterProperties.setFontProvider(fontProvider).setBaseUri(resourceLoc);;
        HtmlConverter.convertToPdf(new FileInputStream(htmlSource), pdfDocument, converterProperties);
    }

    private static void createSimplePdfUA2Document(PdfDocument pdfDocument) throws IOException, XMPException {
        byte[] bytes = Files.readAllBytes(Paths.get(UA_XMP));
        XMPMeta xmpMeta = XMPMetaFactory.parse(new ByteArrayInputStream(bytes));
        pdfDocument.setXmpMetadata(xmpMeta);
        pdfDocument.setTagged();
        pdfDocument.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDocument.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDocument.getDocumentInfo();
        info.setTitle("PdfUA2 Title");
    }
}
