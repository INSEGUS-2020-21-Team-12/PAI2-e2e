import java.awt.GridLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.List;

import javax.net.SocketFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.DiffieHellman;
import utils.TransactionMessage;

public class testMan {
	public static void main(String[] args) {
//		IntegrityVerifierClient(6666, "123543", "321098", 123321.00);
//		IntegrityVerifierClient(6666, "123345", "321765", 123123.00);
//		IntegrityVerifierClient(6666, "123977", "321345", 123666.00);
//		IntegrityVerifierClient(3343, "123123", "321321", 1231.00);
//		IntegrityVerifierClient(3343, "123321", "321123", 12323.00);
		IntegrityVerifierClient(3343, "123666", "321666", 12333.00);
	}
	
	public static void IntegrityVerifierClient(Integer port, String sourceAccount, String destinationAccount, Double amount) {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", port);

			// Crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			//String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			
			// Crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// INTERCAMBIO DE CLAVES
			Integer privateSharedKey = DiffieHellman.keyExchange(input, output);
			System.out.println(privateSharedKey);
			
			TransactionMessage transaction = transactionMessageInput(privateSharedKey, "HmacSHA256",sourceAccount, destinationAccount, amount);
			

			// Envío del mensaje al servidor
			TransactionMessage.send(transaction, output);
			System.out.println("transaccion enviada");
			
			// Importante para que el mensaje se envíe
			output.flush();

			// Lee la respuesta del servidor
			String respuesta = input.readLine();

			// Muestra la respuesta al cliente
			JOptionPane.showMessageDialog(null, respuesta);

			// Se cierra la conexion
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Salida de la aplicacion
		finally {
			System.exit(0);
		}
	}
	private static TransactionMessage transactionMessageInput(Integer privateSharedKey, String hmac, String sourceAccount, String destinationAccount, Double amount) {
		//JFrame frame = new JFrame("Nueva transacción");
		TransactionMessage res = null;
			System.out.println("Cuenta origen: " + sourceAccount);
			System.out.println("Cuenta destino: " + destinationAccount);
			System.out.println("Cantidad: " + amount);
			
			res = new TransactionMessage(sourceAccount, destinationAccount, amount,
					privateSharedKey, hmac);
		
		return res;
	}
	
}
