package edu.gdut.service;

import edu.gdut.service.GA.*;
import edu.gdut.util.ArraysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
@Component
public class CRE extends Common{
    @Autowired
    private IRE ire;
    @Autowired
    private AUC auc;
    private final int genes = 7;//基因（feature）个数
    private final int geneLength = 14;//单个基因转成二进制后的长度，保留4位有效数字的话，2^14够存，所以就14位吧
    private final int initPopSize = 40;//初始化种群的个体数
    private final int maxGenerationCount = 5000;//遗传算法计算代数

    /**
     * @Description 对Training数据集分别使用随机产生的权重、 IRE方法产生的权重作初始权重，运用遗传算法分别得到feature的最优权重
     * @param trainingData 训练集数据
     * @param label 训练集标签
     * @return 最佳权重
     */
    public List<Double> optimalWeights(Map<String, List<Double[]>> trainingData, List<Integer> label){
        //计算训练集每个feature的fraud焦元的auc值
        final List<Double> aucList = auc.auc(trainingData, label, 0);
        //IRE方法计算每个feature的权重
        final List<Double> ireFeatureWeights = ire.featureWeights(aucList);
        //创建适应度计算类
        FitnessCal fitnessCal =  new AucFitnessCal(geneLength, trainingData, label);
        //设置适应度计算类
        Individual.setFitnessCal(fitnessCal);
        //设置总基因长度
        Individual.setDefaultGeneLength(genes*geneLength);
        //初始化种群
        byte[] initGenes = ArraysUtil.toBytes(ireFeatureWeights, genes*geneLength);
        List<byte[]> initPop = new ArrayList<>();
        for(int i = 0; i<initPopSize; i++){
            initPop.add(initGenes);
        }
        Population myPop = new Population(initPop);
        //开始产生下一代，并进行进化
        List<Population> pList = new ArrayList<>();
        int generationCount = 0;
        while (generationCount < maxGenerationCount) {
            myPop = GA.evolvePopulation(myPop);
            pList.add(myPop);
            generationCount++;
        }
        //倒序排
        Collections.sort(pList, new Comparator<Population>() {
            @Override
            public int compare(Population o1, Population o2) {
                if (o1.getFittest().getFitness() > o2.getFittest().getFitness())
                    return -1;
                return 1;
            }
        });
        Population pop = pList.get(0);
        Individual individual = pop.getFittest();
        byte[] genes = individual.getGenes();
        List<Double> optimalWeights = new ArrayList<>();
        for (int i = 0;i<genes.length;i+=geneLength){
            byte[] gene = Arrays.copyOfRange(genes, i, i+geneLength);
            double weight = ArraysUtil.toDouble(gene);
            optimalWeights.add(weight);
        }
        return optimalWeights;
    }

    /**
     * @Description 利用 feature 的最优权重对 test 数据集加权， 并计算 DS 合成结果
     * @param trainingData 训练集数据
     * @param label 训练集标签
     * @param testData  测试集
     * @return DS合成结果
     */
    public Map<String, Double[]> cre(Map<String, List<Double[]>> trainingData, List<Integer> label,
                                           Map<String, List<Double[]>> testData){
        List<Double> optimalWeights =  optimalWeights(trainingData,label);
        weightedData(testData, optimalWeights);
        Map<String, Double[]> result = dsFuse(testData);
        return result;
    }
}
