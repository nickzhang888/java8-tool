package com.nick.api.controller;

import com.nick.api.domain.PersonalInfo;
import com.nick.api.domain.Student;
import com.nick.api.service.StudentService;
import com.nick.common.annotation.RepeatSubmit;
import com.nick.common.core.controller.BaseController;
import com.nick.common.core.domain.AjaxResult;
import com.nick.common.core.page.TableDataInfo;
import com.nick.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StudentController extends BaseController {
    @Autowired
    private Environment environment;

    @Autowired
    private PersonalInfo personalInfo;

    //获取端口号
    @Value("${server.port}")
    private Integer port;

    @GetMapping("/getPortByValue")
    public Integer getPortByValue() {
        return port;
    }

    @GetMapping("/getPort")
    public Integer getPort() {
        return Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port")));
    }

    @GetMapping("/getPersonalInfo")
    public String getPersonalInfo() {
        return personalInfo.toString();
    }

    @GetMapping("/hello")
    public AjaxResult hello() {
        String message = "hello world";
        return AjaxResult.success(message);
    }

    @GetMapping("/test")
    public AjaxResult test() {
        Student s1 = new Student("小妮", 18, "男");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", s1);
        return ajax;
    }

    @Autowired
    private StudentService studentService;

    @GetMapping("/getUser")
    @RepeatSubmit()
    public TableDataInfo getUser(Student student) {
        System.err.println(student.getClass().getName());
        startPage();
        List<Student> list = studentService.findUser(student);
        return getDataTable(list);
    }

    @PostMapping("/exportUser")
    @ResponseBody
    public void export(HttpServletResponse response, Student student) throws IOException {
        List<Student> list = studentService.findUser(student);
        ExcelUtil<Student> util = new ExcelUtil<>(Student.class);
        util.exportExcel(response, list, "用户数据");
    }
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Student> util = new ExcelUtil<>(Student.class);
        List<Student> list = util.importExcel(file.getInputStream());
        studentService.importUser(list, updateSupport);
        return success("导入成功");
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
