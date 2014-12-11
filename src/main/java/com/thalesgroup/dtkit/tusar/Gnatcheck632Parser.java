/*******************************************************************************
 * Copyright (c) 2012 Thales Global Services                                    *
 * Author : Aravindan Mahendran                                                 *
 *                                                                              *
 * The MIT License                                                              *
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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thalesgroup.dtkit.tusar.model.Tusar.Violations;
import com.thalesgroup.dtkit.tusar.model.Tusar.Violations.File.Violation;
import org.jenkinsci.lib.dtkit.util.converter.ConversionException;
import org.jenkinsci.lib.dtkit.util.validator.ValidationException;
import com.thalesgroup.tusar.v10.Tusar;
import com.thalesgroup.tusar.violations.v4.ViolationsComplexType;



public class Gnatcheck632Parser {
/**
 * TODO : To be refactored... (using more constants, adding more comments)
 */


	// member data
	private List<String> section1, section3, rules;

	private Map<String, Map<String, List<String>>> map;
	private Map<String, String> mapRules;
	
	public static final int NB_DIAG_ID_FIELDS = 7;
	public static final int NB_SF_FIELDS = 5;


	public boolean validateInputFile(File inputFile) throws ValidationException {

		return true;
	}

	public boolean validateOutputFile(File outputFile) {
		return true;
	}

	public void convert(File inputFile, File outputFile) throws ConversionException, FileNotFoundException {
		Scanner scanner = new Scanner(inputFile);
		boolean inGenerateReport = false;
		boolean inViolatedRules = false;
		boolean inDiagId = false;
		boolean inSources = false;
		boolean inSF = false;
		String currentSF = "";
		
		Tusar tusarResult = new Tusar();
		ViolationsComplexType violations = new ViolationsComplexType();
		
		Map<String, List<String[]>> messagesMap = new HashMap<String, List<String[]>>();
		Map<String, List<String[]>> rulesIdMap = new HashMap<String, List<String[]>>();
		Map<String, String> namesMap = new HashMap<String, String>();

		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
		
			if (line.startsWith("Generate report ...")){
				inGenerateReport = true;
				if (inSources){
					inSources = false;
					fillViolations(rulesIdMap, messagesMap, namesMap, violations);
					rulesIdMap = new HashMap<String, List<String[]>>();
					messagesMap = new HashMap<String, List<String[]>>();
					namesMap = new HashMap<String, String>();
				}
			}
			
			else if (inGenerateReport && line.startsWith("...Done")){
				inGenerateReport = false;
			}
			
			else if (inGenerateReport){
				String[] values = line.trim().split(":"); //0: name, 1: line, 2: column, 3: messages
				//Maximium of messages in 'Generate report' part: 500...
				//If number of messages > 500, this message appears : 'gnatcheck: Maximum diagnoses reached, see the report file for full details'
				if (values.length>=4){
					List<String[]> entry = messagesMap.get(values[0]);
					if (entry==null){
						entry=new ArrayList<String[]>();
						entry.add(values);
						messagesMap.put(values[0], entry);
					}
					else {
						entry.add(values);
					}
				}
			}
			
			else if (line.startsWith("**** Diag_Table start *****")){
				inGenerateReport = false;
				inViolatedRules = true;
			}
			
			else if (inViolatedRules && line.startsWith("Diag_Id =")){
				inDiagId = true;
			}
			
			else if (inDiagId){
				String[] arguments = new String[NB_DIAG_ID_FIELDS];
				arguments[0] = line;
				for (int i = 1; i<NB_DIAG_ID_FIELDS;i++){
					arguments[i] = scanner.nextLine().trim();
				}
				String ruleId = arguments[0].split(" = ")[1].trim();
				String fileNumber = arguments[1].split(" = ")[1].trim();
				String lineNumber = arguments[2].split("Col=")[0].split("=")[1].trim();
				
				List<String[]> entry = rulesIdMap.get(fileNumber);
				if (entry==null){
					entry=new ArrayList<String[]>();
					entry.add(new String[]{ruleId, fileNumber, lineNumber});
					rulesIdMap.put(fileNumber, entry);
				}
				else {
					entry.add(new String[]{ruleId, fileNumber, lineNumber});
				}
				
				inDiagId = false;
			}
			
			else if (line.startsWith("**** Diag_Table end *****")){
				inViolatedRules = false;
				inDiagId=false;
			}
			
			else if (line.startsWith("-= Argument sources =-")){
				inSources = true;
			}
			
			else if (inSources && line.startsWith("SF =")){
				inSF = true;
				currentSF = line.split("=")[1].trim();
			}
			
			else if (inSF){
				String[] arguments = new String[NB_SF_FIELDS];
				arguments[0] = line;
				for (int i = 1; i<NB_SF_FIELDS;i++){
					arguments[i] = scanner.nextLine().trim();
				}
				String shortName = arguments[1].split(">")[1].split("<")[0].trim();
				
				namesMap.put(currentSF, shortName);
				inSF = false;
			}
			
		}
		scanner.close();
		
		fillViolations(rulesIdMap, messagesMap, namesMap, violations);
		tusarResult.setViolations(violations);
		
		try {
			marshal(tusarResult, outputFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Creates an XML file from a TUSAR V10 node by calling the function marshal() of JAXB Marshaller
	 * @param node TUSAR V10 node
	 * @param outputFile The XML file generated
	 * @throws JAXBException
	 */
	private static void marshal(Object node, File outputFile) throws JAXBException{
		ClassLoader cl = com.thalesgroup.tusar.v10.ObjectFactory.class.getClassLoader();
		JAXBContext jc = JAXBContext.newInstance("com.thalesgroup.tusar.v10",cl);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		m.marshal(node, outputFile);
	}
	
	private void fillViolations(Map<String, List<String[]>>rulesIdMap, Map<String, List<String[]>>messagesMap, Map<String, String>namesMap, ViolationsComplexType violations ){
		for (String fileNumber : rulesIdMap.keySet()){
			ViolationsComplexType.File file = new ViolationsComplexType.File();
			String fileName = namesMap.get(fileNumber);
			file.setPath(fileName);
			List<String[]> messages = messagesMap.get(fileName); //0: name, 1: line, 2: column, 3: messages
			List<String[]> rules = rulesIdMap.get(fileNumber); //0: rule ID, 1: fileNumber, 2: line
			for (int i = 0; i<rules.size(); i++){
				ViolationsComplexType.File.Violation violation = new ViolationsComplexType.File.Violation();
				violation.setKey(rules.get(i)[0]);
				violation.setLine(rules.get(i)[2]);
				if (i<messages.size()){
					violation.setColumn(messages.get(i)[2]);
					String message = messages.get(i)[3];
					if (messages.get(i).length > 4){
						for (int j =4; j<messages.get(i).length;j++){
							message+= ":"+messages.get(i)[j];
						}
					}
					violation.setMessage(message);
				}
				else {
					violation.setColumn("");
					violation.setMessage("");
				}
				file.getViolation().add(violation);
			}
			violations.getFile().add(file);
		}
	}
}