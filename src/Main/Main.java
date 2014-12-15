package Main;

import java.io.File;

import gui.GraphFrame;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;
		File xmlFile = new File("test/makefile/run6.xml");
		doc = dbBuilder.parse(xmlFile);
		
		Parser p = new Parser(doc);
		showFrame(p);
	}
	
	public static void showFrame (Parser p) {
		new GraphFrame(p.getParseredMap());
	}

}
