


import java.io.IOException;
import java.util.HashMap;


public class AnalizadorLexico {
	// atributos
	private Archivo fuente;
	private String lexema;
	private char actual;
	private String estado;
	private int linea_comentario;
	private int columna_lexico;

	// tipos de literales etc
	private String entero = "entero";
	private String caracter = "caracter";
	private String cadena = "string";
	private String idMetVar = "IdMetVar";
	private String idClase = "IdClase";
	private HashMap<String, String> claves;

	// constructor
	public AnalizadorLexico(Archivo fuente) throws IOException {
		this.setFuente(fuente);
		this.actual = fuente.getNext();
		this.lexema = "";
		this.estado = "q0";
		this.linea_comentario = -1;
		this.columna_lexico = 0;
		this.setPalabrasClaves();
	}

	private void setPalabrasClaves() {
		claves = new HashMap<String, String>();

		claves.put("class", "p_class");
		claves.put("String", "p_string");
		claves.put("public", "p_public");
		claves.put("if", "p_if");
		claves.put("this", "p_this");
		claves.put("extends", "p_extends");
		claves.put("boolean", "p_boolean");
		claves.put("private", "p_private");
		claves.put("else", "p_else");
		claves.put("new", "p_new");
		claves.put("static", "p_static");
		claves.put("char", "p_char");
		claves.put("void", "p_void");
		claves.put("while", "p_while");
		claves.put("true", "p_true");
		claves.put("dynamic", "p_dynamic");
		claves.put("int", "p_int");
		claves.put("null", "null");
		claves.put("return", "p_return");
		claves.put("false", "p_false");
	}

	// getters and setters
	public Archivo getFuente() {
		return fuente;
	}

	private void setFuente(Archivo fuente) {
		this.fuente = fuente;
	}

	public Token getNextToken() throws ExceptionLexico, StringAbierto {
		Token t = null;
		lexema = "";
		
		try{
			//mientras sea null, el token no se armo
			while (t == null) {
				// print(estado);
				
				//depediendo del estado, se ejecuta un case distinto
				switch (estado) {
	
				/**
				 * ESTADO INICIAL
				 */
				case "q0":
	
					// fin de archivo
					if (esFin(actual)) {
						return t;
	
						// es un espacio ?
					} else if (esEspacio(actual)) {
						this.columna_lexico = fuente.getColumna();
						avanzar();
						this.estado = "q0";
	
						// es un digito ?
					} else if (esDigito(actual)) {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q1";
	
						// es comilla simple (') ?
					} else if (actual == 39) {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q3";
	
						// es comilla doble (") ?
					} else if (actual == 34) {
						this.columna_lexico = fuente.getColumna();
						avanzar();
						this.estado = "q7";
	
						// parentesis que abre
					} else if (actual == '(') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q22";
	
						// parentesis que cierra
					} else if (actual == ')') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q23";
	
						// llave que abre
					} else if (actual == '{') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q24";
	
						// llave que cierra
					} else if (actual == '}') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q25";
	
						// coma
					} else if (actual == ',') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q26";
	
						// punto
					} else if (actual == '.') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q27";
	
						// punto y coma
					} else if (actual == ';') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q28";
	
						// es un corchete que abre
					} else if (actual == '[') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q29";
	
						// corchete que cierra
					} else if (actual == ']') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q30";
	
						// es un &
					} else if (actual == '&') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q44";
	
						// es un /
					} else if (actual == '/') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.linea_comentario = fuente.getLinea();
						this.estado = "q43";
	
						// operador o '|'
					} else if (actual == '|') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q46";
	
						// operador diferencia y negacion
					} else if (actual == '!') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q33";
	
						// operador mayor
					} else if (actual == '>') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q31";
	
						// es un operador igual
					} else if (actual == '=') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q37";
	
						// es un operador menor
					} else if (actual == '<') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q32";
	
						// es un operador +
					} else if (actual == '+') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q39";
	
						// es un operador -
					} else if (actual == '-') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q38";
	
						// es un operador *
					} else if (actual == '*') {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q40";
	
						// puede ser palabra clave o idMetVar
					} else if (esIdMetVar(actual)) {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q6";
	
						// puede ser palabra clave o idClase
					} else if (esIdClase(actual)) {
						this.columna_lexico = fuente.getColumna();
						consumir();
						this.estado = "q2";
	
					} else {
						throw new CharInesperado(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				/**
				 * IDENTIFICADOR O IDMETVAR
				 */
				case "q6":
					// verifico que sea un caracter valido para identificador
					if (esId(actual)) {
						consumir();
						this.estado = "q6";
	
						// si encuentro otro caracter, es un identificador o
						// palanbra clave
					} else {
						if (claves.containsKey(this.lexema)) {
							t = new Token(claves.get(lexema), lexema, fuente.getLinea(), this.columna_lexico);
							this.reiniciar();
						} else {
							t = new Token(idMetVar, lexema, fuente.getLinea(), this.columna_lexico);
							this.reiniciar();
						}
	
					}
	
					break;
	
				/**
				 * IDENTIFICADOR O IDCLASE
				 */
				case "q2":
					// verifico que sea un caracter valido para identificador
					if (esId(actual)) {
						consumir();
						this.estado = "q2";
	
						// si encuentro otro caracter, es un identificador o
						// palanbra clave
					} else {
	
						if (claves.containsKey(this.lexema)) {
							t = new Token(claves.get(lexema), lexema, fuente.getLinea(), this.columna_lexico);
							this.reiniciar();
						} else {
							t = new Token(idClase, lexema, fuente.getLinea(), this.columna_lexico);
							this.reiniciar();
						}
					}
	
					break;
	
				/**
				 * DIGITO
				 */
				case "q1":
					// es un digito
					if (esDigito(actual)) {
						consumir();
						this.estado = "q1";
	
						// Es otro caracter
						// Creo el Token si el caracter que sigue corresponde con un
						// fin de numero
					} else if (esDigitoValido(actual)) {
						t = new Token(entero, lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					} else {
						// vamos a un estado que solo tira un error
						this.estado = "q9";
					}
					break;
	
				case "q9":
					// Lanzamos un error
					throw new IntFormato(actual, fuente.getColumna(), fuente.getLinea());
	
					// break;
	
					/**
					 * FIN DIGITO
					 */
	
					/**
					 * CHAR
					 */
				case "q3":
					// es barra invertida (\) ?
					if (actual == 92) {
						avanzar();
						this.estado = "q52";
	
						// es un caracter ?
					} else if (esChar(actual)) {
						consumir();
						this.estado = "q4";
	
						// no es un caracter ascii
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q4":
					// es comilla simple ?
					if (actual==39) {
						consumir();
						this.estado = "q5";
					} else {
						throw new CharFormat(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q5":
					// si llegamos hasta aca, es un lexema caracter
					t = new Token(caracter, lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				case "q52":
					// es una t para el TAB
					if (actual == 't') {
						consumir('\t');
						this.estado = "q55";
						// TERMINAR como traducir a TAB
	
						// es una n de salto de linea
					} else if (actual == 'n') {
						consumir('\n');
						this.estado = "q56";
						// TERMINAR como traducir a salto de linea
	
					}else if(actual==39){
						throw new CharFormat(actual, fuente.getColumna(), fuente.getLinea());
						
						// es otro char
					} else if (esChar(actual)) {
						consumir();
						this.estado = "q4";
	
						// otro caracter no valido (no ascii)
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q55":
					// es comilla simple ?
					if (actual == 39) {
						consumir();
						this.estado = "q5";
					} else {
						throw new CharFormat(actual, fuente.getColumna(), fuente.getLinea());
					}
	
					break;
	
				case "q56":
					// es comilla simple ?
					if (actual == 39) {
						consumir();
						this.estado = "q5";
					} else {
						throw new CharFormat(actual, fuente.getColumna(), fuente.getLinea());
					}
	
					break;
				/**
				 * FIN CHAR
				 */
	
				/**
				 * STRING
				 */
				case "q7":
					// es un salto de linea literal en string, hay que usar \n
					if (actual == '\n') {
						this.estado = "q61";
						
					// es una comilla doble de cierre
					} else if (actual == 34) {
						avanzar();
						this.estado = "q8";
	
					// puede ser un salto de linea
					} else if (actual == 92) {
						avanzar();
						this.estado = "q60";
	
					} else if(esFin(actual)){
						this.estado = "q61";						
						
					// es un caracter
					} else if (esChar(actual)) {
						consumir();
						this.estado = "q7";
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
	
					break;
	
				case "q60":
					// es un salto de linea o tab dentro de un string
					if (actual == 'n') {
						consumir('\n');
						this.estado = "q7";
	
					// es un tab
					} else if (actual == 't') {
						consumir('\t');
						this.estado = "q7";
	
					// me fijo si termino
					} else if (actual == 34) {
						consumir((char) 92);
						this.estado = "q8";
	
					// era solo un div invertido, agrego sin avanzar para q7
					// siga la ejecucion
					} else {
						agregar((char) 92);
						this.estado = "q7";
					}
	
					break;
	
				case "q61":
					throw new StringAbierto(this.columna_lexico, fuente.getLinea());
					// break;
	
				case "q8":
					// si llegamos hasta aca, es un lexema string
					t = new Token(cadena, lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN STRING
				 */
	
				/**
				 * PUNTUACION
				 */
	
				// parentesis que abre
				case "q22":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("pa_a", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// parentesis que cierra
				case "q23":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("pa_c", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// llave que abre
				case "q24":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("ll_a", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// llave que cierra
				case "q25":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("ll_c", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// coma
				case "q26":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("coma", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// punto
				case "q27":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("punto", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// punto y coma
				case "q28":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("punto_coma", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// corchete que abre
				case "q29":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("co_a", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				// corchete que cierra
				case "q30":
					// si llegamos hasta aca, es un lexema puntuacion
					t = new Token("co_c", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN PUNTUACION
				 */
	
				/**
				 * OPERADOR AND
				 */
				case "q44":
					// es un &
					if (actual == '&') {
						consumir();
						this.estado = "q45";
					} else {
						throw new OperadorAnd(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q45":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_and", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN OPERADOR AND
				 */
	
				/**
				 * OPERADOR DIV y COMENTARIOS
				 */
				case "q43":
					// es un *
					if (actual == '*') {
						avanzar();
						this.estado = "q42";
	
						// es un /
					} else if (actual == '/') {
						avanzar();
						this.estado = "q50";
	
						// es el operador div
					} else {
						// si llegamos hasta aca, es un lexema operador
						t = new Token("op_div", lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					}
					break;
	
				case "q42":
					// es un asterisco
					if (actual == '*') {
						avanzar();
						this.estado = "q48";
	
						// es un caracter
					} else if (esChar(actual)) {
						avanzar();
						this.estado = "q42";
	
						// comentario sin terminar
					} else if (actual == 0) {
						this.estado = "q10";
	
						// caracter no ascii
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q48":
					// es un div
					if (actual == '/') {
						avanzar();
						this.estado = "q49";
	
						// es un *
					} else if (actual == '*') {
						avanzar();
						this.estado = "q48";
	
					} else if (esChar(actual)) {
						avanzar();
						this.estado = "q42";
	
						// comentario sin terminar
					} else if (actual == 0) {
						this.estado = "q10";
						// caracter no ascii
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q10":
					throw new ComentarioAbierto(fuente.getColumna(), fuente.getLinea(), this.linea_comentario);
					// break;
	
				case "q49":
					// estamos en el fin de un comentario
					reiniciar();
					break;
	
				case "q50":
					// es un salto de linea?
					if (actual == '\n') {
						avanzar();
						this.estado = "q49";
						
					//es un char y sigo consumiento	
					} else if (esChar(actual)) {
						avanzar();
						this.estado = "q50";
					
					//termino el archivo	
					} else if(actual == 0){
						this.estado = "q0";
						
					} else {
						throw new CharAscii(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				/**
				 * FIN OPERADOR DIV y COMENTARIOS
				 */
	
				/**
				 * OPERADOR O
				 */
				case "q46":
					if (actual == '|') {
						consumir();
						this.estado = "q47";
					} else {
						throw new OperadorOR(actual, fuente.getColumna(), fuente.getLinea());
					}
					break;
	
				case "q47":
					t = new Token("op_or", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN OPERADOR O
				 */
	
				/**
				 * OPERADOR NEGACION(!) y DISTINTO (!=)
				 */
				case "q33":
					// es un igual
					if (actual == '=') {
						consumir();
						this.estado = "q34";
	
						// es el operador de negacion
					} else {
						t = new Token("op_neg", lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					}
					break;
	
				// es un DISTINTO
				case "q34":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_dis", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN OPERADOR NEGACION
				 */
	
				/**
				 * OPERADOR MAYOR '>' o MAYOR O IGUAL
				 */
	
				case "q31":
					// es un igual
					if (actual == '=') {
						consumir();
						this.estado = "q35";
					} else {
						// es un operador mayor
						t = new Token("op_mayor", lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					}
					break;
	
				// es un MAYOR O IGUAL (>=)
				case "q35":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_mai", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN OPERADOR MAYOR '>' O MAYOR O IGUAL
				 */
	
				/**
				 * OPERADOR MENOR '>' o MENOR O IGUAL
				 */
	
				case "q32":
					// es un igual
					if (actual == '=') {
						consumir();
						this.estado = "q36";
					} else {
						// es un operador MENOR
						t = new Token("op_menor", lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					}
					break;
	
				// es un MENOR O IGUAL (>=)
				case "q36":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_mei", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN OPERADOR MENOR '>' O MENOR O IGUAL
				 */
	
				/**
				 * ASIGNACION e IGUAL
				 */
	
				case "q37":
					// es un igual
					if (actual == '=') {
						consumir();
						this.estado = "q41";
					} else {
						// es un operador ASIGNACION
						t = new Token("asignacion", lexema, fuente.getLinea(), this.columna_lexico);
						this.reiniciar();
					}
					break;
	
				// es IGUAL (=)
				case "q41":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_igual", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN ASIGNACION E IGUAL
				 */
	
				/**
				 * MENOS
				 */
				// es un menos
				case "q38":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_menos", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN MENOS
				 */
	
				/**
				 * MAS
				 */
				// es un menos
				case "q39":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_mas", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN MAS
				 */
	
				/**
				 * MULT
				 */
				// es un menos
				case "q40":
					// si llegamos hasta aca, es un lexema operador
					t = new Token("op_mult", lexema, fuente.getLinea(), this.columna_lexico);
					this.reiniciar();
					break;
	
				/**
				 * FIN MULT
				 */
	
				default:
	
					break;
				}
			}
		}catch(IOException e){
			System.out.println("No se pudo leer un caracter. Verifique si tiene permisos sobre el archivo.");
		}
		return t;
	}

	/** Agrega 
	 * @throws IOException */
	private void consumir() throws IOException {
		this.lexema += actual;
		actual = this.fuente.getNext();
	}

	/** Agrega un car especifico y avanza 
	 * @throws IOException */
	private void consumir(char c) throws IOException {
		this.lexema += c;
		actual = this.fuente.getNext();
	}

	/** Agrega un caracter especifico */
	private void agregar(char c) {
		this.lexema += c;
	}

	/** Avanzar 
	 * @throws IOException */
	private void avanzar() throws IOException {
		actual = this.fuente.getNext();
	}

	/** Reiniciar */
	private void reiniciar() {
		this.lexema = "";
		this.estado = "q0";
	}

	/** true si es espacio, salto de linea o tab */
	private boolean esEspacio(char c) {
		return c == ' ' || c == '\n' || c == '\t' || c == 13;
	}

	/** true si es fin de archivo */
	private boolean esFin(char c) {
		return c == 0;
	}

	/** true si es un operador o signo de puntuacion */
	private boolean esDigitoValido(char c) {
		return (c > 0 && c < 65) || (c > 90 && c < 97) || (c > 122 && c < 256);
	}

	/** true si es digito entre 0 y 9 */
	private boolean esDigito(char c) {
		return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
				|| c == '9';
	}

	/** true si es ascii excepto nulo (0) */
	private boolean esChar(char c) {
		return c > 0 && c < 256;
	}

	/**
	 * true si es una letra valida de inicio para identificador de tipo IdMetVar
	 */
	private boolean esIdMetVar(char c) {
		return c > 96 && c < 123;
	}

	/**
	 * true si es una letra valida de inicio para identificador de tipo IdClase
	 */
	private boolean esIdClase(char c) {
		return c > 64 && c < 90;
	}

	/**
	 * true si es una letra valida para identificador (mayusculas, minusculas,
	 * guion bajo)
	 */
	private boolean esId(char c) {
		return (c > 64 && c < 90) || (c > 96 && c < 123) || c == 95 || (c > 47 && c < 58);
	}

	@SuppressWarnings("unused")
	private void print(String c) {
		System.out.println(c);
	}

}
