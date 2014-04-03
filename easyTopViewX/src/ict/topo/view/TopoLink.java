package ict.topo.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;

import javax.swing.JLabel;

public class TopoLink extends JLabel
{
    private static final long serialVersionUID = 649231849786877232L;

    private TopoNode lnode, rnode;

    private JLabel left, right;

    private String lineName;

    private int lineCost;

    private Color lineColor;

    private int thickness;// 线的粗细

    private Dimension screenSize;

    public int x0, y0, x1, y1, lw, lh, rw, rh;

    public boolean hasArrow;

    public TopoLink(String name, TopoNode node1, TopoNode node2)
    {
        lnode = node1;
        rnode = node2;
        lineName = name;
        lineColor = Color.BLACK;
        lineCost = 0;
        left = lnode;
        right = rnode;
        thickness = 1;
        hasArrow = false;
        setSize();
    }

    public TopoLink(String name, int cost, TopoNode node1, TopoNode node2)
    {
        lnode = node1;
        rnode = node2;
        lineName = name;
        lineCost = cost;
        lineColor = Color.BLACK;
        thickness = 1;
        setSize();
    }

    public TopoLink(String name, TopoNode node1, TopoNode node2, Color c)
    {
        lineName = name;
        lnode = node1;
        rnode = node2;
        left = lnode;
        right = rnode;
        thickness = 1;
        lineColor = c;
        hasArrow = true;
        setSize();
    }

    public void setSize()
    {
        // 获得窗口大小
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
    }

    public void setCost(int cost)
    {
        lineCost = cost;
    }

    public int getCost()
    {
        return lineCost;
    }

    public String getLineName()
    {
        return lineName;
    }

    public TopoNode getLnode()
    {
        return lnode;
    }

    public TopoNode getRnode()
    {
        return rnode;
    }

    public void setColor(Color color)
    {
        lineColor = color;
    }

    public void setThickness(int thick)
    {
        thickness = thick;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        ((Graphics2D)g).setStroke(new BasicStroke(thickness,
                                                  BasicStroke.CAP_BUTT,
                                                  BasicStroke.JOIN_BEVEL));
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                         RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(lineColor);
        if (hasArrow)
        {
            drawLine(g);
        }
        else
        {
            left = lnode;
            right = rnode;
            x0 = left.getX();
            y0 = left.getY();
            lw = left.getWidth();
            lh = left.getHeight();
            x1 = right.getX();
            y1 = right.getY();
            rw = right.getWidth();
            rh = right.getHeight();
            g.drawLine(x0 + lw / 2, y0 + lh / 2, x1 + rw / 2, y1 + rh / 2);
        }
    }

    private void drawLine(Graphics g)
    {
        // TODO Auto-generated method stub
        double H = 10; // 箭头高度
        double L = 6; // 底边的一半
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        left = lnode;
        right = rnode;
        x0 = left.getX();
        y0 = left.getY();
        lw = left.getWidth();
        lh = left.getHeight();
        x1 = right.getX();
        y1 = right.getY();
        rw = right.getWidth();
        rh = right.getHeight();
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        int sx = x0 + lw / 2, sy = y0 + lh / 2;
        int ex = x1 + rw / 2, ey = y1 + rh / 2;
        double d = Math.sqrt(Math.abs(ex - sx)
                             * Math.abs(ex - sx)
                             + Math.abs(ey - sy)
                             * Math.abs(ey - sy));

        double xishu = (d - 39) / d;
        double arrx = xishu * (ex - sx) + sx + 1.5;
        double arry = xishu * (ey - sy) + sy + 1.5;
        double[] arrXY_1 = rotateVec(arrx - x0,
                                     arry - y0,
                                     awrad,
                                     true,
                                     arraow_len);
        double[] arrXY_2 = rotateVec(arrx - x0,
                                     arry - y0,
                                     -awrad,
                                     true,
                                     arraow_len);
        double x_3 = arrx - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = arry - arrXY_1[1];
        double x_4 = arrx - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = arry - arrXY_2[1];

        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线

        g.drawLine(sx, sy, ex, ey);

        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(arrx, arry);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.closePath();
        // 实心箭头
        ((Graphics2D)g).fill(triangle);
        // 非实心箭头
        // g2.draw(triangle);

    }

    // 计算
    public static double[] rotateVec(double e,
                                     double f,
                                     double ang,
                                     boolean isChLen,
                                     double newLen)
    {

        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = e * Math.cos(ang) - f * Math.sin(ang);
        double vy = e * Math.sin(ang) + f * Math.cos(ang);
        if (isChLen)
        {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }

}