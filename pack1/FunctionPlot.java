package pack1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class FunctionPlot extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private double[] x;
	private double[] y;
	
	public FunctionPlot(double[] x, double[] y, double minRange, double maxRange) {
		
		this.x = x;
		this.y = y;
		setSize(1000, 1000);
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setStroke(new BasicStroke(5));
		
		for (int i = 0; i < this.x.length - 1; i++) {
			
			if (i%5 == 0) {
			
				gr.setColor(Color.red);
				
			}
			else {
				
				gr.setColor(Color.BLUE);
				
			}
			
			Shape s = new Line2D.Double((this.x[i] - 1400) * 3 + 20, 1000 - (this.y[i] * 1000 + 500), (this.x[i+1] - 1400) * 3 + 20, 1000- (this.y[i+1] * 1000 + 500));
			gr.draw(s);
			//gr.drawLine((int) (this.x[i] - 1400) * 3, (int) (this.y[i] * 1000), (int) (this.x[i+1] - 1400) * 3, (int) (this.y[i+1] * 1000));
			
		}
		
	}
	

}
