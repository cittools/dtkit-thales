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

package com.thalesgroup.dtkit.tusar.ncover;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.jenkinsci.lib.dtkit.model.InputMetricOther;
import org.jenkinsci.lib.dtkit.model.InputType;
import com.thalesgroup.dtkit.processor.InputMetric;
import com.thalesgroup.dtkit.tusar.model.TusarModel;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationError;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;



@XmlType(name = "NCover", namespace = "tusar")
@InputMetric
public class NCover extends InputMetricOther {
	@Override
    public InputType getToolType() {
        return InputType.COVERAGE;
    }

    @Override
    public String getToolName() {
        return "NCover";
    }

    @Override
    public String getToolVersion() {
        return "1.5.8";
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public void convert(File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
    	NCoverConvertor nCoverConvertor = new NCoverConvertor();
    	try {
			nCoverConvertor.convert(inputFile, outFile);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /*
     *  Gives the validation process for the output file
     *
     * @return true if the input file is valid, false otherwise
     */
    @Override
    public boolean validateOutputFile(File inputXMLFile) throws ValidationException {
        List<ValidationError> errors = TusarModel.OUTPUT_TUSAR_10_0.validate(inputXMLFile);
        this.setOutputValidationErrors(errors);
        return errors.isEmpty();
    }

	@Override
	public boolean validateInputFile(File inputXMLFile)
			throws ValidationException {
		return true;
	}
}
