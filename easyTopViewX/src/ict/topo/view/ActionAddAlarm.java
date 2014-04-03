package ict.topo.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

public class ActionAddAlarm extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1260348956423289402L;
	//定义告警级别 7种
	private static Color[] colors  = {Color.GREEN,Color.CYAN,new Color(205,155,255),Color.BLUE,Color.YELLOW,new Color(255,150,0),Color.RED};

	public void actionPerformed(ActionEvent e)
	{
		// 之前已被赋值，这里获取该拓扑对象。也可以通过TopoGraphView.getContextGraphView()快速获取唯一实例。
		
		Component topoView = (Component) getValue("topoGraphView");
		String rs = JOptionPane.showInputDialog(topoView,
				"请输入要添加的告警对象的级别(0,1,2,3,4,5,6),0-最低，6-最高");
		if (rs == null || rs.equals(""))
		{
			rs = "2";
		}
		int level = Integer.parseInt(rs);
		Component obj = (Component)getValue("selectTopoObject");
		if(obj instanceof TopoLink)
		{
			((TopoLink)obj).setColor(colors[level]);
			((TopoLink)obj).repaint();
		}else if(obj instanceof TopoNode)
		{
			((TopoNode)obj).setColor(colors[level]);
		}
	}

	public ActionAddAlarm()
	{
		super("添加告警");
	}
}