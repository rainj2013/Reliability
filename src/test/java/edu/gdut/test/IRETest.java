package edu.gdut.test;

import edu.gdut.service.reliability.AUC;
import edu.gdut.service.reliability.IRE;
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
 * Date:  16-11-14 下午7:58
 */
public class IRETest {
    @Test
    public void test(){
        IRE ire = new IRE();
        AUC auc = new AUC();
        try {
            List<Double> aucList = auc.auc(XlsUtil.readData("/home/rainj2013/桌面/test.xls", 5, 1), XlsUtil.readLabel("/home/rainj2013/桌面/test.xls", 3),0);
            List<Double> featureWeights = ire.featureWeights(aucList);
            Map<String ,List<Double[]>> map = XlsUtil.readData("/home/rainj2013/桌面/test.xls", 5, 1);
            ire.weightedData(map, featureWeights);
            Map<String,Double[]> dsResult = ire.dsFuse(map);
            for (int i = 1; i<=1401; i++){
                System.out.println(i+"  "+ Arrays.toString(dsResult.get(Integer.toString(i))));
            }

        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
