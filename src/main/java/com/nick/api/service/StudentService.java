package com.nick.api.service;

import com.nick.api.entity.Student;
import com.nick.api.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentMapper studentMapper;

    public List<Student> findUser(Student student) {
        return studentMapper.findUser(student);
    }

    public int addUser(Student student) {
        return studentMapper.addUser(student);
    }

    public int updateUser(Student student) {
        return studentMapper.updateUser(student);
    }

    public int deleteUserById(Integer id) {
        return studentMapper.deleteUserById(id);
    }

    // @Override
    // public List<Student> findUser() {
    // return studentMapper.findUser();
    // }

}
