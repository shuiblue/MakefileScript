package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JTreeFrame extends JFrame {
	public JTreeFrame(TreeNode n) {
		super("Test Draw Tree");
		initComponents(n);
		setSize(1000, 1000);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void initComponents(TreeNode n) {
		/*
		 * 初始化树的数据
		 */

		/*
		 * 创建一个用于绘制树的面板并将树传入,使用相对对齐方式
		 */
		TreePanel panel1 = new TreePanel(TreePanel.CHILD_ALIGN_RELATIVE);
		panel1.setTree(n);

		/*
		 * 创建一个用于绘制树的面板并将树传入,使用绝对对齐方式
		 */
//		TreePanel panel2 = new TreePanel(TreePanel.CHILD_ALIGN_ABSOLUTE);
//		panel2.setTree(n);
//		panel2.setBackground(Color.BLACK);
//		panel2.setGridColor(Color.WHITE);
//		panel2.setLinkLineColor(Color.WHITE);
//		panel2.setStringColor(Color.BLACK);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 1));
		contentPane.add(panel1);
//		contentPane.add(panel2);

		add(contentPane, BorderLayout.CENTER);
	}
}