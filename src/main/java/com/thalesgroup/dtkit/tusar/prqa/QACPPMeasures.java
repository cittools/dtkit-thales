package com.thalesgroup.dtkit.tusar.prqa;

import java.io.File;
import java.util.Map;

import javax.xml.bind.annotation.XmlType;

import com.thalesgroup.dtkit.metrics.model.InputMetricOther;
import com.thalesgroup.dtkit.metrics.model.InputType;
import com.thalesgroup.dtkit.processor.InputMetric;
import com.thalesgroup.dtkit.util.converter.ConversionException;
import com.thalesgroup.dtkit.util.validator.ValidationException;
import com.thalesgroup.dtkit.metrics.model.OutputMetric;
import com.thalesgroup.dtkit.tusar.model.TusarModel;

@XmlType(name = "QACPPMeasures", namespace = "tusar")
@InputMetric
public class QACPPMeasures extends InputMetricOther{

	@Override
	public InputType getToolType() {
		return InputType.MEASURE;
	}

	@Override
	public String getToolName() {
		return "QACPP";
	}

	@Override
	public String getToolVersion() {
		return "2.5.1-R";
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public OutputMetric getOutputFormatType() {
		return TusarModel.OUTPUT_TUSAR_10_0;
	}
	
	/**
	 * The inputFile must be a .met file
	 */
	@Override
	public void convert(File inputFile, File outFile, Map<String, Object> params)
			throws ConversionException {
		PRQAParser.convertPRQAMeasures(inputFile, outFile);
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
