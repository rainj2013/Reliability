package edu.gdut.service;

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
        for(Map.Entry<String, Double[]> e: map.entrySet()){
            List<Double[]> list = new ArrayList<>();
            list.add(e.getValue());
            temp.put(e.getKey(), list);
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

            for (int objectId = 1; objectId < objectSize + 1; objectId++) {
                //取每个object中其中一个feature对应焦元的值
                featureValues = map.get(Integer.toString(objectId)).get(featureId - 1);
                features.add(new AUCBean( featureValues[0], featureValues[1], label.get(objectId - 1) == 1));
            }
            //按照焦元的值进行排序
            features.sort(new Comparator<AUCBean>() {
                @Override
                public int compare(AUCBean o1, AUCBean o2) {
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
                }
            });

            int zheng = 0;//正样本的排序之和
            int count = 0;//正样本个数
            //AUC=((正样本的倒序排序序数之和)-M*(M+1)/2)/(M*N)

            for (int i = features.size() - 1; i >= 0; i--) {
                AUCBean aucBean = features.get(i);
                if (element==0&&aucBean.isLabel()) {//对于unFraud焦元来说，正样本是标记为unFraud的
                    zheng += (i + 1);
                    count++;
                }
            }
            double featureAUC = (double) (zheng - count * (count + 1) / 2) / (count * (objectSize - count));
            aucList.add(featureAUC);
        }
        return aucList;
    }
}
