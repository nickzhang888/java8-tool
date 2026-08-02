package com.nick.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nick.api.domain.City;
import com.nick.api.mapper.CityMapper;
import com.nick.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityMapper cityMapper;

    /**
     * 按 name（模糊）或 code（精确）查询；两者都传时为 OR。
     * 没有有效条件时返回空列表（避免误查全表）。
     */
    public List<City> findCity(City city) {
        if (city == null) {
            return Collections.emptyList();
        }

        boolean hasName = StringUtils.isNotEmpty(city.getName());
        boolean hasCode = city.getCode() != null;

        // 没传 name/code，或传了空字符串 → 不查全表
        if (!hasName && !hasCode) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<City> queryWrapper = new LambdaQueryWrapper<>();
        if (hasName && hasCode) {
            queryWrapper.and(w -> w.like(City::getName, city.getName())
                    .or()
                    .eq(City::getCode, city.getCode()));
        } else if (hasName) {
            queryWrapper.like(City::getName, city.getName());
        } else {
            queryWrapper.eq(City::getCode, city.getCode());
        }

        return cityMapper.selectList(queryWrapper);
    }

    /** 查询全部城市（批量任务用） */
    public List<City> listAll() {
        return cityMapper.selectList(null);
    }

    public int addCity(City city) {
        return cityMapper.addCity(city);
    }

    public int updateCity(City city) {
        return cityMapper.updateCity(city);
    }

    public int deleteCity(Integer id) {
        return cityMapper.deleteCity(id);
    }

}
