package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class TilingHero {
    public static final String DEST = "./target/sandbox/merge/tiling_hero.pdf";

    public static final String RESOURCE = "./src/main/resources/pdfs/hero.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TilingHero().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(RESOURCE));
        PdfDocument resultDoc = new PdfDocument(new PdfWriter(dest));
        PdfPage srcFirstPage = srcDoc.getFirstPage();

        Rectangle pageSize = srcFirstPage.getPageSizeWithRotation();
        float width = pageSize.getWidth();
        float height = pageSize.getHeight();

        // The top left rectangle of the tiled pdf picture
        Rectangle mediaBox = new Rectangle(0, 3 * height, width, height);
        resultDoc.setDefaultPageSize(new PageSize(mediaBox));

        PdfFormXObject page = srcFirstPage.copyAsFormXObject(resultDoc);
        for (int i = 1; i <= 16; i++) {
            PdfCanvas canvas = new PdfCanvas(resultDoc.addNewPage());
            canvas.addXObjectWithTransformationMatrix(page, 4, 0, 0, 4, 0, 0);

            float xCoordinate = (i % 4) * width;
            float yCoordinate = (4 - (i / 4)) * height;
            mediaBox = new Rectangle(xCoordinate, yCoordinate, width, -height);
            resultDoc.setDefaultPageSize(new PageSize(mediaBox));
        }

        srcDoc.close();
        resultDoc.close();
    }
}
