package parser;




import java.lang.reflect.*;
import java.io.*;
import java.lang.String.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;







public class ParseMethod {

	public static void main(String args[]) throws Exception
	{
		int Mcount=0,MthdLen=0;
		
		String FolderPath = "";
		File folder = new File("../jitsi/src/net/java/sip/communicator/impl/protocol");
		
		
		ArrayList<FlowerObject> objs = parseFlowers(folder);
		for(FlowerObject flower : objs) {
			System.out.println(flower.getName());
		}
		XMLwritter.GenerateXML(objs);

	}
	
	

	
	

	
	

	
	public static int getLineNumber(File file)
	{
		int lineNumber = 0;
		
		 FileReader fr;
		try {
			fr = new FileReader(file);
		    LineNumberReader lnr = new LineNumberReader(fr);


	            while (lnr.readLine() != null){
	        	lineNumber++;
	            }

	            lnr.close();
		}
		catch (IOException e)
		{
			return 0;
		}
		
		return lineNumber;
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
	

	
	
	public static ArrayList<FlowerObject> parseFlowers(File Directory)
	{
		Date date = new Date();
		String sourceDirectory = "";
		String[] rootPathElements =Directory.getAbsolutePath().split("\\\\");
		
		for(String s : rootPathElements)
		{
			if(s.equals("src"))
			{
				sourceDirectory+=s;
				break;
			}
			sourceDirectory=sourceDirectory+s+"\\";
		}
		
		System.out.println(sourceDirectory);

		ArrayList<FlowerObject> listOfFlowers = new ArrayList<FlowerObject>();
		
		ArrayList<File> filePaths = new ArrayList<File>();
		
		filePaths = traverseFolder(Directory, filePaths);
		System.out.println(filePaths.size());

		final Set<String> dependencies = new HashSet<String>();	
		
		
		
		for(File file : filePaths)
		{
			dependencies.add(file.getName().split("\\.")[0]);
		}
		
		
		for(File file : filePaths)
		{
		
	
		String pack = "";
		String name = "";
		int LineNumber = 0;
		BufferedReader in=null;

		final HashMap<String, Integer> map = new HashMap<String,Integer>(25); 
		map.put("MethodNumber", 0);
		
		 
		try {
	        	ASTParser parser = ASTParser.newParser(AST.JLS3);
	        	in = new BufferedReader(new FileReader(file));
	            // parse the file
	        	String fi = "";
	        	String sCurrentLine;
	        	while ((sCurrentLine = in.readLine()) != null) {
					fi=fi+"\n"+sCurrentLine;
					LineNumber++;
				}
	            
	           parser.setSource(fi.toCharArray());
	           parser.setKind(ASTParser.K_COMPILATION_UNIT);
	        
	   			parser.setResolveBindings(true); // we need bindings later on
	   			parser.setUnitName(file.getName());
	   			String[] srcPath = {sourceDirectory};
	   			parser.setEnvironment(null, srcPath, new String[] {"UTF-8"}, true);
	      
	         final CompilationUnit cu = (CompilationUnit) parser.createAST(null); 
	        if(cu==null)
	        	continue;
	        if(cu.getPackage()!=null) {
	        String[] temp=cu.getPackage().toString().split("\\.");
	        pack = temp[temp.length-1];
	        pack = pack.substring(0,pack.length()-2);
	        }
	        
	        name=file.getName().split("\\.")[0]; 
	        cu.accept(new ASTVisitor() {
	        	
	   			public boolean visit(FieldDeclaration node){
	   				String value = node.getType().toString();
	   				if(!dependencies.contains(value))
	   					return true;
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
	   			
	   			public boolean visit(MethodDeclaration node){
	   				
	   				if(map.containsKey("MethodNumber"))
	   				{
	   					map.put("MethodNumber",map.get("MethodNumber")+1);
	   				}
	   
	   				return true;
	   			}
	   			
	   			public boolean visit(TypeLiteral node){
	   					String value = node.getType().toString();
	   					if(!dependencies.contains(value))
		   					return true;
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
	   				if(!dependencies.contains(value))
	   					return true;
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
	                        if(dependencies.contains(value))
	                        {
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
	                }
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
		

		

	       FlowerObject fObj = new FlowerObject();
	       fObj.setLineNumber(LineNumber);
	       fObj.setMethodNumber(map.get("MethodNumber"));
	       fObj.setName(name);
	       map.remove(name);
	       
	       
	       fObj.setPackname(pack);
	       fObj.setImportClasses(map);
	       if(map.get("MethodNumber")==0)
	       {
	    	   continue;
	       }
	       listOfFlowers.add(fObj);
	       
//	       System.out.println("Line Number is: "+LineNumber);
//	       System.out.println(map.get("MethodNumber"));
//	       System.out.println("Name is : "+name);
//	       System.out.println("Package is: "+pack);
//	       System.out.println("Depenedency: ");
//	       for(Map.Entry<String, Integer> entry : map.entrySet())
//	    	   System.out.println(entry.getKey()+" "+entry.getValue());
	       
		}	
//		System.out.println(filePaths.size());
//		System.out.println((new Date()).getTime()-date.getTime());
		return listOfFlowers;
	}
}
