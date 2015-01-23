package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	
	public void initHasRun() {
		hasRun = new HashSet<String>();
	}
	private static Set<String> hasRun;
	// 递归返回自己和子节点的rcp，每行用\n分割
	public String getScripts(Set<String> config) {
		if(hasRun!=null && hasRun.contains(this.getName())) {
			return "";
		}
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
						if ((config.contains(defCond) && condVal.equals("true")) 
								|| (!config.contains(defCond) && condVal.equals("false"))) {
							
							childScript = tt.getScripts(config);
							if (!childScript.trim().equals("")) {
								if (res.equals("")) {
									res = childScript.trim();
								} else {
									res = res + "\n" + childScript.trim();
								}
							}
						} 
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
				}
			}
		}

		// 访问自己
//		Boolean isCondition = this.getEdge().containsKey(sonName);
//		if (!isCondition) {
		String rcpScrpt = getRcpScript(config);
			if (res.equals("")) {
				res = rcpScrpt;
			} else {
				res = res + "\n" + rcpScrpt;
			}
		if (hasRun == null) {
			hasRun = new HashSet<String>();
		}
		hasRun.add(this.getName());
//		}
		// System.out.println(res);
		// return res;

		return res;
	}
	
	public String getRcpScript (Set<String> config) {
		String res ="";
		if (this.getRcps() == null || this.getRcps().equals("")) {
			return "";
		}
		if (!this.getRcps().contains("Choice")) {
			return this.getRcps();
		}
		String[] Choice = this.getRcps().split("Choice");
		
		for(int i =0; i< Choice.length; i++){
			if (Choice[i].trim().equals("")) {
				continue;
			}
			String choiceStr = Choice[i];
			String[] scpt = choiceStr.substring(0, Choice[i].length()-2).split(",");
			Boolean isfind = false;
			if (scpt[0].trim().contains("!ifdef")) {
				isfind = true;
				for(String cond : config) {
					if (scpt[0].contains(cond)){
						isfind = false;
						break;
					}
				}
			} else if (scpt[0].trim().contains("ifdef")){
				for(String cond : config) {
					if (scpt[0].contains(cond)){
						isfind = true;
						break;
					}
				}
			} else {
				if (res.equals("")) {
					res = Choice[i].trim();
				} else {
					res = res + "\n" + Choice[i].trim();
				}
				continue;
			}
			if (!isfind) {
				continue;
			}
			for(int j =1 ; j < scpt.length; j++) {
				if (scpt[j].trim().equals("")) {
					continue;
				}
				String choice = scpt[j].trim();
				if (!choice.equals("")){
					if (res.equals("")) {
						res = choice;
					} else {
						res = res + "\n" + choice;
					}
				}
			}
		}
		return res;
		
		
	}
}
