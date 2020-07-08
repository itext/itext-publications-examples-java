package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class KeyValueTable2 {
    public static final String DEST = "./target/sandbox/tables/key_value_table2.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new KeyValueTable2().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

        UserObject rohit = new UserObject();
        rohit.setName("Rohit");
        rohit.setId("6633429");
        rohit.setReputation(1);
        rohit.setJobtitle("Copy/paste artist");

        UserObject bruno = new UserObject();
        bruno.setName("Bruno Lowagie");
        bruno.setId("1622493");
        bruno.setReputation(42690);
        bruno.setJobtitle("Java Rockstar");

        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdf);

        document.add(createTable(rohit, bruno, bold, regular));

        document.close();
    }

    private static Table createTable(UserObject user1, UserObject user2, PdfFont bold, PdfFont regular) {
        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();

        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(new Paragraph("Name:")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user1.getName())));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user2.getName())));

        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(new Paragraph("Id:")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user1.getId())));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user2.getId())));

        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(new Paragraph("Reputation:")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(String.valueOf(user1.getReputation()))));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(String.valueOf(user2.getReputation()))));

        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add(new Paragraph("Job title:")));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user1.getJobtitle())));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(new Paragraph(user2.getJobtitle())));

        return table;
    }


    private static class UserObject {
        protected String name = "";
        protected String id = "";
        protected int reputation = 0;
        protected String jobtitle = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getReputation() {
            return reputation;
        }

        public void setReputation(int reputation) {
            this.reputation = reputation;
        }

        public String getJobtitle() {
            return jobtitle;
        }

        public void setJobtitle(String jobtitle) {
            this.jobtitle = jobtitle;
        }
    }
}
