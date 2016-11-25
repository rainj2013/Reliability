package edu.gdut.service;

import edu.gdut.dao.CalTaskMapper;
import edu.gdut.domain.CalTask;
import edu.gdut.service.reliability.*;
import edu.gdut.util.StringUtil;
import edu.gdut.util.XlsUtil;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
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
    @Autowired(required = false)
    private CalTaskMapper calTaskMapper;

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
    @Autowired
    private IRE4ThreeEle ire4ThreeEle;
    @Autowired
    private CRE4ThreeEle cre4ThreeEle;

    private static final int CALING = 0;
    private static final int COMPLETE = 0;
    private static final int ERROR =2;

    private static ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public void handle(String fileName, String algoName, String remark) throws IOException, BiffException {
        Map<String, List<Double[]>> trainingData, testData;
        List<Integer> label;
        String dataFile = "xls"+ File.separator + fileName;

        try {
            trainingData = XlsUtil.readData(dataFile, 0, 7);
            testData = XlsUtil.readData(dataFile, 2, 7);
            label = XlsUtil.readLabel(dataFile, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        Task task = null;
        CalTask calTask = new CalTask(StringUtil.randomString(),dataFile, remark, algoName,
                new Timestamp(System.currentTimeMillis()), CALING);
        switch (algoName) {
            case "IRE":
                task = new Task(iRE, trainingData, label, testData, calTask);
                break;
            case "CRE":
                task = new Task(cRE, trainingData, label, testData, calTask);
                break;
            case "ICRE":
                task = new Task(iCRE, trainingData, label, testData, calTask);
                break;
            case "IIRE":
                task = new Task(iLRE, trainingData, label, testData, calTask);
                break;
            case "IRE3":
                task = new Task(ire4ThreeEle, trainingData, label, testData, calTask);
                break;
            case "CRE3":
                task = new Task(cre4ThreeEle, trainingData, label, testData, calTask);
                break;
            default:
                break;
        }
        if (null != task) {
            calTaskMapper.insert(calTask);
            executorService.submit(task);
        }

    }

    class Task implements Runnable {
        private Cal c;
        private Map<String, List<Double[]>> trainingData;
        private List<Integer> label;
        private Map<String, List<Double[]>> testData;
        private CalTask calTask;

        public Task(Cal c, Map<String, List<Double[]>> trainingData, List<Integer> label,
                       Map<String, List<Double[]>> testData, CalTask calTask) {
            this.c = c;
            this.label = label;
            this.trainingData = trainingData;
            this.testData = testData;
            this.calTask = calTask;
        }

        @Override
        public void run() {
            Map<String, Double[]> result = c.cal(trainingData, label, testData);
            String fileName = calTask.getId()+".xls";
            try (OutputStream out = new FileOutputStream("xls"+File.separator+fileName)) {
                XlsUtil.writeXls(out, result);
            } catch (Exception e) {
                e.printStackTrace();
                calTask.setStatus(ERROR);
                calTask.setFinTime(new Timestamp(System.currentTimeMillis()));
                calTaskMapper.update(calTask);
            }
            calTask.setStatus(COMPLETE);
            calTask.setResultFile(fileName);
            calTask.setFinTime(new Timestamp(System.currentTimeMillis()));
            calTaskMapper.update(calTask);
        }
    }

}
