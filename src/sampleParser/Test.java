package sampleParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import parser.FlowerObject;
 
public class Test {
	
	private static String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
	private static ArrayList<File> traverseFolder(File targetFolder, ArrayList<File> arr)
	{
		for (File file : targetFolder.listFiles())
		{
			if(file.isDirectory()){
				arr = traverseFolder(file,arr);
			}
			else
			{
				arr.add(file);
			}
		}
		return arr;
		
	}
	
	public static void main(String args[]){
		
		
		File folder = new File("C:\\Users\\Hsiao\\Documents\\GitHub\\jitsi\\src\\net\\java\\sip\\communicator\\impl\\protocol");
		//"C:\\Users\\Majin\\Documents\\GitHub\\jitsi\\src\\net\\java\\sip\\communicator\\impl\\protocol\\icq\\IcqAccountID.java"
		
		
		
//		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//		
//		IProject project = root.getProject("jitsi");
//		try {
//			project.open(null /* IProgressMonitor */);
//		} catch (CoreException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		IJavaProject javaProject = JavaCore.create(project);
//		
		
		String parentFolderName = folder.getName();
		
		ArrayList<FlowerObject> listOfFlowers = new ArrayList<FlowerObject>();
		
		ArrayList<File> filelist = new ArrayList<File>();
		filelist = traverseFolder(folder, filelist);
		System.out.println(filelist.size());

		for(File file : filelist)
		{
		
				
				System.out.println("Parsing file: " + file.getPath());
				
				parse(file.getPath(), null);
				
				break;
		}
		

	}
	
	public static void parse (String path, IJavaProject javaProject){
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		
		String source = "";
		try {
			source = readFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		parser.setSource(source.toCharArray());
		//parser.setSource("/*abc*/".toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		//ASTNode node = parser.createAST(null);
		//parser.setProject(javaProject);

		parser.setResolveBindings(true); // we need bindings later on

		
	   
		parser.setUnitName("jitsi/src/net/java/sip/communicator/impl/protocol/dict/ContactDictImpl.java");
		String[] classPath = {"C:\\Users\\Hsiao\\Documents\\GitHub\\jitsi\\lib"};
		String[] srcPath = {"C:\\Users\\Hsiao\\Documents\\GitHub\\jitsi\\src"};
		parser.setEnvironment(null, srcPath, new String[] {"UTF-8"}, true);
	
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
 
		cu.accept(new ASTVisitor() {
 
//			Set names = new HashSet();
// 
//			public boolean visit(VariableDeclarationFragment node) {
//				SimpleName name = node.getName();
//				this.names.add(name.getIdentifier());
//				System.out.println("Declaration of '"+name+"' at line"+cu.getLineNumber(name.getStartPosition()));
//				return false; // do not continue to avoid usage info
//			}
// 
//			public boolean visit(SimpleName node) {
//				if (this.names.contains(node.getIdentifier())) {
//				System.out.println("Usage of '" + node + "' at line " +	cu.getLineNumber(node.getStartPosition()));
//				}
//				return true;
//			}
			
			public boolean visit(MethodInvocation node) {
				
				Expression expression = node.getExpression();
                if (expression != null) {
//                    System.out.print("Expr: " + expression.toString());
                    ITypeBinding typeBinding = expression.resolveTypeBinding();
                    if (typeBinding != null) {
                        System.out.println("Type: " + typeBinding.getName() + " Var: " + expression.toString() + " Method: " + node.getName().getIdentifier());
                    }else{
                    	System.out.println("Var: " + expression.toString() + " Method: " + node.getName().getIdentifier());
                    }
                }else{
                	System.out.println("Method: " + node.getName().getIdentifier());
                }
                
//                IMethodBinding binding = node.resolveMethodBinding();
//                if (binding != null) {
//                    ITypeBinding type = binding.getDeclaringClass();
//                    if (type != null) {
//                        System.out.print(" Decl: " + type.getName());
//                    }
//                }
				
				
					//System.out.println("Method name: "+ node.getName().getIdentifier() + " Type: " + node.resolveTypeBinding());
				return true;
			}
 
		});
	}
}