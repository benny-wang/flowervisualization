package visualization;
import java.awt.Color;
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
	public ClassNode classNode;
	public Color color;
	public int size;
	public int x;
	public int y;
	public String contributor;
	public Commit commit;
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
		
		this.changed = false;
	}
	
	public void makeDarker(){
		//Make darker
		//HSB/HSV (Hue-Saturation-Brightness/Value )
		
		Color oColor = this.color;
		
	    float hsbVals[] = Color.RGBtoHSB( oColor.getRed(),
	    		oColor.getGreen(),
	    		oColor.getBlue(), null );
		
	    Color darkerColor = Color.getHSBColor( hsbVals[0], hsbVals[1], 0.98f * hsbVals[2] );
	    this.color = darkerColor;
	}
	
	public static boolean checkFlowerCollision (int x, int y, int radius, Map<String, Flower> flowers){
		for (Flower flower : flowers.values()) {
//			Flower flower = flowers[i];
			
			
			if(flower != null && !(flower.x == 0 && flower.y == 0)){
			
			if(checkCollision(x,y,(int) Math.ceil(radius*1.5),flower.x,flower.y,(int) Math.ceil(flower.size*1.5)))
				return true;
			}
		}
		
		
		return false;
	}
	
	public static boolean checkCollision (int x1,int y1,int radius1,int x2,int y2,int radius2){
		double xDif = x1 - x2;
		double yDif = y1 - y2;
		double distanceSquared = xDif * xDif + yDif * yDif;
		boolean collision = distanceSquared < (radius1 + radius2) * (radius1 + radius2);

		return collision;
	}
	
	public static boolean inSurface (int x, int y, int radius){
//		System.out.println("inSurface: "+(x+radius*5 < Visualization.width && x-radius*5 > 0
//				&& y+radius*5 < Visualization.height && y-radius*5 > 0) + " x:" + x + " y:"+y + " radius:" + radius);
		
		int wholeRadius = (int)(radius*2);
		
		return x+wholeRadius < Visualization.width && x-wholeRadius > 0
				&& y+wholeRadius < Visualization.height && y-wholeRadius > 0;
	}
}
