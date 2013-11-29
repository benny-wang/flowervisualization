package visualization;
import java.awt.Color;

public class Contributor {
	private String name;
	private Color color;
	private int firstDate;
	
	public Contributor(String name, Color color){
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(int firstDate) {
		this.firstDate = firstDate;
	}
	
}
