package edu.gdut.service.GA;

import edu.gdut.service.AUC;
import edu.gdut.util.ArraysUtil;
import org.springframework.beans.factory.annotation.Autowired;

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
public class AucFitness extends Common implements FitnessCal{
    @Autowired
    private AUC auc;

    private final int geneLength;//基因长度
    private final Map<String, List<Double[]>> trainingData;//训练集数据
    private final List<Integer> label;//训练集标签

    public AucFitness(int geneLength, Map<String, List<Double[]>> trainingData, List<Integer> label) {
        this.geneLength = geneLength;
        this.trainingData = trainingData;
        this.label = label;
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
        weightedData(trainingData, featureWeights);
        //DS合成
        Map<String,Double[]> dSResult = dsFuse(trainingData);
        //求fraud焦元的AUC值
        double fitness = auc.auc(label, dSResult);

        return fitness;
    }
}
