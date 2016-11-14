package edu.gdut.util;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-14 下午10:47
 */
import java.util.UUID;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
