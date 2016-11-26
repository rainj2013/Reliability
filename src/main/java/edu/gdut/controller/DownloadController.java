package edu.gdut.controller;

import edu.gdut.service.TaskListService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-18 下午11:37
 */
@RestController
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    private TaskListService taskListService;

    @RequestMapping
    public ResponseEntity<byte[]> download(@RequestParam("resultFile") String resultFile) throws IOException {
        File file = new File("xls"+File.separator+resultFile);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", resultFile);
        return new ResponseEntity(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    @RequestMapping("/check")
    public List<Double[]> check(@RequestParam("id") String id){
        List<Double[]> list;
        try {
            list = taskListService.check(id);
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }

}
