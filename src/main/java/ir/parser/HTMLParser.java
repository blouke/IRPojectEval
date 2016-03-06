package ir.parser;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class HTMLParser {

	public static void main(String[] args) {
		String fileSeparator = System.getProperty("file.separator");
		try {
			FileInputStream fileInput = new FileInputStream(System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator+"content.rdf.u8");
			InputSource is;
			is = new InputSource(fileInput);
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(false);
			XMLReader r;
			r = spf.newSAXParser().getXMLReader();
			r.setContentHandler(new RDFHandler());
			r.parse(is);
		} catch (ParserConfigurationException e) {
				e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

}
