/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DrawRectangle {
    public static final String DEST = "./target/sandbox/objects/draw_rectangle.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new DrawRectangle().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        Rectangle rect = new Rectangle(36, 36, 523, 770);
        canvas.setLineWidth(2);
        canvas.rectangle(rect);
        canvas.stroke();

        pdfDoc.close();
    }
}
