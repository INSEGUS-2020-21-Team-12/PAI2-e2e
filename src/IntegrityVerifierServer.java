import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import javax.net.ServerSocketFactory;

import utils.CryptoTools;
import utils.DiffieHellman;
import utils.TransactionMessage;

public class IntegrityVerifierServer {

	private ServerSocket serverSocket;
	
	private Set<String> nonceDatabase = new HashSet<String>();

	// Constructor del Servidor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// Creación de un objeto ServerSocket escuchando peticiones en el puerto 7070
		serverSocket = (ServerSocket) socketFactory.createServerSocket(3343);
	}

	// Ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// Espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.out.println("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				//A partir de ahora, solo se llega aqui cuando se abre una conexion
				System.out.println("Un cliente se ha conectado!");
				// Abre un BufferedReader para leer los datos del cliente
				BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Abre un PrintWriter para enviar datos al cliente
				PrintWriter outputToClient = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				// Se lee del cliente el mensaje y el macdelMensajeEnviado
				
				// INTERCAMBIO DE CLAVES
				Integer privateSharedKey = DiffieHellman.keyExchange(inputFromClient, outputToClient);
				System.out.println("--- ["+privateSharedKey+"] intercambio D-H exitoso, sesion iniciada");
				
				//String mensaje = input.readLine();
				// A continuación habría que calcular el mac del MensajeEnviado que podría ser
				//String macdelMensajeEnviado = input.readLine();
				Boolean aliveSession = true;
				
				while (aliveSession) {
					TransactionMessage transaction = TransactionMessage.receive(inputFromClient);
					System.out.println("--- ["+transaction.getNonce()+"] transaccion recibida");
					
					// mac del MensajeCalculado
					String calculatedMac = CryptoTools.calculateHMAC(transaction.toString(), privateSharedKey, "HmacSHA256");
					
					String transactionResult;
					String transactionResultMessage;
					
					if (nonceDatabase.contains(transaction.getNonce())) {
						transactionResult =  "--- ["+transaction.getNonce()+"] nonce ya utilizado, transaccion rechazada";
						transactionResultMessage = "["+transaction.toString()+"] (rechazada)";
					} 
					else if(!transaction.getMac().equals(calculatedMac)) {
						transactionResult =  "--- ["+transaction.getNonce()+"] mensaje corrupto, transaccion rechazada";
						transactionResultMessage = "["+transaction.toString()+"] (rechazada)";
					} else {
						transactionResult =  "--- ["+transaction.getNonce()+"] mensaje integro, transaccion aceptada";
						transactionResultMessage = "["+transaction.toString()+"] (aceptada)";
						nonceDatabase.add(transaction.getNonce());					
					}
					
					outputToClient.println(transactionResultMessage);
					System.out.println(transactionResult);
					outputToClient.flush();
					
					// The session will continue if an empty line is received from client
					String aliveSessionResponse = inputFromClient.readLine();
					aliveSession = aliveSessionResponse.isEmpty();
				}
				
				System.out.println("--- ["+privateSharedKey+"] sesión finalizada");
				
				outputToClient.close();
				inputFromClient.close();
				socket.close();
				
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			System.out.println("...\n");
		}
	}

	public static void main(String args[]) throws Exception {
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}

}
