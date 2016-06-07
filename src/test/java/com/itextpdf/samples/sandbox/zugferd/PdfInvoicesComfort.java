/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

///*
//* This example was written by Bruno Lowagie.
//*/
//package com.itextpdf.zugferd;
//
//import com.itextpdf.io.font.FontRegisterProvider;
//import com.itextpdf.zugferd.data.InvoiceData;
//import com.itextpdf.zugferd.exceptions.DataIncompleteException;
//import com.itextpdf.zugferd.exceptions.InvalidCodeException;
//import com.itextpdf.zugferd.pojo.Invoice;
//import com.itextpdf.zugferd.pojo.PojoFactory;
//import com.itextpdf.zugferd.profiles.ComfortProfile;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//import java.io.*;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
//* @author Bruno Lowagie
//*/
//public class PdfInvoicesComfort {
//
////    class MyFontProvider extends FontRegisterProvider {
////
////        protected BaseFont regular;
////        protected BaseFont bold;
////
////        public MyFontProvider() throws DocumentException, IOException {
////            regular = BaseFont.createFont("resources/fonts/OpenSans-Regular.ttf", BaseFont.IDENTITY_H, true);
////            bold = BaseFont.createFont("resources/fonts/OpenSans-Bold.ttf", BaseFont.IDENTITY_H, true);
////        }
////
////        public boolean isRegisteredFont(String fontname) {
////            return true;
////        }
////
////        public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
////            Font font;
////            switch (style) {
////                case Font.BOLD:
////                    font = new Font(bold, size);
////                    break;
////                default:
////                    font = new Font(regular, size);
////            }
////            font.setColor(color);
////            return font;
////        }
////    }
////
////    class MyImageProvider implements ImageProvider {
////
////        protected Map<String,Image> cache = new HashMap<String,Image>();
////
////        public Image retrieve(String src) {
////            Image img = cache.get(src);
////            try {
////                if (img == null) {
////                    img = Image.getInstance(getImageRootPath() + src);
////                    store(src, img);
////                }
////            } catch (BadElementException ex) {
////                throw new ExceptionConverter(ex);
////            } catch (IOException ex) {
////                throw new ExceptionConverter(ex);
////            }
////            return img;
////        }
////
////        public String getImageRootPath() {
////            return "resources/zugferd/";
////        }
////
////        public void store(String src, Image img) {
////            cache.put(src, img);
////        }
////
////        public void reset() {
////            cache = new HashMap<String,Image>();
////        }
////    }
//
//    public static final String DEST = "results/zugferd/pdf/comfort%05d.pdf";
//    public static final String XSL = "resources/zugferd/invoice.xsl";
//    public static final String CSS = "resources/zugferd/invoice.css";
//    public static final String ICC = "resources/data/sRGB_CS_profile.icm";
//
//    public static void main(String[] args) throws SQLException, IOException, ParserConfigurationException, SAXException, DataIncompleteException, InvalidCodeException, TransformerException, DocumentException, XMPException {
//        File file = new File(DEST);
//        file.getParentFile().mkdirs();
//        PdfInvoicesComfort app = new PdfInvoicesComfort();
//        StreamSource xsl = new StreamSource(new File(XSL));
//        TransformerFactory factory = TransformerFactory.newInstance();
//        Transformer transformer = factory.newTransformer(xsl);
//        ComfortProfile comfort;
//        InvoiceData invoiceData = new InvoiceData();
//        PojoFactory pojofactory = PojoFactory.getInstance();
//        List<Invoice> invoices = pojofactory.getInvoices();
//        for (Invoice invoice : invoices) {
//            comfort = invoiceData.createComfortProfileData(invoice);
//            InvoiceDOM dom = new InvoiceDOM(comfort);
//            byte[] xml = dom.toXML();
//            app.createPdf(xml, app.createHtml(transformer, xml), String.format(DEST, invoice.getId()));
//        }
//        pojofactory.close();
//    }
//
//    public void createPdf(byte[] xml, byte[] invoice, String dest) throws IOException, XMPException {
//        // step 1
//        Document document = new Document();
//        // step 2
//        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(dest), PdfAConformanceLevel.ZUGFeRDComfort);
//        writer.setTagged();
//        writer.setPdfVersion(PdfWriter.VERSION_1_7);
//        writer.createXmpMetadata();
//        writer.getXmpWriter().setProperty(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName, "ZUGFeRD-invoice.xml");
//        // step 3
//        document.open();
//        // step 4
//        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(ICC));
//        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
//
//        // CSS
//        CSSResolver cssResolver = new StyleAttrCSSResolver();
//        CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(CSS));
//        cssResolver.addCss(cssFile);
//
//        // HTML
//        CssAppliers cssAppliers = new CssAppliersImpl(new MyFontProvider());
//        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
//        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
//        htmlContext.setImageProvider(new MyImageProvider());
//
//        // Pipelines
//        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
//        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
//        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
//
//        // XML Worker
//        XMLWorker worker = new XMLWorker(css, true);
//        XMLParser p = new XMLParser(worker);
//        p.parse(new ByteArrayInputStream(invoice));
//
//        PdfDictionary parameters = new PdfDictionary();
//        parameters.put(PdfName.MODDATE, new PdfDate());
//        PdfFileSpecification fileSpec = writer.addFileAttachment(
//                "ZUGFeRD invoice", xml, null,
//                "ZUGFeRD-invoice.xml", "application/xml",
//                AFRelationshipValue.Alternative, parameters);
//        PdfArray array = new PdfArray();
//        array.add(fileSpec.getReference());
//        writer.getExtraCatalog().put(PdfName.AF, array);
//        // step 5
//        document.close();
//    }
//
//    public byte[] createHtml(Transformer transformer, byte[] comfort) throws IOException, ParserConfigurationException, SAXException, DataIncompleteException, InvalidCodeException, TransformerException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        StreamSource xml = new StreamSource(new ByteArrayInputStream(comfort));
//        Writer writer = new OutputStreamWriter(baos);
//        transformer.transform(xml, new StreamResult(writer));
//        writer.flush();
//        writer.close();
//        return baos.toByteArray();
//    }
//}
