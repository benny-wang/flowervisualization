package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

class Surface extends JPanel {
	
	Flower flower1 = new Flower(Color.BLUE, 70, 35, 35, 7);
	
	private void doDrawing(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(new Ellipse2D.Double(0,0,70,70));
		g2.draw(new Ellipse2D.Double(20,70,35,70));
		
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}
