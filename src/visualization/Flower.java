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
	public int methods;
	public Contributor contributor;
	public Commit commit;
	public int numMethods;
	
	public Flower(Color color, int size, int x, int y, int methods){
		this.color = color;
		this.size = size;
		this.x = x;
		this.y = y;
		this.methods = methods;
	}
}
