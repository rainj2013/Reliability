package edu.gdut.service.reliability;

import edu.gdut.domain.AUCBean;
import org.springframework.stereotype.Service;

import java.util.*;


//关于AUC计算的文章 http://www.tuicool.com/articles/qYNNF3

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-8
 */
@Service
public class AUC {
    /**
     * @Description 计算单个feature的的AUC值
     * @param label 数据集标签
     * @param map 数据集
     * @param element 焦元
     * @return 指定焦元的AUC值
     */
    public Double auc(List<Integer> label ,Map<String, Double[]> map, int element) {
        Map<String, List<Double[]>> temp = new HashMap<>();
        for(int i = 1; i<=map.size(); i++){
            String key = Integer.toString(i);
            List<Double[]> list = new ArrayList<>();
            list.add(map.get(key));
            temp.put(key, list);
        }
        List<Double> result = auc(temp, label, element);
        return result.get(0);
    }

    /**
     * @Description 计算多个feature的指定焦元的AUC值
     * @param map 数据集
     * @param label 数据集标签
     * @param element 焦元
     * @return 每个feature的指定焦元的AUC值
     */
    public List<Double> auc(Map<String, List<Double[]>> map, List<Integer> label, int element) {
        int objectSize = label.size();
        int featureSize = map.get("1").size();
        List<Double> aucList = new ArrayList<>();
        Double[] featureValues;
        for (int featureId = 1; featureId < featureSize + 1; featureId++) {
            //迭代feature，计算每个feature的AUC
            List<AUCBean> features = new ArrayList<>();

            for (int objectId = 1; objectId <= objectSize; objectId++) {
                //取每个object中其中一个feature对应焦元的值
                featureValues = map.get(Integer.toString(objectId)).get(featureId - 1);
                features.add(new AUCBean( featureValues[0], featureValues[1], label.get(objectId - 1) == 1));
            }
            //按照焦元的值进行排序
            features.sort((o1, o2) -> {
                if(element == 0){
                    if (o1.getFraud() > o2.getFraud())
                        return 1;
                    else if (o1.getFraud() < o2.getFraud())
                        return -1;
                    else return 0;
                }else{
                    if (o1.getUnFraud() > o2.getUnFraud())
                        return 1;
                    else if (o1.getUnFraud() < o2.getUnFraud())
                        return -1;
                    else return 0;
                }
            });

            Map<Double, Integer[]> scoresCount = new HashMap<>();
            for (int i = features.size() - 1; i >= 0; i--) {
                AUCBean aucBean = features.get(i);
                double fraud = aucBean.getFraud();
                double unFraud = aucBean.getUnFraud();
                double scoreValue = element==0?fraud:unFraud;
                if (!scoresCount.containsKey(scoreValue)){
                    scoresCount.put(scoreValue, new Integer[]{1,i+1});
                }else {
                    Integer[] score = scoresCount.get(scoreValue);
                    score[0] = score[0] + 1;
                    score[1] = score[1] + i +1;
                }
            }

            double zheng = 0;//正样本的排序之和
            int count = 0;//正样本个数
            //AUC=((正样本的倒序排序序数之和)-M*(M+1)/2)/(M*N)

            for (int i = features.size() - 1; i >= 0; i--) {
                AUCBean aucBean = features.get(i);
                if ((element==0)==aucBean.isLabel()) {//对于unFraud焦元来说，正样本是标记为unFraud的
                    Integer[] score=element==0?scoresCount.get(aucBean.getFraud()):scoresCount.get(aucBean.getUnFraud());
                    zheng += (score[1]/score[0]);
                    count++;
                }
            }
            double featureAUC = (zheng - count * (count + 1) / 2) / (count * (objectSize - count));
            aucList.add(featureAUC);
        }
        return aucList;
    }
}
