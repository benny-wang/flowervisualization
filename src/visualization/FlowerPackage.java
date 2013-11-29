package visualization;
import java.awt.Color;

public class FlowerPackage {
	private Color color;
	private String name;
	
	public FlowerPackage(String name, Color color){
		this.name = name;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}

