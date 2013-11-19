package parser;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeLiteral;
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
	        
	   			parser.setResolveBindings(true); // we need bindings later on
	   			parser.setUnitName(file.getName());
	   			String[] srcPath = {"C:\\Users\\Majin\\Documents\\GitHub\\jitsi\\src"};
	   			parser.setEnvironment(null, srcPath, new String[] {"UTF-8"}, true);
	           
	           final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	           
	           
	         final  ArrayList<String> list = new ArrayList<String>();
	           

	           
	           final HashMap<String, Integer> map = new HashMap<String,Integer>(25);
	   		cu.accept(new ASTVisitor() {
	    
	   			
	   		
	   			public boolean visit(FieldDeclaration node){
	   				String value = node.getType().toString();
	   				list.add(value);
	   				if(map.containsKey(value))
	   				{
	   					map.put(value,map.get(value)+1);
	   				}
	   				else
	   				{
	   					map.put(value, 1);
	   				}
	   				return true;
	   			}
	   			
	   				public boolean visit(TypeLiteral node){
	   					String value = node.getType().toString();
	   					list.add(value);
		   				if(map.containsKey(value))
		   				{
		   					map.put(value,map.get(value)+1);
		   				}
		   				else
		   				{
		   					map.put(value, 1);
		   				}
	   				return true;
	   			}
	   			
	   				
	   			public boolean visit(VariableDeclarationStatement node){
	   				String value = node.getType().toString();
	   				list.add(value);
	   				if(map.containsKey(value))
	   				{
	   					map.put(value,map.get(value)+1);
	   				}
	   				else
	   				{
	   					map.put(value, 1);
	   				}
	   				return true;
	   			}
	   			public boolean visit(MethodInvocation node) {
					
					Expression expression = node.getExpression();
	                if (expression != null) {
	                    ITypeBinding typeBinding = expression.resolveTypeBinding();
	                    if (typeBinding != null) {
	                    	
	                        String value = typeBinding.getName();
	                        list.add(value);
	    	   				if(map.containsKey(value))
	    	   				{
	    	   					map.put(value,map.get(value)+1);
	    	   				}
	    	   				else
	    	   				{
	    	   					map.put(value, 1);
	    	   				}
	                    }
	                }
					return true;
				}
	   		});
	   		
	   		
	   		for(Map.Entry<String, Integer> entry : map.entrySet())
	   		{
	   			System.out.println(entry.getKey()+" "+entry.getValue());
	   		}
	   		
	   		
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
