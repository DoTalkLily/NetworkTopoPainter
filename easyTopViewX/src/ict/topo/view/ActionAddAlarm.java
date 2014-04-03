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
	//����澯���� 7��
	private static Color[] colors  = {Color.GREEN,Color.CYAN,new Color(205,155,255),Color.BLUE,Color.YELLOW,new Color(255,150,0),Color.RED};

	public void actionPerformed(ActionEvent e)
	{
		// ֮ǰ�ѱ���ֵ�������ȡ�����˶���Ҳ����ͨ��TopoGraphView.getContextGraphView()���ٻ�ȡΨһʵ����
		
		Component topoView = (Component) getValue("topoGraphView");
		String rs = JOptionPane.showInputDialog(topoView,
				"������Ҫ��ӵĸ澯����ļ���(0,1,2,3,4,5,6),0-��ͣ�6-���");
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
		super("��Ӹ澯");
	}
}