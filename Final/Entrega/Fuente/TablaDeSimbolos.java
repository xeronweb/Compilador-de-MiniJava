


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;


public class TablaDeSimbolos {
	/**
	 * ATRIBUTOS
	 */
	protected HashMap<String,EntradaClase> clases;
	protected EntradaClase claseActual;
	public EntradaMetodo metodoMain;
	public EntradaConParams metodoActual;
	protected boolean errores;
	public LinkedList<String> errores_detalles;
	public NBloque bloqueActual = null;

	
	/**
	 * CONSTRUCTOR
	 * @throws ExceptionSemanticoDeclaracion
	 */
	public TablaDeSimbolos() throws ExceptionSemanticoDeclaracion{
		clases = new HashMap<String,EntradaClase>();
		errores_detalles = new LinkedList<String>();
		this.claseActual = null;
		this.metodoMain = null;
		this.metodoActual = null;
		this.errores = false;
		
		//agrego la clase por defecto Object
		this.addClase(new EntradaClase(new Token("IdClase","Object",0,0)));
		this.addConstructor(new EntradaConstructor(new Token("IdClase","Object",0,0),claseActual));
		this.getClaseActual().setConsolidado(true);
		
		//crear la clase por defecto System
		this.crearSystem();		
	}
	
	/**
	 * AGREGAR CLASE A LA TS
	 * @param token
	 * @throws ExceptionClase
	 */
	public void addClase(EntradaClase clase) throws ExceptionSemanticoDeclaracion{
		
		//no esta la clase y agrego
		if(!clases.containsKey(clase.getNombre())){

			this.claseActual  = clase;
			clases.put(clase.getNombre(), this.claseActual);		
		
		//error, ya existe la clase
		}else{
			throw new ExceptionClase(clase.getNombre(),clase.getLinea(),clase.getColumna());
		}
		
	}
	
	/**
	 * AGREGAR ATRIBUTOS
	 * @param met
	 * @throws ExceptionSemanticoDeclaracion 
	 */
	public void addAtributos(LinkedList<EntradaAtributo> atrs) throws ExceptionSemanticoDeclaracion{
		this.claseActual.addAtributos(atrs);
	}	
	

	
	/**
	 * AGREGAR CUALQUIER CONSTRCUTOR
	 * @param met
	 * @throws ExceptionSemanticoDeclaracion 
	 */
	public void addConstructor(EntradaConParams ctor) throws ExceptionSemanticoDeclaracion{
		this.claseActual.addConstructor(ctor);
		this.metodoActual = ctor;
	}	
	
	/**
	 * AGREGAR CUALQUIER METODO
	 * @param met
	 * @throws ExceptionSemanticoDeclaracion 
	 */
	public void addMetodo(EntradaMetodo met) throws ExceptionSemanticoDeclaracion{
				
		//es static void main()
		if(met.esEstatico() && met.getTipo().getNombre().equals("void") 
				&& met.getNombre().equals("main") && met.getCantParams()==0){

			//verifico que ya no haya un main
			if(!hayStaticMain()){
				
				this.claseActual.addMetodo(met);
				this.metodoActual = met;
				this.metodoMain = met;
			
			//Ya hay un main definido
			}else{
				throw new ExceptionMainEstatico(claseActual.getLexema(), met.getLinea(), met.getColumna());
			}
			
		//es otro metodo
		}else{
			
			this.claseActual.addMetodo(met);
			this.metodoActual = met;
		}
	}
	
	
	public void setHayErrores(){
		this.errores = true;
	}
	
	
	
	
	
		

	/**
	 * GETTERS
	 */
	public EntradaClase getClaseActual(){
		return this.claseActual;
	}	
	public EntradaConParams getMetodoActual(){
		return this.metodoActual;
	}
	public HashMap<String,EntradaClase> getClases(){
		return this.clases;
	}
	public boolean hayErrores(){
		return this.errores;
	}
	public boolean hayStaticMain(){
		return this.metodoMain!=null;
	}

	/**
	 * SEGUNDA PASADA
	 * 
	 */
	

	//chequear
	public void chequearSentencias() throws ExceptionSemantico{
		
		this.errores_detalles = new LinkedList<String>();
		
		//recorro los metodos y chequeo los bloques
		for(Entry<String,EntradaClase> clase: this.getClases().entrySet()){
			
			this.claseActual = clase.getValue();
			
			//atributos
			for(Entry<String, EntradaAtributo> atr: clase.getValue().getAtributos().entrySet())
				
				//chequeo el bloque de metodo
				atr.getValue().chequear();					
			
			
			//metodos
			for(Entry<String, EntradaConParams> metodo: clase.getValue().getMetodos().entrySet()){

				this.metodoActual = metodo.getValue();
				
				//chequeo el bloque de metodo
				this.metodoActual.chequear();
				
			}	
			
			//constrcutores
			for(Entry<Integer, EntradaConParams> ctor: clase.getValue().getConstructores().entrySet()){

				this.metodoActual = ctor.getValue();
	
				//chequeo el bloque de metodo
				this.metodoActual.chequear();
			}
		}
		
	}
		
	

	/**
	 * Verifica si esta todo bien declarado
	 * @throws ExceptionSemanticoDeclaracion
	 */
	public void chequearDeclaraciones() throws ExceptionSemanticoDeclaracion{
		
		
		//verifico q haya main
		if(!hayStaticMain()){
			try{
				throw new ExceptionSinMain();
			}catch(ExceptionSinMain e){
				System.out.println(e.getMessage());
				this.errores = true;
			}
		}
		

		
		//recorro todas las clases
		for(Entry<String,EntradaClase> clase: this.getClases().entrySet()){
			
			try{
				//verifico herencia circular
				this.verificarHerenciaCircular(clase.getValue());
				
				// verifiar que los tipos existan de todo lo tipo clase
				this.verificarQueExistanLosTiposIDClase(clase.getValue());
			
			}catch(ExceptionHerenciaCircular | ExceptionClaseNoExiste e){
				this.errores = true;				
				if(!this.errores_detalles.contains(e.getMessage())){
					this.errores_detalles.add(e.getMessage());
					System.out.println(e.getMessage());
				}
				
			}
				
			
		}
	}
	
	
	/**
	 * consolida todas las clases
	 * @throws ExceptionSemanticoDeclaracion
	 */
	public void consolidar() throws ExceptionSemanticoDeclaracion{
		//recorro las clases
		for(Entry<String, EntradaClase> clase: this.getClases().entrySet()){
			
			//obtengo la clase de la que hereda nuestra clase actual
			clase.getValue().consolidar(this.clases);
			
		}		
		
	}
	
	
	public void generar(){
		//nombre de main
		String etiqueta_main = Principal.ts.metodoMain.getEtiqueta();
		
		//codigo de incializacion
		Principal.gen.gen(".CODE", "C�digo de inicializaci�n");
		Principal.gen.gen("PUSH "+etiqueta_main, "Voy a llamar a main");
		Principal.gen.gen("CALL", "Voy al metodo que esta el pila");
		Principal.gen.gen("HALT", "Termina la ejecucion");
		Principal.gen.gen("","");
		
		
		for(Entry<String, EntradaClase> clase: this.getClases().entrySet()){
			
			//obtengo la clase de la que hereda nuestra clase actual
			clase.getValue().generar();
			Principal.gen.gen("","");
			
		}
	}
	
	
	/**
	 * Verifica si existen todos los tipos que se usan en una clase
	 * @param clase
	 * @throws ExceptionSemanticoDeclaracion
	 */
	private void verificarQueExistanLosTiposIDClase(EntradaClase clase) throws ExceptionSemanticoDeclaracion{
		
		//verificar si existe la clase que herado se hace en ASIN
		
		//atributos
		for(Entry<String, EntradaAtributo> atr: clase.getAtributos().entrySet()){
			
			try{
				//le pido al tipo de atributo que verifique si su tipo esta en la lista de clases definidas
				atr.getValue().getTipo().verificarTipo(this.getClases(),atr.getValue().getLinea(),atr.getValue().getColumna());
			}catch(ExceptionHerenciaCircular | ExceptionClaseNoExiste e){
				System.out.println(e.getMessage());
			}
		
		}
		
		
		//constructores
		for(Entry<Integer, EntradaConParams> ctor: clase.getConstructores().entrySet()){
			for(Entry<String, EntradaParametro> param: ctor.getValue().getParametros().entrySet()){
				try{
					//le pido al tipo de atributo que verifique si su tipo esta en la lista de clases definidas
					param.getValue().getTipo().verificarTipo(this.getClases(),param.getValue().getLinea(),param.getValue().getColumna());
				}catch(ExceptionHerenciaCircular | ExceptionClaseNoExiste e){
					System.out.println(e.getMessage());
				}				
			}			
		}
		
		
		//metodos
		for(Map.Entry<String, EntradaConParams> met: clase.getMetodos().entrySet()){
			try{
				//le pido al tipo de retorno del metodo que verifique si su tipo esta en la lista de clases definidas
				met.getValue().getTipo().verificarTipo(this.getClases(),met.getValue().getLinea(),met.getValue().getColumna());
			}catch(ExceptionHerenciaCircular | ExceptionClaseNoExiste e){
				System.out.println(e.getMessage());
			}	
		
			for(Map.Entry<String, EntradaParametro> param: met.getValue().getParametros().entrySet()){
				try{
					//le pido al tipo de atributo que verifique si su tipo esta en la lista de clases definidas
					param.getValue().getTipo().verificarTipo(this.getClases(),param.getValue().getLinea(),param.getValue().getColumna());
				}catch(ExceptionHerenciaCircular | ExceptionClaseNoExiste e){
					System.out.println(e.getMessage());
				}					
			}
		
		}
	}
	
	
	/**
	 * verifica si hay herencia circular en los ancestro de Clase
	 * @param clase
	 * @throws ExceptionSemanticoDeclaracion
	 */
	private void verificarHerenciaCircular(EntradaClase clase) throws ExceptionSemanticoDeclaracion{
		LinkedList<String> visitados = new LinkedList<String>();
		visitados.add(clase.getNombre());
		EntradaClase aux = clase;
		
		
		while(clase.getHerencia()!=null){
						
			//obtengo el nombre de la clase herencia
			String herencia = clase.getHerencia();
			
			//verifico q ya no este visitada
			if(visitados.contains(herencia)){
				clase.setConsolidado(true);
				throw new ExceptionHerenciaCircular(aux.getNombre(),herencia,aux.getLinea(),aux.getColumna());
			}
			
			//agrego a visitados y cambio de clase
			visitados.add(clase.getNombre());
			
			if(existeClase(clase.getHerencia()))
				clase = getClase(clase.getHerencia());	
			else{
				clase.setConsolidado(true);
				throw new ExceptionClaseNoExiste(clase.getHerencia(),clase.getLinea(),clase.getColumna());
			}
		}
		
		
	}
	
	/**
	 * Retorna una entrada clase con el key keyClase, si no lanza una excepcion
	 * @param keyClase Key de la clase
	 * @return EntradaClase
	 * @throws ExceptionSemanticoDeclaracion
	 */
	public EntradaClase getClase(String keyClase) throws ExceptionSemanticoDeclaracion{
		EntradaClase clase = getClases().get(keyClase);
		
		if(clase!=null){
			return clase;
		}else{
			throw new ExceptionClaseNoExiste(keyClase,0,0);
		}
		
	}
	public boolean existeClase(String keyClase){
		EntradaClase clase = getClases().get(keyClase);
		
		if(clase!=null){
			return true;
		}else{
			return false;
		}
	}
	
	
	public void imprimir(){
		System.out.println("=======================");
		for(Map.Entry<String,EntradaClase> entry: clases.entrySet()) {
			System.out.println(""+entry.getKey()+" extends "+entry.getValue().getHerencia());
			entry.getValue().imprimir();
			System.out.println("=======================");	
		}	
		
		System.out.println("El metodo static main() est� en la clase: "+this.metodoMain.getClase().getNombre());
		System.out.println("=======================");	
	}
	
	public void imprimirAST(){
		System.out.println("=======================");
		for(Map.Entry<String,EntradaClase> entry: clases.entrySet()) {
			System.out.println(""+entry.getKey()+" extends "+entry.getValue().getHerencia());
			entry.getValue().imprimirAST();
			System.out.println("=======================");	
		}	
		System.out.println("=======================");	
	}
	
	
	private void crearSystem() throws ExceptionSemanticoDeclaracion{
		EntradaMetodo met;
		boolean estatico = false;
		//boolean dinamico = true;
		
		EntradaClase clase = new EntradaClase(new Token("IdClase","System",0,0));
		this.addClase(clase);
		this.getClaseActual().setConsolidado(true);
		
		//constructor
		this.addConstructor(new EntradaConstructor(new Token("IdClase","System",0,0),claseActual));
		
		//agrego static int read()
		met = new EntradaMetodo(estatico,new TipoInt(),new Token("IdMetVar","read",0,0),this.getClaseActual());
		met.setBloque(new NBloqueSystem(new Token("","read",0,0),null,null,met));
		this.addMetodo(met);
		
		//static void printB(boolean b)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printB",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoBoolean(),new Token("boolean","b",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printB",0,0),null,null,met));
		this.addMetodo(met);

		//static void printC(char c)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printC",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoChar(),new Token("char","c",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printC",0,0),null,null,met));
		this.addMetodo(met);
		
		//static void printI(int i)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printI",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoInt(),new Token("int","i",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printI",0,0),null,null,met));
		this.addMetodo(met);

		//static void printS(String s)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printS",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoString(),new Token("String","s",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printS",0,0),null,null,met));
		this.addMetodo(met);
		
		//static void println()
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","println",0,0),this.getClaseActual());
		met.setBloque(new NBloqueSystem(new Token("","println",0,0),null,null,met));
		this.addMetodo(met);
		
		//static void printBln(boolean b)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printBln",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoBoolean(),new Token("boolean","b",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printBln",0,0),null,null,met));
		this.addMetodo(met);

		//static void printCln(char c)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printCln",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoChar(),new Token("char","c",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printCln",0,0),null,null,met));
		this.addMetodo(met);
		
		//static void printIln(int i)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printIln",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoInt(),new Token("int","i",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printIln",0,0),null,null,met));
		this.addMetodo(met);

		//static void printSln(String s)
		met = new EntradaMetodo(estatico,new TipoVoid(),new Token("IdMetVar","printSln",0,0),this.getClaseActual());
		met.addParam(new EntradaParametro(new TipoString(),new Token("String","s",0,0)));
		met.setBloque(new NBloqueSystem(new Token("","printSln",0,0),null,null,met));
		this.addMetodo(met);		
	}
	
	
	
	
}
