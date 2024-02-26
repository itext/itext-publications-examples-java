package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.ParagraphRenderer;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;

public class ThreeParts {
    public static final String DEST = "./target/sandbox/events/three_parts.pdf";

    public static final String SRC_LA = "./src/main/resources/txt/liber1_%s_la.txt";
    public static final String SRC_EN = "./src/main/resources/txt/liber1_%s_en.txt";
    public static final String SRC_FR = "./src/main/resources/txt/liber1_%s_fr.txt";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ThreeParts().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        int firstPageNumber = 1;

        for (int i = 0; i < 3; i++) {

            // latin
            addSection(pdfDoc, readAndCreateParagraph(String.format(SRC_LA, i + 1)),
                    firstPageNumber, 0);

            // english
            addSection(pdfDoc, readAndCreateParagraph(String.format(SRC_EN, i + 1)),
                    firstPageNumber, 1);

            // french
            addSection(pdfDoc, readAndCreateParagraph(String.format(SRC_FR, i + 1)),
                    firstPageNumber, 2);

            firstPageNumber = pdfDoc.getNumberOfPages() + 1;
        }

        pdfDoc.close();
    }

    private static void addSection(PdfDocument pdfDoc, Paragraph paragraph, int pageNumber, int sectionNumber) {
        Document doc = new Document(pdfDoc);
        ParagraphRenderer renderer = (ParagraphRenderer) paragraph.createRendererSubTree();
        renderer.setParent(new DocumentRenderer(doc));

        float pageHeight = pdfDoc.getDefaultPageSize().getHeight();
        float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
        Rectangle textSectionRectangle = new Rectangle(
                doc.getLeftMargin(),
                doc.getBottomMargin() + ((pageHeight - doc.getTopMargin() - doc.getBottomMargin()) / 3) * sectionNumber,
                pageWidth - doc.getLeftMargin() - doc.getRightMargin(),
                (pageHeight - doc.getTopMargin() - doc.getBottomMargin()) / 3);

        // Simulate the positioning of the renderer to find out how much space the text section will occupy.
        LayoutResult layoutResult = renderer
                .layout(new LayoutContext(new LayoutArea(pageNumber, textSectionRectangle)));

        /* Fill the current page section with the content.
         * If the content isn't fully placed in the current page section,
         * it will be split and drawn in the next page section.
         */
        while (layoutResult.getStatus() != LayoutResult.FULL) {
            if (pdfDoc.getNumberOfPages() < pageNumber) {
                pdfDoc.addNewPage();
            }

            pageNumber++;

            layoutResult.getSplitRenderer().draw(new DrawContext(pdfDoc,
                    new PdfCanvas(pdfDoc.getPage(pageNumber - 1)), false));

            renderer = (ParagraphRenderer) layoutResult.getOverflowRenderer();

            layoutResult = renderer
                    .layout(new LayoutContext(new LayoutArea(pageNumber, textSectionRectangle)));
        }

        if (pdfDoc.getNumberOfPages() < pageNumber) {
            pdfDoc.addNewPage();
        }

        renderer.draw(new DrawContext(pdfDoc, new PdfCanvas(pdfDoc.getPage(pageNumber)), false));
    }

    private static Paragraph readAndCreateParagraph(String path) throws IOException {
        Paragraph p = new Paragraph();
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"))) {
            String line = reader.readLine();
            while (null != line) {
                buffer.append(line);
                line = reader.readLine();
            }
        }

        p.setBorder(new SolidBorder(ColorConstants.RED, 1));
        p.add(buffer.toString());
        return p;
    }
}
