/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Table.RowRange;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.renderer.AreaBreakRenderer;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitRowAtSpecificRow {
    public static final String DEST = "./target/sandbox/tables/split_row_at_specific_row.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new SplitRowAtSpecificRow().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.setWidth(550);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc, new PageSize(612, 242));
        for (int i = 0; i < 40; i++) {
            Cell cell;
            if (i == 20) {
                cell = new Cell().add(new Paragraph("Multiple\n\n\n\nLines"));
            } else {
                cell = new Cell().add(new Paragraph(Integer.toString(i)));
            }
            table.addCell(cell);
        }

        table.setNextRenderer(new SplitTableAtSpecificRowRenderer(table, new ArrayList<>(
                Arrays.asList(
                        // first break occurs on row 4
                        4,
                        // break at row 7 gets ignored because if we break at 9 it still fits the page
                        7, 9,
                        // last break occurs at 25 the other rows get rendered as normal
                        25,
                        // break point 36 does not break because the remaining rows still fit the page
                        36,
                        // break point 50 gets ignored because there are not enough rows
                        50
                ))
        ));
        doc.add(table);
        doc.close();
    }

    static class SplitTableAtSpecificRowRenderer extends TableRenderer {

        private final ArrayList<Integer> breakPoints;
        private int amountOfRowsThatAreGoingToBeRendered = 0;

        public SplitTableAtSpecificRowRenderer(Table modelElement, ArrayList<Integer> breakPoints) {
            super(modelElement);
            this.breakPoints = breakPoints;
        }

        @Override
        public IRenderer getNextRenderer() {
            return new SplitTableAtSpecificRowRenderer((Table) modelElement, this.breakPoints);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = null;
            while (result == null) {
                result = attemptLayout(layoutContext, this.breakPoints);
            }
            this.breakPoints.replaceAll(integer -> integer - this.amountOfRowsThatAreGoingToBeRendered);
            return result;
        }

        private LayoutResult attemptLayout(LayoutContext layoutContext, List<Integer> breakPoints) {
            LayoutResult layoutResult = super.layout(layoutContext);
            if (layoutResult.getStatus() == LayoutResult.FULL || breakPoints.isEmpty()) {
                this.amountOfRowsThatAreGoingToBeRendered = getAmountOfRows(layoutResult);
                return layoutResult;
            }
            int breakPointToFix = calculateBreakPoint(layoutContext);
            if (breakPointToFix >= 0) {
                forceAreaBreak(breakPointToFix);
                this.amountOfRowsThatAreGoingToBeRendered = breakPointToFix - 1;
                return null;
            }
            return layoutResult;
        }


        private int calculateBreakPoint(LayoutContext layoutContext) {
            LayoutResult layoutResultWithoutSplits = attemptLayout(layoutContext, Collections.emptyList());
            if (layoutResultWithoutSplits == null) {
                return Integer.MIN_VALUE;
            }
            int amountOfRowsThatFitWithoutSplit = getAmountOfRows(layoutResultWithoutSplits);
            int breakPointToFix = Integer.MIN_VALUE;
            for (Integer breakPoint : new ArrayList<>(breakPoints)) {
                if (breakPoint <= amountOfRowsThatFitWithoutSplit) {
                    breakPoints.remove(breakPoint);
                    if (breakPoint < amountOfRowsThatFitWithoutSplit && breakPoint > breakPointToFix) {
                        breakPointToFix = breakPoint;
                    }
                }
            }
            return breakPointToFix;
        }

        private void forceAreaBreak(int rowIndex) {
            rowIndex++;
            if (rowIndex > rows.size()) {
                return;
            }
            for (CellRenderer cellRenderer : rows.get(rowIndex)) {
                if (cellRenderer != null) {
                    cellRenderer.getChildRenderers()
                            .add(0, new AreaBreakRenderer(new AreaBreak(AreaBreakType.NEXT_PAGE)));
                    break;
                }
            }
        }

        private static int getAmountOfRows(LayoutResult layoutResult) {
            if (layoutResult.getSplitRenderer() == null) {
                return 0;
            }
            return ((SplitTableAtSpecificRowRenderer) layoutResult.getSplitRenderer()).rows.size();
        }
    }

}
