package com.itextpdf.samples.sandbox.graphics;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.gradients.AbstractLinearGradientBuilder;
import com.itextpdf.kernel.colors.gradients.GradientColorStop;
import com.itextpdf.kernel.colors.gradients.LinearGradientBuilder;
import com.itextpdf.kernel.colors.gradients.StrategyBasedLinearGradientBuilder;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BackgroundImage;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;

public class LinearGradientsInLayout {
    public static final String DEST = "./target/sandbox/graphics/linearGradientsInLayout.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new LinearGradientsInLayout().manipulatePdf();
    }

    protected void manipulatePdf() throws Exception {
        Document doc = new Document(new PdfDocument(new PdfWriter(DEST)));

        addLinearGradientAsElementBackground(doc);
        createColorBasedOnAbsolutelyPositionedLinearGradient(doc);

        doc.close();
    }

    private void addLinearGradientAsElementBackground(Document doc) {
        doc.add(new Paragraph("The \"addLinearGradientAsElementBackground\" starts here."));

        AbstractLinearGradientBuilder gradientBuilder = new StrategyBasedLinearGradientBuilder()
                .addColorStop(new GradientColorStop(ColorConstants.RED.getColorValue()))
                .addColorStop(new GradientColorStop(ColorConstants.GREEN.getColorValue()))
                .addColorStop(new GradientColorStop(ColorConstants.BLUE.getColorValue()));

        BackgroundImage backgroundImage = new BackgroundImage.Builder()
                .setLinearGradientBuilder(gradientBuilder).build();

        if (backgroundImage.isBackgroundSpecified()) {
            String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                    "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                    "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
                    "officia deserunt mollit anim id est laborum. ";

            Div div = new Div().add(new Paragraph(text + text + text));
            div.setProperty(Property.BACKGROUND_IMAGE, backgroundImage);
            doc.add(div);

        } else {
            System.out.println("There are no linear gradient elements present within the passing image.");
        }
    }

    private void createColorBasedOnAbsolutelyPositionedLinearGradient(Document doc){

        // The below such linear gradient spans across the whole page and therefore color created from it will be
        // different based at the location of the page.
        AbstractLinearGradientBuilder gradientBuilder = new LinearGradientBuilder()
                .setGradientVector(PageSize.A4.getLeft(), PageSize.A4.getBottom(), PageSize.A4.getRight(), PageSize.A4.getTop())
                .addColorStop(new GradientColorStop(ColorConstants.RED.getColorValue()))
                .addColorStop(new GradientColorStop(ColorConstants.PINK.getColorValue()))
                .addColorStop(new GradientColorStop(ColorConstants.BLUE.getColorValue()));

        Color gradientColor = gradientBuilder.buildColor(PageSize.A4.clone(), null, doc.getPdfDocument());

        doc.add(new Paragraph("The \"createColorBasedOnAbsolutelyPositionedLinearGradient\" starts here.").setFontColor(gradientColor));
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In dapibus aliquam quam. Aliquam at tincidunt mauris. "
                + "Curabitur mollis leo venenatis diam bibendum consectetur. Etiam at lacus ultricies, vulputate dui nec, mattis ex. "
                + "Sed quis leo in purus consectetur sodales. Nam sit amet felis orci. Aliquam non lacus ut nisi hendrerit sollicitudin "
                + "a at ligula. Vivamus condimentum vehicula nulla a blandit. In sit amet ex hendrerit augue iaculis consectetur. "
                + "Etiam semper risus pulvinar, faucibus ex eu, tristique felis. Integer ullamcorper ipsum ac nisi vulputate malesuada."
                + "Nunc sit amet ipsum sollicitudin, consequat lectus ac, finibus erat. Vivamus malesuada a leo vel consequat."
                + "Maecenas ac blandit velit, at eleifend ipsum. Praesent dui orci, molestie a semper eu, varius nec augue. "
                + "Ut vehicula libero ligula, id tristique nisi convallis et. Curabitur nec velit ut nisi commodo rhoncus "
                + "non eu ipsum. Pellentesque eget mauris ex. Nullam et lectus et eros sollicitudin tincidunt. Phasellus "
                + "commodo erat nec diam consectetur elementum. Cras pellentesque commodo est, vel viverra nisi "
                + "vulputate ac. Curabitur interdum nulla at viverra varius. Donec porttitor erat lacus, ac efficitur "
                + "arcu malesuada dignissim. Aenean pretium ex tortor, a porttitor quam mollis vitae. Etiam id nibh dolor."
                + " Curabitur vel ligula tortor. Etiam vestibulum velit neque, a mattis tortor vehicula sed. Sed sit amet "
                + "ipsum leo. Mauris et tincidunt ex. Donec vehicula, magna eget convallis suscipit, nisi tellus ullamcorper "
                + "massa, eu commodo lectus massa ac orci. Fusce nec gravida justo, ac lacinia metus. Etiam porttitor "
                + "massa odio, vestibulum semper ipsum tristique eget. Donec semper elit nibh, tristique placerat arcu "
                + "egestas et. Pellentesque leo metus, sodales in nisi ut, condimentum dignissim eros. Duis maximus eu "
                + "mi faucibus mattis. Quisque leo urna, hendrerit eu finibus nec, ullamcorper at nunc.";
        Paragraph paragraph = new Paragraph(text).setBorder(new SolidBorder(gradientColor, 5));

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        Cell cell = new Cell().add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit."));
        table.addCell(cell);
        table.setBorder(new SolidBorder(gradientColor, 9));

        doc
                .add(paragraph)
                .add(table);
    }
}
