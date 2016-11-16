package edu.gdut.util;

import java.util.ArrayList;
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
            str =  Double.toString(d).split("\\.")[1].substring(0,4);
            temp = Long.toBinaryString(Long.parseLong(str));
            while (temp.length()<bytesSize/doubles.size()){
                temp = "0"+temp;
            }
            for(char c:temp.toCharArray()){
                if (c=='0')
                    bytes[index] = 0;
                else
                    bytes[index] = 1;
                index++;
            }
        }
        return bytes;
    }

    /**
     * @Description 若干0-1之间的小数，所有小数取小数点后的数，转成二进制后存进字节数组
     * @param doubles0 小数列表
     * @param doubles0 另一个小数列表
     * @param bytesSize 字节数组大小
     * @return 字节数组
     */
    //啊啊啊不想写啦这个方法丑死我吧
    public static byte[] toBytes(List<Double> doubles0, List<Double> doubles1, int bytesSize){
        double d0 ,d1;

        List<Double> mix = new ArrayList<>();
        for (int i = 0; i< doubles0.size(); i++){
            mix.add(doubles0.get(i));
            mix.add(doubles1.get(i));
        }

        byte[] bytes = new byte[bytesSize];
        int index = 0;
        String str;
        for(int i = 0;i<mix.size();i+=2){
            d0 = doubles0.get(i);
            d1 = doubles1.get(i);

            str =  Double.toString(d0).split("\\.")[1];
            str = Long.toBinaryString(Long.parseLong(str));
            for(char c:str.toCharArray()){
                bytes[index++] = (byte)c;
            }

            str =  Double.toString(d1).split("\\.")[1];
            str = Long.toBinaryString(Long.parseLong(str));
            for(char c:str.toCharArray()){
                bytes[index++] = (byte)c;
            }

        }
        return bytes;
    }

}
