<!--
/*******************************************************************************
* Copyright (c) 2009 Thales Corporate Services SAS                             *
* Author : Mohamed Koundoussi                                *
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
                xmlns:measures="http://www.thalesgroup.com/tusar/measures/v4"
                xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:decimal-format name="euro" decimal-separator="," grouping-separator="." />
	<xsl:template match="sourcemonitor_metrics">
		<tusar:tusar xmlns:xs="http://www.w3.org/2001/XMLSchema"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:measures="http://www.thalesgroup.com/tusar/measures/v4"
             xmlns:tusar="http://www.thalesgroup.com/tusar/v4"
             version="4.0">
			 <xsl:element name="tusar:measures">
                <xsl:attribute name="toolname">sourcemonitor</xsl:attribute>
				<xsl:attribute name="version">
					<xsl:value-of select="/sourcemonitor_metrics/project/@version"/>
				</xsl:attribute>
				<xsl:if test="//project_language='Java'">				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M4'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M4')],',','.'))*100 div number(translate(metrics/metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>

								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M5'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M6']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M6'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M6']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M6')],',','.'))*number(translate(metrics/metric[(@id='M5')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M10'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M14'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M4'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M4')],',','.'))*100 div number(translate(metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M5'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M6']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M6'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M6']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M6')],',','.'))*number(translate(metric[(@id='M5')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M10'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
										<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M14'],',','.'))"/>
										</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>

				</xsl:if>
                
                
                
                
                
				<xsl:if test="//project_language='C'">
				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M3')],',','.'))*100 div number(translate(metrics/metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M4'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M8'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M12'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M3')],',','.'))*100 div number(translate(metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M4'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M8'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M12'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
					
				</xsl:if>
                
                
                
                
				<xsl:if test="//project_language='C++'">				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M3')],',','.'))*100 div number(translate(metrics/metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M5')],',','.'))*number(translate(metrics/metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M9'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M13'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M4']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M3')],',','.'))*100 div number(translate(metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>

								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M5')],',','.'))*number(translate(metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M9'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M13'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>

				</xsl:if>
                
                
                
                
                
				<xsl:if test="//project_language='VB.NET'">				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M2'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M2')],',','.'))*100 div number(translate(metrics/metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M3')],',','.'))*100 div number(translate(metrics/metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M5')],',','.'))*number(translate(metrics/metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M8'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M13'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M2'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M2')],',','.'))*100 div number(translate(metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M3')],',','.'))*100 div number(translate(metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M5')],',','.'))*number(translate(metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M8'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M13'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
				</xsl:if>
                
                
                
                
				<xsl:if test="//project_language='C#'">				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M2'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M2')],',','.'))*100 div number(translate(metrics/metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M3')],',','.'))*100 div number(translate(metrics/metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M5')],',','.'))*number(translate(metrics/metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M10'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M14'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">STATEMENTS</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M2'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M2']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M2')],',','.'))*100 div number(translate(metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M3'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M3']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">PUBLIC_DOCUMENTED_API_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M3')],',','.'))*100 div number(translate(metric[(@id='M1')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">CLASSES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M4'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M5'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M5']/@type='ratio'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">FUNCTIONS</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M5')],',','.'))*number(translate(metric[(@id='M4')],',','.')) div 100"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FUNCTION_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M10'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">FILE_COMPLEXITY</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M14'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
							</xsl:element>
						</xsl:for-each>
				</xsl:if>
                
                
                
                
				<xsl:if test="//project_language='HTML'">				
					
						<xsl:for-each select="//files/file">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">FILE</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="@file_name"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metrics/metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M1']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[@id='M1'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M1']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metrics/metric[(@id='M1')],',','.'))*100 div number(translate(metrics/metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>

							</xsl:element>
						</xsl:for-each>
						<xsl:for-each select="//checkpoints/checkpoint/metrics">
							<xsl:element name="measures:resource">
								<xsl:attribute name="type">PROJECT</xsl:attribute>
								<xsl:attribute name="value">
									<xsl:value-of select="//project_directory"/>
								</xsl:attribute>
								<xsl:element name="measures:measure">
									<xsl:attribute name="key">LINES</xsl:attribute>
									<xsl:attribute name="value">
										<xsl:value-of select="number(translate(metric[@id='M0'],',','.'))"/>
									</xsl:attribute>
								</xsl:element>
								<xsl:if test="//metric_names/metric_name[@id='M1']/@type='percent'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[@id='M1'],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
								<xsl:if test="//metric_names/metric_name[@id='M1']/@type='number'">
									<xsl:element name="measures:measure">
										<xsl:attribute name="key">COMMENT_LINES_DENSITY</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="number(translate(metric[(@id='M1')],',','.'))*100 div number(translate(metric[(@id='M0')],',','.'))"/>
										</xsl:attribute>
									</xsl:element>
								</xsl:if>
							</xsl:element>
						</xsl:for-each>
				</xsl:if>
                
                
                
                
			 </xsl:element>
		</tusar:tusar>
	</xsl:template>
</xsl:stylesheet>