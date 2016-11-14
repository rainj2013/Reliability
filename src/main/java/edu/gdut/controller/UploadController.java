package edu.gdut.controller;

import edu.gdut.util.FileUtil;
import edu.gdut.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-14 下午10:15
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping("/xls")
    @ResponseBody
    public Map<String,String> xls(@RequestParam(value = "xls") MultipartFile xlsFile){
        Map<String,String> data = new HashMap<>();
        File file = new File("xls");
        if (!file.exists())
            file.mkdirs();
        String fileName = StringUtil.randomString()+".xls";
        try {
            FileUtil.writeToFile(xlsFile.getInputStream(), file.getPath()+File.separator+fileName);
        } catch (IOException e) {
            data.put("code", "0");
            data.put("msg","上传文件失败！");
            return data;
        }
        data.put("code", "1");
        data.put("msg","上传成功！");
        data.put("fileId",fileName);
        return data;
    }
}
