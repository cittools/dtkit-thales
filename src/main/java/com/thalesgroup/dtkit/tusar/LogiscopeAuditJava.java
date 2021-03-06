/*******************************************************************************
 * Copyright (c) 2011 Thales Corporate Services SAS                             *
 * Author : Aravindan Mahendran                                                 *
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

package com.thalesgroup.dtkit.tusar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.jenkinsci.lib.dtkit.model.InputMetricOther;
import org.jenkinsci.lib.dtkit.model.InputType;
import org.jenkinsci.lib.dtkit.model.OutputMetric;
import com.thalesgroup.dtkit.processor.InputMetric;
import com.thalesgroup.dtkit.tusar.model.TusarModel;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationError;
import org.jenkinsci.lib.dtkit.util.validator.ErrorType;

@XmlType(name = "LogiscopeAuditJava", namespace = "tusar")
@InputMetric
public class LogiscopeAuditJava extends InputMetricOther {
	
	@Override
	public InputType getToolType() {
		return InputType.MEASURE;
	}

	@Override
	public String getToolName() {
		return "LogiscopeForJava";
	}

	@Override
	public String getToolVersion() {
		return "6.5.1.0";
	}

	@Override
	public boolean isDefault() {
		return true;
	}
	
	@Override
	public OutputMetric getOutputFormatType() {
		return TusarModel.OUTPUT_TUSAR_4_0;
	}

	@Override
	public void convert(File inputFile, File outFile, Map<String, Object> params)
	throws ConversionException {
		if (!LogiscopeCSVParser.isCSVFileEmpty(inputFile)){
			LogiscopeCSVParser.javaMetricParsing(inputFile, outFile);
		}
	}

	@Override
	public boolean validateInputFile(File inputFile)
	throws ValidationException {
		boolean isCSVFile = false;
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (LogiscopeCSVParser.isCSVFile(inputFile)){
			isCSVFile=true;
		}
		else {
			ValidationError error = new ValidationError(ErrorType.ERROR, -1, null, inputFile.getName()+" is not a CSV file. It won't be converted.");
			errors.add(error);
		}
		if (!LogiscopeCSVParser.isJavaCSVFile(inputFile) && isCSVFile){
			ValidationError error = new ValidationError(ErrorType.WARNING, -1, null, inputFile.getName()+" is not a correct Java logiscope report. It won't be converted.");
			errors.add(error);
		}
		else if (isCSVFile && LogiscopeCSVParser.isCSVFileEmpty(inputFile)){
			ValidationError error = new ValidationError(ErrorType.WARNING, -1, null, inputFile.getName()+" does not contain any measures. It won't be converted.");
			errors.add(error);
		}
		setInputValidationErrors(errors);
		return isCSVFile;
	}

	@Override
	public boolean validateOutputFile(File inputXMLFile)
	throws ValidationException {
		return true;
	}

}
