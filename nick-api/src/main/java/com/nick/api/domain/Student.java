package com.nick.api.domain;

import com.nick.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Student {
    @Excel(name = "用户id", prompt = "用户编号")
    private Integer id;
    @Excel(name = "用户名称")
    private String name;
    @Excel(name = "用户年龄")
    private Integer age;
    @Excel(name = "用户性别")
    private String sex;
    @Excel(name = "描述")
    private String describe;
    private String avatar;

    public Student() {

    }


    public Student(String name, Integer age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }



    @Override
    public String toString() {
        return "Student{" +
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
        Student student = (Student) o;
        return id == student.id && age == student.age && Objects.equals(name, student.name)
                && Objects.equals(sex, student.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, sex);
    }

}
