package com.itextpdf.samples.sandbox.graphics;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;

import java.io.File;

public class GradientTopToBottom {
    public static final String DEST = "./target/sandbox/graphics/gradient_top_to_bottom.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new GradientTopToBottom().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PageSize pageSize = new PageSize(150, 300);
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        pdfDoc.setDefaultPageSize(pageSize);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        PdfShading.Axial axial = new PdfShading.Axial(new PdfDeviceCs.Rgb(), 0, pageSize.getHeight(),
                ColorConstants.WHITE.getColorValue(), 0, 0, ColorConstants.GREEN.getColorValue());
        PdfPattern.Shading pattern = new PdfPattern.Shading(axial);
        canvas.setFillColorShading(pattern);
        canvas.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas.fill();

        pdfDoc.close();
    }
}
