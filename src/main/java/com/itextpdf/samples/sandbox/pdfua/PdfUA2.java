package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PdfUA2 {

    public static final String DEST = "./target/sandbox/pdfua2/pdf_ua.pdf";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static final String UA_XMP = "./src/main/resources/xml/pdf_ua_xmp.xmp";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfUA2().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) {
        try (PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)))){
            Document document = new Document(pdfDocument);
            byte[] bytes = Files.readAllBytes(Paths.get(UA_XMP));
            XMPMeta xmpMeta = XMPMetaFactory.parse(new ByteArrayInputStream(bytes));
            pdfDocument.setXmpMetadata(xmpMeta);
            //TAGGED PDF
            //Make document tagged
            pdfDocument.setTagged();
            pdfDocument.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
            pdfDocument.getCatalog().setLang(new PdfString("en-US"));
            PdfDocumentInfo info = pdfDocument.getDocumentInfo();
            info.setTitle("PdfUA2 Title");
            //PDF/UA
            //Embed font
            PdfFont font = PdfFontFactory.createFont(FONT, "WinAnsi", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

            Paragraph paragraph = new Paragraph("Hello PdfUA2").setFont(font);
            byte[] byteMetaData = pdfDocument.getXmpMetadata();
            //PDF/UA
            //Get string xmp metadata representation
            String documentMetaData = new String(byteMetaData);
            document.add(paragraph);
        } catch (XMPException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
