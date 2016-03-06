package ir.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class HTMLHandler extends DefaultHandler{
	
	String fileName;
	File file;
	FileWriter fileWriter;
	String link;
	private Stack<String> elementStack;
	
	HTMLHandler(){
		try {
			fileName = "links.txt";
			String fileSeparator = System.getProperty("file.separator");
			elementStack = new Stack<String>();
			file = new File(System.getProperty("user.dir")+fileSeparator+"resources"+fileSeparator+fileName);
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		elementStack.push(qName);
		if (qName.equalsIgnoreCase("externalpage")) link = attributes.getValue(0);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		elementStack.pop();
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		String value = new String(ch, start, length).trim();
		if (value.length()==0) return;
		
		if ("topic".equals(elementStack.peek()) && value.startsWith("Top/Shopping/Clothing/Casual")){
			try {
				if (!link.isEmpty()) {
					fileWriter.write(link+"\n");
					link = "";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
