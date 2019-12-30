<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
	<xsl:output method="xml" indent="yes" encoding="UTF-8" />
	
	<xsl:template match="/list">
	<html>
		<head>
			<title>Peace in different languages</title>
			<meta name="description" content="A list of languages, along with the word PEACE in that language." />
			<style>
				tr:nth-child(odd) { background: #c0c0c0; }
			</style>
		</head>
		<body>
			<h1>Saying Peace in different languages</h1>
			<table>
			<xsl:apply-templates />
			</table>
		</body>
	</html>
	</xsl:template>

    <xsl:template match="pace">
	<tr>
		<td><xsl:value-of select="./@language" /></td>
		<td><xsl:value-of select="./@countries" /></td>
		<td><xsl:value-of select="." /></td>
	</tr>
	</xsl:template>
	
</xsl:stylesheet>