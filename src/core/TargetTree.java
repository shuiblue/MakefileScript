package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TargetTree extends TreeNode {
	String name;
	//preqs:children
	Map<String,String> edge = null;
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

	public void setEdge(String ToTarget, String condition) {
		getEdge().put(ToTarget, condition);
	}

	public Map<String, String> getEdge() {
		if (edge == null) {
			setEdge(new HashMap<String, String>());
		}
		return edge;
	}

	public void setEdge(Map<String, String> edge) {
		this.edge = edge;
	}
}
