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
		
		Flower flower1 = new Flower(Color.BLUE, 50, 300, 250, 5);
		Flower[] flowers  = {flower1};
				
		drawFlowers(g2, flowers);
		
    }
	
	private void drawFlowers(Graphics2D g, Flower[] flowers) {
		
		for(int i=0;i<flowers.length;i++){
			Flower flower = flowers[i];
			
			g.setColor(flower.color);
			g.fillOval(flower.x-flower.size/2,flower.y-flower.size/2,flower.size,flower.size);
			drawPedals(g, flower);
		}
		
	}
	
	private void drawPedals(Graphics2D g, Flower flower){			
		
		int pedalSize = flower.size/2;
		
		for(int i=0;i<flower.numMethods;i++){
			
			
			
			g.fillOval(flower.x-pedalSize/2,flower.y + flower.size/2,pedalSize,pedalSize*2);
//			g.draw(new Ellipse2D.Double(flower.x-pedalSize/2,flower.y + flower.size/2,pedalSize,pedalSize*2));
			g.rotate(Math.toRadians(360/flower.numMethods),flower.x,flower.y);
		}
	}

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}
