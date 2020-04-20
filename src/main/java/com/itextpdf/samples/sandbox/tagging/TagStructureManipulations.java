package com.itextpdf.samples.sandbox.tagging;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;

import java.io.File;

public class TagStructureManipulations {
    public static final String DEST = "./target/sandbox/tagging/88th_Academy_Awards_with_stars.pdf";
    public static final String SRC = "./src/main/resources/tagging/88th_Academy_Awards.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new TagStructureManipulations().manipulatePdf(SRC, DEST);
    }

    protected void manipulatePdf(String src, String dest) throws Exception {
        PdfReader reader = new PdfReader(src);
        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        PdfPage firstPage = pdfDocument.getFirstPage();
        PdfCanvas canvas = new PdfCanvas(firstPage);

        // The blue star would be something like logo of our document.
        // So for example we don't want it to be read out loud on every page. To achieve it, we mark it as an Artifact.
        canvas.openTag(new CanvasArtifact());
        drawStar(canvas, 30, 745, ColorConstants.BLUE);
        canvas.closeTag();

        // The green star we want to be a part of actual content and logical structure of the document.
        // To modify tag structure manually we create TagTreePointer. After creation it points at the root tag.
        TagTreePointer tagPointer = new TagTreePointer(pdfDocument);
        tagPointer.addTag(StandardRoles.FIGURE);
        tagPointer.getProperties().setAlternateDescription("The green star.");

        // It is important to set the page at which new content will be tagged
        tagPointer.setPageForTagging(firstPage);

        canvas.openTag(tagPointer.getTagReference());
        drawStar(canvas, 450, 745, ColorConstants.GREEN);
        canvas.closeTag();

        // We can change the position of the existing tags in the tag structure.
        tagPointer.moveToParent();
        TagTreePointer newPositionOfStar = new TagTreePointer(pdfDocument);
        newPositionOfStar.moveToKid(StandardRoles.SECT);
        int indexOfTheGreenStarTag = 2;

        // tagPointer points at the parent of the green star tag
        tagPointer.relocateKid(indexOfTheGreenStarTag, newPositionOfStar);

        // Using the relocateKid method, we can even change the order of the same parent's kids.
        // This could be used to change for example reading order.

        // Make both tagPointer and newPositionOfStar to point at the same tag
        tagPointer.moveToRoot().moveToKid(StandardRoles.SECT);


        // Next added tag to this tag pointer will be added at the 0 position
        newPositionOfStar.setNextNewKidIndex(0);
        indexOfTheGreenStarTag = 2;
        tagPointer.relocateKid(indexOfTheGreenStarTag, newPositionOfStar);

        pdfDocument.close();
    }

    private void drawStar(PdfCanvas canvas, int x, int y, Color color) {
        canvas.setFillColor(color);
        canvas
                .moveTo(x + 10, y)
                .lineTo(x + 80, y + 60)
                .lineTo(x, y + 60)
                .lineTo(x + 70, y)
                .lineTo(x + 40, y + 90)
                .closePathFillStroke();
    }
}
