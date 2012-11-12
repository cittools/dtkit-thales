<?xml version="1.0" encoding="UTF-8"?>
<!--
/*******************************************************************************
* Copyright (c) 2010 Thales Corporate Services SAS                             *
* Author : Gregory Boissinot                                                   *
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
                xmlns:tusar="http://www.thalesgroup.com/tusar/v2"
                xmlns:measures="http://www.thalesgroup.com/tusar/measures/v2"
                xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="*[local-name()='slcnt']">
        <tusar:tusar xmlns:xs="http://www.w3.org/2001/XMLSchema"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:tusar="http://www.thalesgroup.com/tusar/v2"
                     xmlns:measures="http://www.thalesgroup.com/tusar/measures/v2">
            <tusar:measures>
                <xsl:for-each select="*[local-name()='file']">
                    <measures:resource>

                        <xsl:attribute name="type">
                            <xsl:text>FILE</xsl:text>
                        </xsl:attribute>

                        <xsl:attribute name="value">
                            <xsl:value-of select="@name"/>
                        </xsl:attribute>

                        <xsl:for-each select="*[local-name()='metric']">
                            <measures:measure>
                                <xsl:choose>
                                    <xsl:when test="contains(@key, 'LT')">
                                        <xsl:attribute name="key">
                                            <xsl:text>LOC</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@value"/>
                                        </xsl:attribute>
                                    </xsl:when>
                                    <xsl:when test="contains(@key, 'LS')">
                                        <xsl:attribute name="key">
                                            <xsl:text>NCLOC</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@value"/>
                                        </xsl:attribute>
                                    </xsl:when>
                                    <xsl:when test="contains(@key, 'LI')">
                                        <xsl:attribute name="key">
                                            <xsl:text>INSTRUCTIONS</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@value"/>
                                        </xsl:attribute>
                                    </xsl:when>
                                    <xsl:when test="contains(@key, 'LC')">
                                        <xsl:attribute name="key">
                                            <xsl:text>COMMENTS</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@value"/>
                                        </xsl:attribute>
                                    </xsl:when>
                                    <xsl:when test="contains(@key, 'LM')">
                                        <xsl:attribute name="key">
                                            <xsl:text>MIXED</xsl:text>
                                        </xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="@value"/>
                                        </xsl:attribute>
                                    </xsl:when>
                                </xsl:choose>
                            </measures:measure>
                        </xsl:for-each>

                    </measures:resource>
                </xsl:for-each>
            </tusar:measures>
        </tusar:tusar>
    </xsl:template>

    <xsl:template match="text()|@*"/>
</xsl:stylesheet>