<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:tusar="http://www.thalesgroup.com/tusar/v2"
                xmlns:violations="http://www.thalesgroup.com/tusar/violations/v2"
                exclude-result-prefixes="xsi xsl">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="cql">
        <tusar:tusar xmlns:xs="http://www.w3.org/2001/XMLSchema"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xmlns:tusar="http://www.thalesgroup.com/tusar/v2"
                     xmlns:measures="http://www.thalesgroup.com/tusar/measures/v2">
            <tusar:violations>
                <xsl:for-each-group select="data[string-length(module)>0]" group-by="module">
                    <violations:file path="{current-grouping-key()}">
                        <xsl:apply-templates select="current-group()">
                            <xsl:sort select="line" data-type="number"/>
                        </xsl:apply-templates>
                    </violations:file>
                </xsl:for-each-group>
            </tusar:violations>
        </tusar:tusar>
    </xsl:template>

    <xsl:template match="data[line>-1]">
        <violations:violation line="{line}" key="{rule}" message="problem with {label}"/>
    </xsl:template>

    <xsl:template match="text()"/>

</xsl:stylesheet>
