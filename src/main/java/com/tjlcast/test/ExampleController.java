package com.tjlcast.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tangjialiang on 2018/5/6.
 */
@RestController
@RequestMapping("/api/student")
public class ExampleController {

    @Autowired
    Student student ;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Student getStudent() {
        return student ;
    }
}
