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

    // ��ǰ�Ҳ����
    private JLayeredPane currentPane;

    private Map<Integer, ArrayList<TopoNode>> nodeList;

    private Map<Integer, ArrayList<TopoLink>> linkList;

    // ȫ��LayeredPane����洢
    private static ArrayList<JLayeredPane> allPanes;

    private static int lastTab;

    public boolean diy;

    // �Ϸ�tab�������ʾ��ʽ������Ը���tab�������ʾͬһ�����˽ṹfalseΪĬ����ʾ������ͬtab�����ʾ��ͬ���ˣ�trueΪ��ͬtab��ʾ�ṹ��ȫ��ͬ����
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

    // ������϶�ʱ�������¼��� ��¼����갴��(��ʼ�϶�)��λ�á�
    @Override
    public void mouseDragged(MouseEvent e)
    {
        // TODO Auto-generated method stub

        Component com = ((Component)e.getSource());

        if (e.getSource() instanceof TopoNode)
        {// �����label
         // ת������ϵͳ
            Point newPoint = SwingUtilities.convertPoint(com,
                                                         e.getPoint(),
                                                         com.getParent());
            // ���ñ�ǩ����λ��
            com.setLocation(com.getX() + (newPoint.x - point.x),
                            com.getY() + (newPoint.y - point.y));
            // ���������
            point = newPoint;
            com.repaint();
            com.getParent().repaint();

        }
        else if (e.getSource() instanceof JPanel)
        {
            ((JPanel)e.getSource()).repaint();
        }

    }

    // ����갴��ʱ�������¼��� ��¼����갴��(��ʼ�϶�)��λ�á�
    @Override
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub
        if (e.getSource() instanceof TopoNode)
        {// �����label
         // �õ���ǰ�����
            Component com = ((Component)e.getSource());

            // ת������ϵͳ
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
                        // ת������ϵͳ
                        ((JLayeredPane)tempNode.getParent()).moveToFront(tempNode);// ��ѡ��ͼ���ö���
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
            // ����ڵ�Ϊ�գ�ֱ�ӷ���
            if (selectedNode == null)
                return;
            // ��ȡ��ѡ�нڵ�ĸ��ڵ�
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
            // ������ڵ�Ϊ�գ�ֱ�ӷ���
            if (parent == null)
                return;
            // ��ȡѡ�нڵ��ѡ������
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
        // ����Ҽ�
        int mods = e.getModifiers();
        if ((mods & InputEvent.BUTTON3_MASK) != 0)
        {
            allPanes.get(lastTab).add(popupMenu);
            if (com instanceof TopoNode)
            {
                // �����˵�
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
                    tempStr = "��·�ӿڣ�" + showLine.getLineName() + "\n";
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
        String tempStr = "�豸��" + ((TopoNode)com).getText() + "\n";
        if (((TopoNode)com).getInfo() != null)
        {
            tempStr += "��Ϣ��" + ((TopoNode)com).getInfo();
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
            popupMenu.add("�Ҽ�����"
                          + tl.getLnode().getText()
                          + "����"
                          + tl.getRnode().getText());
        }
        else if (com instanceof TopoNode)
        {
            popupMenu.add("�Ҽ�����" + ((TopoNode)com).getText());
        }
        popupMenu.addSeparator();
        MenuItem addAlarm = new MenuItem("��Ӹ澯");
        ActionAddAlarm actionAddAlarm = new ActionAddAlarm();
        actionAddAlarm.putValue("topoGraphView", com.getParent());
        actionAddAlarm.putValue("selectTopoObject", com);
        addAlarm.addActionListener(actionAddAlarm);
        popupMenu.add(addAlarm);
        popupMenu.addSeparator();
        MenuItem removeAlarm = new MenuItem("ɾ���澯");
        ActionRemoveAlarm actionRemoveAlarm = new ActionRemoveAlarm();
        actionRemoveAlarm.putValue("selectTopoObject", com);
        removeAlarm.addActionListener(actionRemoveAlarm);
        popupMenu.add(removeAlarm);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    /**
     * ������Ҫ��չ
     * 
     */
    public void drawColorLines(int tabIndex)
    {
        // TODO Auto-generated method stub

    }

    /**
     * ������Ҫ��չ
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