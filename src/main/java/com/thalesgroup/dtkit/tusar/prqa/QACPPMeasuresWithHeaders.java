/*******************************************************************************
 * Copyright (c) 2011 THALES SYSTEMS ROMÂNIA SRL                                *
 * Author : THALES SYSTEMS ROMÂNIA SRL                                          *
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


package com.thalesgroup.dtkit.tusar.prqa;

import java.io.File;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.jenkinsci.lib.dtkit.model.InputMetricOther;
import org.jenkinsci.lib.dtkit.model.InputType;
import com.thalesgroup.dtkit.processor.InputMetric;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;
import org.jenkinsci.lib.dtkit.model.OutputMetric;
import com.thalesgroup.dtkit.tusar.model.TusarModel;

@XmlType(name = "QACPPMeasuresWithHeaders", namespace = "tusar")
@InputMetric
@SuppressWarnings( "serial" )
public class QACPPMeasuresWithHeaders extends InputMetricOther{

	@Override
	public InputType getToolType() {
		return InputType.MEASURE;
	}

	@Override
	public String getToolName() {
		return "QACPP_Headers";
	}

	@Override
	public String getToolVersion() {
		return "3.0-R";
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public OutputMetric getOutputFormatType() {
		return TusarModel.OUTPUT_TUSAR_11_0;
	}
	
	/**
	 * The inputFile must be a .met file
	 */
	@Override
	public void convert(File inputFile, File outFile, Map<String, Object> params)
			throws ConversionException {
		outFile = PRQAParser.changeOutputFileName(inputFile, outFile);
		PRQAParser.convertPRQAMeasures(inputFile, outFile, true, true);
	}

	@Override
	public boolean validateInputFile(File inputXMLFile)
			throws ValidationException {
		return true;
	}

	@Override
	public boolean validateOutputFile(File inputXMLFile)
			throws ValidationException {
		return true;
	}
}
