package visualization;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Visualization extends JFrame {

	public static int width = 800;
	public static int height = 600;
	private JMenuBar menuBar;
	public JFrame frameLegend;
	private Surface surface;
    public static int legendWidth = 400;
    public static int legendHeight = 300;
    public Visualization() {

        initUI();
    }

    private void initUI() {
        surface = new Surface();
        menuBar = new JMenuBar();
        setTitle("Flower Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(surface);
        setJMenuBar(menuBar);
        addViewMenu();
        addLegendMenu();
        setSize(width, height);
        setLocationRelativeTo(null);
       
    }

    
    private void addLegendMenu() {
    	JMenu legendMenu = new JMenu("Legend");
        menuBar.add(legendMenu);
		JMenuItem enableLegendMenu = new JMenuItem("Enable");
		JMenuItem disableLegendMenu = new JMenuItem("Disable");
		legendMenu.add(enableLegendMenu);
		legendMenu.add(disableLegendMenu);
		enableLegendMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameLegend = new JFrame();
				frameLegend.setSize(legendWidth, legendHeight);
				frameLegend.setVisible(true);
				//surface.setPackageColor = false;
			}

		});
		disableLegendMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//surface.setPackageColor = true;
			}

		});
		
	}

	private void addViewMenu() {
		JMenu menu = new JMenu("View");
		menuBar.add(menu);
		JMenuItem classMenuItem = new JMenuItem("Class");
		JMenuItem classPackageItem = new JMenuItem("Package");
		menu.add(classMenuItem);
		menu.add(classPackageItem);
		classMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				surface.setPackageColor = false;
			}

		});
		classPackageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				surface.setPackageColor = true;
			}

		});
		
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