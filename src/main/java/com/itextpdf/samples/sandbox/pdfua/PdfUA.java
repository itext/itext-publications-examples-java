package com.itextpdf.samples.sandbox.pdfua;

import com.itextpdf.forms.form.element.InputField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfUAConformanceLevel;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.tagging.PdfStructureAttributes;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.pdfua.PdfUAConfig;
import com.itextpdf.pdfua.PdfUADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.print.Doc;

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

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc =  new PdfUADocument(new PdfWriter(dest),
                new PdfUAConfig(PdfUAConformanceLevel.PDFUA_1, "Some title", "en-US"));
        Document document = new Document(pdfDoc, PageSize.A4.rotate());


        //PDF UA requires font's to be embedded this is the way we want to do it
        PdfFont font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, EmbeddingStrategy.PREFER_EMBEDDED);
        document.setFont(font);

        Paragraph p = new Paragraph();
        //You can also set it on individual elements if you want to
        p.setFont(font);
        p.add("The quick brown ");
        document.add(p);


        //Images require the to have an alternative description
        Image img = new Image(ImageDataFactory.create(FOX));
        img.getAccessibilityProperties().setAlternateDescription("Fox");
        document.add(img);

        Paragraph p2 = new Paragraph( " jumps over the lazy ");
        document.add(p2);

        Image img2 = new Image(ImageDataFactory.create(DOG));
        img2.getAccessibilityProperties().setAlternateDescription("Dog");
        document.add(img2);


        Paragraph p3 = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n").setFontSize(20);
        document.add(p3);

        //Let's add a list
        List list = new List().setFontSize(20);
        list.add(new ListItem("quick"));
        list.add(new ListItem("brown"));
        list.add(new ListItem("fox"));
        list.add(new ListItem("jumps"));
        list.add(new ListItem("over"));
        list.add(new ListItem("the"));
        list.add(new ListItem("lazy"));
        list.add(new ListItem("dog"));
        document.add(list);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        addTables(document);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        addFormfields(document);

        document.close();
    }

    private void addTables(Document document){
        //Add a table with the automatic column scope
        Table table = new Table(2);
        Cell  headerCell1 = new Cell().add(new Paragraph("Header 1"));
        headerCell1.getAccessibilityProperties().setRole(StandardRoles.TH);
        Cell  headerCell2 = new Cell().add(new Paragraph("Header 2"));
        headerCell2.getAccessibilityProperties().setRole(StandardRoles.TH);

        table.addHeaderCell(headerCell1);
        table.addHeaderCell(headerCell2);

        table.addCell(new Cell().add(new Paragraph("data 1")));
        table.addCell(new Cell().add(new Paragraph("data 2")));

        document.add(table);
        document.add(new Paragraph("\n\n"));
        //add a table with row scope
        Table table2 = new Table(2);
        Cell  headerCell3 = new Cell().add(new Paragraph("Header 1"));
        PdfStructureAttributes attributes = new PdfStructureAttributes("Table");
        attributes.addEnumAttribute("Scope", "Row");
        headerCell3.getAccessibilityProperties().addAttributes(attributes);
        headerCell3.getAccessibilityProperties().setRole(StandardRoles.TH);

        Cell  headerCell4 = new Cell().add(new Paragraph("Header 2"));
        headerCell4.getAccessibilityProperties().setRole(StandardRoles.TH);
        PdfStructureAttributes attributes2 = new PdfStructureAttributes("Table");
        attributes2.addEnumAttribute("Scope", "Row");
        headerCell4.getAccessibilityProperties().addAttributes(attributes2);


        table2.addCell(headerCell3);
        table2.addCell(new Cell().add(new Paragraph("data 1")));
        table2.addCell(headerCell4);
        table2.addCell(new Cell().add(new Paragraph("data 2")));
        document.add(table2);

        //For complex tables you can also make use of Id's
        Table table3 = new Table(2);
        for (int i = 0; i < 4; i++) {
            Cell cell = new Cell().add(new Paragraph("data " + i));
            PdfStructureAttributes cellAttributes = new PdfStructureAttributes("Table");
            PdfArray headers = new PdfArray();
            headers.add(new PdfString("header_id_1"));
            cellAttributes.getPdfObject().put(PdfName.Headers, headers);
            cell.getAccessibilityProperties().addAttributes(cellAttributes);
            table3.addCell(cell);
        }
        Cell headerCell5 = new Cell(1,2).add(new Paragraph("Header 1"));
        headerCell5.getAccessibilityProperties().setRole(StandardRoles.TH);
        headerCell5.getAccessibilityProperties().setStructureElementIdString("header_id_1");
        PdfStructureAttributes headerAttributes = new PdfStructureAttributes("Table");
        headerAttributes.addEnumAttribute("Scope", "None");
        headerCell5.getAccessibilityProperties().addAttributes(headerAttributes);
        table3.addCell(headerCell5);
        document.add(table3);

        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        //Let's add some headings
        Paragraph h1 = new Paragraph("Heading 1").setFontSize(20);
        h1.getAccessibilityProperties().setRole(StandardRoles.H1);
        document.add(h1);
        Paragraph h2 = new Paragraph("Heading 2").setFontSize(18);
        h2.getAccessibilityProperties().setRole(StandardRoles.H2);
        document.add(h2);
        Paragraph h3 = new Paragraph("Heading 3").setFontSize(16);
        h3.getAccessibilityProperties().setRole(StandardRoles.H3);
        document.add(h3);

    }

    private void addFormfields(Document document){
        //Formfields are also possible
        InputField field = new InputField("name");
        field.getAccessibilityProperties().setAlternateDescription("Name");
        field.setValue("John Doe");
        field.setBackgroundColor(ColorConstants.CYAN);
        document.add(field);

        InputField field2 = new InputField("email");
        field2.getAccessibilityProperties().setAlternateDescription("Email");
        field2.setValue("sales@apryse.com");
        field2.setInteractive(true);
        field2.setBackgroundColor(ColorConstants.YELLOW);
        document.add(field2);

    }
}
