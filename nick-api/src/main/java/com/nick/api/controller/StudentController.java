package com.nick.api.controller;

import com.nick.api.domain.PersonalInfo;
import com.nick.api.domain.Student;
import com.nick.api.service.StudentService;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.core.page.TableDataInfo;
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
public class StudentController extends BaseController {
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
    public AjaxResult hello() {
        String message = "hello world";
        return AjaxResult.success(message);
    }

    @GetMapping("/test")
    public AjaxResult test() {
        String message = "hello world";
        Student s1 = new Student("小妮", 18, "女");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", s1);
        return ajax;
    }

    @Autowired
    private StudentService studentService;

    @GetMapping("/getUser")
    public TableDataInfo getUser(Student student) {
        System.err.println(student.getClass().getName());
        startPage();
        List<Student> students = studentService.findUser(student);
        return getDataTable(students);
    }

    @PostMapping("/addUser")
    public AjaxResult addUser(@RequestBody Student student) {
        return toAjax(studentService.addUser(student));
    }

    @PostMapping("/updateUser")
    public AjaxResult updateUser(@RequestBody Student student) {
        return toAjax(studentService.updateUser(student));
    }

    @PostMapping("/deleteUser")
    public AjaxResult deleteUser(@RequestBody Long[] ids) {
//        Map<String, Object> response = new HashMap<String, Object>();
//        for (Integer id : ids) {
//            studentService.deleteUserById(id);
//        }
//        response.put("code", 200);
//        response.put("message", "删除成功");
//        return ResponseEntity.ok(response);
        return toAjax(studentService.deleteUserByIds(ids));
    }

}
