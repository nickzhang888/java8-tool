package com.nick.api.service;

import com.nick.api.entity.City;
import com.nick.api.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CityService {
    @Autowired
    private CityMapper cityMapper;

    public List<City> findCity(City city) {
        return cityMapper.findCity(city);
    }

    public int addCity(City city) {
        return cityMapper.addCity(city);
    }

}
