/*******************************************************************************
 * Copyright (c) 2011 Thales Corporate Services SAS                             *
 * Author : Aravindan Mahendran                                                 *
 *                                                                              *
 * The MIT license                                                              *
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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thalesgroup.dtkit.tusar.prqa.PRQAConstants.PRQAComponentType;

import com.thalesgroup.tusar.measures.v6.MeasuresComplexType;
import com.thalesgroup.tusar.size.v1.SizeComplexType;
import com.thalesgroup.tusar.size.v1.SizeComplexType.Resource.Measure;
import com.thalesgroup.tusar.v10.Tusar;
import com.thalesgroup.tusar.violations.v4.ViolationsComplexType;

/**
 * This class gives utility functions to parse QAC 7.2-R and QACPP 2.5.1-R reports (Text files).
 *
 */
public class PRQAParser {


	public static final String SEPARATOR = ",";

	private static final String TUSAR_VERSION = "10.0";

	private static final String TUSAR_XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";

	

	/**
	 * Maps which convert QACPP / QAC metrics into Sonar Metrics
	 */
	private static final Map<String,String> sonarFunctionMetrics = new HashMap<String, String>();
	private static final Map<String,String> sonarFileMetrics = new HashMap<String, String>();

	/* 
	 * Those lists are actually empty but it could be filled used with future QAC/QACPP releases
	 */
	private static final Map<String,String> sonarClassMetrics = new HashMap<String, String>();
	private static final Map<String,String> sonarProjectMetrics = new HashMap<String, String>();


	/**
	 * Lists which will be used to know in which kind of element we are (class, function, file...) in QACPP
	 * FILE = STTPP, STTLN, STCCA, STCCB, STCCC, STCDN, STTOT, STVAR, STOPT, STOPN
	 * CLASS = STDIT, STNOP, STMTH
	 * CLASS 2 = STNOC, STWMC, STRFC, STCBO, STLCM (They are in another .met file)
	 * FUNCTION = STSUB, STXLN, STGTO, STCYC, STPAR, STPTH, STMIF, STLIN
	 */
	private static final List<String> qacppFileMetrics = new ArrayList<String>();
	private static final List<String> qacppClassMetrics = new ArrayList<String>();
	private static final List<String> qacppClassMetrics2 = new ArrayList<String>();
	private static final List<String> qacppFunctionMetrics = new ArrayList<String>();

	/**
	 * Lists which will be used to know in which kind of element we are (function, file, project) in QAC
	 */
	private static final List<String> qacProjectMetrics = new ArrayList<String>();
	private static final List<String> qacFileMetrics = new ArrayList<String>();
	private static final List<String> qacFunctionMetrics = new ArrayList<String>();

	static {

		//Filling Sonar Maps
		sonarFunctionMetrics.put(PRQAConstants.STCYC, "FUNCTION_COMPLEXITY");
		sonarFileMetrics.put(PRQAConstants.STTPP, "LINES");
		sonarFileMetrics.put(PRQAConstants.STCYC, "COMPLEXITY");

		//Filling the QACPP file metrics list
		qacppFileMetrics.add(PRQAConstants.STTPP); //Total unpreprocessed source lines
		qacppFileMetrics.add(PRQAConstants.STTLN); //Total preprocessed code lines
		qacppFileMetrics.add(PRQAConstants.STCCA); //Total Number of Characters
		qacppFileMetrics.add(PRQAConstants.STCCB); //Total Number of Code Characters
		qacppFileMetrics.add(PRQAConstants.STCCC); //Total Number of Comment Characters
		qacppFileMetrics.add(PRQAConstants.STCDN); //Comment to code ratio
		qacppFileMetrics.add(PRQAConstants.STTOT); //Total number of tokens used
		qacppFileMetrics.add(PRQAConstants.STVAR); //Number of identifiers
		qacppFileMetrics.add(PRQAConstants.STOPT); //Halstead distinct operators
		qacppFileMetrics.add(PRQAConstants.STOPN); //Halstead distinct operands

		//Filling the QACPP class metrics list
		qacppClassMetrics.add(PRQAConstants.STDIT); //Depth of Inheritance
		qacppClassMetrics.add(PRQAConstants.STNOP); //Number of immediate children
		qacppClassMetrics.add(PRQAConstants.STMTH); //Number of methods available in class

		//Filling the QACPP second class metrics list
		qacppClassMetrics2.add(PRQAConstants.STNOC); //Number of immediate children
		qacppClassMetrics2.add(PRQAConstants.STWMC); //Weighted methods per class
		qacppClassMetrics2.add(PRQAConstants.STRFC); //Response for class
		qacppClassMetrics2.add(PRQAConstants.STCBO); //Coupling between objects
		qacppClassMetrics2.add(PRQAConstants.STLCM); //Lack of cohesion of methods within a class


		//Filling the QACPP function metrics list
		qacppFunctionMetrics.add(PRQAConstants.STSUB); //Number of Function Calls
		qacppFunctionMetrics.add(PRQAConstants.STXLN); //Number of executable lines
		qacppFunctionMetrics.add(PRQAConstants.STGTO); //Number of goto statements
		qacppFunctionMetrics.add(PRQAConstants.STCYC); //Cyclomatic complexity
		qacppFunctionMetrics.add(PRQAConstants.STPAR); //Number of parameters
		qacppFunctionMetrics.add(PRQAConstants.STPTH); //Estimated static path count
		qacppFunctionMetrics.add(PRQAConstants.STMIF); //Maximum nesting of control structures
		qacppFunctionMetrics.add(PRQAConstants.STLIN); //Number of maintainable lines of code


		//Filling the QAC project metrics list
		qacProjectMetrics.add(PRQAConstants.STNRA); //Number of Recursions Across Project
		qacProjectMetrics.add(PRQAConstants.STNEA); //Number of Entry Points Across Project
		qacProjectMetrics.add(PRQAConstants.STNFA); //Number of Functions Across Project
		qacProjectMetrics.add(PRQAConstants.STCYA); //Cyclomatic Complexity Across Project

		//Filling the QAC file metrics list
		qacFileMetrics.add(PRQAConstants.STBME); //COCOMO Embedded Programmer Months
		qacFileMetrics.add(PRQAConstants.STBMO); //COCOMO Organic Programmer Months
		qacFileMetrics.add(PRQAConstants.STBMS); //COCOMO Semi-detached Programmer Months
		qacFileMetrics.add(PRQAConstants.STBUG); //Residual Bugs (token-based estimate)
		qacFileMetrics.add(PRQAConstants.STCDN); //Comment to Code Ratio
		qacFileMetrics.add(PRQAConstants.STDEV); //Estimated Development Time
		qacFileMetrics.add(PRQAConstants.STDIF); //Program Difficulty
		qacFileMetrics.add(PRQAConstants.STECT); //Number of External Variables
		qacFileMetrics.add(PRQAConstants.STEFF); //Program Effort
		qacFileMetrics.add(PRQAConstants.STFCO); //Estimated Function Coupling
		qacFileMetrics.add(PRQAConstants.STFNC); //Number of Function Definitions
		qacFileMetrics.add(PRQAConstants.STHAL); //Halstead Prediction Of STTOT
		qacFileMetrics.add(PRQAConstants.STM20); //Number of Operand Occurrences
		qacFileMetrics.add(PRQAConstants.STM21); //Number of Operator Occurrences
		qacFileMetrics.add(PRQAConstants.STM22); //Number of Statements
		qacFileMetrics.add(PRQAConstants.STM28); //Number of Non-Header Comments
		qacFileMetrics.add(PRQAConstants.STM33); //Number of Internal Comments
		qacFileMetrics.add(PRQAConstants.STMOB); //Code Mobility
		qacFileMetrics.add(PRQAConstants.STOPN); //Halstead Distinct Operands
		qacFileMetrics.add(PRQAConstants.STOPT); //Halstead Distinct Operators
		qacFileMetrics.add(PRQAConstants.STPRT); //Estimated Porting Time
		qacFileMetrics.add(PRQAConstants.STSCT); //Number of Static Variables
		qacFileMetrics.add(PRQAConstants.STSHN); //Shannon Information Content
		qacFileMetrics.add(PRQAConstants.STTDE); //COCOMO Embedded Total Months
		qacFileMetrics.add(PRQAConstants.STTDO); //COCOMO Organic Total Months
		qacFileMetrics.add(PRQAConstants.STTDS); //COCOMO Semi-detached Total Months
		qacFileMetrics.add(PRQAConstants.STTLN); //Total Preprocessed Source Lines
		qacFileMetrics.add(PRQAConstants.STTOT); //Total Number of Tokens
		qacFileMetrics.add(PRQAConstants.STTPP); //Total Unpreprocessed Source Lines
		qacFileMetrics.add(PRQAConstants.STVAR); //Number of Identifiers
		qacFileMetrics.add(PRQAConstants.STVOL); //Program Volume
		qacFileMetrics.add(PRQAConstants.STZIP); //Zipf Prediction of STTOT

		//Filling the QAC function metrics list
		qacFunctionMetrics.add(PRQAConstants.STAKI); //Akiyama's Criterion
		qacFunctionMetrics.add(PRQAConstants.STAV1); //Average Size of Function Statements (variant 1)
		qacFunctionMetrics.add(PRQAConstants.STAV2); //Average Size of Function Statements (variant 2)
		qacFunctionMetrics.add(PRQAConstants.STAV3); //Average Size of Function Statements (variant 3)
		qacFunctionMetrics.add(PRQAConstants.STBAK); //Number of Backward Jumps
		qacFunctionMetrics.add(PRQAConstants.STCAL); //Number of Distinct Function Calls
		qacFunctionMetrics.add(PRQAConstants.STCYC); //Cyclomatic Complexity
		qacFunctionMetrics.add(PRQAConstants.STELF); //Number of Dangling Else-Ifs
		qacFunctionMetrics.add(PRQAConstants.STFN1); //Number of Function Operator Occurrences
		qacFunctionMetrics.add(PRQAConstants.STFN2); //Number of Function Operand Occurrences
		qacFunctionMetrics.add(PRQAConstants.STGTO); //Number of Goto statements
		qacFunctionMetrics.add(PRQAConstants.STKDN); //Knot Density
		qacFunctionMetrics.add(PRQAConstants.STKNT); //Knot Count
		qacFunctionMetrics.add(PRQAConstants.STLCT); //Number of Local Variables Declared
		qacFunctionMetrics.add(PRQAConstants.STLIN); //Number of Maintainable Code Lines
		qacFunctionMetrics.add(PRQAConstants.STLOP); //Number of Logical Operators
		qacFunctionMetrics.add(PRQAConstants.STM07); //Essential Cyclomatic Complexity
		qacFunctionMetrics.add(PRQAConstants.STM19); //Number of Exit Points
		qacFunctionMetrics.add(PRQAConstants.STM29); //Number of Functions Calling this Function
		qacFunctionMetrics.add(PRQAConstants.STMCC); //Myer’s Interval
		qacFunctionMetrics.add(PRQAConstants.STMIF); //Maximum Nesting of Control Structures
		qacFunctionMetrics.add(PRQAConstants.STPAR); //Number of Function Parameters
		qacFunctionMetrics.add(PRQAConstants.STPBG); //Path-Based Residual Bug Estimate
		qacFunctionMetrics.add(PRQAConstants.STPDN); //Path Density
		qacFunctionMetrics.add(PRQAConstants.STPTH); //Estimated Static Path Count
		qacFunctionMetrics.add(PRQAConstants.STRET); //Number of Function Return Points
		qacFunctionMetrics.add(PRQAConstants.STST1); //Number of Statements in Function (variant 1)
		qacFunctionMetrics.add(PRQAConstants.STST2); //Number of Statements in Function (variant 2)
		qacFunctionMetrics.add(PRQAConstants.STST3); //Number of Statements in Function (variant 3)
		qacFunctionMetrics.add(PRQAConstants.STSUB); //Number of Function Calls
		qacFunctionMetrics.add(PRQAConstants.STUNR); //Number of Unreachable Statements
		qacFunctionMetrics.add(PRQAConstants.STUNV); //Number of Unused and Non-Reused Variables
		qacFunctionMetrics.add(PRQAConstants.STXLN); //Number of Executable Lines
	}


	/**
	 * Convert QAC and QACPP violations into a TUSAR file
	 * @param input PRQA report generated by a specific command (see ThalesControl SUM part QAC/QACPP)
	 * @param output the TUSAR report that will be generated.
	 * @throws FileNotFoundException
	 */
	public static void convertPRQAViolationsIntoTusar(String prqaType, File input, File output) throws FileNotFoundException{

		//To store the different violations found in a CSV file
		Map<String, List<String[]>> violationsPerFiles = new HashMap<String, List<String[]>>();
		
		parseViolations(input, violationsPerFiles);

		Tusar tusarV10 = new Tusar();
		tusarV10.setVersion(TUSAR_VERSION);
		tusarV10.setXmlnsXsi(TUSAR_XMLNS_XSI);

		ViolationsComplexType violationsComplexType = new ViolationsComplexType();

		for (String key : violationsPerFiles.keySet() ){
			ViolationsComplexType.File file = new ViolationsComplexType.File();
			file.setPath(key);
			for (String[] violationsValues : violationsPerFiles.get(key)){
				ViolationsComplexType.File.Violation violation = new ViolationsComplexType.File.Violation();
				violation.setSeverity(violationsValues[PRQAConstants.SEVERITY_INDEX]);
				violation.setKey(prqaType+"_"+violationsValues[PRQAConstants.VIOLATION_ID_INDEX]);
				violation.setLine(violationsValues[PRQAConstants.LINE_INDEX]);
				violation.setColumn(violationsValues[PRQAConstants.COLUMN_INDEX]);
				/**
				 * The violation message in QAC file can contain the separator value, so we check the length of the array and we concatenate the last values of the array.
				 */
				if (violationsValues.length != PRQAConstants.MESSAGE_INDEX+1){
					StringBuilder sb = new StringBuilder();
					for (int i=PRQAConstants.MESSAGE_INDEX; i<violationsValues.length;i++){
						sb.append(violationsValues[i]).append(SEPARATOR+" ");
					}
					sb.setLength(sb.length()-2);
					violation.setMessage(sb.toString());
				}
				else {
					violation.setMessage(violationsValues[PRQAConstants.MESSAGE_INDEX]);
				}
				file.getViolation().add(violation);
			}
			violationsComplexType.getFile().add(file);
		}
		tusarV10.setViolations(violationsComplexType);
		try {
			marshal(tusarV10, output);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse the given input file and fill the given map
	 * @param input PRQA report generated by a specific command (see ThalesControl SUM part QAC/QACPP)
	 * @param violationsPerFiles An empty map. Key : File Name / Value : List of strings (SEVERITY_INDEX = 0; VIOLATION_ID_INDEX = 1; LINE_INDEX = 2; COLUMN_INDEX = 3; MESSAGE_INDEX = 4;)
	 * @throws FileNotFoundException
	 */
	private static void parseViolations(File input, Map<String, List<String[]>> violationsPerFiles) throws FileNotFoundException{
		Scanner scanner = new Scanner(input);
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			if (!line.isEmpty()){
				//0 : File path, 1 : Severity , 2 : Violation id, 3 : Line, 4 (useful ?) : Column 
				String[] values = line.split(SEPARATOR);

				//0 : Severity , 1 : Violation id, 2 : Line, 3 (useful ?) : Column 
				String[] violationsValues = new String[values.length-1];
				for (int i =1;i<values.length;i++){
					violationsValues[i-1] = values[i].trim();
				}
				if (violationsPerFiles.get(values[0]) == null){
					List<String[]> list = new ArrayList<String[]>();
					list.add(violationsValues);
					violationsPerFiles.put(values[0], list);
				}
				else {
					violationsPerFiles.get(values[0]).add(violationsValues);
				}
			}
		}
		scanner.close();
	}

	/**
	 * Read lines of the given input file (.met file) and fill the two given maps
	 * @param inputFile a .met file
	 * @param metrics An empty map. Metrics per components (Key : component name / Value : List(Key : Metric Name / Value : Metric Value))
	 * @param fileComponents An empty map. Components of a file (Key : File Name, Value : Set of components)
	 * @throws FileNotFoundException
	 */
	private static void treatCSVMeasures(File inputFile, Map<String, Map<String,String>> metrics, Map<String, Set<String>> fileComponents) throws FileNotFoundException{
		Scanner scanner = new Scanner(inputFile);

		//Getting the index of the column

		String fileName = "";
		String currentComponentName = ""; //Class, Function...

		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			String lineMarkUp = "<S>";
			if (line.startsWith(lineMarkUp)){
				line = line.substring(lineMarkUp.length(), line.length()).trim();
				int indexOfFirstWhiteSpace = line.indexOf(' ');
				if (indexOfFirstWhiteSpace!=-1){
					String key = line.substring( 0, indexOfFirstWhiteSpace).trim();
					String value = line.substring(indexOfFirstWhiteSpace+1, line.length()).trim(); 
					if (PRQAConstants.STFIL.equals(key)){
						fileName = value;
						//If the filename starts and ends with a quote, delete them
						if (fileName.startsWith("\"")){
							fileName = new StringBuilder(fileName).substring(1,fileName.length()-1);
						}
					}
					else if (PRQAConstants.STNAM.equals(key) ){ //We suppose that STFIL always appears before STNAM...
						Map<String, String> values = metrics.get(key);
						if (values == null){
							values = new HashMap<String, String>();
							metrics.put(value, values);
						}

						Set<String> parts = fileComponents.get(fileName);
						if (parts==null){
							parts = new HashSet<String>();
							fileComponents.put(fileName, parts);
						}
						parts.add(value);
						currentComponentName = value;
					}
					else {
						metrics.get(currentComponentName).put(key, value);
					}
				}
			}

		}
		scanner.close();

	}

	/**
	 * Return a Map (Key : project name / Value : List(Key : Metric Name / Value : Metric Value)) with project metrics. 
	 * @param metrics Metrics per components (Key : component name / Value : List(Key : Metric Name / Value : Metric Value))
	 * @param fileComponents Components of a file (Key : File Name, Value : Set of components)
	 * @return a Map (Key : project name / Value : List(Key : Metric Name / Value : Metric Value))
	 */
	private static Map<String, Map<String,String>> getProjectMetrics(Map<String, Map<String,String>> metrics, Map<String, Set<String>> fileComponents){
		Map<String, Map<String,String>> projectMetrics = new HashMap<String, Map<String,String>>();
		for (String fileName : fileComponents.keySet()){
			Map<String, String> additionnedMetrics = new HashMap<String, String>();
			for(String components : fileComponents.get(fileName)){
				Map<String, String> metricsOfComponent = metrics.get(components);
				PRQAComponentType componentType = getComponentType(metricsOfComponent);
				switch(componentType){
				case QAC_PROJECT :
					for (String metricName : metricsOfComponent.keySet()){
						additionnedMetrics.put(metricName, metricsOfComponent.get(metricName));
					}
					break;

				default:
					break;

				}
			}
			if (!additionnedMetrics.isEmpty()){
				projectMetrics.put(fileName, additionnedMetrics);
			}
		}

		return projectMetrics;
	}

	/**
	 * Return a Map (Key : file name / Value : List(Key : Metric Name / Value : Metric Value)) with files and functions/classes metrics. 
	 * For functions/classes metrics for a file, the values are added together.
	 * @param metrics Metrics per components (Key : component name / Value : List(Key : Metric Name / Value : Metric Value))
	 * @param fileComponents Components of a file (Key : File Name, Value : Set of the component)
	 * @return a Map (Key : file name / Value : List(Key : Metric Name / Value : Metric Value))
	 */
	private static Map<String, Map<String,String>> getMetricsPerFile(Map<String, Map<String,String>> metrics, Map<String, Set<String>> fileComponents){
		Map<String, Map<String,String>> metricsPerFile = new HashMap<String, Map<String,String>>();
		for (String fileName : fileComponents.keySet()){
			Map<String, String> additionnedMetrics = new HashMap<String, String>();
			for(String components : fileComponents.get(fileName)){
				Map<String, String> metricsOfComponent = metrics.get(components);
				PRQAComponentType componentType = getComponentType(metricsOfComponent);
				switch(componentType){
				case QACPP_FILE :
				case QAC_FILE :
					for (String metricName : metricsOfComponent.keySet()){
						additionnedMetrics.put(metricName, metricsOfComponent.get(metricName));
					}
					break;

				case QACPP_FUNCTION :
				case QACPP_CLASS :
				case QACPP_CLASS2 :
				case QAC_FUNCTION :
					for (String metricName : metricsOfComponent.keySet()){
						try{
							Double sum = additionnedMetrics.get(metricName) == null ? 0:Double.parseDouble(additionnedMetrics.get(metricName));
							additionnedMetrics.put(metricName, Double.toString(sum+Double.parseDouble(metricsOfComponent.get(metricName))));
						}catch (NumberFormatException e) {
							/*String currentMetricValue = additionnedMetrics.get(metricName);
							additionnedMetrics.put(metricName, currentMetricValue==null?metricsOfComponent.get(metricName):currentMetricValue+"/"+metricsOfComponent.get(metricName));*/
							additionnedMetrics.put(metricName, metricsOfComponent.get(metricName));
						}
					}
					break;

				default:
					break;

				}
			}
			if (!additionnedMetrics.isEmpty()){
				metricsPerFile.put(fileName, additionnedMetrics);
			}
		}

		return metricsPerFile;
	}

	/**
	 * Add the given PRQA metrics into the given TUSAR node
	 * @param tusar The XML Node representing a TUSAR Node
	 * @param projectMetrics Map : Key : file name / Value : List(Key : Metric Name / Value : Metric Value)
	 */
	private static void addFileMeasuresIntoTusar(Tusar tusar, Map<String, Map<String,String>> metricsPerFile){

		SizeComplexType sizeComplexType = tusar.getMeasures().getSize();

		for(String fileName : metricsPerFile.keySet()){
			SizeComplexType.Resource resource = new SizeComplexType.Resource();
			resource.setType("FILE");
			resource.setValue(fileName);

			Map<String,String>  metricsComponent = metricsPerFile.get(fileName);


			for (String metricName : metricsComponent.keySet()){
				Measure measure = new Measure();
				String sonarMetricName = sonarFileMetrics.get(metricName);
				measure.setKey(sonarMetricName==null?metricName:sonarMetricName);
				measure.setValue(metricsComponent.get(metricName));
				resource.getMeasure().add(measure);
			}


			sizeComplexType.getResource().add(resource);
		}

	}

	/**
	 * Add the given PRQA metrics into the given TUSAR node
	 * @param tusar The XML Node representing a TUSAR Node
	 * @param projectMetrics Key : project name / Value : List(Key : Metric Name / Value : Metric Value)
	 */
	private static void addPRQAProjectMeasuresIntoTusar(Tusar tusar, Map<String, Map<String,String>> projectMetrics){

		SizeComplexType sizeComplexType = tusar.getMeasures().getSize();

		for(String fileName : projectMetrics.keySet()){
			SizeComplexType.Resource resource = new SizeComplexType.Resource();
			resource.setType("PROJECT");
			resource.setValue(fileName);

			Map<String,String>  metricsComponent = projectMetrics.get(fileName);


			for (String metricName : metricsComponent.keySet()){
				Measure measure = new Measure();
				String sonarMetricName = sonarProjectMetrics.get(metricName);
				measure.setKey(sonarMetricName==null?metricName:sonarMetricName);
				measure.setValue(metricsComponent.get(metricName));
				resource.getMeasure().add(measure);
			}


			sizeComplexType.getResource().add(resource);
		}

	}

	/**
	 * This function tells us in which kind of component we are (function, class, file...) regarding the given metrics
	 * @param metrics List of metrics associated to their values.
	 * @return The Component type
	 */
	private static PRQAComponentType getComponentType(Map<String, String> metrics){
		if (metrics.keySet().containsAll(qacppFileMetrics)){
			return PRQAComponentType.QACPP_FILE;
		}
		else if (metrics.keySet().containsAll(qacppClassMetrics)){
			return PRQAComponentType.QACPP_CLASS;
		}
		else if (metrics.keySet().containsAll(qacppClassMetrics2)){
			return PRQAComponentType.QACPP_CLASS2;
		}
		else if (metrics.keySet().containsAll(qacppFunctionMetrics)){
			return PRQAComponentType.QACPP_FUNCTION;
		}
		else if (metrics.keySet().containsAll(qacProjectMetrics)){
			return PRQAComponentType.QAC_PROJECT;
		}
		else if (metrics.keySet().containsAll(qacFileMetrics)){
			return PRQAComponentType.QAC_FILE;
		}
		else if (metrics.keySet().containsAll(qacFunctionMetrics)){
			return PRQAComponentType.QAC_FUNCTION;
		}
		else {
			return PRQAComponentType.UNKNOWN;
		}
	}

	/**
	 * Convert a TUSAR node into a TUSAR report
	 * @param node The TUSAR XML node
	 * @param outputFile The TUSAR output file
	 * @throws JAXBException
	 */
	private static void marshal(Object node, File outputFile) throws JAXBException{
		ClassLoader cl = com.thalesgroup.tusar.v10.ObjectFactory.class.getClassLoader();
		JAXBContext jc = JAXBContext.newInstance("com.thalesgroup.tusar.v10",cl);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		m.marshal(node, outputFile);
	}

	/**
	 * Convert the .met reports presents in inputDir into one TUSAR report
	 * @param file The directory containing the .met files
	 * @param outputTusar The TUSAR file the will be generated
	 */
	public static void convertPRQAMeasures(File file, File outputTusar){

		Tusar tusarV10 = new Tusar();
		MeasuresComplexType measuresComplexType = new MeasuresComplexType();
		SizeComplexType sizeComplexType = new SizeComplexType();
		measuresComplexType.setSize(sizeComplexType);
		tusarV10.setMeasures(measuresComplexType);

		Map<String, Map<String,String>> metrics = new HashMap<String, Map<String,String>>(); //Key : function/class/filename, Value : map of metrics (name + value)
		Map<String, Set<String>> fileComponents = new HashMap<String, Set<String>>(); //Key : filename, Value : set of function/class/filename

		//convertAllMetFiles(inputDir, metrics, fileComponents);
		convertMetFile(file, metrics, fileComponents);
		
		/*
		 * TODO : These functions (getMetricsPerFile, getProjectMetrics...) are time consuming, we could use a map to store the prqa component types and use them in these functions.
		 */
		Map<String, Map<String,String>> metricsPerFile = getMetricsPerFile(metrics, fileComponents);
		Map<String, Map<String,String>> projectMetrics = getProjectMetrics(metrics, fileComponents);


		addFileMeasuresIntoTusar(tusarV10, metricsPerFile);
		addPRQAProjectMeasuresIntoTusar(tusarV10, projectMetrics);


		try {
			marshal(tusarV10, outputTusar);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param file A directory containing .met files
	 * @param metrics An empty Map (filled by this function)
	 * @param fileComponents An empty Map (filled by this function)
	 */
	private static void convertMetFile(File file, Map<String, Map<String,String>> metrics, Map<String, Set<String>> fileComponents){
		if (file!=null && file.isFile() && file.getName().endsWith(".met")){
			try {
				treatCSVMeasures(file, metrics, fileComponents);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else{
			//TODO : LOGGER !
			System.err.println("WARNING : " + file.getAbsolutePath()+ " is not a .met file, it won't be analysed.");
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		//convertQACPPIntoViolations(new File ("I:\\tmp\\aravindan\\prqa\\qacpp\\outputqacpp.txt"), new File("D:\\outputtusar.xml") );
		//parseCSVMeasuresFunc(new File("I:\\tmp\\aravindan\\prqa\\QAC-test\\output\\Cgicc.cpp.met"));
		//treatCSVMeasures(new File("I:\\tmp\\aravindan\\prqa\\QAC-test\\Cgicc\\output\\Cgicc.cpp.met"));
		//treatCSVMeasures(new File("I:\\tmp\\aravindan\\prqa\\QAC-test\\output_dlg\\char_and_strings.c.met"));
		
		
		
		//convertPRQAMeasures(new File("I:\\tmp\\aravindan\\prqa\\QAC-test\\output_dlg"), new File("D:/tusar-prqa.xml"));
		//convertPRQAMeasures(new File("I:\\tmp\\aravindan\\prqa\\QAC-test\\Cgicc\\Output"), new File("D:/tusar-prqa.xml"));
		
		File inputDir = new File("I:\\integ_infos\\AMA\\prqa\\QAC-test\\Cgicc\\Output");
		int i = 0;
		for (File file : inputDir.listFiles()){
			i++;
			convertPRQAMeasures(file, new File("D:/prqa-results/tusar-prqa"+i+".xml"));
		}
	}
}
