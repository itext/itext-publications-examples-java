/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * This class creates an XML that has the structure needed to conform with
 * the BASIC level, but the content isn't valid. Instead we used test values
 * that make it easy for us to detect errors.
 */
package com.itextpdf.samples.sandbox.zugferd.test;

import com.itextpdf.licensekey.LicenseKey;
import com.itextpdf.zugferd.InvoiceDOM;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.profiles.BasicProfileImp;
import com.itextpdf.zugferd.validation.basic.DateFormatCode;
import com.itextpdf.zugferd.validation.basic.DocumentTypeCode;
import com.itextpdf.zugferd.validation.basic.MeasurementUnitCode;
import com.itextpdf.zugferd.validation.basic.TaxIDTypeCode;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * @author iText
 */
public class XML4Basic {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, DataIncompleteException, TransformerException, InvalidCodeException, ParseException {
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-multiple-products.xml");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        BasicProfileImp data = new BasicProfileImp(true);
        // SpecifiedExchangedDocumentContext
        data.setTest(true);

        // HeaderExchangedDocument
        data.setId("HeaderExchangedDocument.ID");
        data.setName("HeaderExchangedDocument.Name");
        data.setTypeCode(DocumentTypeCode.COMMERCIAL_INVOICE);
        data.setDate(sdf.parse("2015/04/01"), DateFormatCode.YYYYMMDD);
        data.addNote(new String[]{"HeaderExchangedDocument.Note[0][0]", "HeaderExchangedDocument.Note[0][1]"});
        data.addNote(new String[]{"HeaderExchangedDocument.Note[1][0]", "HeaderExchangedDocument.Note[1][1]"});
        data.addNote(new String[]{"HeaderExchangedDocument.Note[2][0]", "HeaderExchangedDocument.Note[2][1]"});

        // SpecifiedSupplyChainTradeTransaction>
        // Seller
        data.setSellerName("SellerTradeParty.Name");
        data.setSellerPostcode("SellerTradeParty.PostalTradeAddress.Postcode");
        data.setSellerLineOne("SellerTradeParty.PostalTradeAddress.LineOne");
        data.setSellerLineTwo("SellerTradeParty.PostalTradeAddress.LineTwo");
        data.setSellerCityName("SellerTradeParty.PostalTradeAddress.CityName");
        data.setSellerCountryID("BE");
        data.addSellerTaxRegistration(TaxIDTypeCode.VAT, "SellerTradeParty.SpecifiedTaxRegistration.SchemeID[0]");
        data.addSellerTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, "SellerTradeParty.SpecifiedTaxRegistration.SchemeID[1]");
        // Buyer
        data.setBuyerName("BuyerTradeParty.Name");
        data.setBuyerPostcode("BuyerTradeParty.PostalTradeAddress.Postcode");
        data.setBuyerLineOne("BuyerTradeParty.PostalTradeAddress.LineOne");
        data.setBuyerLineTwo("BuyerTradeParty.PostalTradeAddress.LineTwo");
        data.setBuyerCityName("BuyerTradeParty.PostalTradeAddress.CityName");
        data.setBuyerCountryID("FR");
        data.addBuyerTaxRegistration(TaxIDTypeCode.VAT, "BuyerTradeParty.SpecifiedTaxRegistration.SchemeID[0]");
        data.addBuyerTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, "BuyerTradeParty.SpecifiedTaxRegistration.SchemeID[1]");

        // ApplicableSupplyChainTradeDelivery
        data.setDeliveryDate(sdf.parse("2015/04/01"), DateFormatCode.YYYYMMDD);

        // ApplicableSupplyChainTradeSettlement
        data.setPaymentReference("ApplicableSupplyChainTradeSettlement.PaymentReference");
        data.setInvoiceCurrencyCode("EUR");
        data.addPaymentMeans("SpecifiedTradeSettlementPaymentMeans.schemeAgencyID[0]", "SpecifiedTradeSettlementPaymentMeans.ID[0]",
                "SpecifiedTradeSettlementPaymentMeans.IBANID[0]", "SpecifiedTradeSettlementPaymentMeans.AccountName[0]", "SpecifiedTradeSettlementPaymentMeans.ProprietaryID[0]",
                "SpecifiedTradeSettlementPaymentMeans.BICID[0]", "SpecifiedTradeSettlementPaymentMeans.GermanBankleitzahlID[0]", "SpecifiedTradeSettlementPaymentMeans.Name[0]");
        data.addPaymentMeans("SpecifiedTradeSettlementPaymentMeans.schemeAgencyID[1]", "SpecifiedTradeSettlementPaymentMeans.ID[1]",
                "SpecifiedTradeSettlementPaymentMeans.IBANID[1]", "SpecifiedTradeSettlementPaymentMeans.AccountName[1]", "SpecifiedTradeSettlementPaymentMeans.ProprietaryID[1]",
                "SpecifiedTradeSettlementPaymentMeans.BICID[1]", "SpecifiedTradeSettlementPaymentMeans.GermanBankleitzahlID[1]", "SpecifiedTradeSettlementPaymentMeans.Name[1]");

        // ram:ApplicableTradeTax
        data.addApplicableTradeTax("6.00", "EUR", "VAT", "100.00", "EUR", "6.00");
        data.addApplicableTradeTax("21.00", "EUR", "VAT", "100.00", "EUR", "21.00");

        // SpecifiedTradeSettlementMonetarySummation
        data.setMonetarySummation("1000.00", "EUR",
                "0.00", "EUR",
                "0.00", "EUR",
                "1000.00", "EUR",
                "210.00", "EUR",
                "1210.00", "EUR");

        data.addIncludedSupplyChainTradeLineItem("1.0000",
                MeasurementUnitCode.DAY,
                "IncludedSupplyChainTradeLineItem.SpecifiedTradeProduct.Name[0]");
        data.addIncludedSupplyChainTradeLineItem("2.0000",
                MeasurementUnitCode.HR,
                "IncludedSupplyChainTradeLineItem.SpecifiedTradeProduct.Name[1]");
        data.addIncludedSupplyChainTradeLineItem("3.0000",
                MeasurementUnitCode.MIN,
                "IncludedSupplyChainTradeLineItem.SpecifiedTradeProduct.Name[2]");

        // Create the XML
        InvoiceDOM dom = new InvoiceDOM(data);
        byte[] xml = dom.toXML();
        System.out.println(new String(xml));
    }
}
