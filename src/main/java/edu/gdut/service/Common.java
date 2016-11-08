package edu.gdut.service;

import edu.gdut.service.DS.EvidenceDS;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
@Component
public class Common {
    /**
     * @Description 对数据集进行加权
     * @param Data 数据集
     * @param featureWeights 每个feature对应的权重
     */
    public void weightedData(Map<String, List<Double[]>> Data, List<Double> featureWeights) {
        for (Map.Entry<String, List<Double[]>> e : Data.entrySet()) {
            List<Double[]> object = e.getValue();
            int index = 0;
            for (Double[] feature : object) {
                feature[0] *= featureWeights.get(index);
                feature[1] *= featureWeights.get(index);
                feature[2] = 1 - feature[0] - feature[1];
                index++;
            }
        }
    }

    /**
     * @Description DS合成加权后的数据集
     * @param Data 待合成的数据集
     * @return DS合成结果
     */
    public Map<String,Double[]> dsFuse(Map<String, List<Double[]>> Data){
        Map<String,Double[]> DSResult = new HashMap<>();
        int index = 1;
        //取出每个对象
        for (Map.Entry<String, List<Double[]>> e : Data.entrySet()) {
            List<Double[]> object = e.getValue();
            //因为DS算法类的写法原因，这里就很丑陋地先取feature集合第一个feature创建EvidenceDS对象，后面再用循环add
            Double[] feature = object.get(0);
            EvidenceDS evidenceDs = new EvidenceDS(feature[0], feature[1]);
            //从第2个feature开始用循环调用DS合成
            for (int i =1; i<object.size(); i++){
                feature = object.get(i);
                evidenceDs = evidenceDs.add(new EvidenceDS(feature[0], feature[1]));
            }
            //添加到结果集合
            DSResult.put(Integer.toString(index), new Double[]{evidenceDs.getFraud(), evidenceDs.getUnfraud()});
            index++;
        }
        return DSResult;
    }
}
