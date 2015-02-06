package org.wzq.android.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class AbstractCrypter implements ICrypt {
	public static final String CHARSET_UTF8 = "UTF-8";// const

	protected Cipher encryptCipher = null;// 加密
	protected Cipher decryptCipher = null;// 解密

	protected byte[] getByteKey(String key) throws Exception {
		byte[] raw = null;
		try {
			byte[] keyb = key.getBytes(CHARSET_UTF8);
			MessageDigest md = MessageDigest.getInstance("MD5");
			raw = md.digest(keyb);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Crypter init:" + e.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Crypter init:" + e.toString());
		}
		return raw;
	}

	/** init Cipher by password string password ==> md5 md5 ==> init AES cipher */
	protected void init(byte[] key) {
		try {
			SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
			//
			encryptCipher = Cipher.getInstance("AES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			//
			decryptCipher = Cipher.getInstance("AES");
			decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Crypter init:" + e.toString());
		} catch (InvalidKeyException e) {
			throw new RuntimeException("Crypter init:" + e.toString());
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException("Crypter init:" + e.toString());
		}
	}

	final public String encryptString(String str) throws Exception {
		try {
			// 明文String => byte[] ==加密==> byte[] =>String
			byte[] srcBytes = str.getBytes(CHARSET_UTF8);
			byte[] dstBytes = encryptBytes(srcBytes);
			return IOUtils.bytes2HexString(dstBytes);
		} catch (IOException e) {
			throw new RuntimeException("encrypt string fail:" + e.toString());
		}
	}

	final public String decryptString(String str) throws Exception {
		try {
			byte[] encryptBytes = IOUtils.hexString2Bytes(str);
			byte[] decryptBytes = decryptBytes(encryptBytes);
			return new String(decryptBytes, CHARSET_UTF8);
		} catch (IOException e) {
			throw new RuntimeException("decrypt string fail:" + e.toString());
		}
	}

	final public InputStream encryptStream(InputStream ins) throws Exception {
		try {
			// InputStream => ByteArray => encrypt bytes => InputStream
			byte[] bytes = IOUtils.stream2Bytes(ins);
			byte[] encryptBytes = encryptBytes(bytes);
			return IOUtils.bytes2Stream(encryptBytes);
		} catch (IOException e) {
			throw new RuntimeException("encrypt stream fail:" + e.toString());
		}
	}

	final public InputStream decryptStream(InputStream ins) throws Exception {
		try {
			// InputStream => ByteArray => decrypt bytes => InputStream
			byte[] bytes = IOUtils.stream2Bytes(ins);
			byte[] decryptBytes = decryptBytes(bytes);
			return IOUtils.bytes2Stream(decryptBytes);
		} catch (IOException e) {
			throw new RuntimeException("decrypt stream fail:" + e.toString());
		}
	}
}
