package org.wzq.android.crypt;

public interface ICrypt {
    
	byte[] encryptBytes(byte[] bytes) throws Exception;

	byte[] decryptBytes(byte[] bytes) throws Exception;
}
