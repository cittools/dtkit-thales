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

package com.thalesgroup.dtkit.tusar.emma;

import java.io.File;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import org.jenkinsci.lib.dtkit.model.InputMetricOther;
import org.jenkinsci.lib.dtkit.model.InputType;
import com.thalesgroup.dtkit.processor.InputMetric;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;



@XmlType(name = "Emma", namespace = "tusar")
@InputMetric
public class Emma extends InputMetricOther {
	@Override
    public InputType getToolType() {
        return InputType.COVERAGE;
    }

    @Override
    public String getToolName() {
        return "Emma";
    }

    @Override
    public String getToolVersion() {
        return "2.0.5312";
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public void convert(File inputFile, File outFile, Map<String, Object> params) throws ConversionException {
    	EmmaConvertor emc = new EmmaConvertor();
    	emc.convert(inputFile, outFile);
    }

    /*
     *  Gives the validation process for the output file
     *
     * @return true if the input file is valid, false otherwise
     */
    @Override
    public boolean validateOutputFile(File inputXMLFile) throws ValidationException {
        return true;
    }

	@Override
	public boolean validateInputFile(File inputXMLFile)
			throws ValidationException {
		return true;
	}
}
