package com.itextpdf.samples.sandbox.wtpdf;

import com.itextpdf.commons.utils.FileUtil;
import com.itextpdf.forms.form.FormProperty;
import com.itextpdf.forms.form.element.InputField;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WellTaggedPdfConformance;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.pdfua.wtpdf.WellTaggedPdfConfig;
import com.itextpdf.pdfua.wtpdf.WellTaggedPdfDocument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/*
 * WtpdfWithUA2.java
 *
 * Example showing how to create document compliant with Pdf/UA2 and well tagged pdf for reuse and accessibility.
 */

public class WtpdfWithUA2 {

    public static final String DEST = "./target/sandbox/wtpdf/pdf_wtpdfPdfUA2.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";
    public static final String SOURCE_FOLDER = "./src/main/resources/wtpdf/";
    public static final String IMAGE_PATH = "./src/main/resources/img/itext.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WtpdfWithUA2().manipulatePdf(DEST);
    }

    private void manipulatePdf(String dest) throws Exception {
        PdfWriter writer = new PdfWriter(FileUtil.getFileOutputStream(dest), new WriterProperties().
                setPdfVersion(PdfVersion.PDF_2_0));

        List<WellTaggedPdfConformance> conformances = new ArrayList<>();
        conformances.add(WellTaggedPdfConformance.FOR_ACCESSIBILITY);
        conformances.add(WellTaggedPdfConformance.FOR_REUSE);
        PdfDocument pdf = new WellTaggedPdfDocument(writer, new WellTaggedPdfConfig(conformances,
                "well tagged pdf and pdf UA2 compliant document", "en-US"));

        // Setup the metadata for a PDF/UA-2 document
        byte[] bytes = Files.readAllBytes(Paths.get(SOURCE_FOLDER + "simplePdfUA2Wtpdf.xmp"));
        XMPMeta xmpMeta = XMPMetaFactory.parse(new ByteArrayInputStream(bytes));
        pdf.setXmpMetadata(xmpMeta);

        //add content
        addContent(pdf);
    }

    private void addContent(PdfDocument pdf) throws IOException {
        try (Document document = new Document(pdf)) {
            PdfFont font = PdfFontFactory.createFont(FONT, "WinAnsi", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            document.setFont(font);

            //Create a paragraph
            Paragraph paragraph = new Paragraph().add("This document is compliant with well tagged pdf and pdf UA2! " +
                    "Here, look at this in a list:");
            document.add(paragraph);

            //Create a list
            com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
            list.add("compliant with well tagged pdf for reuse");
            list.add("compliant with well tagged pdf for accessibility");
            list.add("compliant with PdfUA2");
            document.add(list);

            //Create a paragraph
            Paragraph paragraph2 = new Paragraph().add("here is also a table");
            document.add(paragraph2);

            //Create a table
            Table table = new Table(new float[]{1, 1, 1});
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setWidth(300);
            for (int i = 0; i < 3; i++) {
                table.addHeaderCell(new Paragraph("Column " + (i + 1)));
            }

            for (int i = 0; i < 3; i++) {
                table.addCell(new Paragraph("element " + (i + 1) + " in column 1"));
                table.addCell(new Paragraph("element " + (i + 1) + " in column 2"));
                table.addCell(new Paragraph("element " + (i + 1) + " in column 3"));
            }
            document.add(table);

            //Create a paragraph
            Paragraph paragraph3 = new Paragraph().add("and an image");
            document.add(paragraph3);

            //Create an image
            Image img = new Image(ImageDataFactory.create(IMAGE_PATH));
            img.getAccessibilityProperties()
                    .setAlternateDescription("Company logo");
            document.add(img);

            // Creating an InputField
            InputField formInputField = new InputField("form input field");
            formInputField.setProperty(FormProperty.FORM_FIELD_VALUE, "an input field also here");
            formInputField.getAccessibilityProperties().setRole(StandardRoles.ANNOT);
            formInputField.getAccessibilityProperties().setAlternateDescription("input field");
            formInputField.setInteractive(true);
            document.add(formInputField);
        }
    }
}
