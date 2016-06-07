<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet exclude-result-prefixes="#all" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ram="urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12" xmlns:rsm="urn:ferd:CrossIndustryDocument:invoice:1p0" xmlns:udt="urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15" version="1.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" />
	<xsl:template match="/rsm:CrossIndustryDocument">
	<html>
	<head><link rel="stylesheet" type="text/css" href="invoice.css" /></head>
	<body><img src="logo.png" alt="Das Company - logo" /><br /><xsl:apply-templates /></body></html></xsl:template>

    <xsl:template match="rsm:SpecifiedExchangedDocumentContext" />
    <xsl:template match="rsm:HeaderExchangedDocument"><h1 id="header"><xsl:value-of select="ram:Name" /><xsl:text> </xsl:text><xsl:value-of select="ram:ID" /></h1>
		<xsl:apply-templates select="ram:IssueDateTime" />
	</xsl:template>
    <xsl:template match="rsm:SpecifiedSupplyChainTradeTransaction">
		<table width="100%" border="0" id="addresses">
		<tr>
		<td valign="Top" width="10%"><b>From:</b></td>
		<td valign="Top" width="40%"><xsl:for-each select="ram:ApplicableSupplyChainTradeAgreement/ram:SellerTradeParty">
			<xsl:call-template name="address" />
		</xsl:for-each>
		</td>
		<td valign="Top" width="10%"><b>To:</b></td>
		<td valign="Top" width="40%"><xsl:for-each select="ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty">
			<xsl:call-template name="address" />
		</xsl:for-each>
		</td>
		</tr>
		</table>
		<table width="100%" id="products">
		<tr class="headerrow"><th>#</th><th>Product</th><th>Unit</th><th>Qty.</th><th>Sub.</th><th>Tax%</th><th>Tax</th><th>Total</th><th>Curr.</th></tr>
		<xsl:for-each select="ram:IncludedSupplyChainTradeLineItem">
		<tr>
		<td align="Right"><xsl:value-of select="ram:AssociatedDocumentLineDocument/ram:LineID" />.</td>
		<td class="bold"><xsl:value-of select="ram:SpecifiedTradeProduct/ram:Name" /></td>
		<td align="Right"><xsl:call-template name="twodecimals"><xsl:with-param name="number" select="ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:ChargeAmount" /></xsl:call-template></td>
		<td align="Right"><xsl:call-template name="twodecimals"><xsl:with-param name="number" select="ram:SpecifiedSupplyChainTradeDelivery/ram:BilledQuantity" /></xsl:call-template></td>
		<td align="Right"><xsl:value-of select="ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount" /></td>
		<td align="Right"><xsl:value-of select="ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent" />%</td>
		<td align="Right"><xsl:call-template name="calcTax">
		<xsl:with-param name="basis" select="ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount" />
		<xsl:with-param name="tax" select="ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent" />
		</xsl:call-template></td>
		<td align="Right" class="bold"><xsl:call-template name="calcWithTax">
		<xsl:with-param name="basis" select="ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount" />
		<xsl:with-param name="tax" select="ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent" />
		</xsl:call-template></td>
		<td><xsl:value-of select="ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount/@currencyID" /></td>
		</tr>
		</xsl:for-each>
		</table>
		<table width="100%" id="totals">
		<tr class="headerrow"><th>Tax</th><th>%</th><th>Base amount:</th><th>Tax amount:</th><th>Total</th><th>Curr.</th></tr>
		<xsl:for-each select="ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax">
		<tr>
		<td align="Right"><xsl:value-of select="ram:TypeCode" /></td>
		<td align="Right"><xsl:value-of select="ram:ApplicablePercent" /></td>
		<td align="Right"><xsl:value-of select="ram:BasisAmount" /></td>
		<td align="Right"><xsl:value-of select="ram:CalculatedAmount" /></td>
		<td align="Right"><xsl:call-template name="calcWithTax">
		<xsl:with-param name="basis" select="ram:BasisAmount" />
		<xsl:with-param name="tax" select="ram:ApplicablePercent" />
		</xsl:call-template></td>
		<td><xsl:value-of select="ram:CalculatedAmount/@currencyID" /></td>
		</tr>
		</xsl:for-each>
		<tr>
		<td class="total" align="Right" colspan="2">Grand total:</td>
		<td class="total" align="Right"><xsl:value-of select="ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:TaxBasisTotalAmount" /></td>
		<td class="total" align="Right"><xsl:value-of select="ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:TaxTotalAmount" /></td>
		<td class="total" align="Right"><xsl:value-of select="ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:GrandTotalAmount" /></td>
		<td class="total"><xsl:value-of select="ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:GrandTotalAmount/@currencyID" /></td>
		</tr>
		</table>
		<table id="wireinfo">
		<tr><td colspan="4">Please wire the amount due to our bank account using the following reference: <xsl:value-of select="ram:ApplicableSupplyChainTradeSettlement/ram:PaymentReference" /></td></tr>
		<tr><th></th><th class="wireheader">Bank</th><th class="wireheader">BIC ID</th><th class="wireheader">IBAN Number</th></tr>
		<xsl:for-each select="ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans">
		<tr>
		<td></td>
		<td><xsl:value-of select="ram:PayeeSpecifiedCreditorFinancialInstitution/ram:Name" /></td>
		<td><xsl:value-of select="ram:PayeeSpecifiedCreditorFinancialInstitution/ram:BICID" /></td>
		<td><xsl:value-of select="ram:PayeePartyCreditorFinancialAccount/ram:IBANID" /></td>
		</tr>
		</xsl:for-each>
		</table>
	</xsl:template>
	
	
	<xsl:template match="udt:DateTimeString"><h2 id="date"><xsl:choose>
			<xsl:when test="@format='610'"><xsl:call-template name="YYYYMM"><xsl:with-param name="date" select="." /></xsl:call-template></xsl:when>
			<xsl:when test="@format='616'"><xsl:call-template name="YYYYWW"><xsl:with-param name="date" select="." /></xsl:call-template></xsl:when>
			<xsl:otherwise><xsl:call-template name="YYYYMMDD"><xsl:with-param name="date" select="." /></xsl:call-template></xsl:otherwise>
		</xsl:choose></h2>
	</xsl:template>
	
	<xsl:template name="address">
		<span class="name"><xsl:value-of select="ram:Name" /></span><br />
		<xsl:value-of select="ram:PostalTradeAddress/ram:LineOne" /><br />
		<xsl:value-of select="ram:PostalTradeAddress/ram:LineTwo" /><br />
		<xsl:value-of select="ram:PostalTradeAddress/ram:CountryID" />-<xsl:value-of select="ram:PostalTradeAddress/ram:PostcodeCode" /><xsl:text> </xsl:text><xsl:value-of select="ram:PostalTradeAddress/ram:CityName" /><br />
		<xsl:for-each select="ram:SpecifiedTaxRegistration/ram:ID">
			<xsl:value-of select="@schemeID" />: <xsl:value-of select="." /><br />
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="calcTax">
        <xsl:param name="basis" />
        <xsl:param name="tax" />
		<xsl:call-template name="twodecimals"><xsl:with-param name="number" select="($basis * $tax) div 100" /></xsl:call-template>
	</xsl:template>
	<xsl:template name="calcWithTax">
        <xsl:param name="basis" />
        <xsl:param name="tax" />
		<xsl:call-template name="twodecimals"><xsl:with-param name="number" select="$basis + (($basis * $tax) div 100)" /></xsl:call-template>
	</xsl:template>
	<xsl:template name="twodecimals">
        <xsl:param name="number" />
		<xsl:value-of select="format-number(round($number*100) div 100, '0.00')"/>
	</xsl:template>
	<xsl:template name="YYYYMMDD">
        <xsl:param name="date" />
		<xsl:value-of select="substring($date,1,4)" />-<xsl:value-of select="substring($date,5,2)" />-<xsl:value-of select="substring($date,7,2)" />
    </xsl:template>
	<xsl:template name="YYYYMM">
        <xsl:param name="date" />
		<xsl:value-of select="substring($date,1,4)" />-<xsl:value-of select="substring($date,5,2)" />
    </xsl:template>
	<xsl:template name="YYYYWW">
        <xsl:param name="date" />
		<xsl:value-of select="substring($date,1,4)" />; week <xsl:value-of select="substring($date,5,2)" />
    </xsl:template>
</xsl:stylesheet>