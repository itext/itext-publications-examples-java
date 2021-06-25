package com.itextpdf.samples.sandbox.events;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TextRenderer;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class CreateTOCinColumn {
    public static final String DEST = "./target/sandbox/events/create_toc_in_column.pdf";

    private static List<AbstractMap.SimpleEntry<String, PdfDestination>> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new CreateTOCinColumn().manipulatePdf(DEST);

    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Rectangle[] columns = {
                new Rectangle(36, 36, 173, 770),
                new Rectangle(213, 36, 173, 770),
                new Rectangle(389, 36, 173, 770)
        };

        doc.setRenderer(new ColumnDocumentRenderer(doc, columns));
        PdfOutline root = pdfDoc.getOutlines(false);
        for (int i = 0; i <= 20; i++) {
            int start = (i * 10) + 1;
            int end = (i + 1) * 10;
            String title = String.format("Numbers from %s to %s", start, end);
            Text c = new Text(title);
            TOCTextRenderer renderer = new TOCTextRenderer(c);
            renderer.setRoot(root);
            c.setNextRenderer(renderer);
            doc.add(new Paragraph(c));
            doc.add(createTable(start, end));
        }

        doc.add(new AreaBreak());
        for (AbstractMap.SimpleEntry<String, PdfDestination> entry : list) {
            Link c = new Link(entry.getKey(), entry.getValue());
            doc.add(new Paragraph(c));
        }

        doc.close();
    }

    private static Table createTable(int start, int end) {
        Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        for (int i = start; i <= end; i++) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i))));
            table.addCell(new Cell().add(new Paragraph("Test")));
        }
        return table;
    }

    private static class TOCTextRenderer extends TextRenderer {
        protected PdfOutline root;

        public TOCTextRenderer(Text modelElement) {
            super(modelElement);
        }

        public void setRoot(PdfOutline root) {
            this.root = root;
        }

        // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
        // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new TOCTextRenderer((Text) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            PdfPage page = drawContext.getDocument().getPage(getOccupiedArea().getPageNumber());
            PdfDestination dest = PdfExplicitDestination.createXYZ(page, rect.getLeft(), rect.getTop(), 0);

            list.add(new AbstractMap.SimpleEntry<String, PdfDestination>(((Text) modelElement).getText(), dest));

            PdfOutline curOutline = root.addOutline(((Text) modelElement).getText());
            curOutline.addDestination(dest);
        }
    }
}
