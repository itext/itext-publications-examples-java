/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.Separation;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfSpecialCs;
import com.itextpdf.kernel.pdf.function.PdfType2Function;

import java.io.File;

public class AddSpotColorShape {
    public static final String DEST = "./target/sandbox/stamper/add_spot_color_shape.pdf";
    public static final String SRC = "./src/main/resources/pdfs/image.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddSpotColorShape().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfPage pdfPage = pdfDoc.getFirstPage();

        pdfPage.setIgnorePageRotationForContent(true);
        PdfCanvas canvas = new PdfCanvas(pdfPage);
        canvas.arc(0, 0, 842, 595, 0, 360);
        canvas.arc(25, 25, 817, 570, 0, 360);
        canvas.arc(50, 50, 792, 545, 0, 360);
        canvas.arc(75, 75, 767, 520, 0, 360);
        canvas.eoClip();
        canvas.endPath();
        canvas.setFillColor(new Separation(createCmykColorSpace(0.8f, 0.3f, 0.3f, 0.1f), 0.4f));
        canvas.rectangle(0, 0, 842, 595);
        canvas.fill();

        pdfDoc.close();
    }

    private PdfSpecialCs.Separation createCmykColorSpace(float c, float m, float y, float k) {
        double[] c0 = new double[] {0, 0, 0, 0};
        double[] c1 = new double[] {c, m, y, k};
        PdfType2Function pdfFunction = new PdfType2Function(new double[] {0, 1}, null, c0, c1, 1);
        PdfSpecialCs.Separation cs = new PdfSpecialCs.Separation("iTextSpotColorCMYK",
                new DeviceCmyk(c, m, y, k).getColorSpace(), pdfFunction);

        return cs;
    }
}
