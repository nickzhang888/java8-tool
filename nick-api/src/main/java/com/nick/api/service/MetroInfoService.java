package com.nick.api.service;

import com.nick.api.domain.MetroInfo;
import com.nick.api.mapper.MetroInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MetroInfoService {
    @Autowired
    private MetroInfoMapper MetroInfoMapper;
    public List<MetroInfo> getMetroInfo(Integer id) {
        return MetroInfoMapper.getAllByMetroId(id);
    }
}
