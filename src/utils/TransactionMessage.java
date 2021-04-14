package utils;

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

}
