package com.wsq.library.algorithm.jianzhi1;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class HmacSHA1 {

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText 被签名的字符串
	 * @param encryptKey  密钥
	 * @return 返回被加密后的字符串
	 * @throws Exception
	 */
	public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
		byte[] data = encryptKey.getBytes("UTF-8");

		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");

		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance("HmacSHA1");

		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes("UTF-8");

		// 完成 Mac 操作
		byte[] finalText = mac.doFinal(text);

		StringBuilder sBuilder = bytesToHexString(finalText);
		return sBuilder.toString().toUpperCase();
	}

	/**
	 * 转换成Hex, 方法1
	 * 
	 * @param bytesArray
	 */
	public static StringBuilder bytesToHexString(byte[] bytesArray) {
		if (bytesArray == null) {
			return null;
		}
		StringBuilder sBuilder = new StringBuilder();
		for (byte b : bytesArray) {
			String hv = String.format("%02x", b);
			sBuilder.append(hv);
		}
		return sBuilder;
	}

	/**
	 * 转换成Hex, 方法2
	 * 
	 * @param bytesArray
	 */
	public static StringBuilder bytesToHexString2(byte[] bytesArray) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; bytesArray != null && n < bytesArray.length; n++) {
			stmp = Integer.toHexString(bytesArray[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs;
	}
	
	/**
	 * hmac+签名算法 加密
	 * @param content  内容
	 * @param charset  字符编码
	 * @param key	         加密秘钥
	 * @param hamaAlgorithm hamc签名算法名称:例如HmacMD5,HmacSHA1,HmacSHA256 
	 * @return
	 */
	public static String getHmacSign(String content, String charset,String key,String hamaAlgorithm){
		byte[] result = null;  
        try {  
            //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称    
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), hamaAlgorithm);  
            //生成一个指定 Mac 算法 的 Mac 对象    
            Mac mac = Mac.getInstance(hamaAlgorithm);  
            //用给定密钥初始化 Mac 对象    
            mac.init(signinKey);  
            //完成 Mac 操作     
            byte[] rawHmac;
			rawHmac = mac.doFinal(content.getBytes(charset));
            result = Base64.encodeBase64(rawHmac);  
  
        } catch (NoSuchAlgorithmException e) {  
            System.err.println(e.getMessage());  
        } catch (InvalidKeyException e) {  
            System.err.println(e.getMessage());  
        }  catch (IllegalStateException | UnsupportedEncodingException e) {
        	System.err.println(e.getMessage());
		} 
        if (null != result) {  
            return new String(result);  
        } else {  
            return null;
        }
    }

	public static void main(String[] args) {
		try {
			String encryptText = "POST&https%3A%2F%2Fapi.twitter.com%2Foauth%2Frequest_token&oauth_callback%3Dhttp%253A%252F%252Fwww.baidu.com%26oauth_consumer_key%3DOPdUgJ7LTOx2WpFHSSEOpk1VE%26oauth_nonce%3D1122334%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1649746894990%26oauth_version%3D1.0";
//			String encryptText = "GET&https://twitter.com/oauth/request_token&oauth_consumer_key=7FVlREOKCuQLzukCSSdQdYhbD&oauth_callback=http://localhost:8080/Twitter_sso/index.jsp&oauth_nonce=-1111514832&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1490154283&oauth_version=1.0";
			String encryptKey = "5FZqwuNujPJhxzV54nPZAQqbZFhZqn9THKLCQeELgxBY1d2rDB&";
//			System.out.println(HmacSHA1.HmacSHA1Encrypt(encryptText, encryptKey));
			System.out.println(HmacSHA1.getHmacSign(encryptText, "UTF-8", encryptKey, "HmacSHA1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
