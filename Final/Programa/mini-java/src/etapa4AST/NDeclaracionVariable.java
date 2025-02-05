package etapa4AST;

import etapa1.Principal;
import etapa1.Token;
import etapa3Entradas.EntradaConParams;
import etapa3Entradas.EntradaVarLocal;
import etapa3Tipos.Tipo;
import etapa3Tipos.TipoIdClase;
import etapa4Exp.ExceptionSemanticoChequeo;

public class NDeclaracionVariable  extends NSentencia{
	//atributos
	protected Tipo tipo;
	protected String nombre;
	protected NBloque bloque;
	public EntradaConParams miMetodo;
	public EntradaVarLocal varLocal;
	
	//constrcutor
	public NDeclaracionVariable(Token token, Tipo tipo, String nombre, NBloque bloque){
		super(token);
		this.tipo = tipo;
		this.nombre = nombre;
		this.bloque = bloque;
	}

	//getters
	public Tipo getTipo() {
		return tipo;
	}

	public String getNombre() {
		return nombre;
	}
	

	//chequear
	public void chequear() throws ExceptionSemanticoChequeo{
		//metodo actual
		miMetodo = Principal.ts.getMetodoActual();
		
		//verifico que no exista una variable con ese nombre
		if(!miMetodo.estaAtr(nombre)){
			
			//es tipo id clase y no existe
			if(tipo instanceof TipoIdClase && !Principal.ts.getClases().containsKey(tipo.getNombre())){
			
				throw new ExceptionSemanticoChequeo("No existe la clase "+tipo.getNombre()+".",token.getLinea(),token.getColumna());
				
			//existe la clase	
			}else{

				//creo la variable local
				varLocal = new EntradaVarLocal(tipo,token);
				
				//la agrego a la lista de variables del bloque
				bloque.getVariables().put(nombre,varLocal);
				
				//la agrego a las variables del metodo
				miMetodo.getVariablesMetodo().put(nombre, varLocal);
			
			}
			
		//ya exixte una variable con ese nombre	
		}else{
			throw new ExceptionSemanticoChequeo("La variable local '"+nombre+"' ya est� definida como local o instancia. ",token.getLinea(),token.getColumna());
		}
		
	}
	

	public void generar() {
	
		
		//calcular offset
		varLocal.offset = -(miMetodo.varsLocales);
		
		//la agrego a la lista de variables del bloque
		bloque.getVariables().put(nombre,varLocal);
		
		//la agrego a las variables del metodo
		miMetodo.getVariablesMetodo().put(nombre, varLocal);
		
		//sumo uno a la cantidad de variables actuales
		Principal.ts.metodoActual.varsLocales++;
		
		//reservo lugar para la declaracion de una variable
		Principal.gen.gen("RMEM 1", "Reservar espacio para var local: "+this.nombre+", de bloque: "+this.bloque);
		
	}
	
	
	//imprimir
	public void imprimir(int n){
		tabs(n); 
		System.out.println("Declaracion variable de bloque "+bloque);
		tabs(n+1); System.out.println("   Tipo: "+tipo.nombre+"");
		tabs(n+1); System.out.println("   Nombre: "+nombre+"");
	}




	

	
	
}
