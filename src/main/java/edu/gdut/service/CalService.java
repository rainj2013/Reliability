package edu.gdut.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-15 上午1:34
 */
@Service
public class CalService {
    @Autowired
    @Qualifier("IRE")
    private IRE iRE;
    @Autowired
    @Qualifier("CRE")
    private CRE cRE;
    @Autowired
    @Qualifier("ILRE")
    private ILRE iLRE;
    @Autowired
    @Qualifier("ICRE")
    private ICRE iCRE;
    private static ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public void handle(Map<String, List<Double[]>> trainingData, List<Integer> label, Map<String, List<Double[]>> testData
            , String algoName, String remark) {
        CalTask calTask = null;
        switch (algoName) {
            case "IRE":
                calTask = new CalTask(iRE, trainingData, label, testData);
                break;
            case "CRE":
                calTask = new CalTask(cRE, trainingData, label, testData);
                break;
            case "ICRE":
                calTask = new CalTask(iCRE, trainingData, label, testData);
                break;
            case "ILRE":
                calTask = new CalTask(iLRE, trainingData, label, testData);
                break;
            default:
                break;
        }
        if (null != calTask) {
            //TODO 将计算任务插入数据库
            executorService.submit(calTask);
        }

    }

    class CalTask implements Runnable {
        private Cal c;
        Map<String, List<Double[]>> trainingData;
        List<Integer> label;
        Map<String, List<Double[]>> testData;

        public CalTask(Cal c, Map<String, List<Double[]>> trainingData, List<Integer> label,
                       Map<String, List<Double[]>> testData) {
            this.c = c;
            this.label = label;
            this.trainingData = trainingData;
            this.testData = testData;
        }

        @Override
        public void run() {
            c.cal(trainingData, label, testData);
            //TODO 将结果写入文件，更新数据库计算状态
        }
    }
}
