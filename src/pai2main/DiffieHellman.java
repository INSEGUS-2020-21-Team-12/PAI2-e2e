package pai2main;

public class DiffieHellman {
	
	/* 
	 Definiendo p y g para el intercambio de claves Diffie-Hellman.
	 Los valores han sido elegidos teniendo en cuenta implementar una buena seguridad y una buena eficiencia.
	 Para que el algoritmo fuera totalmente seguro, p debería ser un número de 20 digítos, pero un número tan grande sería intratable para el programa
	 */
	public Integer p = 10000019;
	public Integer g = 111;
	
	public Integer randomInteger() {
		Integer randomInteger = (int) (Math.random()*(1-p)+p);
		return randomInteger;
	}
	
	public Integer newg(Integer privateKeyServerOrClient) {
		Integer newg = g^(privateKeyServerOrClient) % p;
		return newg;
	}
	
	public Integer privateKey(Integer newg, Integer privateKeyServerOrClient) {
		Integer privateKey = newg^(privateKeyServerOrClient) % p;
		return privateKey;
	}
	
}
