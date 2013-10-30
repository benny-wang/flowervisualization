package parser;

import java.util.ArrayList;


public class FlowerObject {

	
	private int methodNumber;
	private int lineNumber;
	private String name;
	private String packname;
	private ArrayList<String> importClasses = new ArrayList<String>();
	
	
	
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
	public ArrayList<String> getImportClasses() {
		return importClasses;
	}
	public void setImportClasses(ArrayList<String> importClasses) {
		this.importClasses = importClasses;
	}
	
	
	
}
