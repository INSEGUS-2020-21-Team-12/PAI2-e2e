
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utils.TransactionMessage;

public class IntegrityVerifierClient {
	// Constructor que abre una conexión Socket para enviar mensaje/MAC al servidor
	public IntegrityVerifierClient() {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 3343);

			// Crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			//String userName = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			
			TransactionMessage transaction = transactionMessageInput();

			// TODO: Recoger input del usuario y MAC de la transaccion
			//String mensaje = userName, macdelMensaje = null;

			// Envío del mensaje al servidor
			//output.println(mensaje);
			TransactionMessage.send(transaction, output);
			System.out.println("transaccion enviada");
			
			// Habría que calcular el correspondiente MAC con la clave compartida por
			// servidor/cliente
			// output.println(macdelMensaje);
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

	private TransactionMessage transactionMessageInput() {
		//JFrame frame = new JFrame("Nueva transacción");
		TransactionMessage res = null;
		
		JTextField srcAccountField = new JTextField(5);
		JTextField dstAccountField = new JTextField(5);
		JTextField amountField = new JTextField(5);

		JPanel transactionPanel = new JPanel();
		transactionPanel.setLayout(new GridLayout(0, 2, 2, 2));
		
		transactionPanel.add(new JLabel("Cuenta origen:"));
		transactionPanel.add(srcAccountField);
		//transactionPanel.add(Box.createHorizontalStrut(15)); // a spacer
		transactionPanel.add(new JLabel("Cuenta destino:"));
		transactionPanel.add(dstAccountField);
		//transactionPanel.add(Box.createHorizontalStrut(15)); // a spacer
		transactionPanel.add(new JLabel("Cantidad:"));
		transactionPanel.add(amountField);

		int result = JOptionPane.showConfirmDialog(null, transactionPanel, "Complete el mensaje de transacción",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("Cuenta origen: " + srcAccountField.getText());
			System.out.println("Cuenta destino: " + dstAccountField.getText());
			System.out.println("Cantidad: " + amountField.getText());
			
			res = new TransactionMessage(srcAccountField.getText(), dstAccountField.getText(), 
					Double.valueOf(amountField.getText()), 
					"nonce", "mac");
		}
		
		return res;
	}

	// ejecución del cliente de verificación de la integridad
	public static void main(String args[]) {
		new IntegrityVerifierClient();
	}
}