package edu.gdut.service.GA;

import edu.gdut.service.AUC;
import edu.gdut.service.Common;
import edu.gdut.util.ArraysUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-11 下午9:06
 */
public class AucFitnessCal2 extends Common implements FitnessCal{

    private final AUC aUC;

    private final int geneLength;//基因长度
    private final Map<String, List<Double[]>> trainingData;//训练集数据
    private final List<Integer> label;//训练集标签

    public AucFitnessCal2(int geneLength, Map<String, List<Double[]>> trainingData, List<Integer> label, AUC aUC) {
        this.geneLength = geneLength;
        this.trainingData = trainingData;
        this.label = label;
        this.aUC = aUC;
    }

    @Override
    public double getFitness(Individual individual) {
        byte[] genes = individual.getGenes();
        List<Double> fraudWeights = new ArrayList<>();
        List<Double> unFraudWeights = new ArrayList<>();
        //将基因（权重）截取下来，转成十进制的小数。其中每个基因前半部分为fraud焦元的权重，后半部分为unFraud焦元的权重
        for (int i = 0;i<genes.length;i+=geneLength){
            byte[] fraud = Arrays.copyOfRange(genes, i, i+(geneLength/2));
            byte[] unFraud = Arrays.copyOfRange(genes, i+(geneLength/2), i+geneLength);
            fraudWeights.add(ArraysUtil.toDouble(fraud));
            unFraudWeights.add(ArraysUtil.toDouble(unFraud));
        }

        //对训练数据集加权
        Map<String, List<Double[]>> weightedTrainingData = weightedData(trainingData, fraudWeights, unFraudWeights);
        //DS合成
        Map<String,Double[]> dSResult = dsFuse(weightedTrainingData);
        //求fraud焦元的AUC值
        double fitness = aUC.auc(label, dSResult, 0);

        return fitness;
    }
}
