package com.itextpdf.samples.htmlsamples.chapter05;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory;
import com.itextpdf.html2pdf.css.apply.ICssApplier;
import com.itextpdf.html2pdf.css.apply.impl.BlockCssApplier;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Image;
import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.styledxmlparser.node.IElementNode;

/**
 * Converts an HTML file to a PDF document, introducing a custom tag to create
 * a QR Code involving a custom TagWorker and a custom CssApplier.
 */
public class C05E04_QRCode {

    /**
     * The path to the resulting PDF file.
     */
    public static final String DEST = "./target/htmlsamples/ch05/qrcode.pdf";

    /**
     * The path to the source HTML file.
     */
    public static final String SRC = "./src/main/resources/htmlsamples/html/qrcode.html";

    /**
     * The main method of this example.
     *
     * @param args no arguments are needed to run this example.
     * @throws IOException signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-html2pdf_typography.xml");
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        C05E04_QRCode app = new C05E04_QRCode();
        app.createPdf(SRC, DEST);
    }

    /**
     * Creates the PDF file.
     *
     * @param src  the path to the source HTML file
     * @param dest the path to the resulting PDF
     * @throws IOException signals that an I/O exception has occurred.
     */
    public void createPdf(String src, String dest) throws IOException {
        ConverterProperties properties = new ConverterProperties();
        properties
                .setCssApplierFactory(new QRCodeTagCssApplierFactory())
                .setTagWorkerFactory(new QRCodeTagWorkerFactory());
        HtmlConverter.convertToPdf(new File(src), new File(dest), properties);
    }

    /**
     * A factory for creating QRCodeTagCssApplier objects.
     */
    class QRCodeTagCssApplierFactory extends DefaultCssApplierFactory {

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory#getCustomCssApplier(com.itextpdf.html2pdf.html.node.IElementNode)
         */
        @Override
        public ICssApplier getCustomCssApplier(IElementNode tag) {
            if (tag.name().equals("qr")) {
                return new BlockCssApplier();
            }
            return null;
        }
    }

    /**
     * A factory for creating QRCodeTagWorker objects.
     */
    class QRCodeTagWorkerFactory extends DefaultTagWorkerFactory {

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.attach.impl.DefaultTagWorkerFactory#getCustomTagWorker(com.itextpdf.html2pdf.html.node.IElementNode, com.itextpdf.html2pdf.attach.ProcessorContext)
         */
        @Override
        public ITagWorker getCustomTagWorker(IElementNode tag, ProcessorContext context) {
            if (tag.name().equals("qr")) {
                return new QRCodeTagWorker(tag, context);
            }
            return null;
        }
    }

    /**
     * The custom ITagWorker implementation for the qr-tag.
     */
    static class QRCodeTagWorker implements ITagWorker {

        /**
         * The different error corrections that are allowed.
         */
        private static String[] allowedErrorCorrection = {"L", "M", "Q", "H"};

        /**
         * The different characters sets that are allowed.
         */
        private static String[] allowedCharset = {"Cp437", "Shift_JIS", "ISO-8859-1", "ISO-8859-16"};

        /**
         * The QR code object.
         */
        private BarcodeQRCode qrCode;

        /**
         * The QR code as an Image object.
         */
        private Image qrCodeAsImage;

        /**
         * Instantiates a new QR code tag worker.
         *
         * @param element the element node
         * @param context the processor context
         */
        public QRCodeTagWorker(IElementNode element, ProcessorContext context) {
            //Retrieve all necessary properties to create the barcode
            Map<EncodeHintType, Object> hints = new HashMap<>();
            //Character set
            String charset = element.getAttribute("charset");
            if (checkCharacterSet(charset)) {
                hints.put(EncodeHintType.CHARACTER_SET, charset);
            }
            //Error-correction level
            String errorCorrection = element.getAttribute("errorcorrection");
            if (checkErrorCorrectionAllowed(errorCorrection)) {
                ErrorCorrectionLevel errorCorrectionLevel = getErrorCorrectionLevel(errorCorrection);
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
            }
            //Create the QR-code
            qrCode = new BarcodeQRCode("placeholder", hints);

        }

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.attach.ITagWorker#processContent(java.lang.String, com.itextpdf.html2pdf.attach.ProcessorContext)
         */
        @Override
        public boolean processContent(String content, ProcessorContext context) {
            //Add content to the barcode
            qrCode.setCode(content);
            return true;
        }

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.attach.ITagWorker#processTagChild(com.itextpdf.html2pdf.attach.ITagWorker, com.itextpdf.html2pdf.attach.ProcessorContext)
         */
        @Override
        public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
            return false;
        }

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.attach.ITagWorker#processEnd(com.itextpdf.html2pdf.html.node.IElementNode, com.itextpdf.html2pdf.attach.ProcessorContext)
         */
        @Override
        public void processEnd(IElementNode element, ProcessorContext context) {
            //Transform barcode into image
            qrCodeAsImage = new Image(qrCode.createFormXObject(context.getPdfDocument()));

        }

        /* (non-Javadoc)
         * @see com.itextpdf.html2pdf.attach.ITagWorker#getElementResult()
         */
        @Override
        public IPropertyContainer getElementResult() {

            return qrCodeAsImage;
        }

        /**
         * Checks if a type of error correction is allowed.
         *
         * @param toCheck the error correction type to check
         * @return true, if successful
         */
        private static boolean checkErrorCorrectionAllowed(String toCheck) {
            for (int i = 0; i < allowedErrorCorrection.length; i++) {
                if (toCheck.toUpperCase().equals(allowedErrorCorrection[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if a certain character set is allowed.
         *
         * @param toCheck the character set to check
         * @return true, if successful
         */
        private static boolean checkCharacterSet(String toCheck) {
            for (int i = 0; i < allowedCharset.length; i++) {
                if (toCheck.equals(allowedCharset[i])) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets the error correction level.
         *
         * @param level the error correction level as a String
         * @return the error correction level
         */
        private static ErrorCorrectionLevel getErrorCorrectionLevel(String level) {
            switch (level) {
                case "L":
                    return ErrorCorrectionLevel.L;
                case "M":
                    return ErrorCorrectionLevel.M;
                case "Q":
                    return ErrorCorrectionLevel.Q;
                case "H":
                    return ErrorCorrectionLevel.H;
            }
            return null;

        }
    }
}
