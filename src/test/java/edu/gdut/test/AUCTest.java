package edu.gdut.test;

import edu.gdut.service.reliability.AUC;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-29 下午10:21
 */
public class AUCTest {

    @Test
    public void test(){
        AUC auc = new AUC();
        List<Integer> label = Arrays.asList(1,1,1,-1,-1,-1);
        Map<String, List<Double[]>> map = new HashMap<>();
        map.put("1", Arrays.asList(new Double[]{0.8,0d},new Double[]{0.1,0d}));
        map.put("2", Arrays.asList(new Double[]{0.7,0d},new Double[]{0.8,0d}));
        map.put("3", Arrays.asList(new Double[]{0.6,0d},new Double[]{0.9,0d}));
        map.put("4", Arrays.asList(new Double[]{0.5,0d},new Double[]{0.5,0d}));
        map.put("5", Arrays.asList(new Double[]{0.6,0d},new Double[]{0.85,0d}));
        map.put("6", Arrays.asList(new Double[]{0.9,0d},new Double[]{0.2,0d}));

        List<Double> aucs = auc.auc(map, label,0);
        System.out.println(aucs);
    }
}
