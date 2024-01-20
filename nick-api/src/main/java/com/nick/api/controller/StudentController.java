package com.nick.api.controller;

import com.nick.api.entity.PersonalInfo;
import com.nick.api.entity.Student;
import com.nick.api.service.StudentService;
import com.nick.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private Environment environment;

    @Autowired
    private PersonalInfo dataInfo;

    //获取端口号
    @Value("${server.port}")
    private Integer port;

    @GetMapping("/getPort")
    public Integer getPort() {
        Integer p = Integer.valueOf(environment.getProperty("server.port"));
//        Integer p = port;
        return p;
    }

    @GetMapping("/getDataInfo")
    public String getDataInfo() {
        return dataInfo.toString();
    }


    @GetMapping("/hello")
    public ResponseEntity hello() {
        String message = "hello world";
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("code", 200);
        response.put("message", "成功");
        response.put("data", message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public AjaxResult test() {
        String message = "hello world";
        Student s1 = new Student(12, "小妮", 18, "女");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", s1);
        return ajax;
    }

    @Autowired
    private StudentService studentService;

    @GetMapping("/getUser")
    public ResponseEntity getUser(Student student) {
        Map<String, Object> response = new HashMap<String, Object>();

        List<Student> students = studentService.findUser(student);
        response.put("code", 200);
        response.put("message", "成功");
        response.put("data", students);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody Student student) {
        System.out.println(student.getClass().getName());
        Map<String, Object> response = new HashMap<String, Object>();
        studentService.addUser(student);
        response.put("code", 200);
        response.put("message", "新增成功");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody Student student) {
        Map<String, Object> response = new HashMap<String, Object>();
        studentService.updateUser(student);
        response.put("code", 200);
        response.put("message", "修改成功");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestBody Integer[] ids) {
        Map<String, Object> response = new HashMap<String, Object>();

        for (Integer id : ids) {
            studentService.deleteUserById(id);
        }
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}
