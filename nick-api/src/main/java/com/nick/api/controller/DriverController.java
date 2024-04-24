package com.nick.api.controller;

import com.nick.api.domain.PersonalInfo;
import com.nick.api.domain.Driver;
import com.nick.api.service.DriverService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class DriverController extends BaseController {
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
        Driver s1 = new Driver("小妮", 18, "男");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", s1);
        return ajax;
    }

    @Autowired
    private DriverService driverService;

    @GetMapping("/getUser")
    @RepeatSubmit()
    public TableDataInfo getUser(Driver driver) {
        System.err.println(driver.getClass().getName());
        startPage();
        List<Driver> list = driverService.findUser(driver);
        for (Driver d : list) {
            if (d.getIds() == null) {
                d.setIds(new ArrayList<>());
            }if (d.getSort() == null) {
                d.setSort(0);
            }
        }
        return getDataTable(list);
    }

    @PostMapping("/exportUser")
    @ResponseBody
    public void export(HttpServletResponse response, Driver driver) throws IOException {
        List<Driver> list = driverService.findUser(driver);
        ExcelUtil<Driver> util = new ExcelUtil<>(Driver.class);
        util.exportExcel(response, list, "用户数据");
    }

    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Driver> util = new ExcelUtil<>(Driver.class);
        List<Driver> list = util.importExcel(file.getInputStream());
        String msg = driverService.importUser(list, updateSupport);
        return success(msg);
    }

    @PostMapping("/addUser")
    public AjaxResult addUser(@RequestBody Driver driver) {
        return toAjax(driverService.addUser(driver));
    }

    @PostMapping("/updateUser")
    public AjaxResult updateUser(@RequestBody Driver driver) {
        return toAjax(driverService.updateUser(driver));
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
        return toAjax(driverService.deleteUserByIds(ids));
    }

}
