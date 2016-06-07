package com.itextpdf.samples.sandbox.merge;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.utils.PdfMerger;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * This class is a tool for merging PDF <em>page contents</em> in a condensed manner,
 * i.e. if the source pages are only partly filled, the contents of multiple pages
 * are drawn on a single target page. If on the other hand the contents of a source
 * page do not completely fit onto a target page, the source page contents are split
 * and as much as possible of it is added onto the current target page before a new
 * page is started. This actually allows to even copy content from larger pages onto
 * smaller ones, e.g. from A4 to A5 landscape.
 * </p>
 * <p>
 * In contrast to {@link PdfMerger}, though, this class does not copy information
 * beyond page content, in particular it completely ignores annotations and the
 * structure hierarchy of tagged PDFs.
 * </p>
 * <p>
 * Beware: Simple PDF is not particularly designed for this kind of operations. Thus,
 * there are numerous situations in which this tool may not create the desired result.
 * It e.g. should not be used to copy documents with header or footer information as
 * they will be considered part of the content and so copied and probably moved to
 * the middle of the page. Furthermore, water marks, text box backgrounds, etc. will
 * also be considered part of the content and, therefore, also will prevent condensed
 * merging.
 * </p>
 *
 * @author mkl
 */
public class PdfDenseMerger {

    //
    // hidden members
    //
    final PdfDocument pdfDocument;

    PageSize pageSize = PageSize.A4;
    float top;
    float bottom;
    float gap;

    PdfCanvas canvas;
    float yPosition = 0;

    /**
     * This class is used to merge a number of existing documents into one.
     *
     * @param pdfDocument - the document into which source documents will be merged.
     */
    public PdfDenseMerger(PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    //
    // getters and setters for layout values
    //
    public PageSize getPageSize() {
        return pageSize;
    }

    public PdfDenseMerger setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public float getTop() {
        return top;
    }

    public PdfDenseMerger setTop(float top) {
        this.top = top;
        return this;
    }

    public float getBottom() {
        return bottom;
    }

    public PdfDenseMerger setBottom(float bottom) {
        this.bottom = bottom;
        return this;
    }

    public float getGap() {
        return gap;
    }

    public PdfDenseMerger setGap(float gap) {
        this.gap = gap;
        return this;
    }

    public PdfDocument getPdfDocument() {
        return pdfDocument;
    }

    //
    // methods controling the actual merger
    //

    /**
     * This method adds pages from the source document to the target document.
     *
     * @param from     - document, from which pages will be copied.
     * @param fromPage - start page in the range of pages to be copied.
     * @param toPage   - end page in the range to be copied.
     */
    public void addPages(PdfDocument from, int fromPage, int toPage) throws IOException {
        for (int pageNum = fromPage; pageNum <= toPage; pageNum++) {
            merge(from, pageNum);
        }
    }

    /**
     * This method adds pages from the source document to the target document.
     *
     * @param from  - document, from which pages will be copied.
     * @param pages - List of numbers of pages which will be copied.
     */
    public void addPages(PdfDocument from, List<Integer> pages) throws IOException {
        for (Integer pageNum : pages) {
            merge(from, pageNum);
        }
    }

    //
    // helper methods
    //
    void merge(PdfDocument from, int pageNum) throws IOException {
        PdfPage page = from.getPage(pageNum);
        PdfFormXObject formXObject = page.copyAsFormXObject(pdfDocument);

        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(from);
        PageVerticalAnalyzer finder = contentParser.processContent(pageNum, new PageVerticalAnalyzer());
        if (finder.verticalFlips.size() < 2)
            return;
        Rectangle pageSizeToImport = page.getPageSize();

        int startFlip = finder.verticalFlips.size() - 1;
        boolean first = true;
        while (startFlip > 0) {
            if (!first)
                newPage();

            float freeSpace = yPosition - (pageSize.getBottom() + bottom);
            int endFlip = startFlip + 1;
            while ((endFlip > 1) && (finder.verticalFlips.get(startFlip) - finder.verticalFlips.get(endFlip - 2) < freeSpace))
                endFlip -= 2;
            if (endFlip < startFlip) {
                float height = finder.verticalFlips.get(startFlip) - finder.verticalFlips.get(endFlip);

                canvas.saveState();
                canvas.rectangle(0, yPosition - height, pageSizeToImport.getWidth(), height);
                canvas.clip();
                canvas.newPath();

                canvas.addXObject(formXObject, 0, yPosition - (finder.verticalFlips.get(startFlip) - pageSizeToImport.getBottom()));

                canvas.restoreState();
                yPosition -= height + gap;
                startFlip = endFlip - 1;
            } else if (!first)
                throw new IllegalArgumentException(String.format("Page %s content sections too large.", page));
            first = false;
        }
    }

    void newPage() {
        PdfPage page = pdfDocument.addNewPage(pageSize);
        canvas = new PdfCanvas(page);
        yPosition = pageSize.getTop() - top;
    }

}
