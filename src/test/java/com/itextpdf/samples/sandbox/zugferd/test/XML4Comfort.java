/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
 * This class creates an XML that has the structure needed to conform with
 * the COMFORT level, but the content isn't valid. Instead we used test values
 * that make it easy for us to detect errors.
 */
package com.itextpdf.samples.sandbox.zugferd.test;

import com.itextpdf.zugferd.InvoiceDOM;
import com.itextpdf.zugferd.validation.basic.DateFormatCode;
import com.itextpdf.zugferd.validation.basic.DocumentTypeCode;
import com.itextpdf.zugferd.validation.basic.MeasurementUnitCode;
import com.itextpdf.zugferd.validation.basic.TaxIDTypeCode;
import com.itextpdf.zugferd.validation.comfort.FreeTextSubjectCode;
import com.itextpdf.zugferd.validation.comfort.GlobalIdentifierCode;
import com.itextpdf.zugferd.validation.comfort.PaymentMeansCode;
import com.itextpdf.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.zugferd.profiles.ComfortProfileImp;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Bruno Lowagie (iText Software)
 */
public class XML4Comfort {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, DataIncompleteException, TransformerException, InvalidCodeException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        ComfortProfileImp data = new ComfortProfileImp();
        // SpecifiedExchangedDocumentContext
        data.setTest(true);

        // HeaderExchangedDocument
        data.setId("HeaderExchangedDocument.ID");
        data.setName("HeaderExchangedDocument.Name");
        data.setTypeCode(DocumentTypeCode.DEBIT_NOTE_FINANCIAL_ADJUSTMENT);
        data.setDate(sdf.parse("2016/04/01"), DateFormatCode.YYYYMMDD);
        data.addNote(new String[]{"HeaderExchangedDocument.Note[0][0]", "HeaderExchangedDocument.Note[0][1]"}, FreeTextSubjectCode.REGULATORY_INFORMATION);
        data.addNote(new String[]{"HeaderExchangedDocument.Note[1][0]", "HeaderExchangedDocument.Note[1][1]"}, FreeTextSubjectCode.PRICE_CONDITIONS);
        data.addNote(new String[]{"HeaderExchangedDocument.Note[2][0]", "HeaderExchangedDocument.Note[2][1]"}, FreeTextSubjectCode.PAYMENT_INFORMATION);

        // SpecifiedSupplyChainTradeTransaction
        data.setBuyerReference("BuyerReference");
        // Seller
        data.setSellerID("SellerTradeParty.ID");
        data.addSellerGlobalID(GlobalIdentifierCode.DUNS, "SellerTradeParty.GlobalID[0]");
        data.addSellerGlobalID(GlobalIdentifierCode.GTIN, "SellerTradeParty.GlobalID[1]");
        data.addSellerGlobalID(GlobalIdentifierCode.ODETTE, "SellerTradeParty.GlobalID[2]");
        data.setSellerName("SellerTradeParty.Name");
        data.setSellerPostcode("SellerTradeParty.PostalTradeAddress.Postcode");
        data.setSellerLineOne("SellerTradeParty.PostalTradeAddress.LineOne");
        data.setSellerLineTwo("SellerTradeParty.PostalTradeAddress.LineTwo");
        data.setSellerCityName("SellerTradeParty.PostalTradeAddress.CityName");
        data.setSellerCountryID("DE");
        data.addSellerTaxRegistration(TaxIDTypeCode.VAT, "SellerTradeParty.SpecifiedTaxRegistration.SchemeID[0]");
        data.addSellerTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, "SellerTradeParty.SpecifiedTaxRegistration.SchemeID[1]");
        // Buyer
        data.setBuyerID("BuyerTradeParty.ID");
        data.addBuyerGlobalID(GlobalIdentifierCode.DUNS, "BuyerTradeParty.GlobalID[0]");
        data.addBuyerGlobalID(GlobalIdentifierCode.GTIN, "BuyerTradeParty.GlobalID[1]");
        data.addBuyerGlobalID(GlobalIdentifierCode.ODETTE, "BuyerTradeParty.GlobalID[2]");
        data.setBuyerName("BuyerTradeParty.Name");
        data.setBuyerPostcode("BuyerTradeParty.PostalTradeAddress.Postcode");
        data.setBuyerLineOne("BuyerTradeParty.PostalTradeAddress.LineOne");
        data.setBuyerLineTwo("BuyerTradeParty.PostalTradeAddress.LineTwo");
        data.setBuyerCityName("BuyerTradeParty.PostalTradeAddress.CityName");
        data.setBuyerCountryID("BE");
        data.addBuyerTaxRegistration(TaxIDTypeCode.VAT, "BuyerTradeParty.SpecifiedTaxRegistration.SchemeID[0]");
        data.addBuyerTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, "BuyerTradeParty.SpecifiedTaxRegistration.SchemeID[1]");

        // ApplicableSupplyChainTradeAgreement
        data.setBuyerOrderReferencedDocumentIssueDateTime(sdf.parse("2016/04/02"), DateFormatCode.YYYYMMDD);
        data.setBuyerOrderReferencedDocumentID("ApplicableSupplyChainTradeAgreement.BuyerOrderReferencedDocumentID");
        data.setContractReferencedDocumentIssueDateTime(sdf.parse("2016/04/03"), DateFormatCode.YYYYMMDD);
        data.setContractReferencedDocumentID("ApplicableSupplyChainTradeAgreement.ContractReferencedDocument");
        data.setCustomerOrderReferencedDocumentIssueDateTime(sdf.parse("2016/04/04"), DateFormatCode.YYYYMMDD);
        data.setCustomerOrderReferencedDocumentID("ApplicableSupplyChainTradeAgreement.CustomerOrderReferencedDocument");

        // ApplicableSupplyChainTradeDelivery
        data.setDeliveryDate(sdf.parse("2016/04/05"), DateFormatCode.YYYYMMDD);
        data.setDeliveryNoteReferencedDocumentIssueDateTime(sdf.parse("2016/04/06"), DateFormatCode.YYYYMMDD);
        data.setDeliveryNoteReferencedDocumentID("ApplicableSupplyChainTradeAgreement.DeliveryNoteReferencedDocument");

        // ApplicableSupplyChainTradeSettlement
        data.setPaymentReference("ApplicableSupplyChainTradeSettlement.PaymentReference");
        data.setInvoiceCurrencyCode("USD");

        data.setInvoiceeID("InvoiceeTradeParty.ID");
        data.addInvoiceeGlobalID(GlobalIdentifierCode.DUNS, "InvoiceeTradeParty.GlobalID[0]");
        data.addInvoiceeGlobalID(GlobalIdentifierCode.GTIN, "InvoiceeTradeParty.GlobalID[1]");
        data.addInvoiceeGlobalID(GlobalIdentifierCode.ODETTE, "InvoiceeTradeParty.GlobalID[2]");
        data.setInvoiceeName("InvoiceeTradeParty.Name");
        data.setInvoiceePostcode("InvoiceeTradeParty.PostalTradeAddress.Postcode");
        data.setInvoiceeLineOne("InvoiceeTradeParty.PostalTradeAddress.LineOne");
        data.setInvoiceeLineTwo("InvoiceeTradeParty.PostalTradeAddress.LineTwo");
        data.setInvoiceeCityName("InvoiceeTradeParty.PostalTradeAddress.CityName");
        data.setInvoiceeCountryID("BE");
        data.addInvoiceeTaxRegistration(TaxIDTypeCode.VAT, "InvoiceeTradeParty.SpecifiedTaxRegistration.SchemeID[0]");
        data.addInvoiceeTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, "InvoiceeTradeParty.SpecifiedTaxRegistration.SchemeID[1]");

        String[] information0 = {"SpecifiedTradeSettlementPaymentMeans.Information[0][0]", "SpecifiedTradeSettlementPaymentMeans.Information[0][1]", "SpecifiedTradeSettlementPaymentMeans.Information[0][2]"};
        data.addPaymentMeans(PaymentMeansCode.PAYMENT_TO_BANK_ACCOUNT, information0, "SpecifiedTradeSettlementPaymentMeans.schemeAgencyID[0]", "SpecifiedTradeSettlementPaymentMeans.ID[0]",
                "SpecifiedTradeSettlementPaymentMeans.Payer.IBANID[0]", "SpecifiedTradeSettlementPaymentMeans.Payer.ProprietaryID[0]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.IBANID[0]", "SpecifiedTradeSettlementPaymentMeans.Payee.AccountName[0]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.ProprietaryID[0]",
                "SpecifiedTradeSettlementPaymentMeans.Payer.BICID[0]", "SpecifiedTradeSettlementPaymentMeans.Payer.GermanBankleitzahlID[0]", "SpecifiedTradeSettlementPaymentMeans.Payer.Name[0]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.BICID[0]", "SpecifiedTradeSettlementPaymentMeans.Payee.GermanBankleitzahlID[0]", "SpecifiedTradeSettlementPaymentMeans.Payee.Name[0]");
        String[] information1 = {"SpecifiedTradeSettlementPaymentMeans.Information[1][0]", "SpecifiedTradeSettlementPaymentMeans.Information[1][1]"};
        data.addPaymentMeans(PaymentMeansCode.CASH, information1, "SpecifiedTradeSettlementPaymentMeans.schemeAgencyID[1]", "SpecifiedTradeSettlementPaymentMeans.ID[1]",
                "SpecifiedTradeSettlementPaymentMeans.Payer.IBANID[1]", "SpecifiedTradeSettlementPaymentMeans.Payer.ProprietaryID[1]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.IBANID[1]", "SpecifiedTradeSettlementPaymentMeans.Payee.AccountName[1]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.ProprietaryID[1]",
                "SpecifiedTradeSettlementPaymentMeans.Payer.BICID[1]", "SpecifiedTradeSettlementPaymentMeans.Payer.GermanBankleitzahlID[1]", "SpecifiedTradeSettlementPaymentMeans.Payer.Name[1]",
                "SpecifiedTradeSettlementPaymentMeans.Payee.BICID[1]", "SpecifiedTradeSettlementPaymentMeans.Payee.GermanBankleitzahlID[1]", "SpecifiedTradeSettlementPaymentMeans.Payee.Name[1]");

        // ram:ApplicableTradeTax
        data.addApplicableTradeTax("6.00", "USD", "VAT", "ApplicableTradeTax.ExemptionReason[0]", "100.00", "USD", "S", "6.00");
        data.addApplicableTradeTax("21.00", "USD", "VAT", "ApplicableTradeTax.ExemptionReason[1]", "100.00", "USD", "S", "21.00");

        data.setBillingStartEnd(sdf.parse("2016/04/01"), DateFormatCode.YYYYMMDD, sdf.parse("2016/04/30"), DateFormatCode.YYYYMMDD);

        data.addSpecifiedTradeAllowanceCharge(true, "0.1234", "USD", "TradeAllowanceCharge.Reason[0]",
                new String[]{"VAT", "VAT"}, new String[]{"S", "S"}, new String[]{"6.00", "21.00"});
        data.addSpecifiedTradeAllowanceCharge(false, "0.0120", "USD", "TradeAllowanceCharge.Reason[1]",
                new String[]{"VAT", "VAT"}, new String[]{"S", "S"}, new String[]{"0.00", "8.00"});

        data.addSpecifiedLogisticsServiceCharge(
                new String[]{"SpecifiedLogisticsServiceCharge.Description[0][0]", "SpecifiedLogisticsServiceCharge.Description[0][1]"},
                "0.4321", "EUR", new String[]{"VAT", "VAT"}, new String[]{"S", "S"}, new String[]{"6.00", "21.00"});
        data.addSpecifiedLogisticsServiceCharge(
                new String[]{"SpecifiedLogisticsServiceCharge.Description[1][0]", "SpecifiedLogisticsServiceCharge.Description[1][1]"},
                "0.1234", "EUR", new String[]{"VAT", "VAT"}, new String[]{"S", "S"}, new String[]{"0.00", "8.00"});

        data.addSpecifiedTradePaymentTerms(
                new String[]{"SpecifiedTradePaymentTerms.Information[0][0]", "SpecifiedTradePaymentTerms.Information[0][1]"},
                sdf.parse("2016/05/01"), DateFormatCode.YYYYMMDD);
        data.addSpecifiedTradePaymentTerms(
                new String[]{"SpecifiedTradePaymentTerms.Information[1][0]", "SpecifiedTradePaymentTerms.Information[1][1]"},
                sdf.parse("2016/05/02"), DateFormatCode.YYYYMMDD);

        // SpecifiedTradeSettlementMonetarySummation       
        data.setMonetarySummation("1000.00", "USD",
                "0.00", "USD",
                "0.00", "USD",
                "1000.00", "USD",
                "210.00", "USD",
                "1210.00", "USD");
        data.setTotalPrepaidAmount("0.00", "USD");
        data.setDuePayableAmount("1210.00", "USD");

        String[][] notes0 = {
                {"IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[0].Content[0]",
                        "IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[0].Content[1]",
                        "IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[0].Content[2]"},
                {"IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[1].Content[0]",
                        "IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[1].Content[1]"},
                {"IncludedSupplyChainTradeLineItem[0].AssociatedDocumentLineDocument.IncludedNote[2].Content[0]"}
        };
        Boolean[] indicator = {true, false, true};
        String[] actualamount = {"1.0000", "2.0000", "3.0000"};
        String[] actualamountCurr = {"USD", "USD", "USD"};
        String[] reason = {
                "IncludedSupplyChainTradeLineItem[0].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[0].Reason",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[1].Reason",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[2].Reason"
        };
        String[] taxTC = {"VAT", "VAT"};
        String[] taxER = {
                "IncludedSupplyChainTradeLineItem[0].SpecifiedSupplyChainTradeSettlement.SpecifiedTradeSettlementMonetarySummation.ApplicableTradeTax[0].ExemptionReason",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedSupplyChainTradeSettlement.SpecifiedTradeSettlementMonetarySummation.ApplicableTradeTax[1].ExemptionReason"};
        String[] taxCC = {"S", "S"};
        String[] taxAP = {"12.00", "18.00"};
        data.addIncludedSupplyChainTradeLineItem(
                "LINE 1", notes0,
                "10.0000",
                "USD",
                "1.0000",
                MeasurementUnitCode.ITEM,
                indicator, actualamount, actualamountCurr, reason,
                "6.0000",
                "USD",
                "80.0001",
                MeasurementUnitCode.L,
                "1.0000",
                MeasurementUnitCode.HR,
                taxTC, taxER, taxCC, taxAP,
                "5.00",
                "USD",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedTradeProduct.GlobalID",
                GlobalIdentifierCode.GTIN,
                "IncludedSupplyChainTradeLineItem[0].SpecifiedTradeProduct.SellerAssignedID",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedTradeProduct.BuyerAssignedID",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedTradeProduct.Name",
                "IncludedSupplyChainTradeLineItem[0].SpecifiedTradeProduct.Description");
        String[][] notes1 = {
                {"IncludedSupplyChainTradeLineItem.ram:AssociatedDocumentLineDocument[1].IncludedNote[0].Content[0]",
                        "IncludedSupplyChainTradeLineItem.ram:AssociatedDocumentLineDocument[1].IncludedNote[0].Content[1]"},
                {"IncludedSupplyChainTradeLineItem.ram:AssociatedDocumentLineDocument[1].IncludedNote[1].Content[0]"}
        };
        Boolean[] indicator1 = {false, true, false};
        String[] actualamount1 = {"4.0000", "5.0000", "6.0000"};
        String[] actualamountCurr1 = {"USD", "USD", "USD"};
        String[] reason1 = {
                "IncludedSupplyChainTradeLineItem[1].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[0].Reason",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[1].Reason",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedSupplyChainTradeAgreement.GrossPriceProductTradePrice.AppliedTradeAllowanceCharge[2].Reason"
        };
        String[] taxER1 = {
                "IncludedSupplyChainTradeLineItem[1].SpecifiedSupplyChainTradeSettlement.SpecifiedTradeSettlementMonetarySummation.ApplicableTradeTax[0].ExemptionReason",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedSupplyChainTradeSettlement.SpecifiedTradeSettlementMonetarySummation.ApplicableTradeTax[1].ExemptionReason"};
        String[] taxAP1 = {"13.00", "17.00"};
        data.addIncludedSupplyChainTradeLineItem(
                "LINE 2", notes1,
                "20.0000",
                "USD",
                "10.0000",
                MeasurementUnitCode.KG,
                indicator1, actualamount1, actualamountCurr1, reason1,
                "30.0000",
                "USD",
                "15.0000",
                MeasurementUnitCode.M,
                "57.0000",
                MeasurementUnitCode.DAY,
                taxTC, taxER1, taxCC, taxAP1,
                "15.00",
                "USD",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedTradeProduct.GlobalID",
                GlobalIdentifierCode.ODETTE,
                "IncludedSupplyChainTradeLineItem[1].SpecifiedTradeProduct.SellerAssignedID",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedTradeProduct.BuyerAssignedID",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedTradeProduct.Name",
                "IncludedSupplyChainTradeLineItem[1].SpecifiedTradeProduct.Description");
        InvoiceDOM dom = new InvoiceDOM(data);
        byte[] xml = dom.toXML();
        System.out.println(new String(xml));
    }
}
