package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.TreeNode;

public class GraphFrame extends JFrame {
	public GraphFrame(Map<String, TreeNode> map) {
		super("Test Draw Tree");
		initComponents(map);
		setSize(1000, 1000);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initComponents(Map<String, TreeNode> map) {
		/*
		 * 初始化树的数据
		 */

		/*
		 * 创建一个用于绘制树的面板并将树传入,使用相对对齐方式
		 */
		GraphPanel panel1 = new GraphPanel(map);

		add(panel1, BorderLayout.CENTER);
	}
}