package Main;

import java.util.ArrayList;

public class TargetTree extends TreeNode {
	String name;
	//preqs:children
	
	String rcps;

	public TargetTree(TreeNode pParent, ArrayList<TreeNode> pChildren,
			String name, String rcps) {
		super(pParent, pChildren);
		this.name = name;
		this.rcps = rcps;
	}

	public String getName() {
		return name;
	}

	public TargetTree(TreeNode pParent, ArrayList<TreeNode> pChildren) {
		super(pParent, pChildren);
		// TODO Auto-generated constructor stub
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRcps() {
		return rcps;
	}

	public void setRcps(String rcps) {
		this.rcps = rcps;
	}
}
