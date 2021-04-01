package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class PdfUA {
    public static final String DEST = "./target/sandbox/pdfua/pdf_ua.pdf";

    public static final String DOG = "./src/main/resources/img/dog.bmp";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    public static final String FOX = "./src/main/resources/img/fox.bmp";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new PdfUA().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, XMPException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest,
                new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
        Document document = new Document(pdfDoc, PageSize.A4.rotate());

        //TAGGED PDF
        //Make document tagged
        pdfDoc.setTagged();

        //PDF/UA
        //Set document metadata
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        pdfDoc.getCatalog().setLang(new PdfString("en-US"));
        PdfDocumentInfo info = pdfDoc.getDocumentInfo();
        info.setTitle("English pangram");

        Paragraph p = new Paragraph();

        //PDF/UA
        //Embed font
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        p.setFont(font);

        p.add("The quick brown ");

        Image img = new Image(ImageDataFactory.create(FOX));

        //PDF/UA
        //Set alt text
        img.getAccessibilityProperties().setAlternateDescription("Fox");
        p.add(img);
        p.add(" jumps over the lazy ");

        img = new Image(ImageDataFactory.create(DOG));

        //PDF/UA
        //Set alt text
        img.getAccessibilityProperties().setAlternateDescription("Dog");
        p.add(img);

        document.add(p);

        p = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n").setFont(font).setFontSize(20);
        document.add(p);

        List list = new List().setFont(font).setFontSize(20);
        list.add(new ListItem("quick"));
        list.add(new ListItem("brown"));
        list.add(new ListItem("fox"));
        list.add(new ListItem("jumps"));
        list.add(new ListItem("over"));
        list.add(new ListItem("the"));
        list.add(new ListItem("lazy"));
        list.add(new ListItem("dog"));
        document.add(list);

        document.close();
    }
}
