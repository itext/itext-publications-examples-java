/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/29548762/why-is-my-table-not-being-generated-on-my-pdf-file-using-itextsharp
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;

@Category(SampleTest.class)
public class SimpleTable7 extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/simple_table7.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new SimpleTable7().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont titleFont = PdfFontFactory.createFont(FontConstants.COURIER_BOLD);
        Paragraph docTitle = new Paragraph("UCSC Direct - Direct Payment Form").setMarginRight(1);
        docTitle.setFont(titleFont).setFontSize(11);
        doc.add(docTitle);

        PdfFont subtitleFont = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        Paragraph subTitle = new Paragraph("(not to be used for reimbursement of services)");
        subTitle.setFont(subtitleFont).setFontSize(9);
        doc.add(subTitle);

        PdfFont importantNoticeFont = PdfFontFactory.createFont(FontConstants.COURIER);
        Paragraph importantNotice = new Paragraph("Important: Form must be filled out in Adobe Reader or Acrobat Professional 8.1 or above. To save completed forms, Acrobat Professional is required. For technical and accessibility assistance, contact the Campus Controller's Office.");
        importantNotice.setFont(importantNoticeFont).setFontSize(9);
        importantNotice.setFontColor(Color.RED);
        doc.add(importantNotice);

        Table table = new Table(10).
                setWidth(UnitValue.createPercentValue(80));
        Cell cell = new Cell(1, 3).add(docTitle);
        cell.setBorder(Border.NO_BORDER);
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.addCell(cell);

        Cell cellCaveat = new Cell(1, 2).add(subTitle);
        cellCaveat.setBorder(null);
        table.addCell(cellCaveat);

        Cell cellImportantNote = new Cell(1, 5).add(importantNotice);
        cellImportantNote.setBorder(Border.NO_BORDER);
        table.addCell(cellImportantNote);

        doc.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));
        doc.add(new Paragraph("This is the same table, created differently").
                setFont(subtitleFont).setFontSize(9).setMarginBottom(10));

        table = new Table(new float[]{3, 2, 5}).
                setWidth(UnitValue.createPercentValue(80));
        table.addCell(new Cell().add(docTitle).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(subTitle).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(importantNotice).setBorder(Border.NO_BORDER));
        doc.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));

        doc.close();
    }
}
