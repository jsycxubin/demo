package com.jshx.util;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sign {
	
	 private Sign() {
		    throw new IllegalStateException("Sign class");
	 }
	private static final Logger logger=LoggerFactory.getLogger(Sign.class);
  /*  public static void main(String[] args) {
        String jsapi_ticket = "jsapi_ticket";

        // 注意 URL 一定要动态获取，不能 hardcode
        String url = "http://example.com";
        Map<String, String> ret = sign(jsapi_ticket, url);
        for (Map.Entry entry : ret.entrySet()) {
        	logger.info(entry.getKey() + ", " + entry.getValue());
        }
    };*/

    public static Map<String, String> sign(String jsapiticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String noncestr = createnoncestr();
        String timestamp = createtimestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapiticket +
                  "&noncestr=" + noncestr +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        logger.info(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (Exception e)
        {
        	logger.error("Exception",e);
            //e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiticket);
        ret.put("nonceStr", noncestr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createnoncestr() {
        return UUID.randomUUID().toString();
    }

    private static String createtimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
