package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MD5Util
 * @Description TODO MD5加密
 * @author li.biao
 * @date 2015-4-3
 */
public class MD5Util {
	private static char hexDigits[] = { '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * 密码加密MD5
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String password){
		if(TextUtils.isEmpty(password)){
			return "";
		}else{
			try{
				byte[] btInput = password.getBytes();
			    //获得MD5摘要算法的 MessageDigest 对象
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
			     //使用指定的字节更新摘要
				mdInst.update(btInput);
			     //获得密文
	            byte[] md = mdInst.digest();
	            //把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str).toLowerCase();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * md5 32 小写
	 *
	 * @param text
	 * @return
	 */
	public static String encode2(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
