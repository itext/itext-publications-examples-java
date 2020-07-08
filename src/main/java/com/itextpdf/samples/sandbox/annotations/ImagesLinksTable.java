package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.ImageRenderer;

import java.io.File;

public class ImagesLinksTable {
    public static final String DEST = "./target/sandbox/annotations/images_links_table.pdf";

    public static final String IMG = "./src/main/resources/img/info.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ImagesLinksTable().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image img = new Image(ImageDataFactory.create(IMG));
        Paragraph anchor = new Paragraph().add(img);
        anchor.setProperty(Property.ACTION, PdfAction.createURI("https://lowagie.com/"));

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table.addCell(anchor);
        table.addCell("A");
        table.addCell("B");
        table.addCell("C");

        img = new Image(ImageDataFactory.create(IMG));
        img.setNextRenderer(new LinkImageRenderer(img));
        table.addCell(img);

        doc.add(table);

        doc.close();
    }

    protected class LinkImageRenderer extends ImageRenderer {
        public LinkImageRenderer(Image image) {
            super(image);
        }

        @Override
        public IRenderer getNextRenderer() {
            return new LinkImageRenderer((Image) modelElement);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            PdfAnnotation annotation = new PdfLinkAnnotation(getOccupiedAreaBBox())
                    .setAction(PdfAction.createURI("https://lowagie.com/bio"));
            drawContext.getDocument().getLastPage().addAnnotation(annotation);
        }
    }
}
