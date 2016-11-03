package edu.gdut.service;

import edu.gdut.domain.AUCBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


//关于AUC计算的文章 http://www.tuicool.com/articles/qYNNF3
public class AUC {
    public static List<Double> auc(Map<Integer, List<Double>> map, List<Integer> lable) {
        int objectSize = lable.size();
        int featureSize = map.get(1).size();
        List<Double> aucList = new ArrayList<>();

        for (int featureId = 1; featureId < featureSize + 1; featureId++) {
            //迭代feature，计算每个feature的AUC
            List<AUCBean> features = new ArrayList<>();

            for (int objectId = 1; objectId < objectSize + 1; objectId++) {
                //取每个object中其中一个feature的fraud值
                features.add(new AUCBean(
                        map.get(objectId).get(featureId - 1),
                        lable.get(objectId - 1) == 1)
                );
            }
            //按照fraud值进行排序
            features.sort(new Comparator<AUCBean>() {
                @Override
                public int compare(AUCBean o1, AUCBean o2) {
                    if (o1.getFraud() > o2.getFraud())
                        return 1;
                    else
                        return -1;
                }
            });

            int zheng = 0;//正样本的排序之和
            int count = 0;//正样本个数
            //AUC=((正样本的倒序排序序数之和)-M*(M+1)/2)/(M*N)

            for (int i = features.size() - 1; i >= 0; i--) {
                AUCBean aucBean = features.get(i);
                if (aucBean.isLable()) {
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
