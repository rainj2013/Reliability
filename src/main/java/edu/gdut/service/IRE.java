package edu.gdut.service;

import edu.gdut.service.DS.EvidenceDS;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author rainj2013 yangyujian25@gmail.com
 * @ClassName IRE
 * @Description IRE计算
 * @date 2016年10月31日 上午11:09:51
 */
public class IRE {
    private static double threshold = 0.81;

    public void setThreshold(double threshold) {
        IRE.threshold = threshold;
    }

    public List<Double> featureWeights(List<Double> aucList) {
        List<Double> featureWeights = new ArrayList<>();
        List<Double> featureRations = new ArrayList<>();

        for (double featureAuc : aucList) {
            featureRations.add(Math.pow(featureAuc, 2));
        }
        double max = max(featureRations);
        for (double featureRation : featureRations) {
            featureWeights.add(featureRation / max);
        }

        return featureWeights;
    }

    private double max(List<Double> list) {
        double listMax = Collections.max(list);
        return listMax > threshold ? listMax : threshold;
    }

    //对测试数据集进行加权
    public void calTest(Map<String, List<Double[]>> testData, List<Double> featureWeights) {
        for (Entry<String, List<Double[]>> e : testData.entrySet()) {
            List<Double[]> object = e.getValue();
            int index = 0;
            for (Double[] feature : object) {
                feature[0] *= featureWeights.get(index);
                feature[1] *= featureWeights.get(index);
                feature[2] = 1 - feature[0] - feature[1];
                index++;
            }
        }
    }

    //DS合成加权后的测试数据集
    public void DsFuse(Map<String, List<Double[]>> testData){
        HashMap<Integer,Double[]> DSResult = new HashMap<>();
        int index = 1;
        //取出每个对象
        for (Entry<String, List<Double[]>> e : testData.entrySet()) {
            List<Double[]> object = e.getValue();
            //因为DS算法类的写法原因，这里就很丑陋地先取feature集合第一个feature创建EvidenceDS对象，后面再用循环add
            Double[] feature = object.get(0);
            EvidenceDS evidenceDs = new EvidenceDS(feature[0], feature[1]);
            //从第2个feature开始用循环调用DS合成
            for (int i =1; i<object.size(); i++){
                feature = object.get(i);
                evidenceDs = evidenceDs.add(new EvidenceDS(feature[0], feature[1]));
            }
            //添加到结果集合
            DSResult.put(index, new Double[]{evidenceDs.getFraud(), evidenceDs.getUnfraud()});
            index++;
        }
    }
}
