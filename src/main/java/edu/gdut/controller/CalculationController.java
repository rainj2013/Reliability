package edu.gdut.controller;

/**
 * Created by rainj2013 on 16-11-4.
 */

import edu.gdut.service.CalService;
import edu.gdut.util.XlsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calculation")
public class CalculationController {
    @Autowired
    private CalService calService;

    @RequestMapping
    public Map<String, String> cal(String fileName, String algoName, String remark) {
        Map<String, String> data = new HashMap<>();

        Map<String, List<Double[]>> trainingData, testData;
        List<Integer> label;
        try (InputStream ins = new FileInputStream("xls/" + fileName)) {
            trainingData = XlsUtil.readData(ins, 0, 7);
            testData = XlsUtil.readData(ins, 2, 7);
            label = XlsUtil.readLabel(ins, 1);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("code","0");
            data.put("msg","文件解析出错！");
            return data;
        }
        calService.handle(trainingData, label, testData, algoName, remark);
        data.put("code","1");
        data.put("msg","计算任务已提交！");
        return data;
    }

}
