package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import core.TargetTree;
import core.TreeNode;

public class GraphPanel extends JPanel {

	private Map<String, TreeNode> map; 
	private int gridWidth = 50; 
	private int gridHeight = 20; 

	private int childAlign; 
	public static int STYLE_CIRCLE = 1; 

	private Font font = new Font("寰�杞����榛�", Font.BOLD, 14); 

	private Color gridColor = Color.PINK; 
	private Color linkLineColor = Color.green; 
	private Color stringColor = Color.WHITE; 
	private Color conditionColor = Color.orange;

	public GraphPanel() {
		this(STYLE_CIRCLE);
	}

	
	public GraphPanel(Map<String, TreeNode> n) {
		this(n, STYLE_CIRCLE);
	}

	
	public GraphPanel(int childAlign) {
		this(null, childAlign);
	}

	
	public GraphPanel(Map<String, TreeNode> n, int childAlign) {
		super();
		setGraph(n);
		this.childAlign = childAlign;

	}

	
	public void setGraph(Map<String, TreeNode> n) {
		map = n;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		drawAllNode(g);
	}

	
	public void drawAllNode(Graphics g) {
		if (map == null) {
			return;
		}

		int centreX = getWidth() / 2;
		int centreY = getHeight() / 2;
		int radius = Math.min(centreX, centreY)
				- Math.max(gridWidth, gridHeight) / 2;
		int count = map.size();
		int intervalAngle = 360 / count;

		HashMap<String, Point> position = new HashMap<String, Point>();

		int beginAngle = 180;
		for (String key : map.keySet()) {
			int nodeCentreX = (int) (centreX + Math.sin(beginAngle * Math.PI
					/ 180)
					* radius);
			int nodeCentreY = (int) (centreY + Math.cos(beginAngle * Math.PI
					/ 180)
					* radius);
			int nodeStartX = nodeCentreX - gridWidth / 2;
			int nodeStartY = nodeCentreY - gridHeight / 2;
			int fontX = nodeStartX + 10;
			int fontY = nodeStartY + gridHeight / 2 + 5;

			g.setColor(gridColor);
			g.fillRect(nodeStartX, nodeStartY, gridWidth, gridHeight);
			g.setColor(stringColor);
			g.drawString(((TargetTree) map.get(key)).getName(), fontX, fontY); 
			beginAngle += intervalAngle;

			position.put(((TargetTree) map.get(key)).getName(), new Point(
					nodeCentreX, nodeCentreY));
			//System.out.print(((TargetTree) map.get(key)).getName());
		}
		TargetTree son;
		Point lineFrom;
		Point lineTo;
		for (String key : map.keySet()) {
			TargetTree node = ((TargetTree) map.get(key));
			lineFrom = position.get(node.getName());
			if (node.getpChildren() == null || node.getpChildren().size() == 0) {
				continue;
			}
			for (TreeNode child : node.getpChildren()) {
				son = (TargetTree) child;
				lineTo = position.get(son.getName());
				g.setColor(linkLineColor);
				Boolean isCondition = node.getEdge().containsKey(son.getName());
				drawAL((int) lineFrom.getX(), (int) lineFrom.getY(),
						(int) lineTo.getX(), (int) lineTo.getY(),
						(Graphics2D) g, isCondition);
				if (isCondition) {
					int condX = (int) ((lineFrom.getX() + lineTo.getX()) / 2)
							- gridWidth / 2;
					int condY = (int) ((lineFrom.getY() + lineTo.getY()) / 2)
							- gridHeight / 2;
					g.setColor(this.conditionColor);
					g.drawString(node.getEdge().get(son.getName()), condX,
							condY);
				}

			
				// g.drawLine((int)lineFrom.getX(), (int)lineFrom.getY(),
				// (int)lineTo.getX(), (int)lineTo.getY());
			}
		}

	}

	public Color getGridColor() {
		return gridColor;
	}

	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	public Color getLinkLineColor() {
		return linkLineColor;
	}

	public void setLinkLineColor(Color gridLinkLine) {
		this.linkLineColor = gridLinkLine;
	}

	public Color getStringColor() {
		return stringColor;
	}

	public void setStringColor(Color stringColor) {
		this.stringColor = stringColor;
	}

	private static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2,
			Boolean isDotted) {

		double H = 10;
		double L = 4;
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H);
		double arraow_len = Math.sqrt(L * L + H * H);
		double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
		double x_3 = ex - arrXY_1[0];
		double y_3 = ey - arrXY_1[1];
		double x_4 = ex - arrXY_2[0];
		double y_4 = ey - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();

		if (isDotted) {
			Stroke rev = g2.getStroke();
			Stroke dash = new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_ROUND, 3.5f, new float[] { 15, 10, }, 0f);
			g2.setStroke(dash);
			g2.drawLine(sx, sy, ex, ey);
			g2.setStroke(rev);
		} else {
			g2.drawLine(sx, sy, ex, ey);
		}

		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(ex, ey);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();

		g2.fill(triangle);

		// g2.draw(triangle);

	}

	private static double[] rotateVec(int px, int py, double ang,
			boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}
}
