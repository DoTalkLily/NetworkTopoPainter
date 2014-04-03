package ict.topo.view;
import java.awt.*;

import javax.swing.border.Border;

public class MyBorder implements Border {
	private int thinkness ;
    private Color color;
	public MyBorder(Color c)
	{
		thinkness = 1;
		color = c;
	}
	
	public MyBorder()
	{
		color=Color.BLUE;
		thinkness = 1;
	}
	
	public Insets getBorderInsets(Component c) {
		return new Insets(thinkness, thinkness, thinkness, thinkness);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(color);
		g2d.setStroke(new BasicStroke(thinkness, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL, 1, new float[] { thinkness * 2,
						thinkness }, thinkness));
		g2d.drawRect(x + thinkness / 2, y + thinkness / 2, width - thinkness,
				height - thinkness);
	}

	public boolean isBorderOpaque() {
		return false;
	}
}