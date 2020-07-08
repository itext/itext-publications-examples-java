package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CenterText {
    public static final String DEST = "./target/sandbox/objects/center_text.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CenterText().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

        SolidLine line = new SolidLine();
        addParagraphWithTabs(document, line, width);

        // Draw a custom line to fill both sides, as it is described in iText5 example
        MyLine customLine = new MyLine();
        addParagraphWithTabs(document, customLine, width);

        document.close();
    }

    private static void addParagraphWithTabs(Document document, ILineDrawer line, float width) {
        List<TabStop> tabStops = new ArrayList<>();

        // Create a TabStop at the middle of the page
        tabStops.add(new TabStop(width / 2, TabAlignment.CENTER, line));

        // Create a TabStop at the end of the page
        tabStops.add(new TabStop(width, TabAlignment.LEFT, line));

        Paragraph p = new Paragraph().addTabStops(tabStops);
        p
                .add(new Tab())
                .add("Text in the middle")
                .add(new Tab());
        document.add(p);
    }

    private static class MyLine implements ILineDrawer {
        private float lineWidth = 1;
        private float offset = 2.02f;
        private Color color = ColorConstants.BLACK;

        @Override
        public void draw(PdfCanvas canvas, Rectangle drawArea) {
            float coordY = drawArea.getY() + lineWidth / 2 + offset;
            canvas
                    .saveState()
                    .setStrokeColor(color)
                    .setLineWidth(lineWidth)
                    .moveTo(drawArea.getX(), coordY)
                    .lineTo(drawArea.getX() + drawArea.getWidth(), coordY)
                    .stroke()
                    .restoreState();
        }

        @Override
        public float getLineWidth() {
            return lineWidth;
        }

        @Override
        public void setLineWidth(float lineWidth) {
            this.lineWidth = lineWidth;
        }

        @Override
        public Color getColor() {
            return color;
        }

        @Override
        public void setColor(Color color) {
            this.color = color;
        }

        public float getOffset() {
            return offset;
        }

        public void setOffset(float offset) {
            this.offset = offset;
        }
    }
}
