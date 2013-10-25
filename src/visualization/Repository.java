package visualization;

/*
String - Repository name
Array[Contributor class] - Contributors
Array[Commit Class] - Commits
Array[Class Node] - Classes
Array[Method Node] - Methods
Int - average class life?? (for flower color saturation ratio)
Int - largest lines of code of class (for flower ratio)
Int - smallest lines of code
Int - current commit number

 */

public class Repository {
	public String name;
	public Contributor[] contributors;
	public Commit[] commits;
	public ClassNode[] classes;
	public MethodNode[] methods;
	
	public Frame[] frames;
	
}
