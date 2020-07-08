package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class SimpleTable7 {
    public static final String DEST = "./target/sandbox/tables/simple_table7.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SimpleTable7().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
        Paragraph docTitle = new Paragraph("UCSC Direct - Direct Payment Form").setMarginRight(1);
        docTitle.setFont(titleFont).setFontSize(11);
        doc.add(docTitle);

        PdfFont subtitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
        Paragraph subTitle = new Paragraph("(not to be used for reimbursement of services)");
        subTitle.setFont(subtitleFont).setFontSize(9);
        doc.add(subTitle);

        PdfFont importantNoticeFont = PdfFontFactory.createFont(StandardFonts.COURIER);
        Paragraph importantNotice = new Paragraph("Important: Form must be filled out in Adobe Reader or Acrobat " +
                "Professional 8.1 or above. To save completed forms, Acrobat Professional is required. " +
                "For technical and accessibility assistance, contact the Campus Controller's Office.");

        importantNotice.setFont(importantNoticeFont).setFontSize(9);
        importantNotice.setFontColor(ColorConstants.RED);
        doc.add(importantNotice);

        Table table = new Table(UnitValue.createPercentArray(10)).setFixedLayout().
                setWidth(UnitValue.createPercentValue(80));

        Cell cell = new Cell(1, 3).add(docTitle);
        cell.setBorder(Border.NO_BORDER);
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.addCell(cell);

        Cell cellCaveat = new Cell(1, 2).add(subTitle);
        cellCaveat.setBorder(Border.NO_BORDER);
        table.addCell(cellCaveat);

        Cell cellImportantNote = new Cell(1, 5).add(importantNotice);
        cellImportantNote.setBorder(Border.NO_BORDER);

        table.addCell(cellImportantNote);

        doc.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));
        doc.add(new Paragraph("This is the same table, created differently").
                setFont(subtitleFont).setFontSize(9).setMarginBottom(10));

        table = new Table(UnitValue.createPercentArray(new float[] {30, 20, 50})).setFixedLayout()
                .setWidth(UnitValue.createPercentValue(80));
        table.addCell(new Cell().add(docTitle).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(subTitle).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(importantNotice).setBorder(Border.NO_BORDER));

        doc.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));

        doc.close();
    }
}
