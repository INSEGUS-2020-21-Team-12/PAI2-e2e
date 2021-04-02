package pai2main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class IntegrityVerifierClient {
	// Constructor que abre una conexión Socket para enviar mensaje/MAC al servidor
	public IntegrityVerifierClient() {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// Crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");

			// TODO: Recoger input del usuario y MAC de la transaccion
			String mensaje = null, macdelMensaje = null;
			// Envío del mensaje al servidor
			output.println(mensaje);
			// Habría que calcular el correspondiente MAC con la clave compartida por
			// servidor/cliente
			output.println(macdelMensaje);
			// Importante para que el mensaje se envíe
			output.flush();
			// Crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

	// ejecución del cliente de verificación de la integridad
	public static void main(String args[]) {
		new IntegrityVerifierClient();
	}
}
