package edu.gdut.controller;

/**
 * Created by rainj2013 on 16-11-4.
 */

import com.alibaba.fastjson.JSONObject;
import edu.gdut.service.CalService;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/calculation")
public class CalculationController {
    @Autowired
    private CalService calService;

    @RequestMapping("/cal")
    public Map<String, String> cal(@RequestBody JSONObject jsonObj) {
        String fileName = jsonObj.getString("fileName");
        String algoName = jsonObj.getString("algoName");
        String remark = jsonObj.getString("remark");
        Map<String, String> data = new HashMap<>();
        try {
            calService.handle(fileName, algoName, remark);
        } catch (IOException e) {
            data.put("code","0");
            data.put("msg","文件上传失败！");
            return data;
        } catch (BiffException e) {
            data.put("code","0");
            data.put("msg","文件解析失败！");
            return data;
        }
        data.put("code","1");
        data.put("msg","计算任务已提交！");
        return data;
    }

}
