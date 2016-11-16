package edu.gdut.test;

import edu.gdut.util.XlsUtil;
import jxl.read.biff.BiffException;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
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

    @Test
    public void writeTest(){
        try {
            OutputStream out = new FileOutputStream("/home/rainj2013/桌面/write.xls");
            Map<String, Double[]> data = new HashMap<>();
            data.put("1",new Double[]{1.0,0.0,0.0,1.0});
            data.put("2",new Double[]{1.0111111111111,0.0,0.0,1.0});
            data.put("3",new Double[]{1.0,0.0,0.011111111,0.1});
            XlsUtil.writeXls(out, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
