package edu.gdut.service.reliability;

import edu.gdut.service.Cal;
import edu.gdut.service.GA.*;
import edu.gdut.util.ArraysUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
@Service
public class CRE extends Common implements Cal {
    protected Logger log = Logger.getLogger(this.getClass());

    @Qualifier("IRE")
    @Autowired
    protected IRE ire;
    @Autowired
    protected AUC aUC;
    //下面数据的线程安全先不做了，暂时没必要
    private int genes = 7;//基因（feature）个数
    private int geneLength = 14;//单个基因转成二进制后的长度，保留4位有效数字的话，2^14够存，所以就14位吧
    private int initPopSize = 40;//初始化种群的个体数
    private int maxGenerationCount = 500;//遗传算法计算代数

    /**
     * @param genes 基因个数
     * @Description 设置基因（feature）个数
     */
    public void setGenes(int genes) {
        this.genes = genes;
    }

    /**
     * @param geneLength 单个基因转成二进制后的长度
     * @Description 设置单个基因转成二进制后的长度
     */
    public void setGeneLength(int geneLength) {
        this.geneLength = geneLength;
    }

    /**
     * @param initPopSize 初始化种群的个体数
     * @Description 设置初始化种群的个体数
     */
    public void setInitPopSize(int initPopSize) {
        this.initPopSize = initPopSize;
    }

    /**
     * @param maxGenerationCount 遗传算法计算代数
     * @Description 设置遗传算法计算代数
     */
    public void setMaxGenerationCount(int maxGenerationCount) {
        this.maxGenerationCount = maxGenerationCount;
    }

    /**
     * @param trainingData 训练集数据
     * @param label        训练集标签
     * @return 最佳权重
     * @Description 对Training数据集分别使用随机产生的权重、 IRE方法产生的权重作初始权重，运用遗传算法分别得到feature的最优权重
     */
    public List<Double> optimalWeights(Map<String, List<Double[]>> trainingData, List<Integer> label) {
        //计算训练集每个feature的fraud焦元的auc值
        final List<Double> aucList = aUC.auc(trainingData, label, 0);
        //IRE方法计算每个feature的权重
        final List<Double> ireFeatureWeights = ire.featureWeights(aucList);
        //ireFeatureWeights = Arrays.asList(0.349474,0.143842,0.168666,0.186531,0.219134,0.9965085,0.197247);
        //fitness:0.868242
        //[0.2628, 0.10145, 0.15, 0.10668, 0.1533, 0.8182, 0.2186]
        //fitness:0.8698548922056385

        //创建适应度计算类
        FitnessCal fitnessCal = new AucFitnessCal(geneLength, trainingData, label, aUC);
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
        int generationCount = 0;
        while (generationCount < maxGenerationCount) {
            myPop = GA.evolvePopulation(myPop);
            generationCount++;
            log.info("遗传算法正在运算第"+generationCount+"代");
        }

        log.info("适应度：" + myPop.getFittest().getFitness());
        Individual individual = myPop.getFittest();
        byte[] genes = individual.getGenes();
        List<Double> optimalWeights = new ArrayList<>();
        for (int i = 0; i < genes.length; i += geneLength) {
            byte[] gene = Arrays.copyOfRange(genes, i, i + geneLength);
            double weight = ArraysUtil.toDouble(gene);
            optimalWeights.add(weight);
        }
        log.info("最优权重基因组：" + optimalWeights);
        return optimalWeights;
    }

    /**
     * @param trainingData 训练集数据
     * @param label        训练集标签
     * @param testData     测试集
     * @return DS合成结果
     * @Description 利用 feature 的最优权重对 test 数据集加权， 并计算 DS 合成结果
     */
    public Map<String, Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label,
                                     Map<String, List<Double[]>> testData) {
        List<Double> optimalWeights = optimalWeights(trainingData, label);
        Map<String, List<Double[]>> weightedTestData = weightedData(testData, optimalWeights);
        Map<String, Double[]> result = dsFuse(weightedTestData);
        return result;
    }
}
