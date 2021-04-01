package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class CellMethod {
    public static final String DEST = "./target/sandbox/tables/cell_method.pdf";
    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";

    private static PdfFont czechFont;
    private static PdfFont defaultFont;
    private static PdfFont greekFont;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CellMethod().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        czechFont = PdfFontFactory.createFont(FONT, "Cp1250", EmbeddingStrategy.PREFER_EMBEDDED);
        greekFont = PdfFontFactory.createFont(FONT, "Cp1253", EmbeddingStrategy.PREFER_EMBEDDED);
        defaultFont = PdfFontFactory.createFont(FONT, null, EmbeddingStrategy.PREFER_EMBEDDED);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // By default column width is calculated automatically for the best fit.
        // useAllAvailableWidth() method makes table use the whole page's width while placing the content.
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

        table.addCell("Winansi");
        table.addCell(getNormalCell("Test", null, 12));
        table.addCell("Winansi");
        table.addCell(getNormalCell("Test", null, -12));
        table.addCell("Greek");
        table.addCell(getNormalCell("\u039d\u03cd\u03c6\u03b5\u03c2", "greek", 12));
        table.addCell("Czech");
        table.addCell(getNormalCell("\u010c,\u0106,\u0160,\u017d,\u0110", "czech", 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", null, 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", "greek", 12));
        table.addCell("Test");
        table.addCell(getNormalCell(" ", "czech", 12));
        doc.add(table);

        doc.close();
    }

    private static Cell getNormalCell(String string, String language, float size) {
        if (string != null && "".equals(string)) {
            return new Cell();
        }

        PdfFont f = getFontForThisLanguage(language);
        Paragraph paragraph = new Paragraph(string).setFont(f);

        Cell cell = new Cell().add(paragraph);
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);

        if (size < 0) {
            size = -size;
            cell.setFontSize(size);
            cell.setFontColor(ColorConstants.RED);
        }

        return cell;
    }

    private static PdfFont getFontForThisLanguage(String language) {
        if (language == null) {
            return defaultFont;
        }
        switch (language) {
            case "czech": {
                return czechFont;
            }
            case "greek": {
                return greekFont;
            }
            default: {
                return defaultFont;
            }
        }
    }
}
