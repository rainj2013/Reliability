package edu.gdut.service.GA;

import edu.gdut.service.Common;
import edu.gdut.service.IRE4ThreeEle;
import edu.gdut.util.ArraysUtil;

import java.util.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-20 下午9:00
 */
public class DistanceFitnessCal extends Common implements FitnessCal {
    private final int geneLength;
    private final Map<String, List<Double[]>> trainingData;//训练集数据
    private final List<Integer> label;//训练集标签
    private final IRE4ThreeEle ire4ThreeEle;
    private final List<Double[]> labels;

    public DistanceFitnessCal(int geneLength, Map<String, List<Double[]>> trainingData, List<Integer> label,
                             IRE4ThreeEle ire4ThreeEle) {
        this.geneLength = geneLength;
        this.trainingData = trainingData;
        this.label = label;
        this.ire4ThreeEle = ire4ThreeEle;
        labels = ire4ThreeEle.labelsSwitch(label);
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
        Map<String, List<Double[]>> weightedTrainingData = weightedData(trainingData, featureWeights);
        Map<String, Double[]> dsResult = dsFuse(weightedTrainingData);
        Map<String, List<Double[]>> dsData = switchData(dsResult);
        double jd = ire4ThreeEle.jDistance(labels, dsData).get(0);
        double dd = ire4ThreeEle.dDistance(labels, dsData).get(0);
        double fitness = 1-0.5*(dd+jd);
        return fitness;
    }

    private Map<String, List<Double[]>> switchData(Map<String, Double[]> data){
        Map<String, List<Double[]>> result = new HashMap<>();
        for (int objectId = 1; objectId<data.size(); objectId++){
            String id = Integer.toString(objectId);
            List<Double[]> list = new ArrayList<>();
            list.add(data.get(id));
            result.put(id, list);
        }
        return result;
    }
}
