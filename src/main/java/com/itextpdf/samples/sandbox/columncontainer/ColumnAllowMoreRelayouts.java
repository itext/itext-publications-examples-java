/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2024 Apryse Group NV
    Authors: Apryse Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.columncontainer;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.MulticolContainer;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.MulticolRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ColumnAllowMoreRelayouts {

    public static final String DEST = "./target/sandbox/columncontainer/allow_more_re_layouts.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ColumnAllowMoreRelayouts().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        // Add a flowing paragraph
        MulticolContainer container = new MulticolContainer();
        container.setProperty(Property.COLUMN_COUNT, 3);
        container.setNextRenderer(new MultiColRendererAllow10RetriesRenderer(container));
        Paragraph div = new Paragraph(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas malesuada vitae mi sed fermentum. "
                        + "Vestibulum magna orci, iaculis vitae rutrum vitae, bibendum eget urna. Aenean quis metus "
                        + "diam. Vivamus et lobortis nunc. Cras aliquet placerat diam ac pellentesque. Quisque "
                        + "pulvinar velit auctor, luctus mi et, interdum quam. Curabitur ac tristique libero. "
                        + "Vestibulum dictum ipsum sit amet iaculis efficitur. Vestibulum convallis, odio a varius "
                        + "efficitur, sapien nulla convallis arcu, vel interdum est odio nec nibh. Duis euismod a "
                        + "quam et blandit."
                        +
                        "Nulla dapibus pretium lectus, a dictum diam scelerisque ut. Mauris sit amet ipsum urna. Duis"
                        + " lacinia mi sem, non consectetur mi fermentum in. Aenean volutpat convallis eros tristique"
                        + " dignissim. Etiam tincidunt tortor nec arcu ultrices, vel suscipit sapien posuere. Cras "
                        + "aliquet velit in lacus tempor maximus. Fusce suscipit ex ut nisi aliquam scelerisque. Duis"
                        + " feugiat ultrices elementum. Morbi eget metus ut ligula ullamcorper interdum sed finibus "
                        + "justo."
                        +
                        "Nunc dictum ac eros ac porttitor. In viverra ex tortor. Sed ipsum sem, finibus at gravida "
                        + "sit amet, ultrices et odio. Maecenas posuere imperdiet lectus in aliquet. Nunc at nunc "
                        + "eget dui elementum posuere. In vitae venenatis neque. Vestibulum semper eu est in faucibus."
                        +
                        "Aliquam tellus arcu, suscipit a lacus ut, cursus aliquam lacus. Suspendisse mi urna, cursus "
                        + "eu gravida et, accumsan vitae nisl. Duis eget magna posuere, sodales enim id, ornare arcu."

        );
        container.add(div);

        doc.add(container);
        doc.close();
    }

    public static class MultiColRendererAllow10RetriesRenderer extends MulticolRenderer {

        /**
         * Creates a DivRenderer from its corresponding layout object.
         *
         * @param modelElement the {@link MulticolContainer} which this object should manage
         */
        public MultiColRendererAllow10RetriesRenderer(MulticolContainer modelElement) {
            super(modelElement);
            setHeightCalculator(new LayoutInInfiniteHeightCalculator());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public IRenderer getNextRenderer() {
            return new MultiColRendererAllow10RetriesRenderer((MulticolContainer) modelElement);
        }
    }

    public static class LayoutInInfiniteHeightCalculator extends MulticolRenderer.LayoutInInfiniteHeightCalculator {

        public LayoutInInfiniteHeightCalculator() {
            super();
            maxRelayoutCount = 10;
        }
    }
}
