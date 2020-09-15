package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.File;
import java.io.IOException;

public class CutAndPaste {
    public static final String DEST = "./target/sandbox/merge/page229_cut_paste.pdf";

    public static final String SRC = "./src/main/resources/pdfs/page229.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CutAndPaste().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SRC));
        Rectangle pageSize = srcDoc.getFirstPage().getPageSize();
        PdfDocument resultPdfDoc = new PdfDocument(new PdfWriter(dest));
        resultPdfDoc.setDefaultPageSize(new PageSize(pageSize));
        resultPdfDoc.addNewPage();

        PdfFormXObject pageXObject = srcDoc.getFirstPage().copyAsFormXObject(resultPdfDoc);
        Rectangle toMove = new Rectangle(100, 500, 100, 100);

        // Create a formXObject of a page content, in which the area to move is cut.
        PdfFormXObject formXObject1 = new PdfFormXObject(pageSize);
        PdfCanvas canvas1 = new PdfCanvas(formXObject1, resultPdfDoc);
        canvas1.rectangle(0, 0, pageSize.getWidth(), pageSize.getHeight());
        canvas1.rectangle(toMove);

        // This method uses the even-odd rule to determine which regions lie inside the clipping path.
        canvas1.eoClip();
        canvas1.endPath();
        canvas1.addXObjectAt(pageXObject, 0, 0);

        // Create a formXObject of the area to move.
        PdfFormXObject formXObject2 = new PdfFormXObject(pageSize);
        PdfCanvas canvas2 = new PdfCanvas(formXObject2, resultPdfDoc);
        canvas2.rectangle(toMove);

        // This method uses the nonzero winding rule to determine which regions lie inside the clipping path.
        canvas2.clip();
        canvas2.endPath();
        canvas2.addXObjectAt(pageXObject, 0, 0);

        PdfCanvas canvas = new PdfCanvas(resultPdfDoc.getFirstPage());
        canvas.addXObjectAt(formXObject1, 0, 0);

        // Add the area to move content, shifted 10 points to the left and 2 points to the bottom.
        canvas.addXObjectAt(formXObject2, -20, -2);

        srcDoc.close();
        resultPdfDoc.close();
    }
}
