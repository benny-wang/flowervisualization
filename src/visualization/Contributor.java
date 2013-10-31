package visualization;
import java.awt.Color;

/*
String - name
Object[Color] - color (array representation of RGB )
Int - first commit date (join date)

 */

public class Contributor {
	public String name;
	public Color color;
	public int firstDate;
	
	public Contributor(String name, Color color){
		this.name = name;
		this.color = color;
	}
}
