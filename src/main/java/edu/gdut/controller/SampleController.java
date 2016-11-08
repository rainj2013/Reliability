package edu.gdut.controller;

/**
 * Created by rainj2013 on 16-11-4.
 */

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
@Component
@RestController
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello";
    }

}
