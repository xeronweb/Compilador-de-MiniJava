//error: asignacion inline de atributos

class Barco extends Transporte{
	private Barco x;
	public int a = new Barco();
	public Main m;
	
	static int m1(){
		return 1;
	}
	
}


class Transporte{
	public int[] arreglo;
	public int r;
	public Barco b;
	

	
}


class Main{
	
	static void main(){}
	
	static boolean m3(){
		return true;
	}
	
	
}