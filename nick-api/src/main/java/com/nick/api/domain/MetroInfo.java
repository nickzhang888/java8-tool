package com.nick.api.domain;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

/**
 * 车辆信息
 * @TableName metro_info
 */
@Data
public class MetroInfo implements Serializable {
    /**
     * 主键
     */
    private Long metroId;

    /**
     * 车辆编号
     */
    private String metroName;

    /**
     * 状态（0离线 1在线）
     */
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetroInfo metroInfo = (MetroInfo) o;
        return Objects.equals(metroId, metroInfo.metroId) && Objects.equals(metroName, metroInfo.metroName) && Objects.equals(status, metroInfo.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metroId, metroName, status);
    }

    @Override
    public String toString() {
        return "MetroInfo{" +
                "metroId=" + metroId +
                ", metroName='" + metroName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}