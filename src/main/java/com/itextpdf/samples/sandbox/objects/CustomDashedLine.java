package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

public class CustomDashedLine {
    public static final String DEST = "./target/sandbox/objects/custom_dashed_line.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomDashedLine().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Before dashed line"));

        CustomDashedLineSeparator dashedLine = new CustomDashedLineSeparator();
        dashedLine.setDash(10f);
        dashedLine.setPhase(2.5f);
        dashedLine.setGap(7f);
        dashedLine.setLineWidth(3f);
        doc.add(new LineSeparator(dashedLine));
        doc.add(new Paragraph("After dashed line"));

        doc.close();
    }

    protected class CustomDashedLineSeparator extends DottedLine {
        private float dash;
        private float phase;

        public void setDash(float dash) {
            this.dash = dash;
        }

        public void setPhase(float phase) {
            this.phase = phase;
        }

        @Override
        public void draw(PdfCanvas canvas, Rectangle drawArea) {
            canvas.saveState()
                    .setLineWidth(getLineWidth())
                    .setLineDash(dash, gap, phase)
                    .moveTo(drawArea.getX(), drawArea.getY())
                    .lineTo(drawArea.getX() + drawArea.getWidth(), drawArea.getY())
                    .stroke()
                    .restoreState();
        }
    }
}
