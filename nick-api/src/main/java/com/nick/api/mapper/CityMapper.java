package com.nick.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nick.api.domain.City;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CityMapper extends BaseMapper<City> {

    int addCity(City city);

    int updateCity(City city);

    int deleteCity(Integer id);
}
