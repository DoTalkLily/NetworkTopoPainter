package ict.topo.view;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;

public class ActionRemoveAlarm extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4898754024134946342L;

	public void actionPerformed(ActionEvent e)
	{
		Component obj = (Component)getValue("selectTopoObject");
		if(obj instanceof TopoLink)
		{
			((TopoLink)obj).setColor(Color.BLACK);
			((TopoLink)obj).repaint();
		}else if(obj instanceof TopoNode)
		{
			((TopoNode)obj).setColor(Color.WHITE);
		}
	}

	public ActionRemoveAlarm()
	{
		super("Çå³ý¸æ¾¯");
	}
}
