package com.nick.api.mapper;

import com.nick.api.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> findUser(Student student);

    public int addUser(Student student);

    public int updateUser(Student student);

    public int deleteUserById(Integer id);
}
