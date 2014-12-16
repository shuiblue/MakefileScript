package test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	@Test
	public void test_rcp_a() {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = null;
			File xmlFile = new File("test/condition/run.xml");

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

	@Test
	public void test_easy() {
		try {
			
			String make_output = "echo c\necho b\necho a";
			
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
					TargetTree node = ((TargetTree)map.get(key));
					Assert.assertEquals(node.getName(), "A");	
					Assert.assertEquals(node.getScripts().trim(), make_output);
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test public void testDiff() throws IOException {
		String file = "test/condition/makefile";
		String target = "D";
		Set<String> features = new HashSet<String>();
		features.add("foo");
		features.add("bar");
		checkMakefile(file, target, features);
	}

	private void checkMakefile(String file, String target, Set<String> features) throws IOException {
		
		
		Set<Set<String>> configurations = getAllConfigurations(features);
		
		System.out.println(configurations);
		
		for (Set<String> config :configurations) {
			System.out.println("## running config "+config);
			String cmd = "make "+target+" -n -f "+file;
			for (String feature: config)
				cmd += " "+feature+"=1";
			
			Process p = Runtime.getRuntime().exec(cmd);
            
            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));
 
            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));
 
            String line;
            while ((line = stdInput.readLine())!=null) {
            	System.out.println(line);
            }
		}
	}

	private Set<Set<String>> getAllConfigurations(Set<String> features) {
		Set<Set<String>> configurations  = new HashSet<Set<String>>();
		configurations.add(new HashSet());
		for (String feature: features) {
			Set<Set<String>> newconfigurations = new HashSet<Set<String>>(); 
			for (Set<String> c: configurations) {
				HashSet<String> newConfig = new HashSet<String>(c);
				newConfig.add(feature);
				newconfigurations.add(newConfig);
			}
			configurations.addAll(newconfigurations);
		}
		// TODO Auto-generated method stub
		return configurations;
	}
	
	

}
