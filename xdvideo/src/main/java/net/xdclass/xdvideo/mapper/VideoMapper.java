package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.Video;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * video数据访问层
 */
@Component
public interface VideoMapper {

    @Select("select * from video")
   /* @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "cover_img", property = "coverImg"),
            @Result(column = "view_num", property = "viewNum")
    })*/
    List<Video> findAll();
}
