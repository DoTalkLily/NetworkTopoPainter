package ict.topo.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DrawGraph extends JFrame
{
    private static final long serialVersionUID = -1895146153906356102L;

    private Dimension screenSize;

    // 位置定义
    private MyPoint[][] position;

    // 界面元素定义
    private JScrollPane left;

    private JScrollPane right;

    private JScrollPane menuScroll;

    private JPanel leftPanel;

    private JTree tree;

    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;

    private PopupMenu popMenu;

    private JMenuBar menuBar;

    private MyMouseAction mouseListener;

    private JLayeredPane currentPane;

    // 变量定义
    private String frameName;

    // 上方tab总数
    private int tabCount;

    private int currentTab;

    public DrawGraph(String name)
    {
        frameName = name;
        tabCount = 0;
        currentTab = 0;
        init();
    }

    public DrawGraph()
    {
        tabCount = 0;
        currentTab = 0;
        init();
    }

    public DrawGraph(MyMouseAction action)
    {
        tabCount = 0;
        currentTab = 0;
        setMyAction(action);
        init();
    }

    public DrawGraph(String name, MyMouseAction action)
    {
        frameName = name;
        tabCount = 0;
        currentTab = 0;
        setMyAction(action);
        init();
    }

    // 点类，存点的横纵坐标
    public class MyPoint
    {
        int x, y;

        public MyPoint(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    public void setMyAction(MyMouseAction action)
    {
        mouseListener = action;
    }

    public void init()
    {
        // 创建鼠标事件监听
        popMenu = new PopupMenu();
        if (mouseListener == null)
        {
            mouseListener = new MyMouseAction();
        }
        mouseListener.setPopMenu(popMenu);
        // 获得窗口大小
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 初始化位置矩阵
        position = new MyPoint[16][16];
        int tempx = (screenSize.width - 200) / 16, tempy = screenSize.height / 16;
        int x = tempx / 2 - 20, y = tempy / 2 - 25;
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                this.position[i][j] = new MyPoint(x, y);
                x += tempx;
            }
            y += tempy;
            x = tempx / 2 - 20;
        }
        // 元素初始化
        left = new JScrollPane();
        leftPanel = new JPanel();
        right = new JScrollPane();
        menuScroll = new JScrollPane();
        mouseListener.setRightPane(right);
        root = new DefaultMutableTreeNode("设备列表 ");
        tree = new JTree(root);
        treeModel = (DefaultTreeModel)tree.getModel();

        // 设置各元素属性
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(tree);
        left.setBounds(0, 0, 200, screenSize.height - 90);
        left.setBorder(BorderFactory.createEtchedBorder());
        left.setViewportView(leftPanel);
        left.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        left.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        right.setLocation(210, 38);
        right.setSize(screenSize.width - 225, screenSize.height - 118);
        right.setBorder(BorderFactory.createEtchedBorder());
        right.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        right.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        menuScroll.setLocation(210, 0);
        menuScroll.setSize(screenSize.width - 210, 45);
        menuScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        menuScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        // 树结构显示内容定义
        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer)tree.getCellRenderer();

        cellRenderer.setLeafIcon(new ImageIcon(ClassLoader.getSystemResource("images/smallRouter.png")));
        cellRenderer.setOpenIcon(new ImageIcon(ClassLoader.getSystemResource("images/opened.png")));
        cellRenderer.setClosedIcon(new ImageIcon(ClassLoader.getSystemResource("images/closed.png")));
        tree.addMouseListener(mouseListener);

        menuBar = new JMenuBar();
        menuBar.setLayout(null);
        menuBar.setPreferredSize(new Dimension(screenSize.width - 220, 30));
        menuScroll.setViewportView(menuBar);
        getContentPane().setLayout(null);
        setBounds(0, 0, screenSize.width, screenSize.height - 40);
        if (frameName != null)
        {
            setTitle(frameName);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        // 将组件添加到frame中
        getContentPane().add(this.left);
        getContentPane().add(this.right);
        getContentPane().add(this.menuScroll);
    }

    // 获得右上方的menubar对象
    public JMenuBar getRightMenuBar()
    {
        return this.menuBar;
    }

    // 本方法运行新增树节点
    public void addTreeNode(String name)
    {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(name);
        treeModel.insertNodeInto(newNode, root, root.getChildCount());
        TreeNode[] nodes = treeModel.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
    }

    // 将拓扑元素添加到面板上
    public void addTopoData(TopoNode node, JLayeredPane pane)
    {
        JLabel temp = (JLabel)node;
        temp.addMouseListener(mouseListener);
        temp.addMouseMotionListener(mouseListener);
        addTreeNode(temp.getText());
        mouseListener.addNodes(Integer.parseInt(pane.getName()), node);
        pane.add(node);
    }

    public void addTopoData(TopoNode node, int x, int y, JLayeredPane pane)
    {
        if (x <= 0 || x > 15 || y <= 0 || y > 15)
        {
            System.out.print("索引超出范围 ，输入1-15之间的数！");
            return;
        }
        MyPoint point;
        point = position[x - 1][y - 1];
        node.setXY(point.x, point.y);
        node.addMouseListener(mouseListener);
        node.addMouseMotionListener(mouseListener);
        addTreeNode(node.getText());
        mouseListener.addNodes(Integer.parseInt(pane.getName()), node);
        pane.add(node);
    }

    // 重载
    public void addTopoData(TopoLink link, JLayeredPane pane)
    {
        link.addMouseListener(mouseListener);
        link.addMouseMotionListener(mouseListener);
        mouseListener.addlinks(Integer.parseInt(pane.getName()), link);
        pane.add(link);
    }

    // 将拓扑元素添加到面板上
    public void addTopoData(TopoNode node)
    {
        JLabel temp = (JLabel)node;
        temp.addMouseListener(mouseListener);
        temp.addMouseMotionListener(mouseListener);
        addTreeNode(temp.getText());
        mouseListener.addNodes(currentTab, node);
        currentPane.add(node);
    }

    public void addTopoData(TopoNode node, int x, int y)
    {
        if (x <= 0 || x > 15 || y <= 0 || y > 15)
        {
            System.out.print("索引超出范围 ，输入1-15之间的数！");
            return;
        }
        MyPoint point;
        point = position[x - 1][y - 1];
        node.setXY(point.x, point.y);
        node.addMouseListener(mouseListener);
        node.addMouseMotionListener(mouseListener);
        addTreeNode(node.getText());
        mouseListener.addNodes(currentTab, node);
        currentPane.add(node);
    }

    // 重载
    public void addTopoData(TopoLink link)
    {
        link.addMouseListener(mouseListener);
        link.addMouseMotionListener(mouseListener);
        mouseListener.addlinks(currentTab, link);
        currentPane.add(link);
    }

    // 设置当前右侧显示界面
    public void setCurrentPane(JLayeredPane current)
    {
        if (current == null)
        {
            System.out.print("无法设置当前界面");
        }
        currentPane = current;
        mouseListener.setCurrentPane(current);
        currentTab = Integer.parseInt(currentPane.getName());
        if (currentTab == 0)
        {
            currentPane.add(popMenu);// 决定弹出菜单是否显示！
            currentPane.setVisible(true);
            right.setViewportView(currentPane);
        }
        right.repaint();
    }

    // 创建新的右侧面板
    public JLayeredPane createPane()
    {
        tabCount++;
        JMenu menu1 = new JMenu("       界面" + tabCount + "    ");
        menu1.setName(tabCount - 1 + "");
        menu1.setBounds((tabCount - 1) * 80, 0, 85, 28);
        menu1.setBorder(BorderFactory.createEtchedBorder());
        menu1.addMouseListener(mouseListener);
        menuBar.add(menu1);
        // 如果上方tab总宽度超过了右侧面板宽度，则显示滚动条
        if (tabCount * 80 >= this.screenSize.width - 210)
        {
            menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width + 80,
                                                   30));
        }
        menuBar.repaint();
        JLayeredPane tempPanel = new JLayeredPane();
        tempPanel.addMouseListener(mouseListener);
        tempPanel.addMouseMotionListener(mouseListener);
        tempPanel.setName(tabCount - 1 + "");
        tempPanel.setOpaque(true);
        tempPanel.setBackground(Color.white);
        tempPanel.setPreferredSize(new Dimension(screenSize.width - 210,
                                                 screenSize.height - 60));
        tempPanel.setVisible(false);
        right.add(tempPanel);
        mouseListener.addPanes(tempPanel);
        return tempPanel;
    }

    public MyPoint[][] getPosition()
    {
        return position;
    }
}