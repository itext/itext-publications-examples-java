/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/22093488/itext-how-do-i-get-the-rendered-dimensions-of-text
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class TruncateTextInCell extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/tables/truncate_text_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TruncateTextInCell().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(5);
        Cell cell;
        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                cell = new Cell();
                if (r == 'D' && c == 2) {
                    cell.setNextRenderer(new FitCellRenderer(cell,
                            "D2 is a cell with more content than we can fit into the cell."));
                } else {
                    cell.add(new Paragraph(String.valueOf((char) r) + String.valueOf(c)));
                }
                table.addCell(cell);
            }
        }
        doc.add(table);

        doc.close();
    }

    private static class FitCellRenderer extends CellRenderer {
        private String content;

        public FitCellRenderer(Cell modelElement, String content) throws IOException {
            super(modelElement);
            this.content = content;
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            PdfFont bf = getPropertyAsFont(Property.FONT);
            float availableWidth = layoutContext.getArea().getBBox().getWidth();
            int contentLength = content.length();
            int leftChar = 0;
            int rightChar = contentLength - 1;
            availableWidth -= bf.getWidth("...", 12);
            while (leftChar < contentLength && rightChar != leftChar) {
                availableWidth -= bf.getWidth(content.charAt(leftChar), 12);
                if (availableWidth > 0)
                    leftChar++;
                else
                    break;
                availableWidth -= bf.getWidth(content.charAt(rightChar), 12);
                if (availableWidth > 0)
                    rightChar--;
                else
                    break;
            }
            String newContent = content.substring(0, leftChar) + "..." + content.substring(rightChar);
            Paragraph p = new Paragraph(newContent);

            IRenderer pr = p.createRendererSubTree().setParent(this);
            this.childRenderers.add(pr);
            return super.layout(layoutContext);
        }
    }
}



