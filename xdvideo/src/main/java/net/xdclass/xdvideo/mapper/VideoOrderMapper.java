package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.VideoOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单dao层
 */
public interface VideoOrderMapper {

    @Insert("INSERT INTO `video_order` (`openid`, `out_trade_no`, `state`, `create_time`," +
            " `notify_time`, `total_fee`, `nickname`, `head_img`, `video_id`, `video_title`," +
            " `video_img`, `user_id`, `ip`, `del`)" +
            "VALUES" +
            "(#{openid},#{outTradeNo},#{state},#{createTime},#{notifyTime},#{totalFee}," +
            "#{nickname},#{headImg},#{videoId},#{videoTitle},#{videoImg},#{userId},#{ip},#{del});")
    //@Options注解来获取自增后的主键id值，通过videoOrder.getId()获取
    //keyProperty java对象的属性；keyColumn表示数据库的字段
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insert(VideoOrder videoOrder);

    /**
     * 根据主键查找订单  del=0 订单未删除 del=1 表示软删除
     * @param id
     * @return
     */
    @Select("select * from video_order where id=#{order_id} and del=0")
    VideoOrder findById(@Param("order_id") int id);

    /**
     * 根据交易订单号获取订单对象
     * @param outTradeNo
     * @return
     */
    @Select("select * from video_order where out_trade_no=#{out_trade_no} and del=0")
    VideoOrder findByOutTradeNo(@Param("out_trade_no") String outTradeNo);

    /**
     * 逻辑删除订单 userId防止横向越权
     * @param id
     * @param userId
     * @return
     */
    @Update("update video_order set del=0 where id=#{id} and user_id =#{userId}")
    int del(@Param("id") int id, @Param("userId") int userId);

    /**
     * 查找我的全部订单
     * @param userId
     * @return
     */
    @Select("select * from video_order where user_id =#{userId}")
    List<VideoOrder> findMyOrderList(int userId);

    /**
     * 根据订单流水号更新订单状态
     * @param videoOrder
     * @return
     */
    @Update("update video_order set state=#{state}, notify_time=#{notifyTime}, openid=#{openid}" +
            " where out_trade_no=#{outTradeNo} and state=0 and del=0")
    int updateVideoOderByOutTradeNo(VideoOrder videoOrder);



}
