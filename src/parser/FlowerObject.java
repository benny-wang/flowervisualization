package parser;

import java.util.ArrayList;
import java.util.HashMap;


public class FlowerObject {

	//The number of methods in a Class
	private int methodNumber;
	//The number of lines in a Class
	private int lineNumber;
	//The name of the Class
	private String name;
	//The Package of the class
	private String packname;
	//Dependencies of the Class
	private HashMap<String,Integer> importClasses = new HashMap<String,Integer>();
	//Age of the Class since last changed
	private int	age;
	//The Contributor that changed last
	private String lastCommitter;
	

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
	public void setAge(int age) {
		this.age = age;
	}
	public int getAge() {
		return age;
	}
	public void setLastCommitter(String lastCommitter) {
		this.lastCommitter = lastCommitter;
	}
	public String getLastCommitter() {
		return lastCommitter;
	}
	
	
	
}
