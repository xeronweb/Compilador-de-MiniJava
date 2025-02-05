import java.io.FileNotFoundException;
import java.io.IOException;

public class Tester {
	private int test;
	private int total_test;
	
	
	
	public Tester(int cant){
		this.total_test = cant;
		test = 1;
		
		this.testear();	
		
	}
	
	
	public void testear(){
		
		
		
		for(int i = test; i<=this.total_test; i++){
			try {
	        	//creamos el analizador lexico
				AnalizadorLexico alex = new AnalizadorLexico(new Archivo("test/D/"+i+".java"));
				try {
					AnalizadorSintactico asin = new AnalizadorSintactico(alex);
					asin.empezar();
				} catch (ExceptionLexico e) {
					System.out.println(e.getMessage());
				} catch (ExceptionSintactico e) {					
					
					System.out.println("Test n* "+i+": "+e.getMessage());
					System.out.println("------------------------");
					
					this.test = i+1;
					testear();
					return;
					
				}
				

				
				System.out.println("Test n* "+i+": no hubo errores");
				System.out.println("------------------------");
				
				
					
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				return;
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}
		}
	}
	
	
}
