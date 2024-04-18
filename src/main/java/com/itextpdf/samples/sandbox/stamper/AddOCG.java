package com.itextpdf.samples.sandbox.stamper;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.layer.PdfLayer;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddOCG {
    public static final String DEST = "./target/sandbox/stamper/add_ocg.pdf";
    public static final String SRC = "./src/main/resources/pdfs/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new AddOCG().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC), new PdfWriter(dest));
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());

        PdfLayer nested = new PdfLayer("Nested layers", pdfDoc);
        PdfLayer nested1 = new PdfLayer("Nested layer 1", pdfDoc);
        PdfLayer nested2 = new PdfLayer("Nested layer 2", pdfDoc);
        nested2.setLocked(true);
        nested.addChild(nested1);
        nested.addChild(nested2);

        canvas.beginLayer(nested);
        Canvas canvasModel = new Canvas(canvas, pdfDoc.getDefaultPageSize());
        canvasModel.showTextAligned("nested layers", 50, 765, TextAlignment.LEFT, 0);
        canvas.endLayer();

        canvas.beginLayer(nested1);
        canvasModel.showTextAligned("nested layers 1", 100, 800, TextAlignment.LEFT, 0);
        canvas.endLayer();

        canvas.beginLayer(nested2);
        canvasModel.showTextAligned("nested layers 2", 100, 750, TextAlignment.LEFT, 0);
        canvas.endLayer();

        PdfLayer group = PdfLayer.createTitle("Grouped layers", pdfDoc);
        PdfLayer layer1 = new PdfLayer("Group: layer 1", pdfDoc);
        PdfLayer layer2 = new PdfLayer("Group: layer 2", pdfDoc);
        group.addChild(layer1);
        group.addChild(layer2);

        canvas.beginLayer(layer1);
        canvasModel.showTextAligned("layer 1 in the group", 50, 700, TextAlignment.LEFT, 0);
        canvas.endLayer();

        canvas.beginLayer(layer2);
        canvasModel.showTextAligned("layer 2 in the group", 50, 675, TextAlignment.LEFT, 0);
        canvas.endLayer();

        PdfLayer radiogroup = PdfLayer.createTitle("Radio group", pdfDoc);
        PdfLayer radio1 = new PdfLayer("Radiogroup: layer 1", pdfDoc);
        radio1.setOn(true);
        PdfLayer radio2 = new PdfLayer("Radiogroup: layer 2", pdfDoc);
        radio2.setOn(false);
        PdfLayer radio3 = new PdfLayer("Radiogroup: layer 3", pdfDoc);
        radio3.setOn(false);
        radiogroup.addChild(radio1);
        radiogroup.addChild(radio2);
        radiogroup.addChild(radio3);
        List<PdfLayer> options = new ArrayList<>();
        options.add(radio1);
        options.add(radio2);
        options.add(radio3);
        PdfLayer.addOCGRadioGroup(pdfDoc, options);

        canvas.beginLayer(radio1);
        canvasModel.showTextAligned("option 1", 50, 600, TextAlignment.LEFT, 0);
        canvas.endLayer();

        canvas.beginLayer(radio2);
        canvasModel.showTextAligned("option 2", 50, 575, TextAlignment.LEFT, 0);
        canvas.endLayer();

        canvas.beginLayer(radio3);
        canvasModel.showTextAligned("option 3", 50, 550, TextAlignment.LEFT, 0);
        canvas.endLayer();

        PdfLayer not_printed = new PdfLayer("not printed", pdfDoc);
        not_printed.setOnPanel(false);
        not_printed.setPrint("Print", false);

        canvas.beginLayer(not_printed);
        canvasModel.showTextAligned("PRINT THIS PAGE", 300, 700,
                TextAlignment.CENTER, (float) Math.toRadians(90));
        canvas.endLayer();

        PdfLayer zoom = new PdfLayer("Zoom 0.75-1.25", pdfDoc);
        zoom.setOnPanel(false);
        zoom.setZoom(0.75f, 1.25f);

        canvas.beginLayer(zoom);
        canvasModel
                .showTextAligned("Only visible if the zoomfactor is between 75 and 125%",
                        30, 530, TextAlignment.LEFT, (float) Math.toRadians(90));
        canvas.endLayer();

        pdfDoc.close();
    }
}
