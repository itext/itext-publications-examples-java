package com.itextpdf.samples.sandbox.zugferd;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfAConformance;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDate;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfOutputIntent;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.xmp.XMPConst;
import com.itextpdf.kernel.xmp.XMPException;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.kernel.xmp.options.PropertyOptions;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.pdfa.PdfADocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class BasicSample {

    public static final String DEST = "./target/sandbox/zugferd/invoice_with_zugferd.pdf";
    public static final String ZUGFERD_XML = "./src/main/resources/xml/factur-x.xml";
    public static final String ICC_PROFILE = "./src/main/resources/data/sRGB_CS_profile.icm";

    public static final String FONT = "./src/main/resources/font/FreeSans.ttf";;

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        createZugferdInvoice();
    }

    private static void createZugferdInvoice()
            throws IOException, XMPException, ParserConfigurationException, SAXException {
        InputStream inputStream = new FileInputStream(ICC_PROFILE);
        PdfADocument pdfDoc = new PdfADocument(new PdfWriter(DEST), PdfAConformance.PDF_A_3B,
                new PdfOutputIntent("Custom", "",
                        null, "sRGB IEC61966-2.1", inputStream));

        Document document = new Document(pdfDoc);
        PdfFont font = PdfFontFactory.createFont(FONT, "WinAnsi", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

        XMPMeta xmp = createValidXmp(pdfDoc);
        pdfDoc.setXmpMetadata(xmp);
        document.setFont(font);

        File xmlFile = new File(ZUGFERD_XML);

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, new FileInputStream(xmlFile),
                "ZUGFeRD invoice", "ZUGFeRD-invoice.xml", new PdfName("application/xml"), parameters, PdfName.Alternative);
        pdfDoc.addFileAttachment("ZUGFeRD invoice", fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getPdfObject().getIndirectReference());
        pdfDoc.getCatalog().put(PdfName.AF, array);

        fillDocumentFromXml(document, xmlFile);
        document.close();
    }

    private static void fillDocumentFromXml(Document document ,File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document xmlDocument = builder.parse(xmlFile);

        document.add(new Paragraph("Invoice with ZUGFeRD").setFontSize(18));
        document.add(new Paragraph("Ensuring the readability of XML data of electronic invoices").setBackgroundColor(ColorConstants.GRAY));
        Table xmlInfoTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        xmlInfoTable.addCell("Stylesheet-Version:");
        xmlInfoTable.addCell("2.5 vom 10.01.2023");
        xmlInfoTable.startNewRow();
        xmlInfoTable.addCell("Invoice Standard:");
        xmlInfoTable.addCell("Factur-x 1.0 Profil BASIC");
        xmlInfoTable.startNewRow();
        xmlInfoTable.addCell("URN ID");
        xmlInfoTable.addCell(xmlDocument.getElementsByTagName("ram:ID").item(0).getTextContent());
        xmlInfoTable.startNewRow();
        xmlInfoTable.addCell("The XML input file:");
        xmlInfoTable.addCell("factur-x.xml");
        document.add(xmlInfoTable);

        document.add(new Paragraph("Seller").setBackgroundColor(ColorConstants.GRAY));
        Table sellerTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        sellerTable.addCell("Name:");
        sellerTable.addCell(xmlDocument.getElementsByTagName("ram:Name").item(1).getTextContent());
        sellerTable.startNewRow();
        sellerTable.addCell("Address:");
        sellerTable.addCell(xmlDocument.getElementsByTagName("ram:LineOne").item(0).getTextContent());
        sellerTable.startNewRow();
        sellerTable.addCell("Tax number:");
        sellerTable.addCell(xmlDocument.getElementsByTagName("ram:ID").item(2).getTextContent());
        document.add(sellerTable);

        document.add(new Paragraph("Buyer / Beneficiary").setBackgroundColor(ColorConstants.GRAY));
        Table buyerTable = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
        buyerTable.addCell("Name:");
        buyerTable.addCell(xmlDocument.getElementsByTagName("ram:Name").item(2).getTextContent());
        buyerTable.startNewRow();
        buyerTable.addCell("Address:");
        buyerTable.addCell(xmlDocument.getElementsByTagName("ram:LineOne").item(1).getTextContent());
        document.add(buyerTable);
        document.add(new Paragraph("Position data").setBackgroundColor(ColorConstants.GRAY));

        Table positionTable = new Table(UnitValue.createPercentArray(8)).useAllAvailableWidth();
        positionTable.addCell("Pos number");
        positionTable.addCell("Reference");
        positionTable.addCell("Description");
        positionTable.addCell("Net price");
        positionTable.addCell("Quantity");
        positionTable.addCell("Name");
        positionTable.addCell("Tax rate");
        positionTable.addCell("Net amount");
        positionTable.startNewRow();
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:LineID").item(0).getTextContent());
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:GlobalID").item(0).getTextContent());
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:Name").item(0).getTextContent());
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:ChargeAmount").item(0).getTextContent());
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:BilledQuantity").item(0).getTextContent());
        positionTable.addCell("Stk");
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:RateApplicablePercent").item(0).getTextContent());
        positionTable.addCell(xmlDocument.getElementsByTagName("ram:LineTotalAmount").item(0).getTextContent());
        document.add(positionTable);
    }

    private static XMPMeta createValidXmp(PdfADocument pdfDoc) throws XMPException {
        XMPMeta xmp = pdfDoc.getXmpMetadata(true);
        String zugferdNamespace = "urn:ferd:pdfa:CrossIndustryDocument:invoice:1p0#";
        String zugferdPrefix = "zf";
        XMPMetaFactory.getSchemaRegistry().registerNamespace(zugferdNamespace, zugferdPrefix);

        xmp.setProperty(zugferdNamespace, "DocumentType", "INVOICE");
        xmp.setProperty(zugferdNamespace, "Version", "1.0");
        xmp.setProperty(zugferdNamespace, "ConformanceLevel", "BASIC");
        xmp.setProperty(zugferdNamespace, "DocumentFileName", "factur-x.xml");

        PropertyOptions bagOptions = new PropertyOptions(PropertyOptions.ARRAY);
        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, "schemas", null, bagOptions);

        String bagPath = "pdfaExtension:schemas";

        int newItemIndex = xmp.countArrayItems(XMPConst.NS_PDFA_EXTENSION, bagPath) + 1;
        String newItemPath = bagPath + "[" + newItemIndex + "]";

        PropertyOptions structOptions = new PropertyOptions(PropertyOptions.STRUCT);
        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, newItemPath, null, structOptions);

        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, newItemPath, XMPConst.NS_PDFA_SCHEMA, "schema", "Factur-X PDFA Extension Schema");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, newItemPath, XMPConst.NS_PDFA_SCHEMA, "namespaceURI", zugferdNamespace);
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, newItemPath, XMPConst.NS_PDFA_SCHEMA, "prefix", "fx");

        String seqPath = newItemPath + "/pdfaSchema:property";
        PropertyOptions seqOptions = new PropertyOptions(PropertyOptions.ARRAY_ORDERED);
        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, seqPath, null, seqOptions);

        String firstSeqItemPath = seqPath + "[1]";
        String secondSeqItemPath = seqPath + "[2]";
        String thirdSeqItemPath = seqPath + "[3]";
        String fourthSeqItemPath = seqPath + "[4]";

        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, firstSeqItemPath, null, structOptions);
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, firstSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "name", "DocumentFileName");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, firstSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "valueType", "Text");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, firstSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "category", "external");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, firstSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "description", "The name of the embedded XML document");

        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, secondSeqItemPath, null, structOptions);
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, secondSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "name", "DocumentType");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, secondSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "valueType", "Text");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, secondSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "category", "external");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, secondSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "description", "The type of the hybrid document in capital letters, e.g. INVOICE or ORDER");

        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, thirdSeqItemPath, null, structOptions);
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, thirdSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "name", "Version");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, thirdSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "valueType", "Text");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, thirdSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "category", "external");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, thirdSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "description", "The actual version of the standard applying to the embedded XML document");

        xmp.setProperty(XMPConst.NS_PDFA_EXTENSION, fourthSeqItemPath, null, structOptions);
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, fourthSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "name", "ConformanceLevel");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, fourthSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "valueType", "Text");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, fourthSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "category", "external");
        xmp.setStructField(XMPConst.NS_PDFA_EXTENSION, fourthSeqItemPath, XMPConst.NS_PDFA_PROPERTY, "description", "The conformance level of the embedded XML document");

        return xmp;
    }
}
