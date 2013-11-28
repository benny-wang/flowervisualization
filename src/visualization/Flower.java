package visualization;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Flower {
	public Color color;
	public int size;
	public double x;
	public double y;
	public String contributor;
	public int numMethods;
	public boolean exist = false;
	public String packageName;
	public int age = 0;
	final static float sixMonthsTime = 3600*120*24;
	
	
	public boolean changed;
	
	public String methodName;
	
	public Map<String, Integer> dependencies;
	
	
	public Flower(String methodName, Color color, int size, int x, int y, int numMethods, String contributor, Map<String, Integer> dependencies, int age){
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.numMethods = numMethods;
		this.contributor = contributor;
		this.methodName = methodName;
		this.changed = false;
		this.dependencies = dependencies;
		this.age = age;
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
		this.packageName = flower.packageName;
	}
	
	public void desaturateColor(){
		//HSB/HSV (Hue-Saturation-Brightness/Value )		
		
	    float hsbVals[] = Color.RGBtoHSB( this.color.getRed(),
	    		this.color.getGreen(),
	    		this.color.getBlue(), null );
    
	    Color darkerColor = Color.getHSBColor( hsbVals[0], 0.985f * hsbVals[1], hsbVals[2] );	    	    
	    this.color = darkerColor;
	}
	
	public void desaturateColorByAge(){

	    float hsbVals[] = Color.RGBtoHSB( this.color.getRed(),
	    		this.color.getGreen(),
	    		this.color.getBlue(), null );
	    
	    float tempage = (float) 0.1;

		if(this.age< sixMonthsTime)
			tempage = (float)( Math.pow(sixMonthsTime-this.age,2)/Math.pow(sixMonthsTime,2));

	    Color ageColor = Color.getHSBColor( hsbVals[0], tempage, hsbVals[2] );	    	    
	    this.color = ageColor;
	}

	public static Flower getCollidedFlower(double x, double y, Map<String, Flower> flowers, double zoom, double zoomX, double zoomY, double draggedX, double draggedY) {
		
		Iterator it = flowers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			Flower flower = (Flower) pairs.getValue();
			if (flower != null && !(flower.x == 0 && flower.y == 0)) {
				if (checkCollision((x - draggedX - zoomX)/zoom, (y - draggedY - zoomY)/zoom, 1, flower.x, flower.y, ((double) flower.size) / 2 * 3)) {
					return flower;
				}
			}
		}

		return null;
	}
	public static boolean checkFlowerCollision (int x, int y, int diameter, Map<String, Flower> flowers){
		
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
	
	public double getAngle(Flower target) {
	    double theta = Math.atan2(target.y - y, target.x - x);
	    double angle = Math.toDegrees(theta);
	    
	    if (angle < 0) {
	        angle += 360;
	    }	    
	    return theta;
	}
	
	public double getDistance(Flower target) {
	    return Math.sqrt((target.y - y) * (target.y - y) + (target.x - x) * (target.x - x));
	}
	
	public void repulsion(Map<String, Flower> flowers){
		
		Flower flower = this;
		
		for (Flower repulFlower : flowers.values()) {
		
		if(flower != null && !flower.methodName.equals(repulFlower.methodName)){
			
			int mass = flower.dependencies.size() + 1;			
			int repulMass = repulFlower.dependencies.size() + 1;

			double xDiff = flower.x - repulFlower.x;
			double yDiff = flower.y - repulFlower.y;	
			
			double bflowerRadius = flower.size / 2 * 3 + repulFlower.size / 2 * 3;
			double distance = getDistance(repulFlower);
			
			double speed = 1 * ( flower.size / 2 * 3  * 3 - distance) * (repulMass/mass);
			
			double xRepul = flower.size/2 * 3 - xDiff;
			double yRepul = flower.size/2 * 3 - yDiff;
			
			if(xRepul < 0){
				xRepul = 0;
			}
			
			if(yRepul < 0){
				xRepul = 0;
			}
			
			if(speed < 0){
				speed = 0;
			}
	
			flower.x = flower.x - speed * Math.cos(getAngle(repulFlower)) / 100;
			flower.y = flower.y - speed * Math.sin(getAngle(repulFlower)) / 100;
		}
		
		}
	
		
	}
	
	public void biDirectionAttraction (Flower attrFlower, int callCount){
		Flower flower = this;
		
		double bflowerRadius = flower.size / 2 * 3 + attrFlower.size / 2 * 3;
		double distance = getDistance(attrFlower) - bflowerRadius;
		
		int mass = flower.dependencies.size() + 1;			
		int attrMass = attrFlower.dependencies.size() + 1;
		
		double speed = distance * (attrMass/mass) * Math.sqrt(callCount);
		
		double x = flower.x + speed * Math.cos(getAngle(attrFlower)) / 100;
		double y = flower.y + speed * Math.sin(getAngle(attrFlower)) / 100;
		
		flower.x = x;
		flower.y = y;
		
	}
	
	public void attraction(Map<String, Flower> flowers){
		
		Flower flower = this;
		
		Map<String, Integer> dependencies = flower.dependencies;
		
		Flower attrFlower;
		
		for (Map.Entry<String, Integer> entry : dependencies.entrySet()) {
		    String methodName = entry.getKey();
		    int callCount = entry.getValue();

			attrFlower = flowers.get(methodName);
					
			if(attrFlower != null && !flower.methodName.equals(attrFlower.methodName)){
				double bflowerRadius = flower.size / 2 * 3 + attrFlower.size / 2 * 3;
				double distance = getDistance(attrFlower) - bflowerRadius;
				
				int mass = flower.dependencies.size() + 1;			
				int attrMass = attrFlower.dependencies.size() + 1;
				
				double speed = distance * (mass/attrMass) * Math.sqrt(callCount);
				
					attrFlower.x = attrFlower.x - speed * Math.cos(getAngle(attrFlower)) / 100;
					attrFlower.y = attrFlower.y - speed * Math.sin(getAngle(attrFlower)) / 100;
					
					biDirectionAttraction(attrFlower,callCount);
			}			
		}
	}
	
	public static boolean checkCollision (double x1,double y1,double radius1,double x2,double y2,double radius2){
		double xDif = x2 - x1;
		double yDif = y2 - y1;
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean collision = distanceSquared < (radius1 + radius2) * (radius1 + radius2);

		return collision;
	}
	
	public static boolean checkEveryCollision (double x1,double y1,double radius1, Flower currentFlower, Map<String, Flower> flowers){
		
		for (Flower attrFlower : flowers.values()) {
			if(!attrFlower.methodName.equals(currentFlower.methodName)){
				
			if(checkCollision(x1,y1,radius1/2*3,attrFlower.x,attrFlower.y,attrFlower.size/2*3))
				return true;
			}
		}
		return false;
	}
	
	public static boolean inSurface (double x, double y, int radius){
		int wholeFlowerRadius = (int)(radius*1.5);
		
		return x+wholeFlowerRadius < Visualization.width && x-wholeFlowerRadius > 0
				&& y+wholeFlowerRadius < Visualization.height && y-wholeFlowerRadius > 0;
	}
}
