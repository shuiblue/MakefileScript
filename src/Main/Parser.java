package Main;

import gui.GraphFrame;
import gui.JTreeFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.ConditionTreeNode;
import core.TargetTree;
import core.TreeNode;
import core.XMLTreeNode;

public class Parser {
	static String TAG_CONCAT = "CONCAT";
	static String TAG_TARGET = "target-models";
	static String TAG_PREQS = "PREQS";
	static String TAG_RCP = "RCPs";
	static String TAG_P = "P";
	static String TAG_SELECT = "SELECT";
	static String TAG_TRUE = "TRUE";
	static String TAG_FALSE = "FALSE";
	static String TAG_LEFT = "LEFT";
	static String TAG_RIGHT = "RIGHT";
	static String TAG_VALUE = "value";

	private Map<String, TreeNode> map = null;
	TargetTree root = null;
	String script = "";

	public Parser(Document doc) throws Exception {
		NodeList rule_list = doc.getElementsByTagName("RULE");
		ArrayList<XMLTreeNode> ruleTree = new ArrayList<XMLTreeNode>();
		String targetName;
		map = new HashMap<String, TreeNode>();

		// ---------get rule---------
		for (int i = 0; i < rule_list.getLength(); i++) {
			ArrayList<XMLTreeNode> rcpTree = new ArrayList<XMLTreeNode>();
			ArrayList<XMLTreeNode> preqTree = new ArrayList<XMLTreeNode>();

			// --------get target----------
			Node targetNode = getNodeByTagInSon(rule_list.item(i), TAG_TARGET);
			targetName = concatName(targetNode);

			// --------get PREQS-------
			Node preqsNode = getNodeByTagInSon(rule_list.item(i), TAG_PREQS);
			if (preqsNode.hasChildNodes()) {
				for (int t = 0; t < preqsNode.getChildNodes().getLength(); t++) {
					Node pNode = preqsNode.getChildNodes().item(t);
					NodeList pNode_list = pNode.getChildNodes();
					// ----------P----------
					if (pNode.getNodeName().trim().equals(TAG_P)) {
						for (int p = 0; p < pNode_list.getLength(); p++) {
							// ---------CONCAT-------
							if (pNode_list.item(p).getNodeName().trim()
									.equals(TAG_CONCAT)
									|| pNode_list.item(p).getNodeName().trim()
											.equals(TAG_VALUE)) {
								preqTree.add(new XMLTreeNode(
										concatName(pNode_list.item(p))));
								// ----------SELECT-----------
							} else if (pNode_list.item(p).getNodeName().trim()
									.equals(TAG_SELECT)) {

								Node select = pNode_list.item(p);
								preqTree.addAll(parseSelectNode(select));

							}
						}
					}
				}
			}

			// ------------ get RCP ----------
			Node rpcsNode = getNodeByTagInSon(rule_list.item(i), TAG_RCP);
			if (rpcsNode.hasChildNodes()) {
				for (int t = 0; t < rpcsNode.getChildNodes().getLength(); t++) {
					Node rpcNode = rpcsNode.getChildNodes().item(t);
					XMLTreeNode x = concat(rpcNode);
					if (x != null) {
						rcpTree.add(x);
					}
				}
			}
			// --------- built XMLTreeNode --------
			XMLTreeNode x = new XMLTreeNode(targetName, preqTree, rcpTree);
			ruleTree.add(x); // add target tree to rule
			combineRcpLeafs(x);
			String rcp = x.combinRcp();
			//rcp是递归的取xmlTreeNode中的target的，所以把自己也取了，所以在这里处理一下，去掉自己
			rcp = rcp.substring(targetName.length()+1);
			
			
			/*
			 * (TreeNode pParent, ArrayList<TreeNode> pChildren,String name,
			 * String rcps)
			 */
			TreeNode t = new TargetTree(null, null, targetName, rcp);
			map.put(targetName, t);
		}

		// -----iterator ruleTree -------
		for (Iterator<XMLTreeNode> i = ruleTree.iterator(); i.hasNext();) {
			XMLTreeNode node = i.next();
			for (Iterator<XMLTreeNode> j = node.getPrerequisite().iterator(); j
					.hasNext();) {
				XMLTreeNode preqsNode = j.next();
				Boolean isCondtion = (preqsNode instanceof ConditionTreeNode);
				String[] targets = preqsNode.getTarget().split(" ");
				String a = "";
				for (int t = 0; t < targets.length; t++) {
					a = targets[t];
					if (map.get(a) != null) {
						((TargetTree) map.get(a)).setName(targets[t]);
						TargetTree fatherNode = (TargetTree) map.get(node
								.getTarget());
						TargetTree sonNode = (TargetTree) map.get(a);
						fatherNode.getpChildren().add(sonNode);
						sonNode.setpParent(fatherNode);
						if (isCondtion) {
							fatherNode.setEdge(
									sonNode.getName(),
									((ConditionTreeNode) preqsNode)
											.getCondition()
											+ " : "
											+ ((ConditionTreeNode) preqsNode)
													.getValue());
						}

					}
				}
			}
		}
		root = new TargetTree(null, new ArrayList<TreeNode>());
		root.setName("root");

		for (String key : map.keySet()) {
			if (map.get(key).getpParent() == null) {
				root.getpChildren().add(map.get(key));
//				String a = printScript(map.get(key));
				// int size = ((TargetTree) map.get(key)).getRcps().length();
				// //
				// System.out.println(((TargetTree)map.get(key)).getRcps().substring(2,
				// // size-1));
				// a += ((TargetTree) map.get(key)).getRcps().substring(2,
				// size - 1);
				// // System.out.println(a);
			}

		}

	}

	public Map<String, TreeNode> getParseredMap() {
		return map;
	}

	static public ArrayList<XMLTreeNode> combineRcpLeafs(XMLTreeNode root) {
		ArrayList<XMLTreeNode> add = new ArrayList<XMLTreeNode>();
		ArrayList<XMLTreeNode> del = new ArrayList<XMLTreeNode>();
		ArrayList<XMLTreeNode> tmp = null;

		if (root.getRcp() != null) {
			// ArrayList<XMLTreeNode> copy = new ArrayList<XMLTreeNode>();
			// copy.addAll(root.getRcp());
			for (Iterator<XMLTreeNode> i = root.getRcp().iterator(); i
					.hasNext();) {
				XMLTreeNode node = i.next();
				node.setParent(root);
				tmp = combineRcpLeafs(node);
				if (tmp != null) {
					add.addAll(tmp);
					del.add(node);
				}
			}
			root.getRcp().addAll(0, add);
			root.getRcp().removeAll(del);
		}

		if ((root.getTarget() == null || root.getTarget().equals(""))
				&& (root.getPrerequisite() == null || root.getPrerequisite()
						.isEmpty())) {

			for (Iterator<XMLTreeNode> i = root.getRcp().iterator(); i
					.hasNext();) {
				XMLTreeNode node = i.next();
				node.setParent(root.getParent());
			}
			return root.getRcp();

		}
		return null;
	}

	// ---------parseSelectNode-------
	static public ArrayList<XMLTreeNode> parseSelectNode(Node select) {
		NodeList sele_children = select.getChildNodes();
		// -----sel-list-----
		Node sel_list = sele_children.item(0);
		Node item = sel_list.getChildNodes().item(0);

		// ----------- CR ------------
		Node cr = getNodeByTagInSonWideFrist(select, "CR");
		String condition = "";
		String val = "";
		if (cr != null) {
			// ------------ condition -------------
			condition = cr.getAttributes().getNamedItem("condition").toString();
			val = cr.getAttributes().getNamedItem("isNot").toString();
		}
		ConditionTreeNode trueNode = null;
		ConditionTreeNode falseNode = null;
		ArrayList<XMLTreeNode> selectTree = new ArrayList<XMLTreeNode>();

		for (int s = 0; s < sele_children.getLength(); s++) {
			String concatName = concatName(sele_children.item(s));
			// -----------TRUE----------
			if (sele_children.item(s).getNodeName().trim().equals(TAG_TRUE)) {

				if (concatName == null || concatName.equals("")) {
					continue;
				}

				trueNode = new ConditionTreeNode(condition,
						val.equals("isNot=\"0\"") ? "true" : "false",
						concatName);
				// ----------FALSE-------
			} else if (sele_children.item(s).getNodeName().trim()
					.equals(TAG_FALSE)) {

				if (concatName == null || concatName.equals("")) {
					continue;
				}
				falseNode = new ConditionTreeNode(condition,
						val.equals("isNot=\"1\"") ? "false" : "true",
						concatName);
				// -------- CONCAT -------
			} else if (sele_children.item(s).getNodeName().trim()
					.equals(TAG_CONCAT)) {

			}
		}
		if (trueNode != null) {
			selectTree.add(trueNode);
		}
		if (falseNode != null) {
			selectTree.add(falseNode);
		}
		return selectTree;
	}

	// --------if 'CONCAT' label---------
	static public String concatName(Node node) {
		if (node == null) {
			return "";
		}
		if (node.getNodeName().trim().equals(TAG_VALUE)) {
			return node.getTextContent().trim();
		} else if (node.getNodeName().trim().equals(TAG_CONCAT)) {
			String left = "";
			String right = "";

			for (int j = 0; j < node.getChildNodes().getLength(); j++) {
				if (node.getChildNodes().item(j).getNodeName().trim()
						.equals(TAG_LEFT)) {
					left = concatName(node.getChildNodes().item(j));
				} else if (node.getChildNodes().item(j).getNodeName().trim()
						.equals(TAG_RIGHT)) {
					right = concatName(node.getChildNodes().item(j));
				}
			}
			if (left.trim().equals("")) {
				return right;
			} else if (right.trim().equals("")) {
				return left;
			} else {
				return left + " " + right;
			}
		} else {
			if (node.hasChildNodes()) {
				NodeList nl = node.getChildNodes();
				for (int i = 0; i < nl.getLength(); i++) {
					if (nl.item(i).getNodeName().trim().equals(TAG_CONCAT)) {
						return concatName(nl.item(i));
					} else if (nl.item(i).getNodeName().trim()
							.equals(TAG_VALUE)) {
						return nl.item(i).getTextContent().trim();
					}
				}
				return "";
			} else {
				return "";
			}
		}
	}

	// getNodeByTagInSon
	static public Node getNodeByTagInSon(Node node, String tagNam) {
		if (node.getNodeName().trim().equals(tagNam)) {
			return node;
		}
		if (!node.hasChildNodes()) {
			return null;
		}
		NodeList children = node.getChildNodes();
		Node n;
		for (int i = 0; i < children.getLength(); i++) {
			n = getNodeByTagInSon(children.item(i), tagNam);
			if (n != null) {
				return n;
			}
		}
		return null;
	}

	// getNodeByTagInSon---------WideFrist
	static public Node getNodeByTagInSonWideFrist(Node node, String tagNam) {
		if (node.getNodeName().trim().equals(tagNam)) {
			return node;
		}
		if (!node.hasChildNodes()) {
			return null;
		}
		NodeList children = node.getChildNodes();
		Node n;
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName().trim().equals(tagNam)) {
				return children.item(i);
			}
		}
		for (int i = 0; i < children.getLength(); i++) {
			n = getNodeByTagInSon(children.item(i), tagNam);
			if (n != null) {
				return n;
			}
		}
		return null;
	}

	// --------concat-----------
	static public XMLTreeNode concat(Node n) {
		if (!n.hasChildNodes()) {
			String name = n.getTextContent().trim();
			if (name.equals("")) {
				return null;
			}
			return new XMLTreeNode(name);
		}
		NodeList chilren = n.getChildNodes();
		for (int i = 0; i < chilren.getLength(); i++) {
			Node nodeI = chilren.item(i);
			String nodeName = nodeI.getNodeName().trim();
			if (nodeName.equals(TAG_CONCAT)) {
				Node leftNode = getNodeByTagInSonWideFrist(nodeI, TAG_LEFT);
				Node rightNode = getNodeByTagInSonWideFrist(nodeI, TAG_RIGHT);
				XMLTreeNode leftTreeNode = concat(leftNode);
				XMLTreeNode rightTreeNode = concat(rightNode);

				XMLTreeNode x = new XMLTreeNode("",
						new ArrayList<XMLTreeNode>(),
						new ArrayList<XMLTreeNode>());
				if (leftTreeNode != null) {
					x.getRcp().add(leftTreeNode);
				}
				if (rightTreeNode != null) {
					x.getRcp().add(rightTreeNode);
				}
				return x;
			} else if (nodeName.equals(TAG_RIGHT) || nodeName.equals(TAG_LEFT)) {
				return concat(nodeI);
			} else if (nodeName.equals(TAG_SELECT)) {
				NodeList sele_children = nodeI.getChildNodes();
				Node sel_list = sele_children.item(0);
				Node item = sel_list.getChildNodes().item(0);
				// getNodeByTagInSon sel_list
				// item cr
				Node cr = getNodeByTagInSonWideFrist(n, "CR");
				String condition = "";
				String val = "";
				if (cr != null) {
					condition = cr.getAttributes().getNamedItem("condition")
							.toString();
					val = cr.getAttributes().getNamedItem("isNot").toString();
				}
				ConditionTreeNode trueNode = null;
				ConditionTreeNode falseNode = null;
				ConditionTreeNode selectTree = new ConditionTreeNode("");
				selectTree.setRcp(new ArrayList<XMLTreeNode>());
				for (int s = 0; s < sele_children.getLength(); s++) {
					ConditionTreeNode cnode;
					XMLTreeNode tnode = null;
					if (sele_children.item(s).getNodeName().trim()
							.equals(TAG_TRUE)) {
						tnode = concat(sele_children.item(s));
						if (tnode != null) {
							cnode = new ConditionTreeNode(tnode);
							cnode.setCondition(condition);
							if (val.equals("isNot=\"0\"")) {
								cnode.setValue("true");
							} else {
								cnode.setValue("false");
							}
							selectTree.getRcp().add(cnode);
						}

					} else if (sele_children.item(s).getNodeName().trim()
							.equals(TAG_FALSE)) {

						tnode = concat(sele_children.item(s));
						if (tnode != null) {
							cnode = new ConditionTreeNode(tnode);
							cnode.setCondition(condition);

							if (val.equals("isNot=\"1\"")) {
								cnode.setValue("false");
							} else {
								cnode.setValue("true");
							}

							selectTree.getRcp().add(cnode);
						}

					}
				}
				return selectTree;
			} else if (nodeName.equals(TAG_TRUE) || nodeName.equals(TAG_FALSE)) {
				return concat(nodeI);
			} else if (nodeName.equals(TAG_VALUE)) {
				String name = nodeI.getTextContent().trim();
				if (name.equals("")) {
					return null;
				}
				return new XMLTreeNode(name);
			}
		}
		return null;
	}
}