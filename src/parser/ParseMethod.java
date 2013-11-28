package parser;





import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

	//Traverse the files in the folder including subfolders.
	//Return an ArrayList of all the files.
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
	

	
	//Parses all the java files in side the directory and returns an ArrayList of Flower Objects
	//Argument: a File Directory Pointing to the Part of Source Code Interested
	//Return: ArrayList of Flower Objects with AST information Filled in.
	public static ArrayList<FlowerObject> parseFlowers(File Directory)
	{
		String sourceDirectory = "";
		String[] rootPathElements =Directory.getAbsolutePath().split("\\\\");
		ArrayList<FlowerObject> listOfFlowers = new ArrayList<FlowerObject>();
		ArrayList<File> filePaths = new ArrayList<File>();	
		final Set<String> releventDependencies = new HashSet<String>();			
		final HashMap<String, Integer> dependencyMap = new HashMap<String,Integer>(25); 
		
		
		//Calculate the Path for the Source Directory
		sourceDirectory = calculateSource(rootPathElements);
		//Traverse and Store all the files in side the Source Directory
		filePaths = traverseFolder(Directory, filePaths);
		//Add a temporary variable to Store the Method Number. 
		dependencyMap.put("MethodNumber", 0);
		
		//Calculate the relevent dependency ie. All classes that is with in the selected top level package
		for(File file : filePaths)
		{
			releventDependencies.add(file.getName().split("\\.")[0]);
		}
		
		
		//Traverse Each File
		for(File file : filePaths)
		{
		
	
		String pack = "";
		String name = "";
		int LineNumber = 0;
		BufferedReader in=null;


		
		 
		try {
				//Build An AST Tree with AST Parser
	        	ASTParser parser = ASTParser.newParser(AST.JLS3);
	        	in = new BufferedReader(new FileReader(file));
	            // parse the file
	        	String fi = "";
	        	String sCurrentLine;
	        	
	        	//Take note how many lines are there inside each file and stores it.
	        	while ((sCurrentLine = in.readLine()) != null) {
					fi=fi+"\n"+sCurrentLine;
					LineNumber++;
				}
	            
	           parser.setSource(fi.toCharArray());
	           parser.setKind(ASTParser.K_COMPILATION_UNIT);
	        
	   			parser.setResolveBindings(true); 
	   			parser.setUnitName(file.getName());
	   			String[] srcPath = {sourceDirectory};
	   			parser.setEnvironment(null, srcPath, new String[] {"UTF-8"}, true);
	      
	   			
	   		//Build the Syntax tree	
	        final CompilationUnit cu = (CompilationUnit) parser.createAST(null); 
	        
	        //Check if the file is indeed a java source.
	        if(cu==null)
	        	continue;
	        if(cu.getPackage()!=null) {
	        pack = cu.getPackage().getName().toString();
	        }

	        
	        name=file.getName().split("\\.")[0]; 
	        
	        //Implement A visitor for the AST to retrieve all the relevent information
	        cu.accept(new ASTVisitor() {
	        	
	   			public boolean visit(FieldDeclaration node){
	   				String value = node.getType().toString();
	   				if(!releventDependencies.contains(value))
	   					return true;
	   				if(dependencyMap.containsKey(value))
	   				{
	   					dependencyMap.put(value,dependencyMap.get(value)+1);
	   				}
	   				else
	   				{
	   					dependencyMap.put(value, 1);
	   				}
	   				return true;
	   			}
	   			
	   			public boolean visit(MethodDeclaration node){
	   				
	   				if(dependencyMap.containsKey("MethodNumber"))
	   				{
	   					dependencyMap.put("MethodNumber",dependencyMap.get("MethodNumber")+1);
	   				}
	   
	   				return true;
	   			}
	   			
	   			public boolean visit(TypeLiteral node){
	   					String value = node.getType().toString();
	   					if(!releventDependencies.contains(value))
		   					return true;
		   				if(dependencyMap.containsKey(value))
		   				{
		   					dependencyMap.put(value,dependencyMap.get(value)+1);
		   				}
		   				else
		   				{
		   					dependencyMap.put(value, 1);
		   				}
	   				return true;
	   			}
	   			
	   				
	   			public boolean visit(VariableDeclarationStatement node){
	   				String value = node.getType().toString();
	   				if(!releventDependencies.contains(value))
	   					return true;
	   				if(dependencyMap.containsKey(value))
	   				{
	   					dependencyMap.put(value,dependencyMap.get(value)+1);
	   				}
	   				else
	   				{
	   					dependencyMap.put(value, 1);
	   				}
	   				return true;
	   			}
	   			public boolean visit(MethodInvocation node) {
					
					Expression expression = node.getExpression();
	                if (expression != null) {
	                    ITypeBinding typeBinding = expression.resolveTypeBinding();
	                    if (typeBinding != null) {
	                    	
	                        String value = typeBinding.getName();
	                        if(releventDependencies.contains(value))
	                        {
	    	   				if(dependencyMap.containsKey(value))
	    	   				{
	    	   					dependencyMap.put(value,dependencyMap.get(value)+1);
	    	   				}
	    	   				else
	    	   				{
	    	   					dependencyMap.put(value, 1);
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
		

		
			// From the information retrieved build an Flower Object.
	       FlowerObject fObj = new FlowerObject();
	       fObj.setLineNumber(LineNumber);
	       fObj.setMethodNumber(dependencyMap.get("MethodNumber"));
	       fObj.setName(name);
	       dependencyMap.remove(name);
	       fObj.setPackname(pack);
	       fObj.setImportClasses(dependencyMap);
	       if(dependencyMap.get("MethodNumber")==0)
	       {
	    	   continue;
	       }
	       listOfFlowers.add(fObj);

		}	

		return listOfFlowers;
	}



	//Calculate the reletive absolute path of the source folder from "src".
	//Return string inside it the absolute path.
	private static String calculateSource(
			String[] rootPathElements) {
		String sourceDirectory = "";
		
		for(String s : rootPathElements)
		{
			if(s.equals("src"))
			{
				sourceDirectory+=s;
				break;
			}
			sourceDirectory=sourceDirectory+s+"\\";
		}
		return sourceDirectory;
	}
}
