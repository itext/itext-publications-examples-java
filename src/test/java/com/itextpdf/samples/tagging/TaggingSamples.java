/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.tagging;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.CanvasArtifact;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.tagutils.TagTreePointer;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.SampleTest;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

import static org.junit.Assert.fail;

@Category(SampleTest.class)
public class TaggingSamples extends ExtendedITextTest {
    public static final String src = "./src/test/resources/tagging/";
    public static final String dest = "./target/test/resources/tagging/";

    private static final String text1 = "The 88th Academy Awards ceremony, presented by the Academy of Motion Picture Arts and Sciences (AMPAS), " +
            "honored the best films of 2015 and took place on February 28, 2016, at the Dolby Theatre in Hollywood, Los Angeles, beginning " +
            "at 5:30 p.m. PST. During the ceremony, AMPAS presented Academy Awards (commonly referred to as Oscars) in 24 categories. The " +
            "ceremony was televised in the United States by ABC, and produced by David Hill and Reginald Hudlin and directed by Glenn Weiss. " +
            "Actor Chris Rock hosted the show for the second time, having previously hosted the 77th ceremony held in 2005.\n" +
            "In related events, the Academy held its 7th Annual Governors Awards ceremony at the Grand Ballroom of the Hollywood and Highland " +
            "Center on November 14, 2015. On February 13, 2016, in a ceremony at the Beverly Wilshire Hotel in Beverly Hills, California, the " +
            "Academy Awards for Technical Achievement were presented by hosts Olivia Munn and Jason Segel.\n" +
            "Mad Max: Fury Road won six awards, the most for the evening, and Spotlight won two awards including Best Picture. Other winners " +
            "include The Revenant with three awards, and A Girl in the River: The Price of Forgiveness, Amy, Bear Story, The Big Short, Bridge " +
            "of Spies, The Danish Girl, Ex Machina, The Hateful Eight, Inside Out, Room, Son of Saul, Spectre, and Stutterer with one each. The" +
            " telecast garnered more than 34 million viewers in the United States, making it the least watched Oscar ceremony since the 80th Academy Awards in 2008.";
    private static final String text2 = "In related events, the Academy held its 7th Annual Governors Awards ceremony at the Grand Ballroom of the Hollywood and Highland " +
            "Center on November 14, 2015. On February 13, 2016, in a ceremony at the Beverly Wilshire Hotel in Beverly Hills, California, the Academy " +
            "Awards for Technical Achievement were presented by hosts Olivia Munn and Jason Segel.";
    private static final String text3 = "Mad Max: Fury Road won six awards, the most for the evening, and Spotlight won two awards including Best Picture. Other winners include " +
            "The Revenant with three awards, and A Girl in the River: The Price of Forgiveness, Amy, Bear Story, The Big Short, Bridge of Spies, The Danish Girl, " +
            "Ex Machina, The Hateful Eight, Inside Out, Room, Son of Saul, Spectre, and Stutterer with one each. The telecast garnered more than 34 million viewers " +
            "in the United States, making it the least watched Oscar ceremony since the 80th Academy Awards in 2008.";
    private static final String text4 = "The nominees for the 88th Academy Awards were announced on January 14, 2016, at 5:30 a.m. PST " +
            "(13:30 UTC), at the Samuel Goldwyn Theater in Beverly Hills, California, by directors Guillermo del Toro " +
            "and Ang Lee, Academy president Cheryl Boone Isaacs, and actor John Krasinski. The Revenant received the " +
            "most nominations with twelve total, with Mad Max: Fury Road coming in second with ten. For the second consecutive " +
            "year, a film directed by Alejandro G. Iñárritu received the most nominations. Composer Anohni became the second" +
            " transgender person to be nominated for an Oscar. (Angela Morley was the first, in 1974 and 1976.) Sylvester " +
            "Stallone became the sixth person to be nominated for playing the same role in two different films.";
    private static final String text5 = "The winners were announced during the awards ceremony on February 28, 2016. With two Oscars, Spotlight " +
            "was the first film since The Greatest Show on Earth in 1952 to win Best Picture with only one other award. " +
            "Alejandro G. Iñárritu became the only Mexican and third director to win two consecutive Oscars for Best" +
            " Director after John Ford in 1940–1941 and Joseph L. Mankiewicz in 1949–1950, respectively. At the age of 87," +
            " Ennio Morricone became the oldest winner in Oscar history for a competitive award. Having previously won " +
            "for Gravity and Birdman, Emmanuel Lubezki became the first person to win three consecutive Best " +
            "Cinematography awards. Bear Story became the first ever Chilean film to win an Oscar.";

    @BeforeClass
    public static void before() {
        createOrClearDestinationFolder(dest);
    }

    /**
     * Creating a tagged document using layout module.
     */
    @Test
    public void createTaggedDocument() throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest +  "88th_Academy_Awards.pdf"));

        pdfDocument.setTagged(); // mark that this document is tagged

        Document document = new Document(pdfDocument);
        document.setTopMargin(100); // save some space at the beginning of pages for future additions


        // Here we obtain a TagTreePointer which is used internally for auto tagging.
        // In the beginning, this instance points at the root of the tag structure tree.
        // We can move it and as a result all new content will be under the current position of the auto tagging pointer.
        // Auto tagging pointer is also used for tagging annotations and forms, so the same approach could be used there.
        TagTreePointer autoTaggingPointer = pdfDocument.getTagStructureContext().getAutoTaggingPointer();
        autoTaggingPointer.addTag(PdfName.Sect); // create a new tag, which will be a kid of the root element


        // add some content to the page
        Paragraph p = new Paragraph().add(text1).add(text2).add(text3);
        document.add(p);

        Table table = new Table(2);
        table.setWidth(350)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(new Paragraph("Nominations").setBold());
        table.addHeaderCell(new Paragraph("Film").setBold());
        table.addCell("12").addCell("The Revenant");
        table.addCell("10").addCell("Mad Max: Fury Road");
        table.addCell("7").addCell("The Martian");
        table.addCell("6").addCell("Spotlight");
        table.addCell("5").addCell("Star Wars: The Force Awakens");
        table.addCell("4").addCell("Room");
        table.addCell("3").addCell("The Hateful Eight");
        table.addCell("2").addCell("Ex Machina");
        document.add(table);


        // From here we want to create another section of the document.
        autoTaggingPointer
                .moveToParent() // we move the root tag
                .addTag(PdfName.Sect); // and create a new 'Section' tag


        p = new Paragraph(text4);
        p.add(text5);
        document.add(p);

        table = new Table(2);
        table.setWidth(350)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(TextAlignment.CENTER);
        table.addHeaderCell(new Paragraph("Awards").setBold());
        table.addHeaderCell(new Paragraph("Film").setBold());
        table.addCell("6").addCell("Mad Max: Fury Road");
        table.addCell("3").addCell("The Revenant");
        table.addCell("2").addCell("Spotlight");
        document.add(table);

        // Layout element roles could be changed, or set to null if you want to omit it in the tag structure.
        // But be aware that if you set null to the role of the tag which immediate kids are page content items,
        // then this content won't be tagged at all (for example if you set role to null for Text element, then the
        // text on the page won't be tagged too).
        Paragraph caption = new Paragraph().setTextAlignment(TextAlignment.CENTER);
        caption.setRole(null);
        Text captionText = new Text("Table 2, winners");
        captionText.setRole(PdfName.Caption);
        caption.add(captionText);
        document.add(caption);

        // By default, root tag has role of 'Document'. Let's change it to 'Part'.
        autoTaggingPointer
                .moveToRoot() // we move to the root tag (here we also could have used moveToParent method
                .setRole(PdfName.Part); // and change the role of the tag the pointer points to

        document.close();

        // checking that everything worked as expected
        compareResult(dest + "88th_Academy_Awards.pdf", src + "88th_Academy_Awards.pdf");
    }

    /**
     * Tagging manually new content. Modifying the tag structure.
     */
    @Test
    public void addStars() throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        PdfReader reader = new PdfReader(src + "88th_Academy_Awards.pdf");
        PdfWriter writer = new PdfWriter(dest + "88th_Academy_Awards_with_stars.pdf");
        PdfDocument pdfDocument = new PdfDocument(reader, writer);

        PdfPage firstPage = pdfDocument.getFirstPage();
        PdfCanvas canvas = new PdfCanvas(firstPage);

        // The blue star would be something like logo of our document.
        // So for example we don't want it to be read out loud on every page. To achieve it, we mark it as an Artifact.
        canvas.openTag(new CanvasArtifact());
        drawStar(canvas, 30, 745, Color.BLUE);
        canvas.closeTag();

        // The green star we want to be a part of actual content and logical structure of the document.
        // To modify tag structure manually we create TagTreePointer. After creation it points at the root tag.
        TagTreePointer tagPointer = new TagTreePointer(pdfDocument);
        tagPointer.addTag(PdfName.Figure);
        tagPointer.getProperties().setAlternateDescription("The green star.");
        tagPointer.setPageForTagging(firstPage); // it is important to set the page at which new content will be tagged

        canvas.openTag(tagPointer.getTagReference());
        drawStar(canvas, 450, 745, Color.GREEN);
        canvas.closeTag();


        // We can change the position of the existing tags in the tag structure.
        tagPointer.moveToParent();
        TagTreePointer newPositionOfStar = new TagTreePointer(pdfDocument);
        newPositionOfStar.moveToKid(PdfName.Sect);
        int indexOfTheGreenStarTag = 2;
        // tagPointer points at the parent of the green star tag
        tagPointer.relocateKid(indexOfTheGreenStarTag, newPositionOfStar);

        // Using the relocateKid method, we can even change the order of the same parent's kids.
        // This could be used to change for example reading order.
        tagPointer.moveToRoot().moveToKid(PdfName.Sect); // now both tagPointer and newPositionOfStar point at the same tag
        newPositionOfStar.setNextNewKidIndex(0); // next added tag to this tag pointer will be added at the 0 position
        indexOfTheGreenStarTag = 2;
        tagPointer.relocateKid(indexOfTheGreenStarTag, newPositionOfStar);

        pdfDocument.close();

        // checking that everything worked as expected
        compareResult(dest + "88th_Academy_Awards_with_stars.pdf", src + "88th_Academy_Awards_with_stars.pdf");
    }

    /**
     * Marking the whole layout element as artifact.
     */
    @Test
    public void addArtifactTable() throws IOException, InterruptedException, SAXException, ParserConfigurationException {
        PdfReader reader = new PdfReader(src + "88th_Academy_Awards.pdf");
        PdfWriter writer = new PdfWriter(dest + "88th_Academy_Awards_artifact_table.pdf");
        PdfDocument pdfDocument = new PdfDocument(reader, writer);
        Document document = new Document(pdfDocument);

        document.add(new AreaBreak(AreaBreakType.LAST_PAGE));

        Table table = new Table(2);
        table.addCell(new Cell().add("Created as a sample document.").setBorder(null));
        table.addCell(new Cell().add("30.03.2016").setBorder(null));
        table.setFixedPosition(40, 150, 500);

        // This marks the whole table contents as an Artifact.
        // NOTE: Only content that is already added before this call will be marked as Artifact. New content will be tagged, unless you make this call again.
        table.setRole(PdfName.Artifact);
        document.add(table);

        document.close();

        // checking that everything worked as expected
        compareResult(dest + "88th_Academy_Awards_artifact_table.pdf", src + "88th_Academy_Awards_artifact_table.pdf");
    }

    public void drawStar(PdfCanvas canvas, int x, int y, Color color) {
        canvas.setFillColor(color);
        canvas.moveTo(x + 10, y)
                .lineTo(x + 80, y + 60)
                .lineTo(x, y + 60)
                .lineTo(x + 70, y)
                .lineTo(x + 40, y + 90)
                .closePathFillStroke();
    }

    private void compareResult(String outPdf, String cmpPdf)
            throws IOException, InterruptedException, ParserConfigurationException, SAXException {
        CompareTool compareTool = new CompareTool();

        String contentDifferences = compareTool.compareByContent(outPdf,
                cmpPdf, dest, "diff_");
        String taggedStructureDifferences = compareTool.compareTagStructures(outPdf, cmpPdf);

        String errorMessage = "";
        errorMessage += taggedStructureDifferences == null ? "" : taggedStructureDifferences + "\n";
        errorMessage += contentDifferences == null ? "" : contentDifferences;
        if (!errorMessage.isEmpty()) {
            fail(errorMessage);
        }
    }
}
