package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.Video;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * video数据访问层
 */
@Component
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();
}
