package parser;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class astTest {

	
	public static void main(String args[])
	{
		File file = new File("C:\\Users\\Majin\\Documents\\GitHub\\jitsi\\src\\net\\java\\sip\\communicator\\impl\\protocol\\jabber\\CallPeerJabberImpl.java");
		
		BufferedReader in=null;

	        try {
	        	ASTParser parser = ASTParser.newParser(AST.JLS3);
	        	in = new BufferedReader(new FileReader(file));
	            // parse the file
	        	String fi = "";
	        	String sCurrentLine;
	        	while ((sCurrentLine = in.readLine()) != null) {
					fi=fi+"\n"+sCurrentLine;
				}
	            
	           parser.setSource(fi.toCharArray());
	           parser.setKind(ASTParser.K_COMPILATION_UNIT);
	            
	           final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	           
	           
	           
	   		cu.accept(new ASTVisitor() {
	    
	   			Set names = new HashSet();
	    
	   			public boolean visit(ClassInstanceCreation   node){
	   				
	   				System.out.println("ClassInstanceCreation: "+node.toString());
	   				
	   				return true;
	   			}
	   			
	   			public boolean visit(FieldDeclaration node){
	   				
	   				System.out.println("FieldDeclaration: "+node.getType()+"' at line"+cu.getLineNumber(node.getStartPosition()));
	   				
	   				return true;
	   			}
	   			
//	   				public boolean visit(TypeLiteral node){
//	   				
//	   				System.out.println("TypeLiteral: "+node.toString()+"' at line"+cu.getLineNumber(node.getStartPosition()));
//	   				
//	   				return true;
//	   			}
	   			
	   				public boolean visit(SimpleName node){
		   				
		   				System.out.println("SimpleName: "+node.toString()+"' at line"+cu.getLineNumber(node.getStartPosition()));
		   				
		   				return true;
		   			}
	   				
	   				public boolean visit(InstanceofExpression node){
		   				
		   				System.out.println("InstanceofExpression: "+node.toString()+"' at line"+cu.getLineNumber(node.getStartPosition()));
		   				
		   				return true;
		   			}
	   				
//	   					public boolean visit(MethodInvocation node){
//		   				
//		   				System.out.println("MethodInvocation: "+"' at line"+cu.getLineNumber(node.getStartPosition()));
//		   				
//		   				return true;
//		   			}
	   				
	   			public boolean visit(VariableDeclarationStatement node){
	   				
	   				System.out.println("VariableDeclarationStatement: "+node.toString());
	   		
	   				return true;
	   			}
	   			
	    
	   		});
	   		
	           
	           
	        }  catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        finally {
	            try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	}
	
}
