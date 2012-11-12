package com.thalesgroup.dtkit.tusar;

import org.junit.Test;


public class LogiscopeTest extends AbstractTest {
    
    @Test
    public void violationsTestCase1() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeRuleChecker.class, "logiscope/violations/testcase1/input.csv", "logiscope/violations/testcase1/result.xml");
    }
    
    @Test
    public void adaMeasuresTestCase1() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditAda.class, "logiscope/measures/ada/testcase1/input.csv", "logiscope/measures/ada/testcase1/result.xml");
    }
    
    @Test
    public void adaMeasuresTestCase2() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditAda.class, "logiscope/measures/ada/testcase2/input.csv", "logiscope/measures/ada/testcase2/result.xml");
    }
    
    @Test
    public void cMeasuresTestCase1() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditC.class, "logiscope/measures/c/testcase1/input.csv", "logiscope/measures/c/testcase1/result.xml");
    }
    
    @Test
    public void cMeasuresTestCase2() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditC.class, "logiscope/measures/c/testcase2/input.csv", "logiscope/measures/c/testcase2/result.xml");
    }
    
    @Test
    public void cppMeasuresTestCase1() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditCpp.class, "logiscope/measures/cpp/testcase1/input.csv", "logiscope/measures/cpp/testcase1/result.xml");
    }
    
    @Test
    public void cppMeasuresTestCase2() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditCpp.class, "logiscope/measures/cpp/testcase2/input.csv", "logiscope/measures/cpp/testcase2/result.xml");
    }
    
    @Test
    public void javaMeasuresTestCase1() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditJava.class, "logiscope/measures/java/testcase1/input.csv", "logiscope/measures/java/testcase1/result.xml");
    }
    
    @Test
    public void javaMeasuresTestCase2() throws Exception {
    	convertAndValidateWithoutXSL(LogiscopeAuditJava.class, "logiscope/measures/java/testcase2/input.csv", "logiscope/measures/java/testcase2/result.xml");
    }

}