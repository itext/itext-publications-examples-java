package com.itextpdf.samples.sandbox.tables;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.File;

public class ImagesInChunkInCell {
    public static final String DEST = "./target/sandbox/tables/images_in_chunk_in_cell.pdf";

    public static final String IMG = "./src/main/resources/img/bulb.gif";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new ImagesInChunkInCell().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Image image = new Image(ImageDataFactory.create(IMG));
        Table table = new Table(new float[] {120});
        Paragraph listOfDots = new Paragraph();

        for (int i = 0; i < 40; i++) {
            listOfDots.add(image);
            listOfDots.add(new Text(" "));
        }

        table.addCell(listOfDots);

        doc.add(table);

        doc.close();
    }
}
