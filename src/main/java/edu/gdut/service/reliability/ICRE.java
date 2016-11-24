package edu.gdut.service.reliability;

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

    private int genes = 7;//基因（feature）个数
    private int geneLength = 28;//单个焦元基因转成二进制后的长度，保留4位有效数字的话，2^14够存，每个基因2个焦元，2*14
    private int initPopSize = 40;//初始化种群的个体数
    private int maxGenerationCount = 500;//遗传算法计算代数

    /**
     * @Description 对 Training 数据集运用遗传算法对每一个 feature 的 fraud、 unfraud 焦元寻优， 得到每一个
        feature 在 fraud、 unFraud 焦元的最优权重。
     * @param label 训练集标签
     * @param trainingData 训练集数据
     * @return 最优权重
     */
    public List<Double[]> optimalWeights(List<Integer> label, Map<String, List<Double[]>> trainingData){
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
            log.info("遗传算法正在运算第"+generationCount+"代");
            generationCount++;
        }
        //倒序排
        Collections.sort(pList, Population::compareTo);
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
        Map<String, List<Double[]>> weightedTestData = weightedData(testData,fraudWeights, unFraudWeights);
        return dsFuse(weightedTestData);
    }
}
