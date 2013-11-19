package parser;

import java.util.ArrayList;
import java.util.HashMap;


public class FlowerObject {

	
	private int methodNumber;
	private int lineNumber;
	private String name;
	private String packname;
	private HashMap<String,Integer> importClasses = new HashMap<String,Integer>();
	private double xCoord;
	private double yCoord;
	
	
	public double getxCoord() {
		return xCoord;
	}
	public void setxCoord(double xCoord) {
		this.xCoord = xCoord;
	}
	public double getyCoord() {
		return yCoord;
	}
	public void setyCoord(double yCoord) {
		this.yCoord = yCoord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public int getMethodNumber() {
		return methodNumber;
	}
	public void setMethodNumber(int methodNumber) {
		this.methodNumber = methodNumber;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public HashMap<String,Integer> getImportClasses() {
		return importClasses;
	}
	public void setImportClasses(HashMap<String,Integer> importClasses) {
		this.importClasses = importClasses;
	}
	
	
	
}
