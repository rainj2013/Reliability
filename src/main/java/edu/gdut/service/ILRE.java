package edu.gdut.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-10 下午11:13
 */
@Service
public class ILRE extends IRE{
    @Autowired
    private AUC auc;

    /**
     * @Description 对应文档的iLRE算法
     * @param trainingData 训练集数据
     * @param label 训练集标签
     * @param testData 测试数据集
     */
    public Map<String,Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label, Map<String, List<Double[]>> testData){

        //Step1： 分别计算 Training 数据集中每个 feature 的单焦元即 fraud、 unfraud 焦元对应的 AUC 值；
        List<Double> fraudAUC = auc.auc(trainingData, label, 0);//各个feature的fraud焦元的AUC值
        List<Double> unFraudAUC = auc.auc(trainingData, label, 1);//各个feature的unFraud焦元的AUC值

        //Step2： 利用 fraud、 unFraud 焦元的 AUC 值计算对应的权重
        List<Double> fraudWeight = featureWeights(fraudAUC);//各个feature的fraud焦元的权重
        List<Double> unFraudWeight = featureWeights(unFraudAUC);//各个feature的unFraud焦元的权重

        //Step3： 利用 fraud、 unFraud 焦元的权重对 test 数据集加权， 并计算DS合成结果
        weightedData(testData, fraudWeight, unFraudWeight);//利用fraud、 unFraud焦元的权重对test数据集加权
        Map<String,Double[]> result = dsFuse(testData);//DS合成

        return result;
    }
}
