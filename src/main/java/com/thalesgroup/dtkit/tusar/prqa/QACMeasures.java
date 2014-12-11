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

@XmlType(name = "QACPPMeasures", namespace = "tusar")
@InputMetric
@SuppressWarnings( "serial" )
public class QACMeasures extends InputMetricOther{

	@Override
	public InputType getToolType() {
		return InputType.MEASURE;
	}

	@Override
	public String getToolName() {
		return "QAC";
	}

	@Override
	public String getToolVersion() {
		return "7.2-R";
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
		PRQAParser.convertPRQAMeasures(inputFile, outFile, false, false);
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
