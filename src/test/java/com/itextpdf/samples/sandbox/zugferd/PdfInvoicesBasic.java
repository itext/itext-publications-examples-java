/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

package com.itextpdf.samples.sandbox.zugferd;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.samples.sandbox.zugferd.data.InvoiceData;
import com.itextpdf.samples.sandbox.zugferd.pojo.Invoice;
import com.itextpdf.samples.sandbox.zugferd.pojo.Item;
import com.itextpdf.samples.sandbox.zugferd.pojo.PojoFactory;
import com.itextpdf.samples.sandbox.zugferd.pojo.Product;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.zugferd.InvoiceDOM;
import com.itextpdf.zugferd.ZugferdDocument;
import com.itextpdf.zugferd.ZugferdXMPUtil;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.profiles.IBasicProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

/**
 * Reads invoice data from a test database and creates ZUGFeRD invoices
 * (Basic profile).
 *
 * @author Bruno Lowagie
 */
@Category(SampleTest.class)
public class PdfInvoicesBasic extends GenericTest {
    // Since all the document are technically almost the same
    // we will check only the first one
    public static final String DEST = "./target/test/resources/zugferd/pdfa/basic00000.pdf";

    public static final String DEST_PATTERN = "./target/test/resources/zugferd/pdfa/basic%05d.pdf";
    public static final String ICC = "./src/test/resources/data/sRGB_CS_profile.icm";
    public static final String FONT = "./src/test/resources/font/OpenSans-Regular.ttf";
    public static final String FONTB = "./src/test/resources/font/OpenSans-Bold.ttf";

    protected PdfFont font;
    protected PdfFont fontb;

    public static void main(String[] args) throws IOException, ParserConfigurationException, SQLException, SAXException, TransformerException, XMPException, ParseException, DataIncompleteException, InvalidCodeException {
        new PdfInvoicesBasic().manipulatePdf(DEST_PATTERN);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, ParserConfigurationException, SQLException, SAXException, TransformerException, XMPException, ParseException, DataIncompleteException, InvalidCodeException {
        Locale.setDefault(Locale.ENGLISH);
        File file = new File(DEST_PATTERN);
        file.getParentFile().mkdirs();
        PdfInvoicesBasic app = new PdfInvoicesBasic();
        PojoFactory factory = PojoFactory.getInstance();
        List<Invoice> invoices = factory.getInvoices();
        for (Invoice invoice : invoices) {
            app.createPdf(invoice);
        }
        factory.close();
    }

    public void createPdf(Invoice invoice) throws ParserConfigurationException, SAXException, TransformerException, IOException, XMPException, ParseException, DataIncompleteException, InvalidCodeException {
        font = PdfFontFactory.createFont(FONT, PdfEncodings.WINANSI, true);
        fontb = PdfFontFactory.createFont(FONTB, PdfEncodings.WINANSI, true);

        String dest = String.format(DEST_PATTERN, invoice.getId());
        InvoiceData invoiceData = new InvoiceData();
        IBasicProfile basic = invoiceData.createBasicProfileData(invoice);

        FileInputStream is = new FileInputStream(ICC);
        ZugferdDocument pdfDoc = new ZugferdDocument(new PdfWriter(dest),
                new PdfOutputIntent("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", is));
        Document document = new Document(pdfDoc, new PageSize(PageSize.A4));

        XMPMeta xmp = XMPMetaFactory.parseFromBuffer(pdfDoc.getXmpMetadata(true));
        xmp.setProperty(ZugferdXMPUtil.ZUGFERD_SCHEMA_NS, ZugferdXMPUtil.ZUGFERD_DOCUMENT_FILE_NAME, "ZUGFeRD-invoice.xml");
        pdfDoc.setXmpMetadata(xmp);

        // header
        Paragraph p;
        p = new Paragraph(basic.getName() + " " + basic.getId()).setFont(font).setFontSize(14);
        p.setTextAlignment(TextAlignment.RIGHT);
        document.add(p);
        p = new Paragraph(convertDate(basic.getDateTime(), "MMM dd, yyyy")).setFont(font).setFontSize(12);
        p.setTextAlignment(TextAlignment.RIGHT);
        document.add(p);

        // Address seller / buyer
        Table table = new Table(2);
        table.setWidthPercent(100);
        Cell seller = getPartyAddress("From:",
                basic.getSellerName(),
                basic.getSellerLineOne(),
                basic.getSellerLineTwo(),
                basic.getSellerCountryID(),
                basic.getSellerPostcode(),
                basic.getSellerCityName());
        table.addCell(seller);
        Cell buyer = getPartyAddress("To:",
                basic.getBuyerName(),
                basic.getBuyerLineOne(),
                basic.getBuyerLineTwo(),
                basic.getBuyerCountryID(),
                basic.getBuyerPostcode(),
                basic.getBuyerCityName());
        table.addCell(buyer);
        seller = getPartyTax(basic.getSellerTaxRegistrationID(),
                basic.getSellerTaxRegistrationSchemeID());
        table.addCell(seller);
        buyer = getPartyTax(basic.getBuyerTaxRegistrationID(),
                basic.getBuyerTaxRegistrationSchemeID());
        table.addCell(buyer);
        document.add(table);

        // line items
        table = new Table(new float[]{7, 2, 1, 2, 2, 2});
        table.setWidthPercent(100);
        table.setMarginTop(10);
        table.setMarginBottom(10);
        table.addCell(getCell("Item:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Price:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Qty:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Subtotal:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("VAT:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Total:", TextAlignment.LEFT, fontb, 12));
        Product product;
        for (Item item : invoice.getItems()) {
            product = item.getProduct();
            table.addCell(getCell(product.getName(), TextAlignment.LEFT, font, 12));
            table.addCell(getCell(InvoiceData.format2dec(InvoiceData.round(product.getPrice())), TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(String.valueOf(item.getQuantity()), TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(InvoiceData.format2dec(InvoiceData.round(item.getCost())), TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(InvoiceData.format2dec(InvoiceData.round(product.getVat())), TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(
                    InvoiceData.format2dec(InvoiceData.round(item.getCost() + ((item.getCost() * product.getVat()) / 100))),
                    TextAlignment.RIGHT, font, 12));
        }
        document.add(table);

        // grand totals
        document.add(getTotalsTable(
                basic.getTaxBasisTotalAmount(), basic.getTaxTotalAmount(), basic.getGrandTotalAmount(), basic.getGrandTotalAmountCurrencyID(),
                basic.getTaxTypeCode(), basic.getTaxApplicablePercent(),
                basic.getTaxBasisAmount(), basic.getTaxCalculatedAmount(), basic.getTaxCalculatedAmountCurrencyID()));

        // payment info
        document.add(getPaymentInfo(basic.getPaymentReference(), basic.getPaymentMeansPayeeFinancialInstitutionBIC(), basic.getPaymentMeansPayeeAccountIBAN()));

        // XML version
        InvoiceDOM dom = new InvoiceDOM(basic);
        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());
        // platform-independent newlines
        byte[] xml = new String(dom.toXML()).replace("\r\n", "\n").getBytes();
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, xml, "ZUGFeRD invoice", "ZUGFeRD-invoice.xml",
                new PdfName("application/xml"), parameters, PdfName.Alternative, false);
        pdfDoc.addFileAttachment("ZUGFeRD invoice", fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getPdfObject().getIndirectReference());
        pdfDoc.getCatalog().put(PdfName.AF, array);

        document.close();
    }

    public Cell getPartyAddress(String who, String name, String line1, String line2, String countryID, String postcode, String city) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        if (null != who) {
            cell.add(new Paragraph(who).setFont(fontb).setFontSize(12));
        }
        if (null != name) {
            cell.add(new Paragraph(name).setFont(font).setFontSize(12));
        }
        if (null != line1) {
            cell.add(new Paragraph(line1).setFont(font).setFontSize(12));
        }
        if (null != line2) {
            cell.add(new Paragraph(line2).setFont(font).setFontSize(12));
        }
        if (null != countryID && null != postcode && null != city) {
            cell.add(new Paragraph(String.format("%s-%s %s", countryID, postcode, city)).setFont(font).setFontSize(12));
        }
        return cell;
    }

    public Cell getPartyTax(String[] taxId, String[] taxSchema) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(new Paragraph("Tax ID(s):").setFont(fontb).setFontSize(10));
        if (taxId.length == 0) {
            cell.add(new Paragraph("Not applicable").setFont(font).setFontSize(10));
        } else {
            int n = taxId.length;
            for (int i = 0; i < n; i++) {
                cell.add(new Paragraph(String.format("%s: %s", taxSchema[i], taxId[i])).setFont(font).setFontSize(10));
            }
        }
        return cell;
    }

    public Table getTotalsTable(String tBase, String tTax, String tTotal, String tCurrency,
                                String[] type, String[] percentage, String base[], String tax[], String currency[]) {
        Table table = new Table(new float[]{1, 1, 3, 3, 3, 1});
        table.setWidthPercent(100);
        table.addCell(getCell("TAX", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("%", TextAlignment.RIGHT, fontb, 12));
        table.addCell(getCell("Base amount:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Tax amount:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("Total:", TextAlignment.LEFT, fontb, 12));
        table.addCell(getCell("", TextAlignment.LEFT, fontb, 12));
        int n = type.length;
        for (int i = 0; i < n; i++) {
            table.addCell(getCell(type[i], TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(percentage[i], TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(base[i], TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(tax[i], TextAlignment.RIGHT, font, 12));
            double total = Double.parseDouble(base[i]) + Double.parseDouble(tax[i]);
            table.addCell(getCell(InvoiceData.format2dec(InvoiceData.round(total)), TextAlignment.RIGHT, font, 12));
            table.addCell(getCell(currency[i], TextAlignment.LEFT, font, 12));
        }
        Cell cell = getCell(1, 2, "", TextAlignment.LEFT, font, 12);
        cell.setBorder(Border.NO_BORDER);
        table.addCell(cell);
        table.addCell(getCell(tBase, TextAlignment.RIGHT, fontb, 12));
        table.addCell(getCell(tTax, TextAlignment.RIGHT, fontb, 12));
        table.addCell(getCell(tTotal, TextAlignment.RIGHT, fontb, 12));
        table.addCell(getCell(tCurrency, TextAlignment.LEFT, fontb, 12));
        return table;
    }

    public Cell getCell(int rowspan, int colspan, String value, TextAlignment alignment, PdfFont font, int fontSize) {
        Cell cell = new Cell(rowspan, colspan);
        Paragraph p = new Paragraph(value).setFont(font).setFontSize(fontSize);
        p.setTextAlignment(alignment);
        cell.add(p);
        return cell;
    }

    public Cell getCell(String value, TextAlignment alignment, PdfFont font, int fontSize) {
        return getCell(1, 1, value, alignment, font, fontSize);
    }

    public Paragraph getPaymentInfo(String ref, String[] bic, String[] iban) {
        Paragraph p = new Paragraph(String.format(
                "Please wire the amount due to our bank account using the following reference: %s",
                ref)).setFont(font).setFontSize(12);
        int n = bic.length;
        for (int i = 0; i < n; i++) {
            p.add(String.format("BIC: %s - IBAN: %s", bic[i], iban[i]));
        }
        return p;
    }

    public String convertDate(Date d, String newFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        return sdf.format(d);
    }
}
