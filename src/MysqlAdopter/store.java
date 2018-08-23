package MysqlAdopter;
import java.io.*; 

class store{
	
    void hash() throws Exception
    {    
    	         try{    
    	           FileWriter fw=new FileWriter("F:\\testout.txt");    
    	           fw.write("");    
    	           fw.close();    
    	          }catch(Exception e){System.out.println(e);}    
    	          System.out.println("Success...");
    	          
    	          FileReader fr=new FileReader("F:\\testout.txt");    
    	          int i;    
    	          while((i=fr.read())!=-1)    
    	          System.out.print((char)i);    
    	          fr.close();    
    	     }    
    	} 
