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


public class Visualization extends JFrame {

    public Visualization() {

        initUI();
    }

    private void initUI() {
        
        setTitle("Flower Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new Surface());

        setSize(800, 600);
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