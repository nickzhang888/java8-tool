package com.nick.api.mapper;
import java.util.List;
import com.nick.api.domain.MetroInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 尼克
 * @description 针对表【metro_info(车辆信息)】的数据库操作Mapper
 * @createDate 2024-04-17 21:34:04
 * @Entity com.nick.api.domain.MetroInfo
 */
@Mapper
public interface MetroInfoMapper  {
     List<MetroInfo> getAllByMetroId(Long id);
}




