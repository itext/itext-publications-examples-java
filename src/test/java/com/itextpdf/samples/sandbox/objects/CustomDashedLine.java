/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/27752409/using-itext-to-draw-separator-line-as-continuous-hypen-in-a-table-row
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;

@Category(SampleTest.class)
public class CustomDashedLine extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/custom_dashed_line.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new CustomDashedLine().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("Before dashed line"));
        CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
        separator.setDash(10);
        separator.setGap(7);
        separator.setLineWidth(3);
        doc.add(new LineSeparator(separator));
        doc.add(new Paragraph("After dashed line"));

        doc.close();
    }


    class CustomDashedLineSeparator extends DottedLine {
        protected float dash = 5;
        protected float phase = 2.5f;

        public float getDash() {
            return dash;
        }

        public float getPhase() {
            return phase;
        }

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
