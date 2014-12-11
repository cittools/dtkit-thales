<?xml version="1.0" encoding="UTF-8"?>
<!--
/*******************************************************************************
* Copyright (c) 2012 Thales Global Services SAS                                *
* Author : Aravindan Mahendran                                                 *
*                                                                              *
* The MIT license.                                                             *
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
                xmlns:violations="http://www.thalesgroup.com/tusar/violations/v4"
                xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="FxCopReport">

        <tusar:tusar
                xmlns:violations="http://www.thalesgroup.com/tusar/violations/v4"
                xmlns:tusar="http://www.thalesgroup.com/tusar/v10"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                version="10.0">

            <xsl:element name="tusar:violations">

                <xsl:attribute name="toolname">fxcop</xsl:attribute>
                
                <xsl:apply-templates select="//Issue"/>

            </xsl:element>
        </tusar:tusar>
    </xsl:template>
    
    <xsl:template match="Issue">
        <xsl:if test = "@File">
            <xsl:element name="violations:file">
                <xsl:attribute name="path"><xsl:value-of select="@Path"/>\<xsl:value-of select="@File"/></xsl:attribute>
                <xsl:element name="violations:violation">
                    <xsl:attribute name="line"><xsl:value-of select = "@Line"/></xsl:attribute>
                    <xsl:attribute name="key"><xsl:value-of select = "../@TypeName"/></xsl:attribute>
                    <xsl:attribute name="severity"><xsl:value-of select = "@Level"/></xsl:attribute>
                    <xsl:attribute name="message"><xsl:value-of select = "."/></xsl:attribute>
                </xsl:element>
            </xsl:element>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
