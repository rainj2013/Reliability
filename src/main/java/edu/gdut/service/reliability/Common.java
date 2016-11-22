package edu.gdut.service.reliability;

import edu.gdut.service.DS.EvidenceDS;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
@Service
public abstract class Common {
    /**
     * @param data           数据集
     * @param featureWeights 每个feature对应的权重
     * @Description 对数据集进行加权
     * @return 加权后的数据
     */
    public Map<String, List<Double[]>> weightedData(Map<String, List<Double[]>> data, List<Double> featureWeights) {
        Map<String, List<Double[]>> weightedData = new HashMap<>();

        for (int objectId = 1;objectId<=data.size();objectId++) {
            String id = Integer.toString(objectId);
            List<Double[]> features = new ArrayList<>();
            List<Double[]> object = data.get(id);
            int index = 0;
            for (Double[] feature : object) {
                Double[] featureWeight = new Double[feature.length];
                featureWeight[0] = feature[0] * featureWeights.get(index);
                featureWeight[1] = feature[1] * featureWeights.get(index);
                featureWeight[2] = 1 - featureWeight[0] - featureWeight[1];
                features.add(featureWeight);
                index++;
            }
            weightedData.put(id, features);
        }
        return weightedData;
    }

    /**
     * @param data           数据集
     * @param fraudWeights   fraud焦元权重
     * @param unFraudWeights unFraud焦元权重
     * @return 加权后的数据
     * @Description 给数据集的fraud和unFraud焦元分别加权
     */
    public Map<String, List<Double[]>> weightedData(Map<String, List<Double[]>> data, List<Double> fraudWeights,
                                                    List<Double> unFraudWeights) {
        Map<String, List<Double[]>> weightedData = new HashMap<>();
        for (int objectId = 1;objectId<=data.size();objectId++) {
            String id = Integer.toString(objectId);
            List<Double[]> object = data.get(id);
            List<Double[]> features = new ArrayList<>();
            int index = 0;
            for (Double[] feature : object) {
                Double[] featureWeight = new Double[feature.length];
                featureWeight[0] = feature[0] * fraudWeights.get(index);
                featureWeight[1] = feature[1] * unFraudWeights.get(index);
                featureWeight[2] = 1 - featureWeight[0] - featureWeight[1];
                features.add(featureWeight);
                index++;
            }
            weightedData.put(id, features);
        }
        return weightedData;
    }

    /**
     * @param data 待合成的数据集
     * @return DS合成结果
     * @Description DS合成加权后的数据集
     */
    public Map<String, Double[]> dsFuse(Map<String, List<Double[]>> data) {
        Map<String, Double[]> DSResult = new HashMap<>();
        //取出每个对象
        for (int index = 1; index<=data.size(); index++) {
            List<Double[]> object = data.get(Integer.toString(index));
            //因为DS算法类的写法原因，这里就很丑陋地先取feature集合第一个feature创建EvidenceDS对象，后面再用循环add
            Double[] feature = object.get(0);
            EvidenceDS evidenceDs = new EvidenceDS(feature[0], feature[1]);
            //从第2个feature开始用循环调用DS合成
            for (int i = 1; i < object.size(); i++) {
                feature = object.get(i);
                evidenceDs = evidenceDs.add(new EvidenceDS(feature[0], feature[1]));
            }
            //添加到结果集合
            DSResult.put(Integer.toString(index), new Double[]{evidenceDs.getFraud(), evidenceDs.getUnfraud(),
                    evidenceDs.getUncertainty()});
        }
        return DSResult;
    }
}
