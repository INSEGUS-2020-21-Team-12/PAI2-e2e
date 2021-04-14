package utils;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTools {
	
	public static String calculateHMAC(String data, Integer secretKey, String algorithmName) {
			
			// bytesSecretKey = BigInteger.valueOf(secretKey).toByteArray()
		// encodedKey = Base64.getEncoder().encodeToString(bytesSecretKey)
		String base64HMAC = "";
		try {
			Mac macGenerator = Mac.getInstance(algorithmName);
		
			byte[] secretKeyBytes =  BigInteger.valueOf(secretKey).toByteArray();
			SecretKeySpec hmacSecretKey = new SecretKeySpec(secretKeyBytes, algorithmName);

			macGenerator.init(hmacSecretKey);
			macGenerator.update(data.getBytes());
		
			base64HMAC = Base64.getEncoder().encodeToString(macGenerator.doFinal());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return base64HMAC;
	}
	
	public static String generateNonce() {
		byte[] randomBytes = new byte[12];
        new SecureRandom().nextBytes(randomBytes);
        
        StringBuilder result = new StringBuilder();
        for (byte temp : randomBytes) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
	}

}
