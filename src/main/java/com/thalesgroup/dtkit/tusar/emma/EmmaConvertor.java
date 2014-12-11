package com.thalesgroup.dtkit.tusar.emma;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.thalesgroup.tusar.coverage.v4.CoverageComplexType;
import com.thalesgroup.tusar.line_coverage.v1.LineCoverageComplexType;
import com.thalesgroup.tusar.v10.Tusar;

public class EmmaConvertor {

	private static final String TABLE_TAG = "<TABLE CLASS=\"s\" ";
	private static final String TABLE_END_TAG = "</TABLE>";
	private static final String TR_TAG = "<TR CLASS" ;
	private static final String TR_END_TAG = "</TR>" ;
	private static final String FILE_NAME_PREFIX = "COVERAGE SUMMARY FOR SOURCE FILE [";
	private static final String DEFAULT_PACKAGE = "default package";
	
	private static final String TOOLNAME = "Emma";
	private static final String VERSION = "2.0.5312";
	
	public void convert(File htmlFile, File outputFile){
		if (!htmlFile.getName().endsWith(".html") && !htmlFile.getName().endsWith(".htm")){
			return;
		}
		try {
			String fileContent = getFileContent(htmlFile);
			int indexTable = fileContent.indexOf(TABLE_TAG);
			if (indexTable == -1){
				return;
			}
			
			//Getting the full name of the file
			String fullName = getFullName(fileContent);
			
			String table = fileContent.substring(indexTable);

			//Getting Coverage Data of the file
			Map<String, String> values = getValuesFromTable(table.substring(0,table.indexOf(TABLE_END_TAG)));
			
			Tusar tusar = new Tusar();
			CoverageComplexType coverageComplexType = new CoverageComplexType();
			coverageComplexType.setToolname(TOOLNAME);
			coverageComplexType.setVersion(VERSION);
			
			LineCoverageComplexType lineCoverageComplexType = new LineCoverageComplexType();
			LineCoverageComplexType.File file = new LineCoverageComplexType.File();
			file.setPath(fullName);
			
			for (String lineNumber : values.keySet()){
				LineCoverageComplexType.File.Line line = new LineCoverageComplexType.File.Line();
				line.setNumber(lineNumber);
				line.setHits(values.get(lineNumber));
				file.getLine().add(line);
			}
			lineCoverageComplexType.getFile().add(file);
			
			coverageComplexType.setLineCoverage(lineCoverageComplexType);
			tusar.setCoverage(coverageComplexType);
			
			try {
				marshal(tusar, outputFile);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return;
	}

	private String getFullName(String fileContent) {
		String packageName = fileContent.substring(fileContent.lastIndexOf("[")+1,fileContent.lastIndexOf("]"));
		packageName = packageName.replaceAll("\\.", "/");
		packageName = packageName.substring(packageName.indexOf(">")+1, packageName.lastIndexOf("<"));
		
		//It seems that it's not the space character that is used in the HTML files isn't the same one than in the static field DEFAULT_PACKAGE
		//We have to replace them by the good space character in the case of default package
		packageName = packageName.replaceAll("default.package", DEFAULT_PACKAGE);

		if (DEFAULT_PACKAGE.equals(packageName)){
			packageName = "";
		}
		else {
			packageName=packageName+"/";
		}
		
		
		String tmpContent = fileContent.replaceAll("COVERAGE.SUMMARY.FOR.SOURCE.FILE.\\[", FILE_NAME_PREFIX);
		int beginIndex = tmpContent.indexOf(FILE_NAME_PREFIX);
		if (beginIndex == -1){
			return null;
		}
		
		String fileName = tmpContent.substring(beginIndex + FILE_NAME_PREFIX.length());
		fileName = fileName.substring(0, fileName.indexOf("]"));
		fileName = fileName.substring(fileName.indexOf(">")+1, fileName.lastIndexOf("<"));
		
		return packageName+fileName;
	}

	private Map<String,String> getValuesFromTable(String table) {
		Map<String,String> valuesPerFile = new HashMap<String,String>();


		//Remove everything before the first "<TR CLASS=" (tag corresponding to a line that is covered or not) that appears in the table
		String rawData = table;

		int beginIndex = table.indexOf(TR_TAG);
		while (beginIndex!=-1){
			rawData = rawData.substring(beginIndex);
			int endIndex = rawData.indexOf(TR_END_TAG);
			String line = rawData.substring(0,endIndex);

			//Storing data
			Pattern p = Pattern.compile("<TR CLASS=\"(.*?)\"><TD(?: TITLE=\".*?\")? CLASS=\"l\">(?:<A NAME=\"\\d*?\">)?(\\d*?)(?:</A>)?</TD><TD(?: TITLE=\".*?\")?>.*?</TD>");
			Matcher m = p.matcher(line);
			if (m.matches()){
				String coveredType = m.group(1);
				String lineNumber = m.group(2);
				
				valuesPerFile.put(lineNumber, coveredType.equals("z")?"0":"1");
			}

			//Removing the previously treated "<TR CLASS="
			rawData = rawData.substring(endIndex+TR_END_TAG.length());
			beginIndex = rawData.indexOf(TR_TAG);

		}

		return valuesPerFile;
	}

	private String getFileContent(File htmlFile) throws FileNotFoundException{
		Scanner scanner = new Scanner(htmlFile);
		StringBuilder stringBuilder = new StringBuilder();
		while (scanner.hasNextLine()){
			stringBuilder.append(scanner.nextLine());
		}
		scanner.close();

		return stringBuilder.toString();
	}
	
	/**
	 * Creates an XML file from a TUSAR V10 node by calling the function marshal() of JAXB Marshaller
	 * @param node TUSAR V10 node
	 * @param outputFile The XML file generated
	 * @throws JAXBException
	 */
	private void marshal(Object node, File outputFile) throws JAXBException{
		ClassLoader cl = com.thalesgroup.tusar.v10.ObjectFactory.class.getClassLoader();
		JAXBContext jc = JAXBContext.newInstance("com.thalesgroup.tusar.v10",cl);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		m.marshal(node, outputFile);
	}
	
}
