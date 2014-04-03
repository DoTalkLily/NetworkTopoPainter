package ict.topo.view;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//节点类
public class TopoNode extends JLabel
{
    private static final long serialVersionUID = 8443089188719938763L;

    private String infomation;

    private Color color;

    // syn = 0 是路由器，syn = 1是交换机
    public TopoNode(int syn, String name)
    {
        setSize(55, 45);
        infomation = null;
        color = Color.WHITE;
        setOpaque(true);
        if (syn == 0)// router
        {
            ImageIcon routerIcon = new ImageIcon(ClassLoader.getSystemResource("images/smallRouter.png"));
            setIcon(routerIcon);
        }
        else
        {
            ImageIcon switchIcon = new ImageIcon(ClassLoader.getSystemResource("images/smallSwitch.png"));
            setIcon(switchIcon);
        }
        // 设置文字显示位置为图片下方
        setHorizontalAlignment(JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setText(name);

    }

    public void setXY(int x, int y)
    {
        setBounds(x, y, 55, 45);
    }

    public void setInfo(String info)
    {
        infomation = info;
    }

    public String getInfo()
    {
        return infomation;
    }

    public void setColor(Color c)
    {
        color = c;
        this.repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        setBackground(color);
    }

}