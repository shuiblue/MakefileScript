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
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import core.TargetTree;
import core.TreeNode;
import Main.Parser;

public class TEST {

	// @Test
	// public void test_rcp_a() {
	//
	// try {
	// DocumentBuilderFactory dbFactory = DocumentBuilderFactory
	// .newInstance();
	// DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
	// Document doc = null;
	// File xmlFile = new File("test/condition/run.xml");
	//
	// doc = dbBuilder.parse(xmlFile);
	// Parser p = new Parser(doc);
	// Map<String, TreeNode> map = p.getParseredMap();
	// TargetTree nodeA = (TargetTree) map.get("A");
	// Assert.assertNotNull(nodeA);
	//
	// TargetTree nodeB = (TargetTree) map.get("B");
	// Assert.assertNotNull(nodeB);
	//
	// TargetTree nodeC = (TargetTree) map.get("C");
	// Assert.assertNotNull(nodeC);
	//
	// TargetTree nodeD = (TargetTree) map.get("D");
	// Assert.assertNotNull(nodeD);
	//
	// TargetTree nodeE = (TargetTree) map.get("E");
	// Assert.assertNotNull(nodeE);
	//
	// TargetTree nodeF = (TargetTree) map.get("F");
	// Assert.assertNotNull(nodeF);
	//
	// Assert.assertTrue(nodeA.getpChildren().contains(nodeB));
	// Assert.assertTrue(nodeA.getpChildren().contains(nodeC));
	// Assert.assertTrue(nodeA.getpChildren().contains(nodeE));
	//
	// Assert.assertFalse(nodeA.getpChildren().contains(nodeD));
	// Assert.assertFalse(nodeA.getpChildren().contains(nodeF));
	//
	// Assert.assertTrue(nodeB.getpChildren().contains(nodeE));
	// Assert.assertTrue(nodeB.getpChildren().contains(nodeC));
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// @Test
	// public void test_easy() {
	// try {
	//
	// String make_output = "echo c\necho b\necho a";
	//
	// DocumentBuilderFactory dbFactory = DocumentBuilderFactory
	// .newInstance();
	// DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
	// Document doc = null;
	// File xmlFile = new File("test/easy/abc.xml");
	// doc = dbBuilder.parse(xmlFile);
	// String parse_output = "";
	// Parser p = new Parser(doc);
	// Map<String, TreeNode> map = p.getParseredMap();
	// for (String key : map.keySet()) {
	// if (map.get(key).getpParent() == null) {
	// TargetTree node = ((TargetTree) map.get(key));
	// Assert.assertEquals(node.getName(), "A");
	// Assert.assertEquals(node.getScripts(config).trim(), make_output);
	// }
	//
	// }
	//
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
//
	@Test
	public void testDiff_C() throws IOException {
		String makefile = "test/condition/makefile";
		String xmlfile = "test/condition/run.xml";
		String target = "C";
		Set<String> features = new HashSet<String>();
		features.add("foo");
		features.add("bar");
		try {
			checkMakefile(makefile, xmlfile, target, features);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test@Ignore("symake bug")
	public void testDiff_A() throws IOException {
		String makefile = "test/condition/makefile";
		String xmlfile = "test/condition/run.xml";
		String target = "A";
		Set<String> features = new HashSet<String>();
		features.add("foo");
		features.add("bar");
		try {
			checkMakefile(makefile, xmlfile, target, features);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testDiff_D() throws IOException {
		String makefile = "test/condition/makefile";
		String xmlfile = "test/condition/run.xml";
		String target = "D";
		Set<String> features = new HashSet<String>();
		features.add("foo");
		features.add("bar");
		try {
			checkMakefile(makefile, xmlfile, target, features);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkMakefile(String makefile, String xmlfile, String target, Set<String> features)
			throws IOException, Exception {
		Set<Set<String>> configurations = getAllConfigurations(features);
		System.out.println(configurations);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document doc = null;
		File xmlFile = new File(xmlfile);
		doc = dbBuilder.parse(xmlFile);
		String parse_output = "";
		Parser par = new Parser(doc);
		Map<String, TreeNode> map = par.getParseredMap();
		TargetTree node;
		Object tmp;
		for (Set<String> config : configurations) {
			String make_output = "";
			System.out.println("## running config " + config);
			String cmd = "make " + target + " -n -f " + makefile;
			for (String feature : config)
				cmd += " " + feature + "=1";
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			String line;

			while ((line = stdInput.readLine()) != null) {
				make_output += line + "\n";
			}
			tmp = map.get(target);
			Assert.assertNotNull(tmp);
			Assert.assertTrue(tmp instanceof TargetTree);
			node = (TargetTree)tmp;
			node.initHasRun();
			parse_output = node.getScripts(config);
			
			System.out.println(parse_output+"\n");
			System.out.println(make_output);
			System.out.println("\n\n\n");
			Assert.assertEquals(parse_output.trim(), make_output.trim());

		}
	}

	private Set<Set<String>> getAllConfigurations(Set<String> features) {
		Set<Set<String>> configurations = new HashSet<Set<String>>();
		configurations.add(new HashSet());
		for (String feature : features) {
			Set<Set<String>> newconfigurations = new HashSet<Set<String>>();
			for (Set<String> c : configurations) {
				HashSet<String> newConfig = new HashSet<String>(c);
				newConfig.add(feature);
				newconfigurations.add(newConfig);
			}
			configurations.addAll(newconfigurations);
		}
		return configurations;
	}

}
