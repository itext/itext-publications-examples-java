/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.html.qrcode;

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
 * The tagworker processes a {@code <qr>} tag using iText Barcode functionality
 */
public class QRCodeTagWorker implements ITagWorker {
    private static String[] allowedErrorCorrection = {"L","M","Q","H"};
    private static String[] allowedCharset = {"Cp437","Shift_JIS","ISO-8859-1","ISO-8859-16"};
    private BarcodeQRCode qrCode;
    private Image qrCodeAsImage;

    /**
     * <p>Constructor for QRCodeTagWorker.</p>
     *
     * @param element a {@link com.itextpdf.styledxmlparser.node.IElementNode} object.
     * @param context a {@link com.itextpdf.html2pdf.attach.ProcessorContext} object.
     */
    public QRCodeTagWorker(IElementNode element, ProcessorContext context){
        //Retrieve all necessary properties to create the barcode
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //Character set
        String charset = element.getAttribute("charset");
        if(checkCharacterSet(charset )){
            hints.put(EncodeHintType.CHARACTER_SET, charset);
        }
        //Error-correction level
        String errorCorrection = element.getAttribute("errorcorrection");
        if(checkErrorCorrectionAllowed(errorCorrection)){
            ErrorCorrectionLevel errorCorrectionLevel = getErrorCorrectionLevel(errorCorrection);
            hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        }
        //Create the QR-code
        qrCode = new BarcodeQRCode("placeholder",hints);

    }

    /** {@inheritDoc} */
    @Override
    public void processEnd(IElementNode element, ProcessorContext context) {
        //Transform barcode into image
        qrCodeAsImage = new Image(qrCode.createFormXObject(context.getPdfDocument()));

    }

    /** {@inheritDoc} */
    @Override
    public boolean processContent(String content, ProcessorContext context) {
        //Add content to the barcode
        qrCode.setCode(content);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean processTagChild(ITagWorker childTagWorker, ProcessorContext context) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public IPropertyContainer getElementResult() {

        return qrCodeAsImage;
    }

    private static boolean checkErrorCorrectionAllowed(String toCheck){
        for(int i = 0; i<allowedErrorCorrection.length;i++){
            if(toCheck.toUpperCase().equals(allowedErrorCorrection[i])){
                return true;
            }
        }
        return false;
    }

    private static boolean checkCharacterSet(String toCheck){
        for(int i = 0; i<allowedCharset.length;i++){
            if(toCheck.equals(allowedCharset[i])){
                return true;
            }
        }
        return false;
    }

    private static ErrorCorrectionLevel getErrorCorrectionLevel(String level){
        switch(level) {
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
