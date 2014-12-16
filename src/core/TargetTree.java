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
	
	// 递归返回自己和子节点的rcp，每行用\n分割
	public String getScripts() {
		String res = "";
		String childScript = "";
		//先访问儿子
		if (pChildren != null && !pChildren.isEmpty()){
			for(TreeNode i : pChildren){
				if (i instanceof TargetTree) {
					childScript = ((TargetTree)i).getScripts();
					if (!childScript.trim().equals("")) {
						if(res.equals("")) {
							res = childScript.trim();
						} else {
							res = res + "\n"+ childScript.trim();
						}
					}
				}
			}
		}
		
		//访问自己
		if(res.equals("")) {
			res = rcps;
		} else {
			res = res+"\n"+rcps;
		}
		return res;
	}
}
