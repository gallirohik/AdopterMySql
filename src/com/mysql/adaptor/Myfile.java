package com.mysql.adaptor;


import java.io.*;
import java.io.BufferedReader;
public class Myfile {

	void Write(String Str)
	{		  
  
		         try{    
		           FileWriter fw=new FileWriter("C:\\Users\\Ramesh\\eclipse-workspace\\MysqlAdopter\\src\\com\\delta.txt");    	           
		           fw.write(Str);
		           fw.close();    
		          }
		           catch(Exception e){System.out.println(e);}    
		          System.out.println("Success...");    
		               
	}
	String read(){
		   {
		      String thisLine = null;
		      
		      try {
		      
		         // open input stream test.txt for reading purpose.
		    	  BufferedReader br
		    	   = new BufferedReader(new FileReader("C:\\Users\\Ramesh\\eclipse-workspace\\MysqlAdopter\\src\\com\\delta.txt"));

		         
		         while ((thisLine = br.readLine()) != null) {
		            return thisLine;
		         }       
		      } catch(Exception e) {
		         e.printStackTrace();
		      }
		   }
		return null;
		}

	}

