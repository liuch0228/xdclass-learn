package net.xdclass.xdvideo.utils;

import sun.swing.plaf.synth.DefaultSynthStyle;

import java.security.MessageDigest;
import java.util.UUID;

/**
 *
 */
public class CommonUtils {
    /**
     * 生成UUID 订单唯一标识
     *
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * MD5工具
     * @param data
     * @return
     */
    public static String MD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //生成摘要
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
