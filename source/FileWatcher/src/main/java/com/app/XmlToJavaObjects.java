package com.app;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.app.pojo.CISDocument;

public class XmlToJavaObjects {

	public static void main(String[] args) throws IOException, JAXBException {
		// get file content in the form of string
		File file = new File(
				"C:\\\\Users\\\\intakhabalam.s\\\\Documents\\\\sharedoc\\\\Test_Results_990\\\\Output_990_IB_XML_Acceptance.xml");

		CISDocument cisDoc = (CISDocument) convertXMLToObject(file, CISDocument.class);
		System.out.println(cisDoc);

		convertObjectToXML(file, cisDoc);
		getValuesFromXML(file);

	}
    /**
     * THis meho
     * @param xml
     * @return
     */
	private static String getValuesFromXML(File xml) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// document = builder.parse(new InputSource(new StringReader(xml.toString())));
			Document document = builder.parse(xml);
			Element rootElement = document.getDocumentElement();
			String requestQueueName = getValuesFromXMLNode("CarrierCode", rootElement);
			System.out.println(requestQueueName);

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String getValuesFromXMLNode(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}

		return null;
	}

	/**
	 * This method will give XML to Object
	 * 
	 * @param filePage
	 * @param clazz
	 * @return
	 */
	public static Object convertXMLToObject(File fileName, Class<?> clazz) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return jaxbUnmarshaller.unmarshal(fileName);
		} catch (JAXBException e) {
			e.printStackTrace();

		}
		return null;

	}

	/**
	 * 
	 * @param fileName
	 * @param cisDoc
	 */
	private static void convertObjectToXML(File fileName, CISDocument cisDoc) {

		try {
			JAXBContext context = JAXBContext.newInstance(CISDocument.class);
			Marshaller m = context.createMarshaller();
			// for pretty-print XML in JAXB
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Write to System.out for debugging
			m.marshal(cisDoc, System.out);
			// Write to File
			m.marshal(cisDoc, fileName);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
