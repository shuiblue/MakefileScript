package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import core.TargetTree;
import core.TreeNode;
import Main.Parser;

public class TEST {

//	@Test
//	public void test_rcp_a() {
//
//		try {
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
//					.newInstance();
//			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
//			Document doc = null;
//			File xmlFile = new File("test/condition/run.xml");
//
//			doc = dbBuilder.parse(xmlFile);
//			Parser p = new Parser(doc);
//			Map<String, TreeNode> map = p.getParseredMap();
//			TargetTree nodeA = (TargetTree) map.get("A");
//			Assert.assertNotNull(nodeA);
//
//			TargetTree nodeB = (TargetTree) map.get("B");
//			Assert.assertNotNull(nodeB);
//
//			TargetTree nodeC = (TargetTree) map.get("C");
//			Assert.assertNotNull(nodeC);
//
//			TargetTree nodeD = (TargetTree) map.get("D");
//			Assert.assertNotNull(nodeD);
//
//			TargetTree nodeE = (TargetTree) map.get("E");
//			Assert.assertNotNull(nodeE);
//
//			TargetTree nodeF = (TargetTree) map.get("F");
//			Assert.assertNotNull(nodeF);
//
//			Assert.assertTrue(nodeA.getpChildren().contains(nodeB));
//			Assert.assertTrue(nodeA.getpChildren().contains(nodeC));
//			Assert.assertTrue(nodeA.getpChildren().contains(nodeE));
//
//			Assert.assertFalse(nodeA.getpChildren().contains(nodeD));
//			Assert.assertFalse(nodeA.getpChildren().contains(nodeF));
//
//			Assert.assertTrue(nodeB.getpChildren().contains(nodeE));
//			Assert.assertTrue(nodeB.getpChildren().contains(nodeC));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Test
	public void test_easy() {
		try {
			
			String make_output = "echo c"+"\n"+"echo b"+"\n"+"echo a";
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			File xmlFile = new File("test/easy/abc.xml");
			doc = dbBuilder.parse(xmlFile);
			String parse_output = "";
			Parser p = new Parser(doc);
			Map<String, TreeNode> map = p.getParseredMap();
			for (String key : map.keySet()) {
				if (map.get(key).getpParent() == null) {
				  String s=  p.printScript(map.get(key));
				   int size =((TargetTree)map.get(key)).getRcps().length();
				   parse_output += ((TargetTree)map.get(key)).getRcps().substring(2, size-1)+"\n";
				   parse_output += s;
				  
				}
				
				 System.out.println("parse_output---"+parse_output);
				Assert.assertEquals(parse_output, make_output);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
