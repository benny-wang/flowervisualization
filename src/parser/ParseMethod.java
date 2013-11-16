package parser;


import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.NameExpr;

import java.lang.reflect.*;
import java.io.*;
import java.lang.String.*;
import java.util.ArrayList;
import java.util.List;







public class ParseMethod {

	public static void main(String args[]) throws Exception
	{
		int Mcount=0,MthdLen=0;
		
		String FolderPath = "";
		File folder = new File("C:\\Users\\Majin\\Documents\\GitHub\\jitsi\\src\\net\\java\\sip\\communicator\\impl\\protocol");
		
		
		ArrayList<FlowerObject> objs = parseFlowers(folder);
		
		XMLwritter.GenerateXML(objs);

	}
	
	
	public static ArrayList<String> getDependencies(CompilationUnit cu)
	{
		ArrayList<String> dependencies = new ArrayList<String>();
		
		//TODO
		
		return dependencies;
		
		
	}
	
	
	public static ArrayList<String> getImports(CompilationUnit cu)
	{
		ArrayList<String> iypes = new ArrayList<String>();
		List<ImportDeclaration> dypes = cu.getImports();
		if(dypes!=null){
		for(ImportDeclaration im : dypes)
		{
			String name = im.toString();
			iypes.add(name);
		}
		}
		
		return iypes;
	}
	
	
	public static int getMethodNumber(CompilationUnit cu)
	{
		int methodNumber =0;
		
		
		 List<TypeDeclaration> types = cu.getTypes();
		 
		 //System.out.println("Import Name is: " + cu.getImports());
	        for (TypeDeclaration type : types) {
	        
	        	
	            List<BodyDeclaration> members = type.getMembers();
	            
	
	            if(members == null)
	            	continue;
	            for (BodyDeclaration member : members) {
	            	
	            	
	            	if (member instanceof FieldDeclaration)
                    {
                        FieldDeclaration myType = (FieldDeclaration) member;
                        List <VariableDeclarator> myFields = myType.getVariables();
                        System.out.println("Fields: " + myType.getType() + ":" + myFields.toString());
                    }
	            	
	                if (member instanceof MethodDeclaration) {
	                    MethodDeclaration method = (MethodDeclaration) member;
	                    methodNumber++;
	                    
	                }
	            }
	        }
	      return methodNumber;
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
	
	
	private static ArrayList<FlowerObject> parseFlowers(File Directory)
	{
		
		String parentFolderName = Directory.getName();
		
		ArrayList<FlowerObject> listOfFlowers = new ArrayList<FlowerObject>();
		ArrayList<File> filelist = new ArrayList<File>();
		filelist = traverseFolder(Directory, filelist);
		System.out.println(filelist.size());

		for(File file : filelist)
		{
		int methodNumber = 0;
		int lineNumber = 0;
		String pack = "";
		String name = "";
		ArrayList<String> imports;

		
		FileInputStream in=null;
		CompilationUnit cu=null;
	        try {
	        	
	        	in = new FileInputStream(file);
	            // parse the file
	            cu = JavaParser.parse(in);
	        } catch (ParseException e) {
	        	continue;
			} catch (FileNotFoundException e) {
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
	        
	        if(cu==null)
	        	continue;
	        
	        
	        
	       methodNumber = getMethodNumber(cu);
	       
	       if(methodNumber==0)
	    	   continue;
	       
	       lineNumber = getLineNumber(file);
	       

	       String[] names = file.getPath().split("\\\\");
	       int i = 0;
	       for(; i<names.length;i++)
	       {
	    	   
	    	   if(names[i].equals("src"))
	    	   {
	    		   //System.out.println(names[i]);
	    		   break;
	    	   }
	       }
	       while(i<names.length)
	       {
	    	   name+=("\\"+names[i]);
	    	   i++;
	       }
	    	 System.out.println(name+"============");
	       pack = cu.getPackage().getName().toString();
	   
	       imports = getImports(cu);
	       

	       FlowerObject fObj = new FlowerObject();
	       fObj.setLineNumber(lineNumber);
	       fObj.setMethodNumber(methodNumber);
	       fObj.setName(name);
	       fObj.setPackname(pack);
	       fObj.setImportClasses(imports);
	       listOfFlowers.add(fObj);
  
		}	
		return listOfFlowers;
	}
}
