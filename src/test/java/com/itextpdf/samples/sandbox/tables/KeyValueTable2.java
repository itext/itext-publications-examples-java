/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This question was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/39190026
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(SampleTest.class)
public class KeyValueTable2 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/key_value_table2.pdf";

    protected PdfFont regular;
    protected PdfFont bold;

    @Override
    public void manipulatePdf(String dest) throws IOException {
        regular = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

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
        document.add(createTable(rohit, bruno));
        document.close();
    }

    public Table createTable(UserObject user1, UserObject user2) {
        if (user1 == null) {
            user1 = new UserObject();
        }
        if (user2 == null) {
            user2 = new UserObject();
        }
        Table table = new Table(3);
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add("Name:"));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user1.getName()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user2.getName()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add("Id:"));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user1.getId()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user2.getId()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add("Reputation:"));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(String.valueOf(user1.getReputation())));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(String.valueOf(user2.getReputation())));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(bold).add("Job title:"));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user1.getJobtitle()));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setFont(regular).add(user2.getJobtitle()));
        return table;
    }


    class UserObject {
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
