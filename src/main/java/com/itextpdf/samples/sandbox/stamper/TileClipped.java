package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;

public class TileClipped {
    public static final String DEST = "./target/sandbox/stamper/tile_clipped.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TileClipped().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        float margin = 30;
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle rect = srcDoc.getFirstPage().getPageSizeWithRotation();
        Rectangle pageSize = new Rectangle(rect.getWidth() + margin * 2, rect.getHeight() + margin * 2);

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        // The functionality below will work only for the pages, added after the method is called
        pdfDoc.setDefaultPageSize(new PageSize(pageSize));

        PdfCanvas content = new PdfCanvas(pdfDoc.addNewPage());
        PdfFormXObject page = srcDoc.getFirstPage().copyAsFormXObject(pdfDoc);

        // Adding the same page 16 times with a different offset
        for (int i = 0; i < 16; i++) {
            float x = -rect.getWidth() * (i % 4) + margin;
            float y = rect.getHeight() * (i / 4 - 3) + margin;
            content.rectangle(margin, margin, rect.getWidth(), rect.getHeight());
            content.clip();
            content.endPath();
            content.addXObjectWithTransformationMatrix(page, 4, 0, 0, 4, x, y);
            if (15 != i) {
                content = new PdfCanvas(pdfDoc.addNewPage());
            }
        }

        pdfDoc.close();
    }
}
