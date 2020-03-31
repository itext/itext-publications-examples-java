<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
	<xsl:output method="xml" indent="yes" encoding="UTF-8" />
	
	<xsl:template match="/movies">
	<html>
		<head>
			<title>Movies</title>
			<meta name="description" content="Selection of movies screened at SXSW 2017" />
			<link rel="stylesheet" type="text/css" href="css/movie.css" />
		</head>
		<body>
			<xsl:apply-templates />
		</body>
	</html>
	</xsl:template>

    <xsl:template match="movie">
	<div style="margin-bottom: 60pt;">
		<xsl:element name="img">
			<xsl:attribute name="src">img/<xsl:value-of select="poster"/></xsl:attribute>
			<xsl:attribute name="class">poster</xsl:attribute>
		</xsl:element>
		<h1 id="header"><xsl:value-of select="title" /> (<xsl:value-of select="year" />)</h1>
		<p class="director">Directed by <xsl:value-of select="director" /></p>
		<div class="description"><xsl:value-of select="description" /></div>
		<div class="imdb">Read more about this movie on <xsl:element name="a"><xsl:attribute name="href">https://www.imdb.com/title/tt<xsl:value-of select="id"/></xsl:attribute>IMDB</xsl:element></div>
	</div>
	</xsl:template>
	
</xsl:stylesheet>