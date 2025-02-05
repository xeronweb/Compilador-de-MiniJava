import java.io.FileNotFoundException;
import java.io.IOException;

public class Principal {
	@SuppressWarnings("unused")
	private static AnalizadorSintactico asin;

    public static void main (String [] args) {

    	//verificar si tengo los parametros necesarios
    	if(args.length>0){
    		//verificar si pidio ayuda
    		if(args[0].equals("-h")){
        		System.out.println("Estructura general: <PROGRAM_NAME> <IN_FILE> \n\n\t#<PROGRAM_NAME> es el nombre del ejecutable. \n\t#<IN_FILE> es el archivo a analizar leéxicamente. \n\t");
    		
        	//verificar si solo ingreso el archivo y mostramos resultado por pantalla
    		}else if(args[0]!=null && args.length<2){
    	
    	    	
    	        try {
    	        	//creamos el analizador lexico
    				AnalizadorLexico alex = new AnalizadorLexico(new Archivo(args[0]));
    				try {
						asin = new AnalizadorSintactico(alex);
					} catch (ExceptionLexico e) {
						System.out.println(e.getMessage());
					} catch (ExceptionSintactico e) {
						System.out.println("Fallo algo wache");						
						System.out.println(e.getMessage());
					}
    				
    					
    			} catch (FileNotFoundException e) {
    				System.out.println(e.getMessage());
    			} catch (IOException e) {
    				System.out.println(e.getMessage());
    			}
    	        
    	    //especificó que quiere que el resultado se guarde en un archivo 
    		}else{
    			//mas parametros de los deseados
    			System.out.println("Ingresó más de un argumento. Utilice -h para ayuda.");
    		}
    	}else{
    		//no paso parametros
    		System.out.println("Parámetros insuficientes. Ingres -h para ayuda.");
    	}
    	     
    
    }
    
   
}