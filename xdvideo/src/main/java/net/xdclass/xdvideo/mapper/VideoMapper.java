package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.provider.VideoProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * video数据访问层
 */
public interface VideoMapper {

    @Select("select * from video")
   /* @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "cover_img", property = "coverImg"),
            @Result(column = "view_num", property = "viewNum")
    })*/
    List<Video> findAll();

    @Select("select * from video where id=#{id}")
    Video findById(int id);

//    @Update("update video set  title=#{title}  where id=#{id}")
    //使用sqlprovoder动态更新数据库字段
   @UpdateProvider(type= VideoProvider.class,method = "updateVideo")
    int update(Video video);

    @Delete("delete from video where id=#{id}")
    int delete(int id);


    @Insert("INSERT INTO video( title, summary, cover_img, view_num, price, create_time,online, point)values(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price},#{createTime},#{online},#{point})")
    int save(Video video);


}
