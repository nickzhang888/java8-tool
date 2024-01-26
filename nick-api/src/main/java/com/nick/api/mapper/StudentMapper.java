package com.nick.api.mapper;

import com.nick.api.domain.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    public List<Student> findUser(Student student);

    public int addUser(Student student);

    public int updateUser(Student student);

    public int deleteUserById(Integer id);
}
