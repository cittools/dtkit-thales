package com.thalesgroup.dtkit.tusar.fxcop;

import javax.xml.bind.annotation.XmlType;

import org.jenkinsci.lib.dtkit.model.InputMetricXSL;
import org.jenkinsci.lib.dtkit.model.InputType;
import org.jenkinsci.lib.dtkit.model.OutputMetric;
import com.thalesgroup.dtkit.processor.InputMetric;
import com.thalesgroup.dtkit.tusar.model.TusarModel;

@XmlType(name = "FXCop", namespace = "tusar")
@InputMetric
public class FXCop extends InputMetricXSL {
	@Override
	public InputType getToolType() {
		return InputType.VIOLATION;
	}

	@Override
	public String getToolName() {
		return "FXCop";
	}

	@Override
	public String getToolVersion() {
		return "10.0";
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public OutputMetric getOutputFormatType() {
		return TusarModel.OUTPUT_TUSAR_10_0;
	}
	
	@Override
	public String getXslName() {
		return "fxcop-10.0-to-tusar-10.0.xsl";
	}

}
