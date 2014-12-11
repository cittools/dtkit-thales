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
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.thalesgroup.tusar.coverage.v4.CoverageComplexType;
import com.thalesgroup.tusar.line_coverage.v1.LineCoverageComplexType;
import com.thalesgroup.tusar.v10.Tusar;



public class NCoverConvertor {
	
	private Map<String, Map<String,String>> hitsPerFile;
	
	
	private static final String TOOLNAME = "NCover";
	private static final String VERSION = "1.5.8";
	
	private static final String TAG_TO_TREAT = "seqpnt";
	private static final String LINE = "line";
	private static final String DOCUMENT = "document";
	private static final String VISIT_COUNT = "visitcount";
	private static final String HITS = "hits";
	
	
	public NCoverConvertor() {
		hitsPerFile = new HashMap<String, Map<String,String>>();
	}

	private void readXMLFile(File xmlFile) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		
		NodeList seqpntList = doc.getElementsByTagName(TAG_TO_TREAT);
		
		for (int i = 0; i<seqpntList.getLength(); i++){
			Node seqpntNode = seqpntList.item(i);
			if (seqpntNode.getNodeType() == Node.ELEMENT_NODE){
				Element seqpntElement = (Element) seqpntNode;
				String document = seqpntElement.getAttribute(DOCUMENT);
				Map<String, String> hitsPerLine = hitsPerFile.get(document);
				String line = seqpntElement.getAttribute(LINE);
				String visitCount = seqpntElement.getAttribute(VISIT_COUNT);
				if (hitsPerLine == null){
					hitsPerLine = new HashMap<String, String>();
					hitsPerLine.put(line, visitCount);
					hitsPerFile.put(document, hitsPerLine);
				}
				else {
					String oldVisitCount = hitsPerLine.get(line);
					if (oldVisitCount == null){
						hitsPerLine.put(line, visitCount);
					}
					else {
						hitsPerLine.put(line, visitCount+oldVisitCount);
					}
					hitsPerFile.put(document, hitsPerLine);
				}
			}
		}
	}
	
	private void convertToTusar(File outputFile){
		Tusar tusar = new Tusar();
		CoverageComplexType coverageComplexType = new CoverageComplexType();
		coverageComplexType.setToolname(TOOLNAME);
		coverageComplexType.setVersion(VERSION);
		
		LineCoverageComplexType lineCoverageComplexType = new LineCoverageComplexType();		
		for (String fileName : hitsPerFile.keySet()){
			LineCoverageComplexType.File file = new LineCoverageComplexType.File();
			file.setPath(fileName);
			
			Map<String,String> hitsPerLine = hitsPerFile.get(fileName);
			for (String lineNumber : hitsPerLine.keySet()){
				LineCoverageComplexType.File.Line line = new LineCoverageComplexType.File.Line();
				line.setNumber(lineNumber);
				line.setHits(hitsPerLine.get(lineNumber));
				file.getLine().add(line);
			}
			lineCoverageComplexType.getFile().add(file);
		}
		
		coverageComplexType.setLineCoverage(lineCoverageComplexType);
		tusar.setCoverage(coverageComplexType);
		
		try {
			marshal(tusar, outputFile);
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
	private void marshal(Object node, File outputFile) throws JAXBException{
		ClassLoader cl = com.thalesgroup.tusar.v10.ObjectFactory.class.getClassLoader();
		JAXBContext jc = JAXBContext.newInstance("com.thalesgroup.tusar.v10",cl);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		m.marshal(node, outputFile);
	}
	
	public void convert(File inputFile, File outputFile) throws ParserConfigurationException, SAXException, IOException{
		readXMLFile(inputFile);
		convertToTusar(outputFile);
	}
}
