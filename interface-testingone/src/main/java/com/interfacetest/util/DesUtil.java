package com.interfacetest.util;

import java.nio.charset.Charset;


public class DesUtil {

	private static final String SKEY = "hzydbsv5";
	private static final Charset CHARSET = Charset.forName("utf-8");
	
	/**
	 * 加密
	 * @param srcStr
	 * @return
	 */
	public static String encrypt(String srcStr) {
		byte[] src = srcStr.getBytes(CHARSET);
		byte[] buf = Des.encrypt(src, SKEY);
		return Des.parseByte2HexStr(buf);
	}
	
	/**
	 * 解密
	 * @param hexStr
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String hexStr) throws Exception {
		byte[] src = Des.parseHexStr2Byte(hexStr);		
		byte[] buf = Des.decrypt(src, SKEY);		
		return new String(buf,CHARSET);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(encrypt("qwert12345"));
		System.out.println(encrypt("qwer1234"));
		System.out.println(decrypt("8A3F4CB510B720FF2E5F8B3074BA5EFF"));
	}
}
