package org.wzq.android.crypt;

import java.io.*;

/**
 * byte[] <=> Stream <=> String File 之间的相互转换用到的工具类
 *
 * @author wzq
 * @2014年5月5日
 */
public class IOUtils {
	/** 打印一个byte[] */
	public static String printByteArray(byte[] bytes) {
		StringBuilder sb = new StringBuilder(1024);
		for (byte b : bytes) {
			sb.append(printByte(b)) //
			        .append(" ");
		}
		return sb.toString();
	}

	/**
	 * 供测试,打印一个byte,如 0xab 的打印结果 => ab
	 */
	public static String printByte(byte b) {
		final String BYTE_FORMAT = "%02x";
		return String.format(BYTE_FORMAT, b);
	}

	/**
	 * InputStream => String
	 *
	 * @param in
	 * @param encoding
	 *            null= "UTF-8"
	 * @return
	 * @throws java.io.IOException
	 */
	public static String stream2String(InputStream in, String encoding) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final int BUFFER_SIZE = 1024;
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, data.length)) != -1) {
			outStream.write(data, 0, count);
		}
		data = null;
		if (encoding == null) {
			encoding = "UTF-8";
		}
		return new String(outStream.toByteArray(), encoding);
	}

	/**
	 * inputStream => byte[]
	 *
	 * @param is
	 * @return
	 * @throws java.io.IOException
	 */
	public static byte[] stream2Bytes(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		final int BUFFER_SIZE = 1024;
		byte[] data = new byte[BUFFER_SIZE];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		byte[] bytes = buffer.toByteArray();
		return bytes;
	}

	public static void stream2File(InputStream in, String fullPath) throws IOException {
		File file = new File(fullPath);
		stream2File(in, file);
	}

	public static void stream2File(InputStream in, File file) throws IOException {
		// mkdirs
		File dir = file.getParentFile();
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}

		//
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		final int BUFFER_SIZE = 1024;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((bytesRead = in.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.flush();
		os.close();
		in.close();
	}

	/**
	 * byte[] => file
	 *
	 * @param bytes
	 * @param file
	 *            存储的路径名称
	 * @throws java.io.IOException
	 */
	public static void bytes2File(byte[] bytes, File file) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
		bos.write(bytes);
		bos.flush();
		bos.close();
	}

	/**
	 * file => byte[]
	 *
	 * @param file
	 * @throws java.io.IOException
	 */
	public static byte[] file2Bytes(File file) throws IOException {
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
		buf.read(bytes, 0, bytes.length);
		buf.close();
		return bytes;
	}

	/**
	 * byte[] => InputStream
	 *
	 * @param in
	 * @throws Exception
	 */
	public static InputStream bytes2Stream(byte[] in) throws IOException {
		if (in == null) {
			return null;
		}
		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/**
	 * byte[] => String
	 *
	 * @param bytes
	 * @param encoding
	 *            null=utf-8
	 * @throws java.io.IOException
	 */
	public static String bytes2String(byte[] bytes, String encoding) throws IOException {
		if (bytes == null) {
			return null;
		}
		InputStream is = bytes2Stream(bytes);
		if (encoding == null) {
			encoding = "UTF-8";
		}
		return stream2String(is, encoding);
	}

	public static byte[] string2Bytes(String str, String encoding) throws IOException {
		if (str == null) {
			return null;
		}
		if (encoding == null) {
			encoding = "UTF-8";
		}
		return str.getBytes(encoding);
	}

	public static InputStream string2Stream(String str, String encoding) throws IOException {
		if (str == null) {
			return null;
		}
		byte[] bytes = string2Bytes(str, encoding);
		return bytes2Stream(bytes);
	}

	/**
	 * 拷贝byte数组
	 */
	public static byte[] copyBytes(byte[] src) {
		byte[] copy = new byte[src.length];
		for (int i = 0; i < src.length; i++) {
			copy[i] = src[i];
		}
		return copy;
	}

	/**
	 * byte[] => hex string <br/>
	 * each byte covert to 2 hex char eg byte(0xff) => string (ff)
	 */
	public static String bytes2HexString(byte[] cryptBytes) {
		int len = cryptBytes.length;
		StringBuffer sb = new StringBuffer(len * 2);
		for (int i = 0; i < len; i++) {
			sb.append(String.format("%02x", cryptBytes[i]));
		}
		return sb.toString();
	}

	/** hex String => byte[] eg. String(ff) => byte(0xff) */
	public static byte[] hexString2Bytes(String str) {
		byte[] arrB = str.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
}
