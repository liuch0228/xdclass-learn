package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.dto.VideoOrderDto;
import net.xdclass.xdvideo.service.VideoOrderService;
import net.xdclass.xdvideo.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单接口
 */
@RestController
//@RequestMapping("/user/api/v1/order")
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private VideoOrderService videoOrderService;
    //    @GetMapping("/add")
//    public JsonData saveOrder(){
//        return JsonData.buildSuccess("下单成功");
//    }
    @GetMapping("/add")
    public JsonData saveOrder(@RequestParam(value = "video_id", required = true) int videoId,
                              HttpServletRequest request) {

        //获取用户真实ip
        String ip = IpUtils.getIpAddr(request);
        //获取用户userId
//        int userId = (int)request.getAttribute("user_id");
        int userId= 1;
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setIp(ip);
        //下单的是哪个视频的订单
        videoOrderDto.setVideoId(videoId);

        VideoOrder videoOrder = videoOrderService.save(videoOrderDto);

        return JsonData.buildSuccess(videoOrder);

    }

}




