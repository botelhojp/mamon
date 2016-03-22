package br.pucpr.ppgia.prototipo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;

import javax.crypto.Cipher;

public class Crypto {

	private static final String signatureAlgorithm = "MD5withRSA";

	public static PrivateKey getPrivateKeyFromFile(File cert, String alias,
			String password) throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");
		char[] pwd = password.toCharArray();
		InputStream is = new FileInputStream(cert);
		ks.load(is, pwd);
		is.close();

		Key key = ks.getKey(alias, pwd);
		if (key instanceof PrivateKey) {
			return (PrivateKey) key;
		}
		return null;
	}

	/**
	 * Extrai a chave pública do arquivo.
	 */
	public static PublicKey getPublicKeyFromFile(File cert, String alias,
			String password) throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");
		char[] pwd = password.toCharArray();
		InputStream is = new FileInputStream(cert);
		ks.load(is, pwd);
		//Key key = ks.getKey(alias, pwd);
		Certificate c = ks.getCertificate(alias);
		PublicKey p = c.getPublicKey();
		return p;
	}

	
	public static byte[] crypt(PrivateKey key, byte[] buffer) throws Exception {		
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.ENCRYPT_MODE, key );
	    return cipher.doFinal(buffer);

	}
	
	public static boolean verifySignature(PublicKey key, byte[] buffer, byte[] signed) throws Exception {
		Signature sig = Signature.getInstance(signatureAlgorithm);
		sig.initVerify(key);
		sig.update(buffer, 0, buffer.length);
		return sig.verify(signed);
	}

	
	
	public static byte[] decrypt(PublicKey publicKey, byte[] signed){
		byte[] decryptBytes = null;
		try {
			Cipher dcipher = Cipher.getInstance("RSA");
			dcipher.init(Cipher.DECRYPT_MODE, publicKey);
			decryptBytes  = dcipher.doFinal(signed);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return decryptBytes;
	}

	public static void main(String[] args) {
		String txt = "Mensagem de Testes.";
		try {
			File cert = new File("C:/guj.jks");
			String alias = "guj";
			String pwd = "guj123";
			
			PrivateKey privateKey = getPrivateKeyFromFile(cert, alias, pwd);
			PublicKey publicKey = getPublicKeyFromFile(cert, alias, pwd);
			
			byte[] txtAssinado = crypt(privateKey, txt.getBytes());
			System.out.println("encriptado: " + txt2Hexa(txtAssinado));
			
			byte[] txtDescrypt = decrypt(publicKey, txtAssinado);
			System.out.println("decriptaado: " + new String(txtDescrypt, "UTF-8"));  

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String txt2Hexa(byte[] bytes) {
		if (bytes == null)
			return null;
		String hexDigits = "0123456789abcdef";
		StringBuffer sbuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int j = ((int) bytes[i]) & 0xFF;
			sbuffer.append(hexDigits.charAt(j / 16));
			sbuffer.append(hexDigits.charAt(j % 16));
		}
		return sbuffer.toString();
	}

}
