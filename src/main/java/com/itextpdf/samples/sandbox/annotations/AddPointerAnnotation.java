package com.itextpdf.samples.sandbox.annotations;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLineAnnotation;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import java.io.File;

public class AddPointerAnnotation {
    public static final String DEST = "./target/sandbox/annotations/add_pointer_annotation.pdf";

    public static final String IMG = "./src/main/resources/img/map_cic.png";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddPointerAnnotation().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Image img = new Image(ImageDataFactory.create(IMG));
        Document doc = new Document(pdfDoc, new PageSize(img.getImageWidth(), img.getImageHeight()));
        img.setFixedPosition(0, 0);
        doc.add(img);

        Rectangle rect = new Rectangle(220, 350, 255, 245);
        PdfLineAnnotation lineAnnotation = new PdfLineAnnotation(rect,
                new float[] {220 + 5, 350 + 5, 220 + 255 - 5, 350 + 245 - 5});
        lineAnnotation.setTitle(new PdfString("You are here:"));

        // This method sets the text that will be displayed for the annotation or the alternate description,
        // if this type of annotation does not display text.
        lineAnnotation.setContents("Cambridge Innovation Center");
        lineAnnotation.setColor(ColorConstants.RED);

        // Set to print the annotation when the page is printed
        lineAnnotation.setFlag(PdfAnnotation.PRINT);

        // Set arrow's border style
        PdfDictionary borderStyle = new PdfDictionary();
        borderStyle.put(PdfName.S, PdfName.S);
        borderStyle.put(PdfName.W, new PdfNumber(5));
        lineAnnotation.setBorderStyle(borderStyle);

        PdfArray le = new PdfArray();
        le.add(PdfName.OpenArrow);
        le.add(PdfName.None);
        lineAnnotation.put(PdfName.LE, le);
        lineAnnotation.put(PdfName.IT, PdfName.LineArrow);

        pdfDoc.getFirstPage().addAnnotation(lineAnnotation);

        doc.close();
    }
}
