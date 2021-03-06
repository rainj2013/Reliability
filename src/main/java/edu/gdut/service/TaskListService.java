package edu.gdut.service;

import edu.gdut.dao.CalTaskMapper;
import edu.gdut.domain.CalTask;
import edu.gdut.util.XlsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-16 下午8:22
 */
@Service
public class TaskListService {
    @Autowired(required = false)
    private CalTaskMapper calTaskMapper;

    public List<CalTask> top(int value){
        return calTaskMapper.findTop(value);
    }

    public List<Double[]> check(String id) throws Exception{
        String filePath = "xls" + File.separator + id + ".xls";
        return XlsUtil.read(filePath, 0);
    }
}
