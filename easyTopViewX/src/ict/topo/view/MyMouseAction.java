package ict.topo.view;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class MyMouseAction implements MouseListener, MouseMotionListener
{
    private Point point;

    private static Component lastCom;

    private int selectedIndex, lastSelectedIndex;

    private PopupMenu popupMenu;

    private JScrollPane rightPane;

    // 当前右侧面板
    private JLayeredPane currentPane;

    private Map<Integer, ArrayList<TopoNode>> nodeList;

    private Map<Integer, ArrayList<TopoLink>> linkList;

    // 全部LayeredPane对象存储
    private static ArrayList<JLayeredPane> allPanes;

    private static int lastTab;

    public boolean diy;

    // 上方tab键点击显示方式——针对各种tab点击都显示同一个拓扑结构false为默认显示，即不同tab点击显示不同拓扑，true为不同tab显示结构完全相同拓扑
    private boolean mode;

    public MyMouseAction()
    {
        mode = false;
        JLabel label = new JLabel();
        point = new Point(0, 0);
        lastCom = (Component)label;
        selectedIndex = 0;
        lastTab = 0;
        lastSelectedIndex = -1;
        nodeList = new HashMap<Integer, ArrayList<TopoNode>>();
        linkList = new HashMap<Integer, ArrayList<TopoLink>>();
        allPanes = new ArrayList<JLayeredPane>();
        popupMenu = new PopupMenu();
        rightPane = new JScrollPane();
        currentPane = new JLayeredPane();
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode(boolean mode)
    {
        this.mode = mode;
    }

    /**
     * @return Returns the lastCom.
     */
    public Component getLastCom()
    {
        return lastCom;
    }

    /**
     * @return Returns the currentPane.
     */
    public JLayeredPane getCurrentPane()
    {
        return currentPane;
    }

    /**
     * @param currentPane The currentPane to set.
     */
    public void setCurrentPane(JLayeredPane currentPane)
    {
        this.currentPane = currentPane;
    }

    public void addNodes(int index, TopoNode node)
    {
        if (nodeList.containsKey(index))
        {
            nodeList.get(index).add(node);
        }
        else
        {
            ArrayList<TopoNode> temp = new ArrayList<TopoNode>();
            temp.add(node);
            nodeList.put(index, temp);
        }
    }

    public Map<Integer, ArrayList<TopoNode>> getNodes()
    {
        return this.nodeList;
    }

    public void setCurrentTab(int tab)
    {
        lastTab = tab;
    }

    public void addlinks(int index, TopoLink link)
    {
        if (linkList.containsKey(index))
        {
            linkList.get(index).add(link);
        }
        else
        {
            ArrayList<TopoLink> temp = new ArrayList<TopoLink>();
            temp.add(link);
            linkList.put(index, temp);
        }
    }

    public Map<Integer, ArrayList<TopoLink>> getLinks()
    {
        return this.linkList;
    }

    public void addPanes(JLayeredPane pane)
    {
        allPanes.add(pane);
    }

    public void setPopMenu(PopupMenu popMenu)
    {
        popupMenu = popMenu;
    }

    public void setRightPane(JScrollPane pane)
    {
        rightPane = pane;
    }

    // 当鼠标拖动时触发该事件。 记录下鼠标按下(开始拖动)的位置。
    @Override
    public void mouseDragged(MouseEvent e)
    {
        // TODO Auto-generated method stub

        Component com = ((Component)e.getSource());

        if (e.getSource() instanceof TopoNode)
        {// 如果是label
         // 转换坐标系统
            Point newPoint = SwingUtilities.convertPoint(com,
                                                         e.getPoint(),
                                                         com.getParent());
            // 设置标签的新位置
            com.setLocation(com.getX() + (newPoint.x - point.x),
                            com.getY() + (newPoint.y - point.y));
            // 更改坐标点
            point = newPoint;
            com.repaint();
            com.getParent().repaint();

        }
        else if (e.getSource() instanceof JPanel)
        {
            ((JPanel)e.getSource()).repaint();
        }

    }

    // 当鼠标按下时触发该事件。 记录下鼠标按下(开始拖动)的位置。
    @Override
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub
        if (e.getSource() instanceof TopoNode)
        {// 如果是label
         // 得到当前坐标点
            Component com = ((Component)e.getSource());

            // 转换坐标系统
            point = SwingUtilities.convertPoint(com,
                                                e.getPoint(),
                                                com.getParent());
            JLabel temp = (JLabel)com;
            MyBorder border = new MyBorder();
            temp.setBorder(border);
            if (temp != lastCom && lastCom instanceof JLabel)
            {
                ((JLabel)lastCom).setBorder(null);
                lastCom = temp;
            }

        }
        else if (e.getSource() instanceof TopoLink)
        {
            ((JLabel)lastCom).setBorder(null);
            TopoNode tempNode;
            ArrayList<TopoNode> arr = nodeList.get(lastTab);
            if (arr != null)
            {
                for (int i = 0; i < arr.size(); i++)
                {
                    tempNode = arr.get(i);
                    if (tempNode.getBounds().contains(e.getPoint()))
                    {
                        // 转换坐标系统
                        ((JLayeredPane)tempNode.getParent()).moveToFront(tempNode);// 将选中图标置顶！
                        MyBorder border = new MyBorder();
                        tempNode.setBorder(border);
                        if (tempNode != lastCom && lastCom instanceof JLabel)
                        {
                            ((JLabel)lastCom).setBorder(null);
                            lastCom = tempNode;
                        }
                    }
                }
            }
        }
        else if (e.getSource() instanceof JTree)
        {
            ((JLabel)lastCom).setBorder(null);
            JTree tree = (JTree)e.getSource();
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            // 如果节点为空，直接返回
            if (selectedNode == null)
                return;
            // 获取该选中节点的父节点
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
            // 如果父节点为空，直接返回
            if (parent == null)
                return;
            // 获取选中节点的选中索引
            selectedIndex = parent.getIndex(selectedNode);
            if (lastSelectedIndex != selectedIndex)
            {
                MyBorder border = new MyBorder();
                if (nodeList.get(lastTab).size() <= selectedIndex)
                {
                    return;
                }
                nodeList.get(lastTab).get(selectedIndex).setBorder(border);
                if (lastSelectedIndex != -1)
                {
                    nodeList.get(lastTab).get(lastSelectedIndex).setBorder(null);
                }
                lastSelectedIndex = selectedIndex;
                lastCom = nodeList.get(lastTab).get(lastSelectedIndex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub
        Component com = ((Component)e.getSource());
        String tempStr = null;
        TopoNode tempNode = null;
        // 鼠标右键
        int mods = e.getModifiers();
        if ((mods & InputEvent.BUTTON3_MASK) != 0)
        {
            allPanes.get(lastTab).add(popupMenu);
            if (com instanceof TopoNode)
            {
                // 弹出菜单
                popupMenu.removeAll();
                showMenu(e, com);
            }
            else if (com instanceof TopoLink)
            {
                ArrayList<TopoNode> arr = nodeList.get(lastTab);
                for (int i = 0; i < arr.size(); i++)
                {
                    tempNode = arr.get(i);
                    if (tempNode.getBounds().contains(e.getPoint()))
                    {
                        popupMenu.removeAll();
                        showMenu(e, tempNode);
                        tempNode = null;
                        return;
                    }
                }

                int mx = e.getX(), my = e.getY();
                double min = 200, t = 0;
                TopoLink temp = null, showLine = null;
                ArrayList<TopoLink> ar = linkList.get(lastTab);
                for (int i = 0; i < ar.size(); i++)
                {
                    temp = ar.get(i);
                    t = cal(temp.x0 + temp.lw / 2,
                            temp.y0 + temp.lh / 2,
                            temp.x1 + temp.rw / 2,
                            temp.y1 + temp.rh / 2,
                            mx,
                            my);
                    if (t < min)
                    {
                        min = t;
                        showLine = temp;
                    }
                }
                if (showLine != null)
                {
                    popupMenu.removeAll();
                    showMenu(e, showLine);
                    tempStr = null;
                }
            }
        }
        else if (e.getClickCount() >= 2 && com instanceof JLabel)
        {
            if (com instanceof TopoLink)
            {
                int mx = e.getX(), my = e.getY();
                double min = 200, t = 0;
                TopoLink temp = null, showLine = null;
                ArrayList<TopoLink> ar = linkList.get(lastTab);
                for (int i = 0; i < ar.size(); i++)
                {
                    temp = ar.get(i);
                    t = cal(temp.x0 + temp.lw / 2,
                            temp.y0 + temp.lh / 2,
                            temp.x1 + temp.rw / 2,
                            temp.y1 + temp.rh / 2,
                            mx,
                            my);
                    if (t < min)
                    {
                        min = t;
                        showLine = temp;
                    }
                }

                if (showLine != null)
                {
                    tempStr = "链路接口：" + showLine.getLineName() + "\n";
                    if (showLine.getCost() != 0)
                    {
                        tempStr += "Cost: " + showLine.getCost();
                    }
                    JOptionPane.showMessageDialog(showLine.getParent(), tempStr);
                    tempStr = null;
                }
            }
            else if (com instanceof TopoNode)
            {
                showDialog(e, com);
            }
        }
        else if (com instanceof JMenu)
        {
            int index = Integer.parseInt(((JMenu)com).getName());
            if (!mode)
            {
                if (lastTab != index && index < allPanes.size())
                {
                    allPanes.get(lastTab).setVisible(false);
                    JLayeredPane pane = allPanes.get(index);
                    rightPane.setViewportView(pane);
                    pane.setVisible(true);
                    lastTab = index;
                    currentPane = pane;
                }
            }
            else
            {
                clearColorLines(lastTab);
                drawColorLines(index);
                lastTab = index;
            }
        }
    }

    public void showDialog(MouseEvent e, Component com)
    {
        String tempStr = "设备：" + ((TopoNode)com).getText() + "\n";
        if (((TopoNode)com).getInfo() != null)
        {
            tempStr += "信息：" + ((TopoNode)com).getInfo();
        }
        // JOptionPane.showMessageDialog(com.getParent(), tempStr);
    }

    public double cal(int x1, int y1, int x2, int y2, int mx, int my)
    {
        double d = (Math.abs((y2 - y1)
                             * mx
                             + (x1 - x2)
                             * my
                             + ((x2 * y1) - (x1 * y2))))
                   / (Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x1 - x2, 2)));
        return d;
    }

    public void showMenu(MouseEvent e, Component com)
    {
        if (com instanceof TopoLink)
        {
            TopoLink tl = (TopoLink)com;
            popupMenu.add("右键对象："
                          + tl.getLnode().getText()
                          + "——"
                          + tl.getRnode().getText());
        }
        else if (com instanceof TopoNode)
        {
            popupMenu.add("右键对象：" + ((TopoNode)com).getText());
        }
        popupMenu.addSeparator();
        MenuItem addAlarm = new MenuItem("添加告警");
        ActionAddAlarm actionAddAlarm = new ActionAddAlarm();
        actionAddAlarm.putValue("topoGraphView", com.getParent());
        actionAddAlarm.putValue("selectTopoObject", com);
        addAlarm.addActionListener(actionAddAlarm);
        popupMenu.add(addAlarm);
        popupMenu.addSeparator();
        MenuItem removeAlarm = new MenuItem("删除告警");
        ActionRemoveAlarm actionRemoveAlarm = new ActionRemoveAlarm();
        actionRemoveAlarm.putValue("selectTopoObject", com);
        removeAlarm.addActionListener(actionRemoveAlarm);
        popupMenu.add(removeAlarm);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * 根据需要扩展
     * 
     */
    public void drawColorLines(int tabIndex)
    {
        // TODO Auto-generated method stub

    }

    /**
     * 根据需要扩展
     * 
     */
    public void clearColorLines(int tabIndex)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

}