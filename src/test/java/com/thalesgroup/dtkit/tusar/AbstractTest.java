package com.thalesgroup.dtkit.tusar;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.jenkinsci.lib.dtkit.model.InputMetric;
import org.jenkinsci.lib.dtkit.model.InputMetricXSL;
import com.thalesgroup.dtkit.tusar.model.TusarModel;
import org.jenkinsci.lib.dtkit.util.converter.ConversionService;
import org.jenkinsci.lib.dtkit.util.validator.ValidationError;
import org.jenkinsci.lib.dtkit.util.validator.ValidationService;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.util.List;


public class AbstractTest {

    private static Injector injector;

    @BeforeClass
    public static void initInjector() {
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                //Optional binding, provided by default in Guice)
                bind(ValidationService.class).in(Singleton.class);
                bind(ConversionService.class).in(Singleton.class);
            }
        });
    }


    @Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }


    public void convertAndValidate(Class<? extends InputMetricXSL> classType, String inputXMLPath, String expectedResultPath) throws Exception {

        InputMetricXSL inputMetricXSL = injector.getInstance(classType);

        File outputXMLFile = File.createTempFile("result", "xml");
        File inputXMLFile = new File(this.getClass().getResource(inputXMLPath).toURI());

        //The input file must be valid
        boolean inputResult = inputMetricXSL.validateInputFile(inputXMLFile);
        for (ValidationError validatorError : inputMetricXSL.getInputValidationErrors()) {
             System.out.println("[ERROR] " + validatorError.toString());
        }
        Assert.assertTrue(inputResult);

        inputMetricXSL.convert(inputXMLFile, outputXMLFile);
        Diff myDiff = new Diff(XSLUtil.readXmlAsString(new File(this.getClass().getResource(expectedResultPath).toURI())), XSLUtil.readXmlAsString(outputXMLFile));
        Assert.assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());

        //The generated output file must be valid
        boolean result = inputMetricXSL.validateOutputFile(outputXMLFile);
        for (ValidationError validatorError : inputMetricXSL.getOutputValidationErrors()) {
            System.out.println("[ERROR] " + validatorError.toString());
        }
        Assert.assertTrue(result);

        outputXMLFile.deleteOnExit();
    }
    
    public void convertAndValidateWithoutXSL(Class<? extends InputMetric> inputMetricClassType, String inputXMLPath, String expectedResultPath) throws Exception {

        InputMetric inputMetric = injector.getInstance(inputMetricClassType);

        File outputXMLFile = File.createTempFile("result", "xml");
        File inputXMLFile = new File(this.getClass().getResource(inputXMLPath).toURI());

        //The input file must be valid
        boolean resultInput = inputMetric.validateInputFile(inputXMLFile);
        for (ValidationError validatorError : inputMetric.getInputValidationErrors()) {
            System.out.println("[ERROR] " + validatorError.toString());
        }
        Assert.assertTrue(resultInput);


        inputMetric.convert(inputXMLFile, outputXMLFile);
        Diff myDiff = new Diff(XSLUtil.readXmlAsString(new File(this.getClass().getResource(expectedResultPath).toURI())), XSLUtil.readXmlAsString(outputXMLFile));
        Assert.assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());

        //The generated output file must be valid
        boolean resultOutput = inputMetric.validateOutputFile(outputXMLFile);
        for (ValidationError validatorError : inputMetric.getOutputValidationErrors()) {
            System.out.println("[ERROR] " + validatorError.toString());
        }
        Assert.assertTrue(resultOutput);

        outputXMLFile.deleteOnExit();
    }

    public void validOutputTusarV7(File inputXMLFile) {
        List<ValidationError> errors = TusarModel.OUTPUT_TUSAR_7_0.validate(inputXMLFile);
        for (ValidationError validatorError : errors) {
            System.out.println("[ERROR] " + validatorError.toString());
        }
        Assert.assertTrue(errors.isEmpty());
    }

}
