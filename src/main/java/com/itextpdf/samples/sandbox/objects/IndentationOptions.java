package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.IOException;

public class IndentationOptions {
    public static final String CONTENT = "test A, test B, coconut, coconut, watermelons, apple, oranges, many more " +
            "fruites, carshow, monstertrucks thing, everything is startting on the same point in the line now";
    public static final String LABEL = "A list of stuff: ";
    public static final String DEST = "./target/sandbox/objects/indentation_options.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new IndentationOptions().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        List list = new List().
                setListSymbol(LABEL).
                add(CONTENT);
        doc.add(list);

        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        Paragraph paragraph = new Paragraph(LABEL + CONTENT).setFont(font);
        float indentation = font.getWidth(LABEL, 12);

        // Shift all lines except the first one to the width of the label
        paragraph.setMarginLeft(indentation)
                .setFirstLineIndent(-indentation);
        doc.add(paragraph);

        // Add 4, because the default padding (left and right) of a cell equals 2
        Table table = new Table(new float[]{indentation + 4, 519 - indentation});
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(LABEL)));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(CONTENT)));
        doc.add(table);

        doc.close();
    }
}
