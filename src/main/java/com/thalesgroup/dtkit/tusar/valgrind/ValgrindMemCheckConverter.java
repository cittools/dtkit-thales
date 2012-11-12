/*******************************************************************************
 * Copyright (c) 2012 Thales Corporate Services SAS                             *
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

package com.thalesgroup.dtkit.tusar.valgrind;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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

import com.thalesgroup.tusar.measures.v6.MeasuresComplexType;
import com.thalesgroup.tusar.size.v1.SizeComplexType;
import com.thalesgroup.tusar.size.v1.SizeComplexType.Resource;
import com.thalesgroup.tusar.size.v1.SizeComplexType.Resource.Measure;
import com.thalesgroup.tusar.v10.Tusar;

public class ValgrindMemCheckConverter {
	
	private HashMap<String, Integer> numberOfErrorKind;
	private String exeName;
	
	public ValgrindMemCheckConverter() {
		numberOfErrorKind = new HashMap<String, Integer>();
		exeName="undefined";
	}

	private void readXMLFile(File xmlFile) throws SAXException, IOException, ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		
		NodeList errorList = doc.getElementsByTagName("error");
		
		for (int i = 0; i<errorList.getLength(); i++){
			Node errorNode = errorList.item(i);
			if (errorNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) errorNode;
				Node kindNode = element.getElementsByTagName("kind").item(0).getChildNodes().item(0);
				Integer count = numberOfErrorKind.get(kindNode.getNodeValue());
				if (count == null){
					numberOfErrorKind.put(kindNode.getNodeValue(), 1);
				}
				else {
					numberOfErrorKind.put(kindNode.getNodeValue(), count+1);
				}
			}
		}
		
		//Get the name of the executable
		Node argvNode = doc.getElementsByTagName("argv").item(0);
		Element element = (Element) argvNode;
		Node exeNode = element.getElementsByTagName("exe").item(0).getChildNodes().item(0);
		exeName = exeNode.getNodeValue();

	}
	
	private void convertToTusar(File outputFile){
		Tusar tusar = new Tusar();
		MeasuresComplexType measuresComplexType = new MeasuresComplexType();
		SizeComplexType sizeComplexType = new SizeComplexType();
		sizeComplexType.setToolname("Valgrind");
		sizeComplexType.setVersion("3.2.1");
		Resource resource = new Resource();
		resource.setType("FILE");
		resource.setValue(exeName);
		
		for (String key : numberOfErrorKind.keySet()){
			Measure measure = new Measure();
			measure.setKey(key);
			measure.setValue(numberOfErrorKind.get(key).toString());
			resource.getMeasure().add(measure);
		}
		
		sizeComplexType.getResource().add(resource);
		measuresComplexType.setSize(sizeComplexType);
		tusar.setMeasures(measuresComplexType);
		
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
	private static void marshal(Object node, File outputFile) throws JAXBException{
		ClassLoader cl = com.thalesgroup.tusar.v10.ObjectFactory.class.getClassLoader();
		JAXBContext jc = JAXBContext.newInstance("com.thalesgroup.tusar.v10",cl);
		Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
		m.marshal(node, outputFile);
	}
	
	public void convert(File inputFile, File outputFile) throws SAXException, IOException, ParserConfigurationException{
		readXMLFile(inputFile);
		convertToTusar(outputFile);
	}

}
