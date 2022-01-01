package com.example.demo;


import java.lang.reflect.*;
import java.nio.file.Path;

import java.util.*;
import java.io.*;
import com.example.*;
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



public class Struct{
		String classname;
		String methodname;
		String MethodCall;
		String Scope;
		Struct(String CN,String MN,String MC){
			this.classname=CN;
			this.methodname=MN;
			this.MethodCall=MC;
			this.Scope="Global";
		}
//		Doing d=new Doing("","");
		
		public static class Doing{
			String Classname;
			String Methodname;
			Doing(String CN,String MN){
				this.Classname=CN;
				this.Methodname=MN;
			}
//			Struct s=new Struct("","","");
			public void call() {
				System.out.print("asaa");
			}
		}
		public static enum Player {
		    O, X;
		    public static Player opponentOf(Player player) {
		      return player == X ? O : X;
		    }
		  }
		
	}