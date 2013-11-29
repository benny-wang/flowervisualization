package visualization;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


public class MainWindow extends JFrame {

	public static int width = 1000;
	public static int height = 600;
	private JMenuBar menuBar;
	private Visualizer surface;
    public static int legendWidth = 200;
    public static int legendHeight = 300;
    public MainWindow() {
        surface = new Visualizer();
        menuBar = new JMenuBar();
        addFileMenu();
        addViewMenu();
        addLegendMenu();
        addInfoMenu();
        setSize(width, height);
        setJMenuBar(menuBar);
        add(surface);
        setLocationRelativeTo(null);      
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Flower Visualization");       
        
    }
    


	private void addFileMenu() {
		JMenu fileMenu = new JMenu("Visualization");
        
        JMenuItem openMenu = new JMenuItem("Open");
        openMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.openFile();
			}
		});
        fileMenu.add(openMenu);
        
        JMenuItem randomMenu = new JMenuItem("Randomize");
        randomMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.randomizeFlower();
			}
		});
        fileMenu.add(randomMenu);
        
        
        menuBar.add(fileMenu);
	}

    
    private void addInfoMenu() {
    	JMenu info = new JMenu("Info");
        menuBar.add(info);
		JMenuItem enableInfoMenu = new JMenuItem("Enable");
		JMenuItem disableInfoMenu = new JMenuItem("Disable");
		info.add(enableInfoMenu);
		info.add(disableInfoMenu);
		enableInfoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.flowerInfo = true;
			}

		});
		disableInfoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.flowerInfo = false;
			}

		});
		
		JMenuItem helpInfoMenu = new JMenuItem("Help");
		
		info.add(helpInfoMenu);
		helpInfoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHelpMessage();
			}

		});
		
		
	}
    
    private void showHelpMessage(){
    	JOptionPane.showMessageDialog(this, 
    			  "Select flower - Mouse left click on flower\n"
    			+ "Move flower - Mouse left click hold and drag\n"
    			+ "Move screen - Ctrl + Mouse left click hold and drag\n"
    			+ "Zoom in/out - Ctrl + Mouse scroll wheels");
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
				surface.contributorLegend = true;
			}

		});
		disableLegendMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.contributorLegend = false;
			}

		});
		
	}

	private void addViewMenu() {
		JMenu menu = new JMenu("View");
		menuBar.add(menu);
		JMenuItem contributorMenuItem = new JMenuItem("Contributor");
		JMenuItem classMenuItem = new JMenuItem("Contributor/Age");
		JMenuItem classPackageItem = new JMenuItem("Package");
		JMenuItem classAgeItem = new JMenuItem("Age");
		
		menu.add(contributorMenuItem);
		menu.add(classMenuItem);
		menu.add(classPackageItem);
		menu.add(classAgeItem);
		
		contributorMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.viewState = 3;
				surface.resetLegend();
			}
		});
		
		classMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.viewState = 1;
				surface.resetLegend();
			}

		});
		classPackageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.viewState = 0;
				surface.resetLegend();
			}

		});
		classAgeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				surface.viewState = 2;
				surface.resetLegend();
			}

		});
		
	}

	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                MainWindow ps = new MainWindow();
                ps.setVisible(true);
            }
        });
    }
}