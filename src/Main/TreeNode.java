package Main;

import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Iterator;

public class TreeNode {
	TreeNode pParent;
	ArrayList<TreeNode> pChildren;
	private int layer = 0;

	public void defLayer(int a) {
		this.layer = a;
		if (pChildren == null || pChildren.isEmpty()) {
			return;
		}

		for (Iterator<TreeNode> j = pChildren.iterator(); j.hasNext();) {
			j.next().defLayer(a + 1);
		}
	}

	public void printAllNode(TreeNode n) {
		
		if (n.hasChild()) {
			ArrayList<TreeNode> c = n.getChilds();
			for (TreeNode node : c) {
				printAllNode(node);
				if(node.getLayer()==1){
					System.out.println("----rule:"+((TargetTree)node).getName()+"----");
					System.out.println();
				}
			}
		}
		//System.out.println("----" + ((TargetTree) n).name + "---");
		System.out.println(((TargetTree) n).rcps);
		System.out.println();
		
		
	}

	// public String getAllNodeName(TreeNode n) {
	// String s = n.toString() + "/n";
	// if (n.hasChild()) {
	// ArrayList<TreeNode> c = n.getChilds();
	// for (TreeNode node : c) {
	// s += getAllNodeName(node) + "/n";
	// }
	// }
	// return s;
	// }

	public TreeNode getpParent() {
		return pParent;
	}

	public void setpParent(TreeNode pParent) {
		this.pParent = pParent;
	}

	public ArrayList<TreeNode> getpChildren() {
		if (pChildren == null) {
			pChildren = new ArrayList<TreeNode>();
		}
		return pChildren;
	}

	public void setpChildren(ArrayList<TreeNode> pChildren) {
		this.pChildren = pChildren;
	}

	public TreeNode(TreeNode pParent, ArrayList<TreeNode> pChildren) {
		super();
		this.pParent = pParent;
		this.pChildren = pChildren;
	}

	public int getLayer() {
		// TODO Auto-generated method stub

		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public boolean hasChild() {
		// TODO Auto-generated method stub
		return pChildren == null ? false : true;
	}

	public ArrayList<TreeNode> getChilds() {
		// TODO Auto-generated method stub
		return pChildren;
	}
}
