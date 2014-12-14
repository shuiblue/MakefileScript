package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import core.TargetTree;
import core.TreeNode;

/**
 * TODO 同一层结点过多有BUG，应该对每一层的所有结点都进行个数统计，之后才绘制。
 * 
 * @author John
 *
 */
public class GraphPanel extends JPanel {

	private Map<String, TreeNode> map; // 保存图上所有点
	private int gridWidth = 100; // 每个结点的宽度
	private int gridHeight = 20; // 每个结点的高度


	private int childAlign; // 孩子对齐方式
	//定义怎么画，
	public static int STYLE_CIRCLE = 1; //画一个圆圈

	private Font font = new Font("微软雅黑", Font.BOLD, 14); // 描述结点的字体

	private Color gridColor = Color.BLACK; // 结点背景颜色
	private Color linkLineColor = Color.RED; // 结点连线颜色
	private Color stringColor = Color.WHITE; // 结点描述文字的颜色

	/**
	 * 默认构造
	 */
	public GraphPanel() {
		this(STYLE_CIRCLE);
	}

	/**
	 * 根据传入的Node绘制树，以绝对居中的方式绘制
	 * 
	 * @param n
	 *            要绘制的树
	 */
	public GraphPanel(Map<String, TreeNode> n) {
		this(n, STYLE_CIRCLE);
	}

	/**
	 * 设置要绘制时候的对齐策略
	 * 
	 * @param childAlign
	 *            对齐策略
	 * @see tree.TreePanel#CHILD_ALIGN_RELATIVE
	 * @see tree.TreePanel#CHILD_ALIGN_ABSOLUTE
	 */
	public GraphPanel(int childAlign) {
		this(null, childAlign);
	}

	/**
	 * 根据孩子对齐策略childAlign绘制的树的根结点n
	 * 
	 * @param n
	 *            要绘制的树的根结点
	 * @param childAlign
	 *            对齐策略
	 */
	public GraphPanel(Map<String, TreeNode> n, int childAlign) {
		super();
		setGraph(n);
		this.childAlign = childAlign;

	}

	/**
	 * 设置用于绘制的树
	 * 
	 * @param n
	 *            用于绘制的树的
	 */
	public void setGraph(Map<String, TreeNode> n) {
		map = n;
	}

	// 重写而已，调用自己的绘制方法
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		drawAllNode(g);
	}

	/**
	 * 递归绘制整棵树
	 * 
	 * @param n
	 *            被绘制的Node
	 * @param xPos
	 *            根节点的绘制X位置
	 * @param g
	 *            绘图上下文环境
	 */
	public void drawAllNode(Graphics g){
		if (map == null) {
			return;
		}
		
		int centreX = getWidth() / 2;
		int centreY= getHeight() / 2;
		int radius = Math.min(centreX, centreY) - Math.max(gridWidth, gridHeight) / 2;
		int count = map.size();
		int intervalAngle = 360 / count;
		
		HashMap<String, Point> position = new HashMap<String, Point>();
		
		int beginAngle = 180;
		for (String key : map.keySet()) {
			int nodeCentreX = (int) (centreX + Math.sin(beginAngle * Math.PI/180) * radius);
			int nodeCentreY = (int) (centreY + Math.cos(beginAngle * Math.PI/180) * radius);
			int nodeStartX = nodeCentreX - gridWidth/2;
			int nodeStartY = nodeCentreY - gridHeight/2;
			int fontX = nodeStartX + 10;	
			int fontY = nodeStartY + gridHeight/2 + 5;	
			
			g.setColor(gridColor);
			g.fillRect(nodeStartX, nodeStartY, gridWidth, gridHeight);	//画结点的格子
			g.setColor(stringColor);
			g.drawString(((TargetTree)map.get(key)).getName(), fontX, fontY);		//画结点的名字
			beginAngle += intervalAngle;
			
			position.put(((TargetTree)map.get(key)).getName(), new Point(nodeCentreX, nodeCentreY));
		}
		TargetTree son;
		Point lineFrom;
		Point lineTo;
		for (String key : map.keySet()) {
			TargetTree node = ((TargetTree)map.get(key));
			lineFrom = position.get(node.getName());
			if (node.getpChildren() == null || node.getpChildren().size() == 0) {
				continue;
			}
			for(TreeNode child: node.getpChildren()) {
				son = (TargetTree)child;
				lineTo = position.get(son.getName());
				g.setColor(linkLineColor);
				drawAL((int)lineFrom.getX(), (int)lineFrom.getY(), (int)lineTo.getX(), (int)lineTo.getY(), (Graphics2D) g);
				//g.drawLine((int)lineFrom.getX(), (int)lineFrom.getY(), (int)lineTo.getX(), (int)lineTo.getY());
			}
		}
//		int y = n.getLayer()*(vGap+gridHeight)+startY;
//		int fontY = y + gridHeight - 5;		//5为测试得出的值，你可以通过FM计算更精确的，但会影响速度
//		
//		g.setColor(gridColor);
//		g.fillRect(x, y, gridWidth, gridHeight);	//画结点的格子
//		
//		g.setColor(stringColor);
//		g.drawString(((TargetTree)n).getName(), x, fontY);		//画结点的名字
//		
//		if(n.hasChild()){
//			List<TreeNode> c = n.getChilds();
//			int size = n.getChilds().size();
//			int tempPosx = childAlign == CHILD_ALIGN_RELATIVE 
//						 ? x+gridWidth/2 - (size*(gridWidth+hGap)-hGap)/2
//						 : (getWidth() - size*(gridWidth+hGap)+hGap)/2; 
//			
//			int i = 0;
//			for(TreeNode node : c){
//				int newX = tempPosx+(gridWidth+hGap)*i;	//孩子结点起始X
//				g.setColor(linkLineColor);
//				g.drawLine(x+gridWidth/2, y+gridHeight, newX+gridWidth/2, y+gridHeight+vGap);	//画连接结点的线
//			
//				drawAllNode(node, newX, g);
//				i++;
//			}
//		}
	}

	public Color getGridColor() {
		return gridColor;
	}

	/**
	 * 设置结点背景颜色
	 * 
	 * @param gridColor
	 *            结点背景颜色
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	public Color getLinkLineColor() {
		return linkLineColor;
	}

	/**
	 * 设置结点连接线的颜色
	 * 
	 * @param gridLinkLine
	 *            结点连接线的颜色
	 */
	public void setLinkLineColor(Color gridLinkLine) {
		this.linkLineColor = gridLinkLine;
	}

	public Color getStringColor() {
		return stringColor;
	}

	/**
	 * 设置结点描述的颜色
	 * 
	 * @param stringColor
	 *            结点描述的颜色
	 */
	public void setStringColor(Color stringColor) {
		this.stringColor = stringColor;
	}
	
	private static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2)  
    {  
  
        double H = 10; // 箭头高度  
        double L = 4; // 底边的一半  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // 箭头角度  
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // 画线  
        g2.drawLine(sx, sy, ex, ey);  
        //  
        GeneralPath triangle = new GeneralPath();  
        triangle.moveTo(ex, ey);  
        triangle.lineTo(x3, y3);  
        triangle.lineTo(x4, y4);  
        triangle.closePath();  
        //实心箭头  
        g2.fill(triangle);  
        //非实心箭头  
        //g2.draw(triangle);  
  
    }  
	 // 计算  
	private static double[] rotateVec(int px, int py, double ang,  
            boolean isChLen, double newLen) {  
  
        double mathstr[] = new double[2];  
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度  
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
