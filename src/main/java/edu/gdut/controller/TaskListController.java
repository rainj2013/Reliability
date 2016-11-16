package edu.gdut.controller;

import edu.gdut.domain.CalTask;
import edu.gdut.service.TaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-16 下午8:20
 */
@RestController
@RequestMapping("/taskList")
public class TaskListController {
    @Autowired
    private TaskListService taskListService;
    @RequestMapping("/top")
    public List<CalTask> top(){
        return taskListService.top(100);
    }
}
