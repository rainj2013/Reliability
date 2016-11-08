package edu.gdut.util;

import java.util.List;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
public class ArraysUtil {
    /**
     * @Description 二进制数组转成0-1之间的小数
     * @param bytes 二进制数组
     * @return小数
     */
    public static double toDouble(byte[] bytes){
        String str = "";
        for(byte b : bytes){
            str+=b;
        }
        int temp = Integer.parseInt(str, 2);
        return Double.parseDouble("0."+temp);
    }

    /**
     * @Description 若干0-1之间的小数，所有小数取小数点后的数，转成二进制后存进字节数组
     * @param doubles 小数列表
     * @param bytesSize 字节数组大小
     * @return 字节数组
     */
    public static byte[] toBytes(List<Double> doubles, int bytesSize){
        String str, temp;
        byte[] bytes = new byte[bytesSize];
        int index = 0;
        for(double d : doubles){
            str =  Double.toString(d).split(".")[1];
            temp = Integer.toBinaryString(Integer.parseInt(str));
            for(char c:temp.toCharArray()){
                bytes[index++] = (byte)c;
            }
        }
        return bytes;
    }
}
