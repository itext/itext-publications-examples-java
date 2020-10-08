package com.itextpdf.samples.sandbox.merge.densemerger;

import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.kernel.utils.PdfMerger;

import java.io.IOException;
import java.util.List;

/**
 * This class is a tool for merging PDF <em>page contents</em> in a condensed manner,
 * i.e. if the source pages are only partly filled, the contents of multiple pages
 * are drawn on a single target page. If on the other hand the content of a source
 * page do not completely fit onto a target page, the source page content is split
 * and as much as possible of it is added onto the current target page before a new
 * page is started.
 * <p>
 * In contrast to {@link PdfMerger}, though, this class does not copy information
 * beyond page content, in particular it completely ignores annotations and the
 * structure hierarchy of tagged PDFs.
 * <p>
 * Beware: There are numerous situations in which this tool may not create the desired result.
 * It e.g. should not be used to copy documents with header or footer information as
 * they will be considered part of the content and so copied and probably moved to
 * the middle of the page. Furthermore, water marks, text box backgrounds, etc. will
 * also be considered part of the content and, therefore, also will prevent condensed
 * merging. The page size of the source document should be equal to the page size
 * of the resultant document.
 */
public class PdfDenseMerger {

    private final PdfDocument pdfDocument;

    private PageSize pageSize;
    private float top;
    private float bottom;
    private float gap;

    private PdfCanvas canvas;
    private float yPosition = 0;

    /**
     * Creates a PdfDenseMerger to merge content into the passed pdf document.
     *
     * @param pdfDocument - the document into which source documents will be merged.
     */
    public PdfDenseMerger(PdfDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
        this.pageSize = pdfDocument.getDefaultPageSize();
    }

    //
    // Getters and setters for layout values
    //

    /**
     * Gets the field <code>pageSize</code>.
     *
     * @return a {@link com.itextpdf.kernel.geom.PageSize} object.
     */
    public PageSize getPageSize() {
        return pageSize;
    }

    /**
     * Gets the top margin.
     *
     * @return a float.
     */
    public float getTopMargin() {
        return top;
    }

    /**
     * Sets the top margin.
     *
     * @param top a top margin of the resultant document.
     * @return this element.
     */
    public PdfDenseMerger setTopMargin(float top) {
        this.top = top;
        return this;
    }

    /**
     * Gets the bottom margin.
     *
     * @return a float.
     */
    public float getBottomMargin() {
        return bottom;
    }

    /**
     * Sets the bottom margin.
     *
     * @param bottom a bottom margin of the resultant document.
     * @return this element.
     */
    public PdfDenseMerger setBottomMargin(float bottom) {
        this.bottom = bottom;
        return this;
    }

    /**
     * Gets the gap.
     *
     * @return a float.
     */
    public float getGap() {
        return gap;
    }

    /**
     * Sets the gap between the content of the documents to be merged.
     *
     * @param gap the gap between the content of the documents to be merged.
     * @return this element.
     */
    public PdfDenseMerger setGap(float gap) {
        this.gap = gap;
        return this;
    }

    /**
     * Gets the pdfDocument.
     *
     * @return this element.
     */
    public PdfDocument getPdfDocument() {
        return pdfDocument;
    }

    //
    // Methods controlling the actual merger
    //

    /**
     * Adds pages from the source document to the target document.
     * Note that the page size of the source document is expected
     * to equal the page size of the resultant document
     *
     * @param from     - document, from which pages will be copied.
     * @param fromPage - start page in the range of pages to be copied.
     * @param toPage   - end page in the range to be copied.
     * @throws java.io.IOException if any.
     */
    public void addPages(PdfDocument from, int fromPage, int toPage) throws IOException {
        for (int pageNum = fromPage; pageNum <= toPage; pageNum++) {
            merge(from, pageNum);
        }
    }

    /**
     * Adds pages from the source document to the target document.
     * Note that the page size of the source document is expected
     * to equal the page size of the resultant document
     *
     * @param from  - document, from which pages will be copied.
     * @param pages - List of numbers of pages which will be copied.
     * @throws java.io.IOException if any.
     */
    public void addPages(PdfDocument from, List<Integer> pages) throws IOException {
        for (Integer pageNum : pages) {
            merge(from, pageNum);
        }
    }

    //
    // Helper methods
    //

    private void merge(PdfDocument from, int pageNum) throws IOException {
        PdfPage page = from.getPage(pageNum);
        Rectangle pageSizeToImport = page.getPageSize();
        if (!(pageSize.getHeight() == pageSizeToImport.getHeight())
                || !(pageSize.getWidth() == pageSizeToImport.getWidth())) {
            throw new PdfException("Page size of the copied page should be the same as "
                    + "the page size of the resultant document.");
        }

        PdfFormXObject formXObject = page.copyAsFormXObject(pdfDocument);

        PdfDocumentContentParser contentParser = new PdfDocumentContentParser(from);
        PageVerticalAnalyzer finder = contentParser.processContent(pageNum, new PageVerticalAnalyzer());
        List<Float> verticalFlips = finder.getVerticalFlips();
        if (verticalFlips.size() < 2) {
            return;
        }

        int startFlip = verticalFlips.size() - 1;
        boolean first = true;
        while (startFlip > 0) {
            if (!first) {
                newPage();
            }

            float freeSpace = yPosition - (pageSize.getBottom() + bottom);
            int endFlip = startFlip + 1;
            while ((endFlip > 1) && (verticalFlips.get(startFlip) - verticalFlips.get(endFlip - 2) < freeSpace)) {
                endFlip -= 2;
            }
            if (endFlip < startFlip) {
                float height = verticalFlips.get(startFlip) - verticalFlips.get(endFlip);

                canvas.saveState();
                canvas.rectangle(0, yPosition - height, pageSizeToImport.getWidth(), height);
                canvas.clip();
                canvas.endPath();

                canvas.addXObjectAt(formXObject, 0,
                        yPosition - (verticalFlips.get(startFlip) - pageSizeToImport.getBottom()));

                canvas.restoreState();
                yPosition -= height + gap;
                startFlip = endFlip - 1;
            } else if (!first) {
                throw new IllegalArgumentException(String.format("Page %s content sections too large.", page));
            }
            first = false;
        }
    }

    private void newPage() {
        PdfPage page = pdfDocument.addNewPage(pageSize);
        canvas = new PdfCanvas(page);
        yPosition = pageSize.getTop() - top;
    }

}
