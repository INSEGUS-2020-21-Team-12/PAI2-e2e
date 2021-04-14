package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.DoubleToLongFunction;

public class TransactionMessage {
	
	private String sourceAccount;
	private String destinationAccount;
	private Double amount;
	private String nonce;
	
	private String mac; // message authentication code

	public TransactionMessage(String sourceAccount, String destinationAccount, Double amount, String nonce,
			String mac) {
		super();
		this.sourceAccount = sourceAccount;
		this.destinationAccount = destinationAccount;
		this.amount = amount;
		this.nonce = nonce;
		this.mac = mac;
	}
	
	public static void send(TransactionMessage transaction, PrintWriter output) {
		output.println(transaction.getSourceAccount());
		output.println(transaction.getDestinationAccount());
		output.println(transaction.getAmount());
		output.println(transaction.getNonce());
		output.println(transaction.getMac());
	}
	
	public static TransactionMessage receive(BufferedReader input) throws IOException {
		String sourceAccount = input.readLine();
		String destinationAccount = input.readLine();
		Double amount = Double.parseDouble(input.readLine());
		String nonce = input.readLine();
		String mac = input.readLine();
		
		return new TransactionMessage(sourceAccount, destinationAccount, amount, nonce, mac);
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public String getDestinationAccount() {
		return destinationAccount;
	}

	public Double getAmount() {
		return amount;
	}

	public String getNonce() {
		return nonce;
	}

	public String getMac() {
		return mac;
	}

	@Override
	public String toString() {
		return "TransactionMessage [sourceAccount=" + sourceAccount + ", destinationAccount=" + destinationAccount
				+ ", amount=" + amount + ", nonce=" + nonce + ", mac=" + mac + "]";
	}
	
	
	
	

}
