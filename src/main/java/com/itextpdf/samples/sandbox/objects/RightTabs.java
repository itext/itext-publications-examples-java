package com.itextpdf.samples.sandbox.objects;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

public class RightTabs {
    public static final String DEST = "./target/sandbox/objects/right_tabs.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new RightTabs().manipulatePDf(DEST);
    }

    public void manipulatePDf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDoc);
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

        List<TabStop> tabStops = new ArrayList<TabStop>();
        tabStops.add(new TabStop(width, TabAlignment.RIGHT));

        Paragraph paragraph = new Paragraph()
                .addTabStops(tabStops)
                .add("ABCD")
                .add(new Tab())
                .add("EFGH");
        document.add(paragraph);

        paragraph = new Paragraph()
                .addTabStops(tabStops)
                .add("Text to the left")
                .add(new Tab())
                .add("Text to the right");
        document.add(paragraph);

        paragraph = new Paragraph()
                .addTabStops(tabStops)
                .add("01234")
                .add(new Tab())
                .add("56789");
        document.add(paragraph);

        paragraph = new Paragraph()
                .addTabStops(tabStops)
                .add("iText 5 is old")
                .add(new Tab())
                .add("iText 7 is new");
        document.add(paragraph);

        document.close();
    }
}
