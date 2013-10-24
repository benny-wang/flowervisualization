package visualization;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

public class Visualization extends JFrame {

    public Visualization() {

        initUI();
    }

    private void initUI() {
        
        setTitle("Points");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new Surface());

        setSize(350, 250);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Visualization ps = new Visualization();
                ps.setVisible(true);
            }
        });
    }
}