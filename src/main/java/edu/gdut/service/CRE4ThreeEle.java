package edu.gdut.service;

import edu.gdut.service.GA.*;
import edu.gdut.util.ArraysUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-20 下午8:24
 */
@Service
public class CRE4ThreeEle extends Common implements Cal{
    protected Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private IRE4ThreeEle ire4ThreeEle;

    private int genes = 7;//基因（feature）个数
    private int geneLength = 14;//单个基因转成二进制后的长度，保留4位有效数字的话，2^14够存，所以就14位吧
    private int initPopSize = 40;//初始化种群的个体数
    private int maxGenerationCount = 500;//遗传算法计算代数

    private List<Double> optimalWeights(Map<String, List<Double[]>> trainingData, List<Integer> label){
        //用IRE方法产生初始权重
        List<Double[]> labels = ire4ThreeEle.labelsSwitch(label);
        List<Double> jds = ire4ThreeEle.jDistance(labels, trainingData);
        Map<String, List<Double[]>> decisions = ire4ThreeEle.decision(trainingData);
        List<Double> dds = ire4ThreeEle.dDistance(labels, decisions);
        List<Double> ireFeatureWeights = ire4ThreeEle.featureWeights(dds, jds);
        //创建适应度计算类
        FitnessCal fitnessCal = new DistanceFitnessCal(geneLength, trainingData, label, ire4ThreeEle);
        //设置适应度计算类
        Individual.setFitnessCal(fitnessCal);
        //设置总基因长度
        Individual.setDefaultGeneLength(genes * geneLength);
        //初始化种群
        byte[] initGenes = ArraysUtil.toBytes(ireFeatureWeights, genes * geneLength);
        List<byte[]> initPop = new ArrayList<>();
        for (int i = 0; i < initPopSize; i++) {
            initPop.add(initGenes);
        }
        Population myPop = new Population(initPop);
        //开始产生下一代，并进行进化
        List<Population> pList = new ArrayList<>();
        int generationCount = 0;
        while (generationCount < maxGenerationCount) {
            myPop = GA.evolvePopulation(myPop);
            generationCount++;
            pList.add(myPop);
            log.info("遗传算法正在运算第"+generationCount+"代");
        }
        //倒序排
        Collections.sort(pList, new PopComparator(true));
        Population pop = pList.get(0);
        Individual individual = pop.getFittest();
        byte[] genes = individual.getGenes();
        //取出最优权重
        List<Double> optimalWeights = new ArrayList<>();
        for (int i = 0; i < genes.length; i += geneLength) {
            byte[] gene = Arrays.copyOfRange(genes, i, i + geneLength);
            double weight = ArraysUtil.toDouble(gene);
            optimalWeights.add(weight);
        }
        return optimalWeights;
    }

    @Override
    public Map<String, Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label,
                                     Map<String, List<Double[]>> testData) {
        List<Double> optimalWeights = optimalWeights(trainingData, label);
        Map<String, List<Double[]>> weightedTestData = weightedData(testData, optimalWeights);
        Map<String, Double[]> dsResult = dsFuse(weightedTestData);
        return dsResult;
    }
}
