package visualization;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Date;
import java.util.Map;

class Surface extends JPanel implements ActionListener, MouseListener, MouseWheelListener, MouseMotionListener, ChangeListener  {
	
//	Flower flower1 = new Flower(Color.BLUE, 50, 300, 250, 10, );
//	Flower flower2 = new Flower(Color.RED, 25, 500, 350, 10);
//	Flower[] flowers  = {flower1, flower2};
//	
	int currentFrame = 0;
	public boolean flowerInfo = true;
	public boolean contributorLegend = true;
	int frameRate = 50; //millseconds
	double framesPerSecond = 1/(((double)frameRate)/1000);
	int currentTFrame = 0;
	
	static Repository repo;
	
	private Timer timer;

	private Flower hitFlower;

	private double zoom = 1;
	private double zoomX = 0;
	private double zoomY = 0;
	private double preZoomX = 0;
	private double preZoomY = 0;
	private double preZoom = 1;
	private double maxZoom = .1;
	
	private int wheelMoved = 0;
	private double maxZoomX = 25;
	private double maxZoomY = 25;
	
	private double legendTranslateY = 50;

	private int zoomWidth = getWidth();

	private int zoomHeight = getHeight();

	public int viewState = 2;
	public long lastTimeChecked = 0;
	
	public JSlider frameSlider;
	
    private boolean pauseVideo = false;
	
    //Create a file chooser
    public JFileChooser fc;
	
	public Surface () {

		Initialize();
	}
	
	private void Initialize () {
	       
			JButton playButton = new JButton("Play");
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pauseVideo = false;
				}
			});
			
			JButton pauseButton = new JButton("Pause");
			pauseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					pauseVideo = true;
				}
			});
			
			add(playButton);
			add(pauseButton);
		
	       frameSlider = new JSlider(JSlider.HORIZONTAL,
                   0, 0, 0);
	       frameSlider.addChangeListener(this);

	       //Turn on labels at major tick marks.
	       frameSlider.setMajorTickSpacing(10);
	       //framesPerSecond.setMinorTickSpacing(10);
	       frameSlider.setPaintTicks(true);
	       frameSlider.setPaintLabels(true);
	       
	       frameSlider.setOpaque(false);
	       frameSlider.setBackground(Color.white);
	       
	       Dimension d = frameSlider.getPreferredSize();  
	       frameSlider.setPreferredSize(new Dimension((int) (Visualization.width * .8),d.height)); 
	       
	       add(frameSlider);
	       
	       addMouseListener(this);
	       addMouseWheelListener(this);
	       addMouseMotionListener(this);
	       setBackground(Color.white);
	       setOpaque(true);
	       setDoubleBuffered(true);    
	       
	       File curr = new File("src");
	       fc = new JFileChooser(curr);
	       
	       fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	       fc.setSelectedFile(fc.getCurrentDirectory());
	       fc.setDialogTitle("Directory Chooser");
	       fc.setMultiSelectionEnabled(false);

	}
	
    @Override
    public void actionPerformed (ActionEvent e) {    	
    	    	
    	   	
    	repaint();
    	
    	
//    	for(int i=0;i<flowers.length;i++){
//			Flower flower = flowers[i];
//						
//			flower.makeDarker();		    
//		    //System.out.println("Darker");
//    	}
    	
    	
    	if(System.currentTimeMillis() - lastTimeChecked > 1000) {   		
    		if(currentFrame < repo.frames.length-1 && pauseVideo == false) {
	    		currentFrame++;
	    		frameSlider.setValue(currentFrame);
    		}
    		lastTimeChecked = System.currentTimeMillis(); 
    	}
    	
    	
    	
    }

    
    public void openFile (){
    	int returnVal = fc.showOpenDialog(Surface.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
                        
            //This is where a real application would open the file.
            System.out.println("Opening: " + file.getName() + ".");
            
 	       repo = new Repository(file.getPath());
 	       
 	       resetSurface();

        } else {
        	System.out.println("Open command cancelled by user.");
        }
    }
    
    public void resetSurface(){
	       frameSlider.setMaximum(repo.frames.length-1);
	       frameSlider.setValue(0);
 	       
	       timer = new Timer(frameRate, this);
	       timer.start();
    }
    
    public void drawSliderRect(Graphics2D g){
		g.setColor(new Color(105,114,216));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g.fillRect(0, 0, Visualization.width, 50);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
    }
    
    private void drawPackageLegend (Graphics2D g){
		if (contributorLegend) {
			Font font = Font.decode("Times New Roman");

			int x, y;
			x = Visualization.width - Visualization.legendWidth;
			y = 10;
			
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.fillRect(x, y-10, Visualization.legendWidth, Visualization.height);

			for (FlowerPackage flowerPackage : repo.packageColor.values()) {
				
				int index = flowerPackage.name.indexOf("protocol") + 9;
				String castedName = flowerPackage.name.substring(index);
				
				
//				Rectangle2D nameRect = g.getFontMetrics(font).getStringBounds(
//						castedName, g);
//				double nameWidth = nameRect.getWidth();

				g.setColor(flowerPackage.color);
				g.fillRect(x, y, 25, 25);
				g.setColor(Color.black);
				g.drawString(castedName, x + 30, y + 25);

				y += 25 + 20;

			}
		}
    }
    
    private void drawContributorLegend (Graphics2D g){
		if (contributorLegend) {
			Font font = Font.decode("Times New Roman");

			int x, y;
			x = Visualization.width - Visualization.legendWidth;
			y = 10;
			
			g.setColor(Color.white);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.fillRect(x, y-10, Visualization.legendWidth, Visualization.height);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			for (Contributor contributor : repo.contributorColor.values()) {

				Rectangle2D nameRect = g.getFontMetrics(font).getStringBounds(
						contributor.name, g);
				double nameWidth = nameRect.getWidth();

//				if (x + 30 + 15 + nameWidth > Visualization.width) {
//					y += 25 + 20;
//					x = 10;
//				}
				
//				g.setColor(Color.white);
//				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
//				g.fillRect(x, y, 30 + 15 + (int) nameWidth, 25);
//				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

				g.setColor(contributor.color);
				g.fillRect(x, y, 25, 25);
				g.setColor(Color.black);
				g.drawString(contributor.name, x + 30, y + 25);

//				x += 30 + 15 + nameWidth;
				y += 25 + 20;

			}
		}
    }
    Graphics2D g2;


    private void doDrawing(Graphics g) {
    	updateFlowerFrame();
		g2 = (Graphics2D) g;
		AffineTransform at1 = g2.getTransform();
		drawDragged(g2);

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		drawZoomIn(g2);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, Visualization.width, Visualization.height);
		g.setColor(Color.BLACK);

		drawDependencies(g2);

		AffineTransform at = g2.getTransform();
		
		drawFlowers(g2);

		g2.setTransform(at);
		g2.setTransform(at1);

		g.translate(0, (int) legendTranslateY);
		
		if(viewState == 0){
			drawPackageLegend(g2);
			g2.setTransform(at1);
		}else{
			drawContributorLegend(g2);
			g2.setTransform(at1);
		}		

		drawFlowerInformation(g2);
		g2.setTransform(at1);
		
		
    }
	
	private void updateFlowerFrame() {
		Frame frame = repo.frames[currentFrame];
		Map<String, Flower> flowersMap = repo.flowers;
		
		Flower[] currentFlowers = frame.flowers;
		
    	for(int i=0;i<currentFlowers.length;i++){
			Flower frameFlower = currentFlowers[i];
						
			Flower flower = flowersMap.get(frameFlower.methodName);
			
			
			flower.size = frameFlower.size;
			flower.exist = true;
			flower.numMethods = frameFlower.numMethods;
			flower.contributor = frameFlower.contributor;
    	}
		
	}

	private void drawDragged(Graphics2D g2) {
		AffineTransform old = g2.getTransform();
		AffineTransform tx = new AffineTransform(old);
		tx.translate(draggedX, draggedY);
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		g2.setTransform(tx);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
	}

	private void drawFlowerInformation(Graphics2D g) {
		
		if (hitFlower!=null && flowerInfo) {
			Font font = Font.decode("Times New Roman");

			int x, y;
			x = 0;
			y = getHeight()-73;

			// for(Contributor contributor : repo.contributorColor.values()){

			Rectangle2D nameRect = g.getFontMetrics(font).getStringBounds("Class name: " + hitFlower.methodName, g);
			double nameWidth = nameRect.getWidth();
			String contributor = "Dependency: ";
			boolean first = true;
			for (Map.Entry<String, Integer> entry : hitFlower.dependencies.entrySet()) {
				if(!first){
					contributor+=", ";
				} else first = false;
				String methodName = entry.getKey();
				contributor += methodName;
			}
			Rectangle2D dependRect = g.getFontMetrics(font).getStringBounds(contributor, g);
			g.setColor(Color.white);
			double width = Math.max(nameWidth,  dependRect.getWidth());
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
			g.fillRect( x,  y, 60 + (int) width,20 + (int) width);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			// g.setColor(contributor.color);
			g.fillRect( x, (int) y, 25, 25);
			g.setColor(Color.black);
			g.drawString("Class: " + hitFlower.methodName,  x,  y+18);
			//g.drawString(hitFlower.contributor,  x,  y );

			
			g.setColor(Color.black);
			g.drawString("Package: " + hitFlower.packageName,  x,  y+31);
			
			g.drawString("Number of Methods: " + hitFlower.numMethods,  x,  y+44);
			
			g.drawString("Contributor: " + hitFlower.contributor,  x,  y+57);
			
			Date frameDate = new Date((repo.frames[currentFrame].time - hitFlower.age) * 1000);
			
			g.drawString("Last Changed: " + frameDate.toLocaleString(),  x,  y+70);
			

			}


		}
		// x += 30 + 15 + nameWidth;

		// }

	private void drawZoomIn(Graphics2D g2) {
		
		double diffX = preZoomX - (zoomX - zoomX * zoom);
		double diffY = preZoomY - (zoomY - zoomY * zoom);
		double diffZoom = preZoom - (zoom);
		
		if(Math.abs(diffX) > maxZoomX){
			preZoomX = preZoomX - diffX  / (framesPerSecond/2);			
		}
		
		if(Math.abs(diffY) > maxZoomY){
			preZoomY = preZoomY - diffY  / (framesPerSecond/2);	
		}
		
		if(Math.abs(diffZoom) > maxZoom){
			preZoom = preZoom - diffZoom / (framesPerSecond/2);
		}
		
		//zoomX = preZoomX;
		//zoomY = preZoomY;
		
		AffineTransform old = g2.getTransform();
		AffineTransform tr2 = new AffineTransform(old);
		
		tr2.translate(preZoomX, preZoomY);		
		tr2.scale(preZoom, preZoom);	
		
		//System.out.println(preZoomX + " a:" + preZoom);
		
		g2.setTransform(tr2);

	}

	private void drawDependencies(Graphics2D g2) {
		if(hitFlower != null){
			Map<String, Integer> dependencies = hitFlower.dependencies;
			
			Flower flower;
			
			for (Map.Entry<String, Integer> entry : dependencies.entrySet()) {
			    String methodName = entry.getKey();

				flower = repo.flowers.get(methodName);
				g2.setColor(Color.black);
				if(flower != null && flower.exist == true){				
					g2.draw(new Line2D.Double(hitFlower.x,hitFlower.y,flower.x,flower.y));
				}
			}
		}		
	}
	
	private void drawFlowers(Graphics2D g) {
		
		Frame frame = repo.frames[currentFrame];
		Map<String, Flower> flowersMap = repo.flowers;
		
		Flower[] currentFlowers = frame.flowers;
		
    	for(int i=0;i<currentFlowers.length;i++){
			Flower frameFlower = currentFlowers[i];
						
			Flower flower = flowersMap.get(frameFlower.methodName);
			
			
			flower.size = frameFlower.size;
			
//			if(flower.size > maxFlowerSize){
//				flower.size = maxFlowerSize;
//			}else if(flower.size < maxFlowerSize*.3){
//				flower.size = (int) Math.ceil( maxFlowerSize * .3);
//			}
//			int size = (int) ((int) ((flower.size / repo.maxClassLines) * (maxFlowerSize - maxFlowerSize*0.4)) + maxFlowerSize*0.4);
//			if (size > maxFlowerSize) {
//				flower.size = maxFlowerSize;
//			} else {
//				flower.size = size;
//			}
			flower.exist = true;
			flower.numMethods = frameFlower.numMethods;
			flower.contributor = frameFlower.contributor;
			flower.age = frameFlower.age;
			
			if(frameFlower.age == 0){
				flower.color = frameFlower.color;
				frameFlower.changed = false;

			}
			
			if(viewState == 1){
				flower.makeDarker();
			}else if(viewState == 2 ){
				flower.makeAgeColor();
			}
			
			flower.attraction(repo.flowers);
			flower.repulsion(repo.flowers);
			
//			g.setColor(Color.black);
//			g.fillOval(flower.x-(flower.size*3/2),flower.y-(flower.size*3/2),flower.size*3,flower.size*3);
			
			if(viewState == 0){
				
				FlowerPackage fPackage = repo.packageColor.get(flower.packageName);			
				
				if(fPackage != null)
					g.setColor(fPackage.color);
				else
					g.setColor(flower.color);
			}else if(viewState == 3){
				Contributor contributor = repo.contributorColor.get(flower.contributor);
				
				g.setColor(contributor.color);
			}else{
				g.setColor(flower.color);
			}
			
			Ellipse2D.Double flowerShape = new Ellipse2D.Double(flower.x-flower.size/2, flower.y-flower.size/2, flower.size, flower.size);
//			g.fillOval((int)(flower.x-flower.size/2),(int)(flower.y-flower.size/2),flower.size,flower.size);
			g.fill(flowerShape);
			
			
			drawPetals(g, flower);
    	}
    	
    	
		
//		for(int i=0;i<flowers.length;i++){
//			Flower flower = flowers[i];
//			
//			g.setColor(flower.color);
//			g.fillOval(flower.x-flower.size/2,flower.y-flower.size/2,flower.size,flower.size);
//			drawPetals(g, flower);
//		}
		
	}
	
	private void drawPetals(Graphics2D g, Flower flower){			
		
		int petalSize = flower.size/2;
		
		AffineTransform oldXForm = g.getTransform();
		
		int numPetals = (int) (Math.sqrt(flower.numMethods));
		
		for(int i=0;i<numPetals;i++){
			Ellipse2D.Double petalShape = new Ellipse2D.Double(flower.x-petalSize/2, flower.y + flower.size/2, petalSize, petalSize*2);
			//g.fillOval((int)(flower.x-petalSize/2),(int)(flower.y + flower.size/2),petalSize,petalSize*2);
			g.fill(petalShape);
			
//			g.draw(new Ellipse2D.Double(flower.x-petalSize/2,flower.y + flower.size/2,petalSize,petalSize*2));
			g.rotate(Math.toRadians(360/numPetals),flower.x,flower.y);
		}
		
		g.setTransform(oldXForm); // Restore transform
	}

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);       
        
        if(repo != null)
        doDrawing(g);
        
        drawSliderRect((Graphics2D) g);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		int xpos = e.getX(); 
		int ypos = e.getY();
		System.out.println(xpos+ "  -  " + ypos);
		//hitFlower = checkHit(xpos,ypos);
	}

	
	private Flower checkHit(int xpos, int ypos) {
		return Flower.getCollidedFlower(xpos, ypos, repo.flowers, preZoom, preZoomX, preZoomY, draggedX, draggedY);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	int pressedX;
	int pressedY;
	@Override
	public void mousePressed(MouseEvent e) {
		//if(e.getModifiers() == 14 ){
			
			pressedX = e.getX();
			pressedY = e.getY();
			//System.out.println("Pressed  "+e.getX() + "   " + e.getY());

			//repaint();
	//	}
//		if((SwingUtilities.isRightMouseButton(e) & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK ){
//			last_x = rect.x - e.getX();
//		    last_y = rect.y - e.getY();
//		 
//		    // Checks whether or not the cursor is inside of the rectangle while the
//		    // user is pressing the mouse.
//		    if (rect.contains(e.getX(), e.getY())) {
//		      pressOut = false;
//		      updateLocation(e);
//		    } else {
//		      ShapeMover.label.setText("First position the cursor on the rectangle "
//		        + "and then drag.");
//		      pressOut = true;
//		    }
//		}
			
			hitFlower = checkHit(e.getX(),e.getY());
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		if((e.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK ){
			wheelMoved  = 1;
			zoomX = e.getX();
			zoomY = e.getY();
			int notches = e.getWheelRotation();

            if (notches < 0) {
            	zoom+=0.1;
            } else if (notches> 0){
            	zoom-=0.1;
            }
    		System.out.println("zoom: " + zoom);
    		System.out.println("zoomX: " + zoomX + "zoomY: " +  zoomY);


        }else{
        	if(e.getX() < Visualization.width && e.getX() > Visualization.width - Visualization.legendWidth){
        		int notches = e.getWheelRotation();
        		
        		if (notches < 0 && legendTranslateY < 50) {
        			legendTranslateY += 10;
        		} else if (notches> 0){
        			legendTranslateY -= 10;
        		}
        		
        	}
        }
		
	}
	
	
	boolean dragged = false;
	int draggedX;
	int draggedY;
	@Override
	public void mouseDragged(MouseEvent e) {
		
		int newX = e.getX() - pressedX;
		int newY = e.getY() - pressedY;
		
		if((e.getModifiers() & KeyEvent.CTRL_MASK) == KeyEvent.CTRL_MASK ){
			// increment last offset to last processed by drag event.
			pressedX += newX;
			pressedY += newY;
			
			// update the canvas locations
			draggedY += newY;
			draggedX += newX;
			
			//repaint();
		}else{		
			//hitFlower = checkHit(e.getX(),e.getY());
			if(hitFlower != null){
				hitFlower.x = (e.getX() - draggedX - preZoomX)/preZoom;
				hitFlower.y = (e.getY() - draggedY - preZoomY)/preZoom;
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    //if (!source.getValueIsAdjusting()) {
	        currentFrame = (int)source.getValue();	        
	    //}
	}
	
	public void resetLegend() {
		legendTranslateY = 50;
	}
}
