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
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class ColoredBackground {
    public static final String DEST = "./target/sandbox/tables/colored_background.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ColoredBackground().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        Table table = new Table(UnitValue.createPercentArray(16)).useAllAvailableWidth();

        for (int aw = 0; aw < 16; aw++) {
            Cell cell = new Cell().add(new Paragraph("hi")
                    .setFont(font)
                    .setFontColor(ColorConstants.WHITE));
            cell.setBackgroundColor(ColorConstants.BLUE);
            cell.setBorder(Border.NO_BORDER);
            cell.setTextAlignment(TextAlignment.CENTER);
            table.addCell(cell);
        }

        doc.add(table);

        doc.close();
    }
}
