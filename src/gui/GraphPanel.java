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

/**
 * TODO ���涓�灞�缁���硅��澶����BUG锛�搴�璇ュ�规��涓�灞����������缁���归�借��琛�涓���扮��璁★��涔�������缁���躲��
 * 
 * @author John
 *
 */
public class GraphPanel extends JPanel {

	private Map<String, TreeNode> map; // 淇�瀛���句�����������
	private int gridWidth = 50; // 姣�涓�缁���圭��瀹藉害
	private int gridHeight = 20; // 姣�涓�缁���圭��楂�搴�


	private int childAlign; // 瀛╁��瀵归����瑰��
	//瀹�涔����涔���伙��
	public static int STYLE_CIRCLE = 1; //��讳��涓�������

	private Font font = new Font("寰�杞����榛�", Font.BOLD, 14); // ���杩扮����圭��瀛�浣�

	private Color gridColor = Color.PINK; // 缁���硅�����棰����
	private Color linkLineColor = Color.green; // 缁���硅��绾块�����
	private Color stringColor = Color.WHITE; // 缁���规��杩版��瀛����棰����
	private Color conditionColor = Color.orange;

	/**
	 * 榛�璁ゆ�����
	 */
	public GraphPanel() {
		this(STYLE_CIRCLE);
	}

	/**
	 * ��规��浼���ョ��Node缁���舵��锛�浠ョ��瀵瑰��涓������瑰��缁����
	 * 
	 * @param n
	 *            瑕�缁���剁�����
	 */
	public GraphPanel(Map<String, TreeNode> n) {
		this(n, STYLE_CIRCLE);
	}

	/**
	 * 璁剧疆瑕�缁���舵�跺�����瀵归��绛����
	 * 
	 * @param childAlign
	 *            瀵归��绛����
	 * @see tree.TreePanel#CHILD_ALIGN_RELATIVE
	 * @see tree.TreePanel#CHILD_ALIGN_ABSOLUTE
	 */
	public GraphPanel(int childAlign) {
		this(null, childAlign);
	}

	/**
	 * ��规��瀛╁��瀵归��绛����childAlign缁���剁����������圭�����n
	 * 
	 * @param n
	 *            瑕�缁���剁����������圭�����
	 * @param childAlign
	 *            瀵归��绛����
	 */
	public GraphPanel(Map<String, TreeNode> n, int childAlign) {
		super();
		setGraph(n);
		this.childAlign = childAlign;

	}

	/**
	 * 璁剧疆��ㄤ��缁���剁�����
	 * 
	 * @param n
	 *            ��ㄤ��缁���剁��������
	 */
	public void setGraph(Map<String, TreeNode> n) {
		map = n;
	}

	// ���������宸诧��璋���ㄨ��宸辩��缁���舵�规��
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		drawAllNode(g);
	}

	/**
	 * ���褰�缁���舵�存５���
	 * 
	 * @param n
	 *            琚�缁���剁��Node
	 * @param xPos
	 *            ��硅����圭��缁����X浣�缃�
	 * @param g
	 *            缁���句��涓�������澧�
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
			g.fillRect(nodeStartX, nodeStartY, gridWidth, gridHeight);	//��荤����圭����煎��
			g.setColor(stringColor);
			g.drawString(((TargetTree)map.get(key)).getName(), fontX, fontY) ;		//��荤����圭�����瀛�
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
				Boolean isCondition = node.getEdge().containsKey(son.getName());
				drawAL((int)lineFrom.getX(), (int)lineFrom.getY(), (int)lineTo.getX(), (int)lineTo.getY(), (Graphics2D) g, isCondition);
				if (isCondition) {
					int condX = (int) ((lineFrom.getX() + lineTo.getX())/2) - gridWidth/2;
					int condY = (int) ((lineFrom.getY() + lineTo.getY())/2) - gridHeight/2;
					g.setColor(this.conditionColor);
					g.drawString(node.getEdge().get(son.getName()), condX, condY);
				}

				//g.drawLine((int)lineFrom.getX(), (int)lineFrom.getY(), (int)lineTo.getX(), (int)lineTo.getY());
			}
		}
//		int y = n.getLayer()*(vGap+gridHeight)+startY;
//		int fontY = y + gridHeight - 5;		//5涓烘��璇�寰���虹����硷��浣����浠ラ��杩�FM璁＄����寸簿纭����锛�浣�浼�褰卞�����搴�
//		
//		g.setColor(gridColor);
//		g.fillRect(x, y, gridWidth, gridHeight);	//��荤����圭����煎��
//		
//		g.setColor(stringColor);
//		g.drawString(((TargetTree)n).getName(), x, fontY);		//��荤����圭�����瀛�
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
//				int newX = tempPosx+(gridWidth+hGap)*i;	//瀛╁��缁���硅捣濮�X
//				g.setColor(linkLineColor);
//				g.drawLine(x+gridWidth/2, y+gridHeight, newX+gridWidth/2, y+gridHeight+vGap);	//��昏����ョ����圭��绾�
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
	 * 璁剧疆缁���硅�����棰����
	 * 
	 * @param gridColor
	 *            缁���硅�����棰����
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}

	public Color getLinkLineColor() {
		return linkLineColor;
	}

	/**
	 * 璁剧疆缁���硅����ョ嚎���棰����
	 * 
	 * @param gridLinkLine
	 *            缁���硅����ョ嚎���棰����
	 */
	public void setLinkLineColor(Color gridLinkLine) {
		this.linkLineColor = gridLinkLine;
	}

	public Color getStringColor() {
		return stringColor;
	}

	/**
	 * 璁剧疆缁���规��杩扮��棰����
	 * 
	 * @param stringColor
	 *            缁���规��杩扮��棰����
	 */
	public void setStringColor(Color stringColor) {
		this.stringColor = stringColor;
	}
	
	private static void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2, Boolean isDotted)  
    {  
  
        double H = 10; // 绠�澶撮��搴�  
        double L = 4; // 搴�杈圭��涓����  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // 绠�澶磋��搴�  
        double arraow_len = Math.sqrt(L * L + H * H); // 绠�澶寸����垮害  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)���绗�涓�绔����  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)���绗�浜�绔����  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // ��荤嚎  
        if (isDotted) {
            Stroke rev = g2.getStroke();
            Stroke dash = new BasicStroke(1.5f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_ROUND,
            		3.5f,new float[]{15,10,},0f);
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
        //瀹�蹇�绠�澶�  
        g2.fill(triangle);  
        //���瀹�蹇�绠�澶�  
       // g2.draw(triangle);  
  
    }  
	 // 璁＄��  
	private static double[] rotateVec(int px, int py, double ang,  
            boolean isChLen, double newLen) {  
  
        double mathstr[] = new double[2];  
        // ��㈤�����杞���芥�帮�������板��涔����������x���������y������������杞�瑙������������瑰����垮害�����伴�垮害  
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
