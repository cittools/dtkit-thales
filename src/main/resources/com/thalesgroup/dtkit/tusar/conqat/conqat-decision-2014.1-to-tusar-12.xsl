<!--
/*******************************************************************************
* Copyright (c) 2014 Thales Global Services SAS                                *
* Author : Aravindan Mahendran                                                 *
*                                                                              *
* The MIT license                                                              *
*                                                                              *
* Permission is hereby granted, free of charge, to any person obtaining a copy *
* of this software and associated documentation files (the "Software"), to deal*
* in the Software without restriction, including without limitation the rights *
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell    *
* copies of the Software, and to permit persons to whom the Software is        *
* furnished to do so, subject to the following conditions:                     *
*                                                                              *
* The above copyright notice and this permission notice shall be included in   *
* all copies or substantial portions of the Software.                          *
*                                                                              *
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR   *
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,     *
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  *
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER       *
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,*
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN    *
* THE SOFTWARE.                                                                *
*******************************************************************************/
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:measures="http://www.thalesgroup.com/tusar/measures/v7"
                xmlns:size="http://www.thalesgroup.com/tusar/size/v2"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xmlnscq="http://conqat.cs.tum.edu/ns/node">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    
    <xsl:template match="/">
        <tusar:tusar xmlns:xs="http://www.w3.org/2001/XMLSchema"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:measures="http://www.thalesgroup.com/tusar/measures/v7"
                     xmlns:size="http://www.thalesgroup.com/tusar/size/v2"
                     xmlns:tusar="http://www.thalesgroup.com/tusar/v12"
                     version="12">
            <xsl:element name="tusar:measures">
                <xsl:element name="measures:size">
                    <xsl:attribute name="toolname">ConQATDecision</xsl:attribute>
                    <xsl:attribute name="version">2014.1</xsl:attribute>
                    <xsl:apply-templates select="//xmlnscq:node[@type='element']"/>
                </xsl:element>
            </xsl:element>
        </tusar:tusar>
    </xsl:template>
    
    <xsl:template match="xmlnscq:node">
        <xsl:element name="size:resource">
            <xsl:attribute name="type">FILE</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="substring-after(@id,'/')"/></xsl:attribute> <!-- Substring after because of the project name -->
            <xsl:element name="size:measure">
                <xsl:attribute name="key">uncovered_conditions</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="xmlnscq:value[@key='decision-coverage-volume']" /></xsl:attribute>
            </xsl:element>
			<xsl:element name="size:measure">
                <xsl:attribute name="key">statements</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="xmlnscq:value[@key='statement-coverage-volume']" /></xsl:attribute>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
</xsl:stylesheet>