package net.xdclass.xdvideo.service;

import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.dto.VideoOrderDto;
import org.apache.ibatis.annotations.Param;

/**
 * 订单接口
 */
public interface VideoOrderService {
    /**
     * 下单接口
     * @param videoOrderDto
     * @return
     */
    String save(VideoOrderDto videoOrderDto);

    /**
     * 根据流水号查找订单
     * @param outTradeNo
     * @return
     */
    VideoOrder findByOutTradeNo(String outTradeNo);

    /**
     * 更具流水号更新订单
     * @param videoOrder
     * @return
     */
    int updateVideoOderByOutTradeNo(VideoOrder videoOrder);
}
