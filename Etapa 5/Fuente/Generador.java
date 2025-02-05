

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;


public class Generador {
	//atributos
	private LinkedList<String> lista;
	private LinkedList<String> comentarios;
	

	private HashMap<Integer,String> lista_dw;
	private HashMap<Integer,String> comentarios_dw;

	public static int cant_if = 0; 
	public static int cant_while = 0;
	public static int cant_string = 0;

	//constructor
	public Generador(){

		lista = new LinkedList<String>();
		comentarios = new LinkedList<String>();	
		lista_dw = new HashMap<Integer,String>();
		comentarios_dw = new HashMap<Integer,String>();	
		
	}
	
	
	//agrega un metodo que se va a agregar a a la VT
	public void gen_dw(String instruccion, String comentario, int offset){
		lista_dw.put(offset,instruccion);	
		comentarios_dw.put(offset,comentario);		
	}
	
	//agrega ordenadamente a la VT los metodos
	public void agregar_dw(){
		
		for(int i=0; i<lista_dw.size();i++){
			lista.add(lista_dw.get(i));
			comentarios.add(comentarios_dw.get(i));
		}

		lista_dw = new HashMap<Integer,String>();
		comentarios_dw = new HashMap<Integer,String>();	
		
		
	}
	
	//agregar
	public void gen(String instruccion, String comentario){		
		lista.add(instruccion);	
		comentarios.add(comentario);
	}
	
	//crea un archivo
	public void crearArchivo(String nombre) throws IOException{
		//creo rutina de malloc
		this.extra();
		
		BufferedWriter wr = new BufferedWriter(new FileWriter(nombre));
		int i = 0;
		for(String l: lista){
			wr.append(l); //imprimo instruccion
			wr.append("\t \t \t \t \t \t \t"); //imprimo separacion entre ambos
			if(!l.equals("")) wr.append("; "+comentarios.get(i)); //imprimo comentario
			wr.newLine(); //salto de linea
			i++;
		}
		wr.close();
	}
	
	
	//para lso objetos
	private void extra(){
		gen("simple_heap_init:","");
		gen("RET 0","Retorna inmediatamente");
		gen("simple_malloc:","");
		gen("LOADFP","Inicialización unidad");
		gen("LOADSP","");
		gen("STOREFP","Finaliza inicialización del RA");
		gen("LOADHL","hl");
		gen("DUP","hl");
		gen("PUSH 1","1");
		gen("ADD","hl+1");
		gen("STORE 4","Guarda el resultado (un puntero a la primer celda de la región de memoria)");
		gen("LOAD 3","Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
		gen("ADD","");
		gen("STOREHL","Mueve el heap limit (hl). Expande el heap");
		gen("STOREFP","");
		gen("RET 1","Retorna eliminando el parámetro");
	}
}
