package visualization;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

/*
 * method - updateColor (updates color based on time elapsed since last commit)
Object[Color] - RBG
Object[Class node] - Class
Int - size of the flower
Int - x on the plane
Int - y on the plane
Int - last commit time
Int - average code age?
Object[Contributor] - last contributor
Object[Commit] - last commit
Int - number of methods
*/

public class Flower {
	public Color color;
	public int size;
	public int x;
	public int y;
	public String contributor;
	public int numMethods;
	
	public boolean changed;
	
	public String methodName;
	
	public String[] dependencies;
	
	
	public Flower(String methodName, Color color, int size, int x, int y, int numMethods, String contributor, String[] dependencies){
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.numMethods = numMethods;
		this.contributor = contributor;
		this.methodName = methodName;
		this.changed = false;
		this.dependencies = dependencies;
	}
	
	public Flower(Flower flower){
		this.color = flower.color;
		this.size = flower.size;
		this.x = flower.x;
		this.y = flower.y;
		this.numMethods = flower.numMethods;
		this.contributor = flower.contributor;
		this.methodName = flower.methodName;
		this.dependencies = flower.dependencies;
		this.changed = false;
	}
	
	public void makeDarker(){
		//Make darker
		//HSB/HSV (Hue-Saturation-Brightness/Value )
		
		Color oColor = this.color;
		
	    float hsbVals[] = Color.RGBtoHSB( oColor.getRed(),
	    		oColor.getGreen(),
	    		oColor.getBlue(), null );
		
	    Color darkerColor = Color.getHSBColor( hsbVals[0], hsbVals[1], 0.99f * hsbVals[2] );
	    this.color = darkerColor;
	}

	public static Flower getCollidedFlower(int x, int y, int diameter,
			Map<String, Flower> flowers) {
		Iterator it = flowers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Flower flower = (Flower) pairs.getValue();
			if (flower != null && !(flower.x == 0 && flower.y == 0)) {
				if (checkCollision(x, y, ((double) diameter) / 2 * 3, flower.x,
						flower.y, ((double) flower.size) / 2 * 3)) {
					return flower;
				}
			}
		}

		return null;
	}
	public static boolean checkFlowerCollision (int x, int y, int diameter, Map<String, Flower> flowers){
		
//		for (Flower flower : flowers.values()) {
////			Flower flower = flowers[i];
//			
//			System.out.println("Checking radius: " + radius + " ---Size:" + flower.size/2 * 3 + "--x: " + flower.x + " y: " + flower.y);
//			
//			if(flower != null && !(flower.x == 0 && flower.y == 0)){
//			
//				double flower2Radius = flower.size/2 * 3;
//				
//				
//			if(checkCollision(x,y, radius,flower.x,flower.y, flower2Radius)){				
//				return true;
//			}
//			
//			}
//		}
		
	    Iterator it = flowers.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Flower flower = (Flower) pairs.getValue();
	        
			if(flower != null && !(flower.x == 0 && flower.y == 0)){
				
				
				
			if(checkCollision(x,y, ((double)diameter)/2*3,flower.x,flower.y, ((double)flower.size)/2*3)){				
				return true;
			}
			
			}
	        
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
		
		
		return false;
	}
	
	public static boolean checkCollision (double x1,double y1,double radius1,double x2,double y2,double radius2){
		double xDif = x2 - x1;
		double yDif = y2 - y1;
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean collision = distanceSquared < (radius1 + radius2) * (radius1 + radius2);

		return collision;
	}
	
	public static boolean inSurface (int x, int y, int radius){
//		System.out.println("inSurface: "+(x+radius*5 < Visualization.width && x-radius*5 > 0
//				&& y+radius*5 < Visualization.height && y-radius*5 > 0) + " x:" + x + " y:"+y + " radius:" + radius);
		
		int wholeRadius = (int)(radius*1.5);
		
		return x+wholeRadius < Visualization.width && x-wholeRadius > 0
				&& y+wholeRadius < Visualization.height && y-wholeRadius > 0;
	}
}
