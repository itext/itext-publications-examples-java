/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written in answer to:
 * http://stackoverflow.com/questions/34433510
 */
package com.itextpdf.samples.sandbox.graphics;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class GradientTopToBottom extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/graphics/gradient_top_to_bottom.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new GradientTopToBottom().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PageSize pageSize = new PageSize(150, 300);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(pageSize);
        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfShading.Axial axial = new PdfShading.Axial(new PdfDeviceCs.Rgb(), 0, pageSize.getHeight(), Color.WHITE.getColorValue(),
                0, 0, Color.GREEN.getColorValue());
        PdfPattern.Shading pattern = new PdfPattern.Shading(axial);
        canvas.setFillColorShading(pattern);
        canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas.fill();
        pdfDoc.close();
    }
}
