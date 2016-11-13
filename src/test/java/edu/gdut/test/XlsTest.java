package edu.gdut.test;

import edu.gdut.util.XlsUtil;
import jxl.read.biff.BiffException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-13 下午9:59
 */
public class XlsTest {
    @Test
    public void readDataTest() {
        try {
            Map<String, List<Double[]>> map =  XlsUtil.readData("/home/rainj2013/桌面/test.xls", 0, 7);
            for (int i = 1;i<=map.size();i++){
                List<Double[]> list = map.get(Integer.toString(i));
                for (Double[] d : list){
                    System.out.println(i + Arrays.toString(d));
                }
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readLabelTest(){
        try {
            List<Integer> labels = XlsUtil.readLabel("/home/rainj2013/桌面/test.xls", 1);
            for (int i = 0;i<labels.size();i++){
                System.out.println(labels.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }
}
