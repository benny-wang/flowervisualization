package visualization;
import java.awt.Color;

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
	
	
	public Flower(String methodName, Color color, int size, int x, int y, int numMethods, String contributor){
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.numMethods = numMethods;
		this.contributor = contributor;
		this.methodName = methodName;
		this.changed = false;
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
}
