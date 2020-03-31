<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
	<xsl:output method="xml" indent="yes" encoding="UTF-8" />
	
	<xsl:template match="/movies">
	<html>
		<head>
			<title>Movies</title>
			<meta name="description" content="Selection of movies screened at SXSW 2017" />
			<style>
				tr:nth-child(odd) { background: #cc66ff; }
				tr:nth-child(even) { background: #ffff99; }
			</style>
		</head>
		<body>
			<h1>A selection of SXSW 2017 movies</h1>
			<table style="margin-bottom: 80px;">
			<xsl:apply-templates />
			</table>
		</body>
	</html>
	</xsl:template>

    <xsl:template match="movie">
	<tr>
		<td align="center"><b><xsl:value-of select="year" /></b></td>
		<td><b><xsl:value-of select="title" /></b></td>
	</tr>
	<tr>
		<td rowspan="2" valign="top"><xsl:element name="a">
			<xsl:attribute name="href">https://www.imdb.com/title/tt<xsl:value-of select="id"/></xsl:attribute>
			<xsl:element name="img">
				<xsl:attribute name="src">img/<xsl:value-of select="poster"/></xsl:attribute>
				<xsl:attribute name="width">45px</xsl:attribute>
			</xsl:element>
		</xsl:element></td>
		<td><i>Directed by <xsl:value-of select="director" /></i></td>
	</tr>
	<tr>
		<td><xsl:value-of select="description" /></td>
	</tr>
	</xsl:template>
	
</xsl:stylesheet>