package edu.gdut.service.GA;

import edu.gdut.service.reliability.AUC;
import edu.gdut.service.reliability.Common;
import edu.gdut.util.ArraysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 * 以auc值衡量适应度
 */
public class AucFitnessCal extends Common implements FitnessCal{
    private final AUC aUC;
    private final int geneLength;//基因长度
    private final Map<String, List<Double[]>> trainingData;//训练集数据
    private final List<Integer> label;//训练集标签

    public AucFitnessCal(int geneLength, Map<String, List<Double[]>> trainingData, List<Integer> label, AUC aUC) {
        this.geneLength = geneLength;
        this.trainingData = trainingData;
        this.label = label;
        this.aUC = aUC;
    }

    @Override
    public double getFitness(Individual individual) {
        byte[] genes = individual.getGenes();
        List<Double> featureWeights = new ArrayList<>();
        //将基因（权重）截取下来，转成十进制的小数
        for (int i = 0;i<genes.length;i+=geneLength){
            byte[] gene = Arrays.copyOfRange(genes, i, i+geneLength);
            double weight = ArraysUtil.toDouble(gene);
            featureWeights.add(weight);
        }

        //对训练数据集加权
        Map<String,List<Double[]>> weightedTrainingData = weightedData(trainingData, featureWeights);
        //DS合成
        Map<String,Double[]> dsResult = dsFuse(weightedTrainingData);
        //求fraud焦元的AUC值
        double fitness = aUC.auc(label, dsResult, 0);

        return fitness;
    }
}
