package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;

// In this sample we will show how one can change the text of the cell on split.
// The table contains two columns: the first one will be processed as usually and
// the second one is the on which the required split logic will be performed.
// Mind that for simplification reasons the way in which we handle overflow rows is not designed for a generic case.
// For example, if your table has cells with big rowspan and/or colspans, you may want to update the code a bit.
public class TableSplitPageBreakEvent {
    public static final String DEST = "./target/sandbox/tables/tables_split_pageBreak_event.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TableSplitPageBreakEvent().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table = new Table(2);
        String text = "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "In this sample we will show how one can change the text of the cell on split. The table contains two columns: the first one will be processed as usually and the second one is the on which the required split logic will be performed\n"
                + "\n";

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                table.addCell(new Cell().add(new Paragraph(text)));
            }
        }

        table.setNextRenderer(new CustomTableRenderer(table));

        doc.add(table);
        doc.close();
    }

    private static final class CustomTableRenderer extends TableRenderer {
        private static final int CUSTOM_CONTENT_COLUMN_NUMBER = 1;

        public CustomTableRenderer(Table modelElement) {
            super(modelElement);
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CustomTableRenderer((Table) modelElement);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);
            CustomTableRenderer split = (CustomTableRenderer) result.getSplitRenderer();
            CustomTableRenderer overflow = (CustomTableRenderer) result.getOverflowRenderer();

            // Page split happened
            if (result.getStatus() == LayoutResult.PARTIAL) {
                Table.RowRange splitRange = split.rowRange;
                Table.RowRange overflowRange = overflow.rowRange;

                // The table split happened
                if (splitRange.getFinishRow() == overflowRange.getStartRow()) {
                    if (null != overflow.rows.get(0)) {

                        // Change cell contents on the new page
                        CellRenderer customContentCellRenderer = (CellRenderer) new Cell()
                                .add(new Paragraph("Custom content"))
                                .createRendererSubTree()
                                .setParent(this);
                        overflow.rows.get(0)[CUSTOM_CONTENT_COLUMN_NUMBER] = customContentCellRenderer;
                    }
                }
            }
            return result;
        }
    }
}
