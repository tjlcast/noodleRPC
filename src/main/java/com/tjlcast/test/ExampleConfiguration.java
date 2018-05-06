package com.tjlcast.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tangjialiang on 2018/5/5.
 *
 * 指导某个类的 build
 */
@Configuration
public class ExampleConfiguration {
    @Value("student.name")
    private String name ;

    @Value("student.age")
    private int age ;

    @Bean(name = "tjlcast")
    public Student dataSource() {
        Student student = new Student(name, age) ;
        return student;
    }

}
