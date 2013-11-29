package visualization;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

public class Flower {
	private Color color;
	private int size;
	private double x;
	private double y;
	private String contributor;
	private int numMethods;
	private boolean exist = false;
	private String packageName;
	private int age = 0;
	final static float sixMonthsTime = 3600*120*24;
	
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}

	public int getNumMethods() {
		return numMethods;
	}

	public void setNumMethods(int numMethods) {
		this.numMethods = numMethods;
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, Integer> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Map<String, Integer> dependencies) {
		this.dependencies = dependencies;
	}

	public String className;
	
	public Map<String, Integer> dependencies;
	
	
	public Flower(String methodName, Color color, int size, int x, int y, int numMethods, String contributor, Map<String, Integer> dependencies, int age){
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.numMethods = numMethods;
		this.contributor = contributor;
		this.className = methodName;
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
		this.className = flower.className;
		this.dependencies = flower.dependencies;
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
		
		if(flower != null && !flower.className.equals(repulFlower.className)){
			
			int mass = flower.dependencies.size() + 1;			
			int repulMass = repulFlower.dependencies.size() + 1;
			
			double distance = getDistance(repulFlower);
			
			double speed = 1 * ( flower.size / 2 * 3  * 3 - distance) * (repulMass/mass);
			
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
					
			if(attrFlower != null && !flower.className.equals(attrFlower.className)){
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
	
	public static boolean checkFlowerCollision (double x1,double y1,double radius1, Flower currentFlower, Map<String, Flower> flowers){
		
		for (Flower attrFlower : flowers.values()) {
			if(!attrFlower.className.equals(currentFlower.className)){
				
			if(checkCollision(x1,y1,radius1/2*3,attrFlower.x,attrFlower.y,attrFlower.size/2*3))
				return true;
			}
		}
		return false;
	}
	
	public static boolean inSurface (double x, double y, int radius, double width, double height){
		int wholeFlowerRadius = (int)(radius*1.5);
		
		return x+wholeFlowerRadius < width && x-wholeFlowerRadius > 0
				&& y+wholeFlowerRadius < height && y-wholeFlowerRadius > 0;
	}
}
