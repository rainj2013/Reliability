package edu.gdut.service;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author rainj2013 yangyujian25@gmail.com
 * @ClassName IRE
 * @Description IRE计算
 * @date 2016年10月31日 上午11:09:51
 */
@Component
public class IRE extends Common{
    private static double threshold = 0.81;

    public void setThreshold(double threshold) {
        IRE.threshold = threshold;
    }

    /**
     * @Description 用IRE方法产生权重
     * @param aucList 每个feature的AUC值
     * @return 每个feature的权重
     */
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
}
