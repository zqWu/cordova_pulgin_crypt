package org.wzq.android.crypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

class CryptAES extends AbstractCrypter {

	public CryptAES(String password) throws Exception{
		byte[] raw = getByteKey(password);
		init(raw);
	}

	@Override
	public byte[] encryptBytes(byte[] bytes) {
		try {
			return encryptCipher.doFinal(bytes);
			// synchronized (this.encryptCipher) {
			// return encryptCipher.doFinal(bytes);
			// }
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException("encrypt bytes fail:" + e.toString());
		} catch (BadPaddingException e) {
			throw new RuntimeException("encrypt bytes fail:" + e.toString());
		}
	}

	@Override
	public byte[] decryptBytes(byte[] src) {
		try {
			return decryptCipher.doFinal(src);
			// synchronized (this.encryptCipher) {
			// return decryptCipher.doFinal(src);
			// }
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException("decrypt bytes fail:" + e.toString());
		} catch (BadPaddingException e) {
			throw new RuntimeException("decrypt bytes fail:" + e.toString());
		}
	}
}
