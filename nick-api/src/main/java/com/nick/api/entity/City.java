package com.nick.api.entity;


public class City {
    private Integer id;
    private String name;
    private Integer code;

    public City(Integer id, String name, Integer code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
