import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Principal {

	private static ArrayList<Token> lista;
	private static AnalizadorLexico alex;
	
    public static void main (String [] args) {

    	//verificar si tengo los parametros necesarios
    	if(args.length>0){
    		//verificar si pidio ayuda
    		if(args[0].equals("-h")){
        		System.out.println("Estructura general: <PROGRAM_NAME> <IN_FILE> [<OUT_FILE>] \n\n\t#<PROGRAM_NAME> es el nombre del ejecutable. \n\t#<IN_FILE> es el archivo a analizar leéxicamente. \n\t# <OUT_FILE> es un paraémetro opcional que de estar presente especifica el archivo de salida donde se volcaraé el listado de tokens, lexemas y Nº de línea generado.");
    		
        	//verificar si solo ingreso el archivo y mostramos resultado por pantalla
    		}else if(args[0]!=null && args.length<2){
    			//creo la lista de token
    	    	lista = new ArrayList<Token>();
    	    	
    	        try {
    	        	//creamos el analizador lexico
    				alex = new AnalizadorLexico(new Archivo(args[0]));
    				String tab = "\t";
    				Token aux = alex.getNextToken();
    		        while(aux!=null){
    		        	System.out.println("# '"+aux.getTipo()+"' "+tab+" '"+aux.getLexema()+"'  "+tab+" 'L.: "+aux.getLinea()+"'"+tab+" 'C.: "+aux.getColumna()+"'");
    		        	lista.add(aux);
    		        	aux = alex.getNextToken();
    		        }
    					
    			} catch (FileNotFoundException e) {
    				System.out.println(e.getMessage());
    			} catch (IOException e) {
    				System.out.println(e.getMessage());
    			} catch(ExceptionLexico e){
    				System.out.println(e.getMessage());
    			} catch (StringAbierto e) {
    				System.out.println(e.getMessage());
    			} 			
    	        
    	    //especificó que quiere que el resultado se guarde en un archivo 
    		}else if(args[0]!=null && args.length==2){
    			//creo la lista de token
    	    	lista = new ArrayList<Token>();
    	    	
    	        try {
    	        	FileWriter f = new FileWriter(args[1]); 
    	        	BufferedWriter b = new BufferedWriter(f); 
    	        	//creamos el analizador lexico
    				alex = new AnalizadorLexico(new Archivo(args[0]));
    				String tab = "\t";
    				Token aux = alex.getNextToken();
    		        while(aux!=null){
    		        	b.write("# '"+aux.getTipo()+"' "+tab+" '"+aux.getLexema()+"'  "+tab+" 'L.: "+aux.getLinea()+"'"+tab+" 'C.: "+aux.getColumna()+"'");
    		        	b.newLine();
    		        	lista.add(aux);
    		        	aux = alex.getNextToken();
    		        }
    		        b.close();
        			System.out.println("Archivo creado.");
    
    					
    			} catch (FileNotFoundException e) {
    				System.out.println(e.getMessage());
    			} catch (IOException e) {
    				System.out.println(e.getMessage());
    			} catch(ExceptionLexico e){
    				System.out.println(e.getMessage());
    			} catch (StringAbierto e) {
    				System.out.println(e.getMessage());
    			} 	
    			
    		}else{
    			//mas parametros de los deseados
    			System.out.println("Ingresó más de 2 argumentos. Utilice -h para ayuda.");
    		}
    	}else{
    		//no paso parametros
    		System.out.println("Parámetros insuficientes. Ingres -h para ayuda.");
    	}
    	     
    
    }

	public static ArrayList<Token> getLista() {
		return lista;
	}
    
   
}