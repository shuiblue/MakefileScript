package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TargetTree extends TreeNode {
	String name;
	// preqs:children
	Map<String, String> edge = null;
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

	public Map<String, String> getEdge() {// string1 neibor target, str2 value
		if (edge == null) {
			setEdge(new HashMap<String, String>());
		}
		return edge;
	}

	public void setEdge(Map<String, String> edge) {
		this.edge = edge;
	}

	public String getTargetScript(String target, Set<String> config) {
		if (this.getName().equals(target)) {
			String string = getScripts(config);
			return string;
		}
		return null;
	}
	
	// 递归返回自己和子节点的rcp，每行用\n分割
	public String getScripts(Set<String> config) {
		String res = "";

		String childScript = "";
		String sonName = "";
		TargetTree tt = null;

		// 先访问儿子
		if (pChildren != null && !pChildren.isEmpty()) {
			for (int j = 0; j < pChildren.size(); j++) {
				TreeNode i = pChildren.get(j);
				if (i instanceof TargetTree) {
					tt = (TargetTree) i;
					sonName = tt.getName();
					Boolean isCondition = this.getEdge().containsKey(sonName);
					if (isCondition) {
						String cond = this.getEdge().get(sonName);
						String[] array = cond.split(":");
						String condVal = array[1].trim();
						String[] condionArray = array[0].trim().split("ifdef");
						String defCond = condionArray[1].trim().substring(0,
								condionArray[1].length() - 2);
						if (config.contains(defCond)) {
							childScript = tt.getScripts(config);
							if (!childScript.trim().equals("")) {
								if (res.equals("")) {
									res = childScript.trim();
								} else {
									res = res + "\n" + childScript.trim();
								}
							}
							String[] condRCP = this.getRcps().split("Choice");
							String[] trueRcp = condRCP[1].split(",");
							String trueVal = trueRcp[1];
							res += "\n" + trueVal + "\n";

						} else {
							j++;
							i = pChildren.get(j);
							tt = (TargetTree) i;
							childScript = tt.getScripts(config);
							if (!childScript.trim().equals("")) {
								if (res.equals("")) {
									res = childScript.trim();
								} else {
									res = res + "\n" + childScript.trim();
								}
							}
							String[] condRCP = this.getRcps().split("Choice");
							String[] falseRcp = condRCP[2].split(",");
							String falseVal = falseRcp[1];
							res += "\n" + falseVal + "\n";
						}
//						return res;
					} else {
						childScript = tt.getScripts(config);
						if (!childScript.trim().equals("")) {
							if (res.equals("")) {
								res = childScript.trim();
							} else {
								res = res + "\n" + childScript.trim();
							}
						}
					}
					return res;
				}
			}
		}

		// 访问自己
		Boolean isCondition = this.getEdge().containsKey(sonName);
		if (!isCondition) {

			if (res.equals("")) {
				res = rcps;
			} else {
				res = res + "\n" + rcps;
			}

		}
		// System.out.println(res);
		// return res;

		return res;
	}
}
