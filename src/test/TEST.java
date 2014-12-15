package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import core.TargetTree;
import core.TreeNode;
import Main.Parser;

public class TEST {

	@Test
	public void test_rcp_a() {
		String xml = "file:///Users/shuruiz/Documents/SPL/Symake/samples/a/run5.xml";
		String make_output = "";
		String parseXml_output = "";

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			File xmlFile = new File("test/makefile/run6.xml");
			doc = dbBuilder.parse(xmlFile);
			Parser p = new Parser(doc);
			Map<String, TreeNode> map = p.getParseredMap();
			TargetTree nodeA = (TargetTree) map.get("A");
			Assert.assertNotNull(nodeA);
			
			TargetTree nodeB = (TargetTree) map.get("B");
			Assert.assertNotNull(nodeB);
			
			TargetTree nodeC = (TargetTree) map.get("C");
			Assert.assertNotNull(nodeC);
			
			TargetTree nodeD = (TargetTree) map.get("D");
			Assert.assertNotNull(nodeD);
			
			TargetTree nodeE = (TargetTree) map.get("E");
			Assert.assertNotNull(nodeE);
			
			TargetTree nodeF = (TargetTree) map.get("F");
			Assert.assertNotNull(nodeF);
			
			Assert.assertTrue(nodeA.getpChildren().contains(nodeB));
			Assert.assertTrue(nodeA.getpChildren().contains(nodeC));
			Assert.assertTrue(nodeA.getpChildren().contains(nodeE));
			
			Assert.assertFalse(nodeA.getpChildren().contains(nodeD));
			Assert.assertFalse(nodeA.getpChildren().contains(nodeF));
			
			
			Assert.assertTrue(nodeB.getpChildren().contains(nodeE));
			Assert.assertTrue(nodeB.getpChildren().contains(nodeC));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
}
