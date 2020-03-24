<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
	<xsl:output method="xml" indent="yes" encoding="UTF-8" />
	
	<xsl:template match="/movies">
	<html>
		<head>
			<title>Movies</title>
			<meta name="description" content="Invoice" />
		</head>
		<body style="font-family: 'Noto Sans'">
			<h1>INVOICE #I/2017-0456</h1>
			<h2>August 20, 2017</h2>
			<table width="100%">
				<tr>
					<td width="20%" align="right" valign="top"><b>From:</b></td>
					<td width="30%">The Movie Company<br/>Hollywood Boulevard<br/>Los Angeles, California</td>
					<td width="20%" align="right" valign="top"><b>To:</b></td>
					<td width="30%">Woody Pride<br/>Toy Story Lane<br/>Paris, Texas</td>
				</tr>
			</table>
			<br />
			<table style="margin-bottom: 80px; border: solid black 1px" width="100%">
			<tr style="background: black; color: white;">
				<th>Item #</th>
				<th>Product</th>
				<th>Price</th>
				<th>Qty</th>
				<th>Sub</th>
				<th>Tax %</th>
				<th>Tax</th>
				<th>Total</th>
			</tr>
			<xsl:apply-templates />
			</table>
			<br />
			<table style="margin-bottom: 80px; border: solid black 1px" width="50%" align="right">
			<tr style="background: black; color: white;">
				<th>Base amount:</th>
				<th>Tax amount:</th>
				<th>Grand total:</th>
			</tr>
			<tr align="right">
				<td>$200.00</td>
				<th>$42.20</th>
				<th>$242.20</th>
			</tr>
			</table>
		</body>
	</html>
	</xsl:template>

    <xsl:template match="movie">
	<tr>
		<td><xsl:value-of select="id" /></td>
		<td><xsl:value-of select="title" /></td>
		<td style="text-align: right;">$10.00</td>
		<td style="text-align: right;">1</td>
		<td style="text-align: right;">$10.00</td>
		<td style="text-align: right;">21%</td>
		<td style="text-align: right;">$1.21</td>
		<td style="text-align: right;">$11.21</td>
	</tr>
	</xsl:template>
	
</xsl:stylesheet>