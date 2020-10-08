/**
 * <p>
 * This example is named IncorrectExample because this is not how the problem of rotating pages
 * or of merging documents typically should be solved.
 * However: in the question mentioned above, the use case is very specific
 * and the usage of this example in those circumstances is justified.
 */
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

public class IncorrectExample {
    public static final String DEST = "./target/sandbox/merge/incorrect_example.pdf";

    public static final String SOURCE = "./src/main/resources/pdfs/pages.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new IncorrectExample().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument srcDoc = new PdfDocument(new PdfReader(SOURCE));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));

        for (int i = 1; i <= srcDoc.getNumberOfPages(); i++) {
            PageSize pageSize = getPageSize(srcDoc, i);
            pdfDoc.setDefaultPageSize(pageSize);
            PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
            PdfFormXObject page = srcDoc.getPage(i).copyAsFormXObject(pdfDoc);

            if (isPortrait(srcDoc, i)) {
                canvas.addXObjectAt(page, 0, 0);
            } else {

                // Add page content as formXObject, rotated counterclockwise.
                canvas.addXObjectWithTransformationMatrix(page, 0, 1, -1, 0, pageSize.getWidth(), 0);
            }
        }

        pdfDoc.close();
        srcDoc.close();
    }

    private static PageSize getPageSize(PdfDocument pdfDoc, int pageNumber) {
        PdfPage page = pdfDoc.getPage(pageNumber);
        Rectangle pageSize = page.getPageSize();

        // Returns a page size with the lowest value of the dimensions of the existing page as the width
        // and the highest value as the height. This way, the page will always be in portrait.
        return new PageSize(
                Math.min(pageSize.getWidth(), pageSize.getHeight()),
                Math.max(pageSize.getWidth(), pageSize.getHeight()));
    }

    private static boolean isPortrait(PdfDocument pdfDoc, int pageNumber) {
        PdfPage page = pdfDoc.getPage(pageNumber);

        // This method doesn't take page rotation into account.
        Rectangle pageSize = page.getPageSize();
        return pageSize.getHeight() > pageSize.getWidth();
    }
}
