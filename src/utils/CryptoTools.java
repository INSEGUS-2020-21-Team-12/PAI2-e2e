package utils;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTools {
	
	public static String calculateHMAC(String data, Integer secretKey, String algorithmName) 
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
			
			// bytesSecretKey = BigInteger.valueOf(secretKey).toByteArray()
		// encodedKey = Base64.getEncoder().encodeToString(bytesSecretKey)
		
		Mac macGenerator = Mac.getInstance(algorithmName);
		
		byte[] secretKeyBytes =  BigInteger.valueOf(secretKey).toByteArray();
		SecretKeySpec hmacSecretKey = new SecretKeySpec(secretKeyBytes, algorithmName);
		
		
		
		macGenerator.init(hmacSecretKey);
		macGenerator.update(data.getBytes());
		
		String base64HMAC = Base64.getEncoder().encodeToString(macGenerator.doFinal());
		return base64HMAC;
	}

}
