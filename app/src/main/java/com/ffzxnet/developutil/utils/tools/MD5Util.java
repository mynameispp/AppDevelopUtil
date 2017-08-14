package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import java.security.MessageDigest;

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
}
