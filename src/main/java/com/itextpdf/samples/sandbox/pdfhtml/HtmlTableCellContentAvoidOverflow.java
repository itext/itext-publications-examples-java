package com.itextpdf.samples.sandbox.pdfhtml;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.css.apply.impl.TdTagCssApplier;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.property.OverflowPropertyValue;
import com.itextpdf.layout.property.Property;
import com.itextpdf.styledxmlparser.node.IElementNode;
import com.itextpdf.styledxmlparser.node.IStylesContainer;

import java.io.File;
import java.io.IOException;


public class HtmlTableCellContentAvoidOverflow {
        public static final String DEST = "./target/sandbox/pdfhtml/htmlTableCellContentAvoidOverflow.pdf";
        public static final String HTML = "<table style='width: 100%; table-layout:fixed;'>\n"
            + "    <tbody>\n"
            + "    <tr>\n"
            + "        <td><b>Hamlet</b><br/>\n"
            + "            <table style='width: 100%; border: 0.1mm solid; border-collapse:collapse;table-layout:fixed;'>\n"
            + "                <tbody>\n"
            + "                <tr style='text-align:center;font-weight:bold; border: 0.5px solid black;'>\n"
            + "                    <td style='border: 0.5px solid black;width:13%;'>To be, or not to be</td>\n"
            + "                    <td style='border: 0.5px solid black;width:13%; '> that is the queeeeeeeeeeeestion</td>\n"
            + "                    <td style='border: 0.5px solid black;width:16%'>Whether 'tis nobler</td>\n"
            + "                    <td style='border: 0.5px solid black;width:16%'>in the mind to suffer</td>\n"
            + "                    <td style='border: 0.5px solid black;width:16%'>Or to take aaaaaaaaarms</td>\n"
            + "                    <td style='border: 0.5px solid black;width:13%'>agaaaaaaaaaaainst a sea of troubles,</td>\n"
            + "                    <td style='border: 0.5px solid black;width:10%'>And by opposing end them<br>?</td>\n"
            + "                </tr>\n"
            + "                <tr style='border: 0.5px solid black;'>\n"
            + "                    <td style='border: 0.5px solid black;'>To die: </td>\n"
            + "                    <td style='border: 0.5px solid black;'>to sleeeeeeeeeeeeeep</td>\n"
            + "                    <td style='border: 0.5px solid black;'>No more; and by a sleep</td>\n"
            + "                    <td style='border: 0.5px solid black;'>to say we end</td>\n"
            + "                    <td style='border: 0.5px solid black;'>The heart-ache</td>\n"
            + "                    <td style='border: 0.5px solid black;'>and the thousand naaaaaaaaaaaaaaaaatural shocks</td>\n"
            + "                    <td style='border: 0.5px solid black;text-align:right;'>That flesh is heir to</td>\n"
            + "                </tr>\n"
            + "                </tbody>\n"
            + "            </table>\n"
            + "            <br></td>\n"
            + "    </tr>\n"
            + "    </tbody>\n"
            + "</table>";

        public static void main(String[] args) throws IOException {
            File file = new File(DEST);
            file.getParentFile().mkdirs();

            new HtmlTableCellContentAvoidOverflow().manipulatePdf(HTML, DEST);
        }

        public void manipulatePdf(String html, String pdfDest) throws IOException {
            PdfDocument document = new PdfDocument(new PdfWriter(pdfDest));
            document.addNewPage();

            DefaultCssApplierFactory cssApplierFactory = new CellTagCssApplierFactory();

            ConverterProperties converterProperties = new ConverterProperties();
            // Using custom css applier with OverflowPropertyValue.FIT set,
            // we can achieve that text content of table's cell suits their width
            converterProperties.setCssApplierFactory(cssApplierFactory);

            HtmlConverter.convertToPdf(html, document, converterProperties);
        }

        private static class CellTagCssApplierFactory extends DefaultCssApplierFactory {
            @Override
            public ICssApplier getCustomCssApplier(IElementNode tag) {
                // Custom css applier works only for 'td' html element
                if (tag.name().equals("td")) {
                    return new CellCssApplier();
                }
                return null;
            }
        }

        private static class CellCssApplier extends TdTagCssApplier {
            @Override
            public void apply(ProcessorContext context, IStylesContainer stylesContainer, ITagWorker tagWorker) {
                super.apply(context, stylesContainer, tagWorker);
                IPropertyContainer container = tagWorker.getElementResult();
                if (container != null && container instanceof Cell) {
                    Cell cell = (Cell) container;
                    for (IElement element : cell.getChildren()) {
                        element.setProperty(Property.OVERFLOW_X, OverflowPropertyValue.FIT);
                        element.setProperty(Property.OVERFLOW_Y, OverflowPropertyValue.FIT);
                    }
                }
            }
        }

}
