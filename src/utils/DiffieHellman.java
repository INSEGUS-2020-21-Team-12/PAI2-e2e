package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class DiffieHellman {
	
	/* 
	 Definiendo p y g para el intercambio de claves Diffie-Hellman.
	 Los valores han sido elegidos teniendo en cuenta implementar una buena seguridad y una buena eficiencia.
	 Para que el algoritmo fuera totalmente seguro, p debería ser un número de 20 digítos, pero un número tan grande sería intratable para el programa
	 */
	public static Integer p = 10000019;
	public static Integer g = 111;
	
	public static Integer randomInteger() {
		Integer randomInteger = (int) (Math.random()*(1-p)+p);
		return randomInteger;
	}
	
	public static Integer newg(Integer privateKeyServerOrClient) {
		Integer newg = g^(privateKeyServerOrClient) % p;
		return newg;
	}
	
	public static Integer privateKey(Integer newg, Integer privateKeyServerOrClient) {
		Integer privateKey = newg^(privateKeyServerOrClient) % p;
		return privateKey;
	}
	
	public static Integer keyExchange(BufferedReader input, PrintWriter output) throws NumberFormatException, IOException {
		Integer privateKeyServerOrClient = randomInteger();
		Integer newgtosend = newg(privateKeyServerOrClient);
		output.println(newgtosend);
		output.flush();
		
		Integer newgreceived = Integer.parseInt(input.readLine());
		Integer privateSharedKey = privateKey(newgreceived,privateKeyServerOrClient);
		return privateSharedKey;
	}
	
}
