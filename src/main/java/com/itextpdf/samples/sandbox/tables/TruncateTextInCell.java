package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;

public class TruncateTextInCell {
    public static final String DEST = "./target/sandbox/tables/truncate_text_in_cell.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TruncateTextInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

        for (int r = 'A'; r <= 'Z'; r++) {
            for (int c = 1; c <= 5; c++) {
                Cell cell = new Cell();
                if (r == 'D' && c == 2) {
                    cell.setNextRenderer(new FitCellRenderer(cell,
                            "D2 is a cell with more content than we can fit into the cell."));
                } else {
                    cell.add(new Paragraph(String.valueOf((char) r) + c));
                }

                table.addCell(cell);
            }
        }

        doc.add(table);

        doc.close();
    }

    private static class FitCellRenderer extends CellRenderer {
        private String content;

        public FitCellRenderer(Cell modelElement, String content) {
            super(modelElement);
            this.content = content;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new FitCellRenderer((Cell) modelElement, content);
        }

        /**
         * Method adapts content, that can't be fit into the cell,
         * to prevent truncation by replacing truncated part of content with '...'
         */
        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            PdfFont bf = getPropertyAsFont(Property.FONT);
            int contentLength = content.length();
            int leftChar = 0;
            int rightChar = contentLength - 1;

            Rectangle rect = layoutContext.getArea().getBBox().clone();

            // Cell's margins, borders and paddings should be extracted from the available width as well.
            // Note that this part of the sample was introduced specifically for iText7.
            // since in iText5 the approach of processing cells was different
            applyMargins(rect, false);
            applyBorderBox(rect, false);
            applyPaddings(rect, false);
            float availableWidth = rect.getWidth();

            UnitValue fontSizeUV = this.getPropertyAsUnitValue(Property.FONT_SIZE);

            // Unit values can be of POINT or PERCENT type. In this particular sample
            // the font size value is expected to be of POINT type.
            float fontSize = fontSizeUV.getValue();

            availableWidth -= bf.getWidth("...", fontSize);

            while (leftChar < contentLength && rightChar != leftChar) {
                availableWidth -= bf.getWidth(content.charAt(leftChar), fontSize);
                if (availableWidth > 0) {
                    leftChar++;
                } else {
                    break;
                }

                availableWidth -= bf.getWidth(content.charAt(rightChar), fontSize);

                if (availableWidth > 0) {
                    rightChar--;
                } else {
                    break;
                }
            }

            // left char is the first char which should not be added
            // right char is the last char which should not be added
            String newContent = content.substring(0, leftChar) + "..." + content.substring(rightChar + 1);
            Paragraph p = new Paragraph(newContent);

            // We're operating on a Renderer level here, that's why we need to process a renderer,
            // created with the updated paragraph
            IRenderer pr = p.createRendererSubTree().setParent(this);
            childRenderers.add(pr);

            return super.layout(layoutContext);
        }
    }
}
