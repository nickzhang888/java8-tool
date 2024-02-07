package com.nick.api.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class City {
    private Integer id;
    private String name;
    private Integer code;
    private Double lng;
    private Double lat;

//    添加城市名和code时
    public City(Integer id, String name, Integer code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }
//    根据城市添加经纬度时
    public City( String name, Double lng,Double lat) {
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }
    public City() {

    }

}
