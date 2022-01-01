package com.example.demo;


import java.lang.reflect.*;
import java.nio.file.Path;

import java.util.*;
import java.io.*;
import com.example.*;
import com.example.demo.Struct.Doing;
//import javafx.util.Pair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.body.*;
//com.github.javaparser.ast.body.VariableDeclarator

import java.nio.file.Paths;
import java.nio.file.Files;
import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class DemoApplication {
//	File[] javaFiles = new File[0];
     public static List<File> Files = new ArrayList<File>();
     public static List<File> results = new ArrayList<File>();
     public static Map<Vector<String>, Vector<String>> hashmap
     = new HashMap<Vector<String>, Vector<String>>();
     public static Map<String, Vector<String>> CallsInMethod
     = new HashMap<String, Vector<String>>();
     public static Map<String, Vector<Object>> AllArguments
     = new HashMap<String, Vector<Object>>();
     public static Map<Vector<String>, Integer> LineNumbers
     = new HashMap<Vector<String>, Integer>();
     public static Map<String, Object> ReturnTypes
     = new HashMap<String, Object>();
     public static Map<String, Integer> ArgumentCount
     = new HashMap<String, Integer>();
	 public static StringBuilder finalUML = new StringBuilder();
	 public static Map<Vector<String>, String> ObjectofClasses
     = new HashMap<Vector<String>, String>();
	 public static Map<Vector<String>, String> Scopes
     = new HashMap<Vector<String>, String>();
	 public static Map<String, Vector<String>> AllMethodsInClass
     = new HashMap<String, Vector<String>>();
	 public static Map<String,String> AllClassNames
     = new HashMap<String, String>();
	 public static Map<Vector<String>, String> FileNamesOfClassOrEnumOrInterface
     = new HashMap<Vector<String>, String>();
	 public static Map<String, String> ObjectsinMethod= new HashMap<String, String>();
	public static Map<String, String> Objects= new HashMap<String, String>();
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
//        System.out.println(file);
//        private static final Logger lOGGER = LoggerFactory.getLogger(DemoApplication.class);
 Log.setAdapter(new Log.StandardOutStandardErrorAdapter());
 List<ClassOrInterfaceDeclaration> classOrInterfaces = new ArrayList<ClassOrInterfaceDeclaration>();
 List<EnumDeclaration> enums = new ArrayList<EnumDeclaration>();
 List<UMLClass> umlClasses = new ArrayList<UMLClass>();
 List<String> className = new ArrayList<>();

 String path="/Users/saketh123/Downloads/tictactoe-java-master/src/main/java/ttsu/game";
 Path pathToSource = Paths.get(path);
 
 readFileFolder(path);
 
 
 try {
	 
		for(File file : Files) {
			SourceRoot sourceRoot = new SourceRoot(Paths.get(file.getParentFile().toString()));
			CompilationUnit compilationUnit = sourceRoot.parse("",file.getName());
			
//			if(compilationUnit.getPackageDeclaration() != null && 
//					compilationUnit.getPackageDeclaration().isPresent())
//				throw new Exception("All Java Files should be in Default folder");
		 List<EnumDeclaration> nodes=compilationUnit.findAll(EnumDeclaration.class);
		 List<ClassOrInterfaceDeclaration> classes=compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
		 
		 if(classes.size()>0) {
			    
				List<ClassOrInterfaceDeclaration> classOrInterfaceDeclaration = compilationUnit.getNodesByType(ClassOrInterfaceDeclaration.class);

				 
				for(ClassOrInterfaceDeclaration CorIDec:classOrInterfaceDeclaration)
				{
					String ClassName=CorIDec.getNameAsString();
					AllClassNames.put(ClassName,"present");
					Vector<String>names=new Vector<String>(0);
					names.add(ClassName);
					names.add(file.getName());
					FileNamesOfClassOrEnumOrInterface.put(names, "present");
				classOrInterfaces.add(CorIDec);}
		 }
		 if(nodes.size()>0) {
			 
			 List<EnumDeclaration> enumDeclaration = compilationUnit.getNodesByType(EnumDeclaration.class);
			for(EnumDeclaration Enum:enumDeclaration) {
				String EnumName=Enum.getNameAsString();
				AllClassNames.put(EnumName,"present");
				Vector<String>names=new Vector<String>(0);
				names.add(EnumName);
				names.add(file.getName());
				FileNamesOfClassOrEnumOrInterface.put(names, "present");
				enums.add(Enum);
			}
		 
		 }
		 
		}
		
		
	} catch (Exception e) {
		System.out.println("error");
		e.printStackTrace();
	}
 
 try{
		UMLGenerator generator = new UMLGenerator();
		//finalUML.append(getStaticUML());
		
//		First loop
		classOrInterfaces.stream().forEach(classOrInterface -> {
		
			String ClassName=classOrInterface.getNameAsString();
			
			List<MethodDeclaration> methods=classOrInterface.getMethods();
			AllMethodsInClass.put(ClassName, new Vector<String>(0));
			
			for(MethodDeclaration method:methods) {
				String MethodName=method.getNameAsString();
				Vector<String>AllMethods=AllMethodsInClass.get(ClassName);
				AllMethods.add(method.getNameAsString());
				AllMethodsInClass.put(ClassName, AllMethods);
				List<Parameter> parameters = method.getParameters();

				 ReturnTypes.put(MethodName,method.getType());
				 AllArguments.put(MethodName,new Vector<Object>());
				 
				 for (final Parameter parameter : parameters) {
					 
					 Vector<Object> types=AllArguments.get(MethodName);
				      String type = parameter.getType().toString();
				      String name = parameter.getName().getIdentifier();
//				      Struct Key=new Struct(ClassName,MethodName,name);
//				      Key.Scope="Local";
				      Vector<String> Key=new Vector<String>(0);
						Key.add(ClassName);
						Key.add(MethodName);
						Key.add(name);
						Key.add("Local");
						
//			        	Key.add(ClassName);
//			        	Key.add(name);
			        	ObjectofClasses.put(Key, type);
//				      System.out.println(type+" "+name+"\n");
				      
				     types.add(type);
				     AllArguments.put(MethodName,types);
				     

				    }
				 Vector<String> line=new Vector<String>(0);
                 line.add(ClassName);
                 line.add(method.getNameAsString());
				LineNumbers.put(line, method.getBegin().get().line);
			}
			
		
			
		});
		
//		Second loop for classes
		classOrInterfaces.stream().forEach(classOrInterface -> {
			UMLClassBuilder umlClassBuilder = new UMLClassBuilder(classOrInterface, classOrInterfaces);
			UMLClass umlclass=umlClassBuilder.buildUMLClass();
			String ClassName=classOrInterface.getNameAsString();
			System.out.println("             -----------------------------------------       \n");
			System.out.println("ClassName "+ClassName+"\n");
//			AllMethodsInclass.put(ClassName, null);
			 Objects= new HashMap<String, String>();
			List<FieldDeclaration>fields=umlclass.getFields();
			for(FieldDeclaration field:fields) {
//				System.out.println("this is fieild  "+field.getElementType());
				
				List<VariableDeclarator>vr=field.getVariables();
				
				for(VariableDeclarator v:vr) {
//					System.out.println("this is fieild  "+v.getNameAsString());
//					Struct Key=new Struct(ClassName,"",v.getNameAsString());
					Vector<String> Key=new Vector<String>(0);
					Key.add(ClassName);
					Key.add("");
					Key.add(v.getNameAsString());
					Key.add("Global");
					
					
					Objects.put(v.getNameAsString(), field.getElementType().toString());
					ObjectofClasses.put(Key,field.getElementType().toString());
				}
				
			}
			
//			 for(Map.Entry<String, String> type : ObjectofClasses.entrySet()) {
//				 System.out.print("this is map "+type.getKey()+" -- "+type.getValue()+"\n");
//			 }
			
			
			
			List<MethodDeclaration> methods=umlclass.getMethods();

			
			for(MethodDeclaration method:methods) {
				String MethodName=method.getNameAsString();
				 List<Node> ns=method.getChildNodes();
//				 extract(ns,ClassName,MethodName);
				 
				  ObjectsinMethod= new HashMap<String, String>();
//			
				 Optional<BlockStmt> blockstmt=method.getBody();

				System.out.println("MethodName "+MethodName+"\n");
				ObjectsinMethod=MethodCheck(method.getBody().toString());
				List<Parameter> parameters = method.getParameters();

			
				 
				 for (final Parameter parameter : parameters) {
					 
				      String type = parameter.getType().toString();
				      String name = parameter.getName().getIdentifier();
			        	ObjectsinMethod.put(name, type);
				    }
          
//				hashmap.put(new MethodCalls(ClassName,MethodName), new Vector(0));
				method.accept(new MethodVisitor(ClassName,MethodName), null);
				
			}
			umlClasses.add(umlclass);
			
		});
	

		finalUML.append(generator.getClassOrInterfaceUML(umlClasses));

		 
	}
 finally{

 }
 



    }
    
//    this will extract the object varaibles inside the method mostly reffred as local objects.
    
    static Map<String, String> MethodCheck(String body) {
    	Map<String, String> ObjectsinMethod= new HashMap<String, String>();
    	for(Map.Entry<String, String> Name : AllClassNames.entrySet()) {
//			 System.out.print(Name.getKey()+" -- "+Name.getValue()+"\n");
			 if(body.contains(Name.getKey())){
				 int pos=body.indexOf(Name.getKey());
				 
				 pos+=Name.getKey().length()+1;
				 String s="";
				 for(int i=pos;i<body.length();i++) {

					 if(Character.compare(' ',body.charAt(i))==0||Character.compare(';',body.charAt(i))==0||Character.compare(',',body.charAt(i))==0||Character.compare(')',body.charAt(i))==0) break;
					 
					 s+=body.charAt(i);
					
				 }
				 ObjectsinMethod.put(s,Name.getKey() );
//				 System.out.println("object variable "+s+"\n");
			 }
			 
		 }
    	return ObjectsinMethod;
    }
    
//   static public void extract(List<Node> nodes,String ClassName,String MethodName) {
//	   for(Node node:nodes) {
//		   if(node.getChildNodes().size()>0) {
//			   extract(node.getChildNodes(),ClassName,MethodName);
//			   
//		   }
//		   else {
//			   
//			   check(node.getParentNode().get().toString(),node.toString(),ClassName,MethodName);
////			   System.out.println("childnode"+node.toString()+"\n");
//		   }
//	   }
//   }
//   
//   static public void check(String s,String Objectname,String ClassName,String MethodName) {
////	   System.out.println("parentNode"+s+"\n");
//		   if(s.contains("new")) {
//			   int p=s.indexOf("new");
////			   System.out.println("new position ");
//			   String ans="";
//			   for(int i=p+4;i<s.length();i++) {
//				   if(s.charAt(i)=='(') {
//					   break;
//				   }
//				   ans+=s.charAt(i);
//				  
//			   }
////			   Struct Key=new Struct(ClassName,MethodName,Objectname);
////			   Key.Scope="Local";
//			   
////	        	Key.add(ClassName);
////	        	Key.add(Objectname);
//			   
//			   Vector<String> Key=new Vector<String>(0);
//				Key.add(ClassName);
//				Key.add(MethodName);
//				Key.add(Objectname);
//				Key.add("Local");
//			   ObjectofClasses.put(Key, ans);
////			   System.out.println(ans+"\n");
//		   }
//	   
//   }
   public static class MethodCallVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(MethodCallExpr n, Void arg) {
            // Found a method call
            System.out.println(n.getScope() + " - " + n.getName());
            // Don't forget to call super, it may find more method calls inside the arguments of this method call, for example.
            super.visit(n, arg);
        }
    }
   

   
//   This extract the methoda calls inside medthod and print from which classs
    
    private static class MethodVisitor extends VoidVisitorAdapter
    {
    	String ClassName;
    	String MethodName;
        @Override
        public void visit(MethodCallExpr methodCall, Object arg)
        {

        	String scope=methodCall.getScope().isEmpty()==true?"":methodCall.getScope().get().toString();
        	String Name=methodCall.getName().toString();
        	 Vector<String> Key1=new Vector<String>(0);
				
        	if(scope.equals("")) {
        		if(AllMethodsInClass.get(ClassName).contains(Name)) {
        			 String Alltypes=getargumentTypesOfMethod(Name);
        			 Key1.add(ClassName);
     				Key1.add(Name);
        			 System.out.println(Name+" Method present in same class "+ClassName+ " and in line no " + LineNumbers.get(Key1)+" and the retturn type is "+ReturnTypes.get(Name)+ "and arguments " +Alltypes + " \n");
        		}
        	}
        	else {
        		if(ObjectsinMethod.containsKey(scope)) {
        			
        			 String Alltypes=getargumentTypesOfMethod(Name);
        			 Key1.add(ObjectsinMethod.get(scope));
     				Key1.add(Name);
        			System.out.println(Name+" Method present in class "+ObjectsinMethod.get(scope)+ " and in line no " + LineNumbers.get(Key1)+" and the retturn type is "+ReturnTypes.get(Name)+ "and arguments " +Alltypes + " \n");
        		}
        		else if( Objects.containsKey(scope)) {
        			String Alltypes=getargumentTypesOfMethod(Name);
       			 Key1.add(Objects.get(scope));
       			Key1.add(Name);
        			System.out.println(Name+" Method present in class "+Objects.get(scope)+ " and in line no " + LineNumbers.get(Key1)+" and the retturn type is "+ReturnTypes.get(Name)+ "and arguments " +Alltypes + " \n");
        		}
        	}
            
        	

            List<Expression> args = methodCall.getArguments();

            for(Expression ar:args) {
//            	System.out.println(ar);
            }
            if (args != null)
                handleExpressions(args);
        }

        private void handleExpressions(List<Expression> expressions)
        {
            for (Expression expr : expressions)
            {
                if (expr instanceof MethodCallExpr)
                    {visit((MethodCallExpr) expr, null);
//                    System.out.println(expr.getParentNode().get()+"\n");
                    }
                else if (expr instanceof BinaryExpr)
                {
                    BinaryExpr binExpr = (BinaryExpr)expr;
                    System.out.println("expr "+binExpr+"\n");
                    handleExpressions(Arrays.asList(binExpr.getLeft(), binExpr.getRight()));
                }
            }
        }
        MethodVisitor(String cname,String mname){
        	this.ClassName=cname;
        	this.MethodName=mname;
        }
        
    }
    
    public static String getargumentTypesOfMethod(String Name) {
   	 String Alltypes="(";
		  Vector<Object>types=AllArguments.get(Name);
		  if(types!=null) {
		  for(int k=0;k<types.size();k++) {
			  Alltypes+=types.get(k).toString()+", ";
		  }}
		  Alltypes+=")";
    	return Alltypes;
    }
    
    public static void getFiles(String folderPath) {
    	File folder = new File(folderPath);
    	System.out.println(folder);
    	File[] javaFiles = new File[0];
    	javaFiles = folder.listFiles();
    	for(File file:javaFiles) {
			if(file.isFile()&&(file.getName().toLowerCase().endsWith(".java"))) {
				System.out.println(file);
				Files.add(file);
			}
			else {
				getFiles(file.getPath());
			}
		}
    }
    private static void readFileFolder(String folderPath) {
		System.out.println(folderPath);
		File folder = new File(folderPath);
		System.out.println(folder);
		File[] javaFiles = new File[0];
		try {
			FileFilter fileFilter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(isJavaFile(pathname)||pathname.isDirectory())
						return true;
					return false;
				}
			};
			javaFiles = folder.listFiles(fileFilter);
			for(File file:javaFiles) {
//				System.out.print(file.getParentFile()+"- "+file.getName()+"\n");
				if(file.isDirectory()) {
					readFileFolder(file.getPath());
				}
				else {
					Files.add(file);
				}
			}
//			System.out.println(javaFiles);
			if(javaFiles.length == 0)
				throw new Exception("No Java Files Found in specified folder");
		} catch (FileNotFoundException e) {
//			LOGGER.error("Please enter valid source folder location");
			e.printStackTrace();
		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
//		return javaFiles;
	}

    
    private static boolean isJavaFile(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".java");
	}
    
   

}
