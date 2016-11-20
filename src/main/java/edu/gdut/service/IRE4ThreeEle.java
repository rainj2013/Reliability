package edu.gdut.service;

import Jama.Matrix;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-19 下午3:41
 * @Description IRE算法在3个焦元的情况下的运用
 */
@Service
public class IRE4ThreeEle extends Common implements Cal {

    private static double threshold = 0.81;
    public void setThreshold(double threshold) {
        IRE4ThreeEle.threshold = threshold;
    }

    /**
     * @param labels       训练集标签
     * @param trainingData 训练集数据
     * @return 训练集中所有Object的JD向量的“平均值”
     * @Description 计算 Training数据集中所有feature与其Label的Jousselme距离(JD)，每个feature对应一个jd，所以每个Object对应
     * 一个JD向量
     */
    List<Double> jDistance(List<Double[]> labels, Map<String, List<Double[]>> trainingData) {
        List<Double> jDistance = new ArrayList<>();

        //计算每个feature与label的jd
        List<Double[]> jDistances = new ArrayList<>();
        int featureSize = trainingData.get("1").size();
        Double[] feature, label;
        List<Double[]> features;
        Matrix m1, m2, m3;
        //直接写死了，三类问题(fraud,unFraud,unC)的D矩阵固定是这样的
        Matrix D = new Matrix(new double[][]{{1, 0, 0.5}, {0, 1, 0.5}, {0.5, 0.5, 1}});
        for (int objectId = 1; objectId <= labels.size(); objectId++) {
            //取出每个对象
            features = trainingData.get(Integer.toString(objectId));
            Double[] jds = new Double[featureSize];
            for (int featureId = 0; featureId < featureSize; featureId++) {
                //取出每个feature
                feature = features.get(featureId);
                label = labels.get(objectId - 1);

                m1 = new Matrix(new double[][]{{feature[0]}, {feature[1]}, {feature[2]}});
                m2 = new Matrix(new double[][]{{label[0]}, {label[1]}, {label[2]}});
                m3 = m1.minus(m2);
                double jd = m3.transpose().times(D).times(m3.transpose()).get(0, 0);
                jd = Math.sqrt(0.5 * jd);
                jds[featureId] = jd;
            }
            jDistances.add(jds);
        }

        //计算所有Object的jd向量的“平均值”
        Double[] jds = new Double[featureSize];
        Arrays.fill(jds, 0);
        //统计所有向量的和
        for (Double[] objectJds : jDistances) {
            for (int index = 0; index < featureSize; index++) {
                jds[index] = jds[index] + objectJds[index];
            }
        }
        //除以object的个数
        for (int index = 0; index < featureSize; index++) {
             jDistance.add(jds[index]/trainingData.size());
        }

        return jDistance;
    }

    /**
     * @Description 计算数据集的决策
     * @param data 数据集
     */
    void decision(Map<String, List<Double[]>> data){
        for (int objectId = 1; objectId<=data.size(); objectId++){
            List<Double[]> features = data.get(Integer.toString(objectId));
            DecimalFormat df = new DecimalFormat("######0.0000");

            for (Double[] feature : features){

                //做个保留四位小数先吧
                for(int index = 1; index<feature.length; index++){
                    feature[index] = Double.parseDouble(df.format(feature[index]));
                }

                //获取最大的数的坐标
                int maxIndex = 0;
                for(int index = 1; index<feature.length; index++){
                    if (feature[index]>feature[maxIndex]){
                        maxIndex = index;
                    }
                }

                //选择决策坐标
                for(int index = 0; index<feature.length; index++){
                    if (index==maxIndex)
                        continue;
                    else if(feature[index].equals(feature[maxIndex]))
                        maxIndex = -1;
                }

                //处理决策值
                Arrays.fill(feature, 0d);
                if (maxIndex!=-1){
                    feature[maxIndex] = 1d;
                }

            }
        }
    }

    List<Double> dDistance(List<Double[]> labels, Map<String, List<Double[]>> trainingData){
        List<Double> dDistance = new ArrayList<>();

        //计算每个feature与label的jd
        List<Double[]> dDistances = new ArrayList<>();
        int featureSize = trainingData.get("1").size();
        Double[] feature, label;
        List<Double[]> features;
        Matrix m1, m2, m3;
        for (int objectId = 1; objectId <= labels.size(); objectId++) {
            //取出每个对象
            features = trainingData.get(Integer.toString(objectId));
            Double[] dDs = new Double[featureSize];
            for (int featureId = 0; featureId < featureSize; featureId++) {
                //取出每个feature
                feature = features.get(featureId);
                label = labels.get(objectId - 1);

                m1 = new Matrix(new double[][]{{feature[0]}, {feature[1]}, {feature[2]}});
                m2 = new Matrix(new double[][]{{label[0]}, {label[1]}, {label[2]}});
                m3 = m1.minus(m2);
                dDs[featureId] = 0.5*m3.transpose().times(m3).get(0,0);
            }
            dDistances.add(dDs);
        }

        //计算所有Object的jd向量的“平均值”
        Double[] jds = new Double[featureSize];
        Arrays.fill(jds, 0);
        //统计所有向量的和
        for (Double[] objectJds : dDistances) {
            for (int index = 0; index < featureSize; index++) {
                jds[index] = jds[index] + objectJds[index];
            }
        }
        //除以2倍的object的个数
        for (int index = 0; index < featureSize; index++) {
            dDistance.add(jds[index]/(trainingData.size()*2));
        }
        return dDistance;
    }

    List<Double> featureWeights(List<Double> dd, List<Double> jd){
        List<Double> discountingFactor = new ArrayList<>();
        for (int i = 0; i < dd.size(); i++){
            discountingFactor.add(0.5*(dd.get(i)+jd.get(i)));
        }
        for (int i = 0; i<discountingFactor.size(); i++){
            double fr = Math.pow((1-discountingFactor.get(i)),2);
            discountingFactor.set(i, fr);
        }
        double max = max(discountingFactor);
        for (int i = 0; i<discountingFactor.size(); i++){
            double fr = discountingFactor.get(i);
            discountingFactor.set(i, fr/max);
        }
        return  discountingFactor;
    }

    private double max(List<Double> list) {
        double listMax = Collections.max(list);
        return listMax > threshold ? listMax : threshold;
    }

    @Override
    public Map<String, Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label,
                                     Map<String, List<Double[]>> testData) {
        List<Double[]> labels = labelsSwitch(label);
        //Step1： 计算 Training 数据集中所有 feature 与其 Label 的 Jousselme 距离(JD)
        List<Double> jd = jDistance(labels, trainingData);
        //Step2： Training 数据集处理， 得到 Training 数据的决策
        decision(trainingData);
        //Step3： 根据 Training 数据的决策， 计算 Training 数据集中所有 feature 与其 Label 的决策距离(DD)
        List<Double> dd = dDistance(labels, trainingData);
        //Step4： 利用两种距离计算对应的 featureWeights， 公式如下： 其中阈值为 0.81(可调节)
        List<Double> featureWeights = featureWeights(dd, jd);
        //Step5： 利用 featureWeights 对 test 数据集加权， 并计算 DS 合成结果
        Map<String, List<Double[]>> weightedTestData = weightedData(testData, featureWeights);
        return dsFuse(weightedTestData);
    }

    List<Double[]> labelsSwitch(List<Integer> label){
        List<Double[]> labels = new ArrayList<>();
        for (int i = 0; i<label.size(); i++){
            Double[] doubles = new Double[3];
            Arrays.fill(doubles, 0);
            if(label.get(i)==1){
                doubles[0] = 1d;
            }else{
                doubles[1] = 1d;
            }
            labels.add(doubles);
        }
        return labels;
    }

}
