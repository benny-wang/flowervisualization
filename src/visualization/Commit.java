package visualization;


public class Commit {
/* Object[Contributor] - contributor
Int - commit time
Int - number of methods
Int - lines of code (addition?)
Array[Method Node] - methods
Object[Class Node] - Class Imports
*/
	public int comitTime;
	public int numMethods;
	public int linesCode;
	public MethodNode[] methods;
	public ClassNode[] classes;
}
