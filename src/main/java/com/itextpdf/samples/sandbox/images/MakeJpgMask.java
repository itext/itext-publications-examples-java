package com.itextpdf.samples.sandbox.images;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MakeJpgMask {
    public static final String DEST = "./target/sandbox/images/make_jpg_mask.pdf";

    public static final String IMAGE = "./src/main/resources/img/javaone2013.jpg";
    public static final String MASK = "./src/main/resources/img/berlin2013.jpg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new MakeJpgMask().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = PageSize.A4.rotate();
        Document doc = new Document(pdfDoc, pageSize);

        ImageData image = ImageDataFactory.create(IMAGE);
        ImageData mask = convertToBlackAndWhitePng(MASK);
        mask.makeMask();
        image.setImageMask(mask);

        Image img = new Image(image);
        img.scaleAbsolute(pageSize.getWidth(), pageSize.getHeight());
        img.setFixedPosition(0, 0);
        doc.add(img);

        doc.close();
    }

    private static ImageData convertToBlackAndWhitePng(String image) throws IOException {
        BufferedImage bi = ImageIO.read(new File(image));
        BufferedImage newBi = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
        newBi.getGraphics().drawImage(bi, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(newBi, "png", baos);
        return ImageDataFactory.create(baos.toByteArray());
    }
}
