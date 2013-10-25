package visualization;

/*
String - name
Int - class size (lines of code)
Array[Class node] - Dependencies
Array[Method node] - methods
Int - number of methods
String - Source code
*/

public class ClassNode {
	public String name;
	public int size;
	public ClassNode[] classes;
	public MethodNode[] methods;
	public int numMethods;
	public String source;
}
