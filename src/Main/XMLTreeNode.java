package Main;

import java.util.ArrayList;

public class XMLTreeNode {
	String target;
	String root;
	XMLTreeNode parent;
	ArrayList<XMLTreeNode> prerequisite;
	ArrayList<XMLTreeNode> rcp;

	public XMLTreeNode(String target) {
		this.target = target;
	}

	public XMLTreeNode(String target, ArrayList<XMLTreeNode> prerequisite,
			ArrayList<XMLTreeNode> rcp) {
		this.target = target;
		this.prerequisite = prerequisite;
		this.rcp = rcp;
	}

	public void print(String tab) {
		System.out.println("\n"+tab+">>>>>>>>start>>>>>>>>"+ target);
		if (target !=null && !target.trim().equals("")) {
			System.out.println(tab+"target:" + target);
		}
		if (prerequisite != null && !prerequisite.isEmpty()) {
			for (XMLTreeNode i : prerequisite) {
				System.out.println(tab+"prerequisite:");
				i.print(tab+"\t");
			}
		}
	}
//	public void printPreqs(String tab){
//		if (prerequisite != null && !prerequisite.isEmpty()) {
//			for (XMLTreeNode i : prerequisite) {
//				System.out.print(tab+"prerequisite:");
//				i.printPreqs(tab+"\t");
//			}
//		}
//	}

	public String combinRcp() { 

		String combine = "";
		if (rcp != null && !rcp.isEmpty()) {
			for (XMLTreeNode i : rcp) {
				if (i != null) {
					combine += i.combinRcp();
				}
			}

		}
		if (target !=null) {
			return target+" " + combine;
		} 
		return combine;
		
	}
	
	public void printRcp(String tab) { 
		if (target !=null && !target.trim().equals("")&&rcp==null) {
			System.out.print(" "+target);
		}
		if (rcp != null && !rcp.isEmpty()) {
			for (XMLTreeNode i : rcp) {
				if (i != null) {
					i.printRcp(tab);
				}
			}
		}
	}
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public XMLTreeNode getParent() {
		return parent;
	}

	public void setParent(XMLTreeNode parent) {
		this.parent = parent;
	}

	public ArrayList<XMLTreeNode> getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(ArrayList<XMLTreeNode> prerequisite) {
		this.prerequisite = prerequisite;
	}

	public ArrayList<XMLTreeNode> getRcp() {
		return rcp;
	}

	public void setRcp(ArrayList<XMLTreeNode> rcp) {
		this.rcp = rcp;
	}
}
