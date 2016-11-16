package edu.gdut.service;

import edu.gdut.service.GA.*;
import edu.gdut.util.ArraysUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-11 下午3:10
 */
@Service
public class ICRE extends CRE {

    /**
     * @Description 对 Training 数据集运用遗传算法对每一个 feature 的 fraud、 unfraud 焦元寻优， 得到每一个
        feature 在 fraud、 unFraud 焦元的最优权重。
     * @param label 训练集标签
     * @param trainingData 训练集数据
     * @return 最优权重
     */
    public List<Double[]> optimalWeights(List<Integer> label, Map<String, List<Double[]>> trainingData){
        //因为有fraud/unFraud两个焦元，设置单个基因长度为单个焦元的2倍
        setGeneLength(geneLength*2);

        //计算训练集每个feature的fraud焦元的auc值
        final List<Double> fraudAucList = aUC.auc(trainingData, label, 0);
        //计算训练集每个feature的unFraud焦元的auc值
        final List<Double> unFraudAucList = aUC.auc(trainingData, label, 1);
        //IRE方法计算fraud焦元每个feature的权重
        final List<Double> ireFraudWeights = ire.featureWeights(fraudAucList);
        //IRE方法计算unFraud焦元每个feature的权重
        final List<Double> ireUnFraudWeights = ire.featureWeights(unFraudAucList);
        //创建适应度计算类
        FitnessCal fitnessCal =  new AucFitnessCal2(geneLength, trainingData, label, aUC);
        //设置适应度计算类
        Individual.setFitnessCal(fitnessCal);
        //设置总基因长度
        Individual.setDefaultGeneLength(genes*geneLength);
        //初始化种群
        byte[] initGenes = ArraysUtil.toBytes(ireFraudWeights, ireUnFraudWeights, genes*geneLength);
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
        List<Double[]> optimalWeights = new ArrayList<>();
        for (int i = 0;i<genes.length;i+=geneLength){
            byte[] fraudGene = Arrays.copyOfRange(genes, i, i+(geneLength/2));
            double fraudWeight = ArraysUtil.toDouble(fraudGene);
            byte[] unFraudGene = Arrays.copyOfRange(genes, i+(geneLength/2), i+geneLength);
            double unFraudWeight = ArraysUtil.toDouble(unFraudGene);
            optimalWeights.add(new Double[]{fraudWeight, unFraudWeight});
        }
        return optimalWeights;
    }

    /**
     * @Description 利用每一个 feature 在 fraud、 unfraud 的最优权重对 test 数据集加权， 并计算 DS 合成结果
     * @param trainingData 训练集数据
     * @param label
     * @param testData
     * @return
     */
    public Map<String,Double[]> cal(Map<String, List<Double[]>> trainingData, List<Integer> label,
                                     Map<String, List<Double[]>> testData){
        List<Double[]> optimalWeights = optimalWeights(label,trainingData);
        List<Double> fraudWeights = new ArrayList<>();
        List<Double> unFraudWeights = new ArrayList<>();
        for(Double[] weights : optimalWeights){
            fraudWeights.add(weights[0]);
            unFraudWeights.add(weights[1]);
        }
        weightedData(testData,fraudWeights, unFraudWeights);
        return dsFuse(testData);
    }
}
