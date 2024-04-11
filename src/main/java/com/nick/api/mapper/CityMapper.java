package com.nick.api.mapper;

import com.nick.api.entity.City;
import com.nick.api.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {
    List<City> findCity(City city);

    public int addCity(City city);

    public int updateCity(Student student);

    public int deleteCity(Integer id);
}
