package com.nick.api.domain;

import com.nick.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Driver {
    @Excel(name = "用户id", prompt = "用户编号")
    private Integer id;

    private List<Integer> ids;

    @Excel(name = "用户名称")
    private String name;
    @Excel(name = "用户年龄")
    private Integer age;
    @Excel(name = "用户性别")
    private String sex;
    @Excel(name = "描述")
    private String describe;
    @Excel(name = "头像")
    private String avatar;

    private Integer sort;

    public Driver() {

    }


    public Driver(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }



    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Driver driver = (Driver) o;
        return id == driver.id && age == driver.age && Objects.equals(name, driver.name)
                && Objects.equals(sex, driver.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, sex);
    }

}
