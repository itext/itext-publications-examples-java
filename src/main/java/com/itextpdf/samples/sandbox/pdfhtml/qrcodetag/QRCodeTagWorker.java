package com.itextpdf.samples.sandbox.pdfhtml.qrcodetag;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.html2pdf.attach.ITagWorker;
import com.itextpdf.html2pdf.attach.ProcessorContext;
import com.itextpdf.layout.IPropertyContainer;
import com.itextpdf.layout.element.Image;
import com.itextpdf.styledxmlparser.node.IElementNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Example of a custom tagworker implementation for pdfHTML.
 * The tagworker processes a <qr> tag using iText Barcode functionality
 */
public class QRCodeTagWorker implements ITagWorker {
    private static String[] allowedErrorCorrection = {"L", "M", "Q", "H"};
    private static String[] allowedCharset = {"Cp437", "Shift_JIS", "ISO-8859-1", "ISO-8859-16"};

    private BarcodeQRCode qrCode;
    private Image qrCodeAsImage;

    public QRCodeTagWorker(IElementNode element, ProcessorContext context) {

        // Retrieve all necessary properties to create the barcode
        Map<EncodeHintType, Object> hints = new HashMap<>();

        // Character set
        String charset = element.getAttribute("charset");
        if (checkCharacterSet(charset)) {
            hints.put(EncodeHintType.CHARACTER_SET, charset);
        }

        // Error-correction level
        String errorCorrection = element.getAttribute("errorcorrection");
        if (checkErrorCorrectionAllowed(errorCorrection)) {
            ErrorCorrectionLevel errorCorrectionLevel = getErrorCorrectionLevel(errorCorrection);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        }

        // Create the QR-code
        qrCode = new BarcodeQRCode("placeholder", hints);

    }

    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {

        // Transform barcode into image
        qrCodeAsImage = new Image(qrCode.createFormXObject(context.getPdfDocument()));

    }

    @Override
    public boolean processContent(String content, ProcessorContext context) {

        // Add content to the barcode
        qrCode.setCode(content);
        return true;
    }

    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    @Override
    public IPropertyContainer getElementResult() {

        return qrCodeAsImage;
    }

    private static boolean checkErrorCorrectionAllowed(String toCheck) {
        for (int i = 0; i < allowedErrorCorrection.length; i++) {
            if (toCheck.toUpperCase().equals(allowedErrorCorrection[i])) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkCharacterSet(String toCheck) {
        for (int i = 0; i < allowedCharset.length; i++) {
            if (toCheck.equals(allowedCharset[i])) {
                return true;
            }
        }
        return false;
    }

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
