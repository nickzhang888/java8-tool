package com.nick.api.mapper;

import com.nick.api.domain.Driver;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DriverMapper {
    public List<Driver> findUser(Driver driver);

    public int addUser(Driver driver);

    public int updateUser(Driver driver);

    public int deleteUserByIds(Long[] ids);
}
