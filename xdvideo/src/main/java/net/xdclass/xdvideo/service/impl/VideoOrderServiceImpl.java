package net.xdclass.xdvideo.service.impl;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.dto.VideoOrderDto;
import net.xdclass.xdvideo.mapper.UserMapper;
import net.xdclass.xdvideo.mapper.VideoMapper;
import net.xdclass.xdvideo.mapper.VideoOrderMapper;
import net.xdclass.xdvideo.service.VideoOrderService;
import net.xdclass.xdvideo.utils.CommonUtils;
import net.xdclass.xdvideo.utils.HttpUtils;
import net.xdclass.xdvideo.utils.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class VideoOrderServiceImpl implements VideoOrderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoOrderMapper videoOrderMapper;
    @Autowired
    private WeChatConfig weChatConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(VideoOrderDto videoOrderDto) {
        //查找视频信息
        Video video = videoMapper.findById(videoOrderDto.getVideoId());
        //查找用户信息
        User user = userMapper.findByid(videoOrderDto.getUserId());

        //生成订单
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setTotalFee(video.getPrice());
        videoOrder.setVideoImg(video.getCoverImg());
        videoOrder.setVideoTitle(video.getTitle());
        videoOrder.setCreateTime(new Date());
        videoOrder.setVideoId(video.getId());

        videoOrder.setState(0);
        videoOrder.setUserId(user.getId());
        videoOrder.setHeadImg(user.getHeadImg());
        videoOrder.setNickname(user.getName());

        videoOrder.setDel(0);
        videoOrder.setIp(videoOrderDto.getIp());
        videoOrder.setOutTradeNo(CommonUtils.generateUUID());
        videoOrderMapper.insert(videoOrder);

        //调用微信下单接口，获取支付的codeurl
        String codeURL = uniformOrder(videoOrder);
        return codeURL;
    }

    @Override
    public VideoOrder findByOutTradeNo(String outTradeNo) {
        return videoOrderMapper.findByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateVideoOderByOutTradeNo(VideoOrder videoOrder) {
        return videoOrderMapper.updateVideoOderByOutTradeNo(videoOrder);
    }

    /**
     * 统一下单
     *
     * @param videoOrder
     * @return
     */
    private String uniformOrder(VideoOrder videoOrder) {
        //生成签名  params列表：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
        SortedMap<String, String> params = new TreeMap();
        params.put("appid", weChatConfig.getAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        //商品描述
        params.put("body", videoOrder.getVideoTitle());
        //商户订单号
        params.put("out_trade_no", videoOrder.getOutTradeNo());
        params.put("total_fee", videoOrder.getTotalFee().toString());
        //终端ip ,即用户ip
        params.put("spbill_create_ip", videoOrder.getIp());
        //交易类型,即支付方式  扫码支付：NATIVE
        params.put("trade_type", "NATIVE");
        params.put("notify_url", weChatConfig.getPayCallbackUrl());
        //生成签名sign
        String sign = WXPayUtil.createSign(params, weChatConfig.getKey());
        params.put("sign", sign);
        String payXml = StringUtils.EMPTY;
        try {
            // 请求参数转xml
            payXml = WXPayUtil.mapToXml(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //统一下单,返回一个xml字符串
        String orderStr = HttpUtils.doPost(weChatConfig.getUNIFIED_ORDER_URL(), payXml);
        System.out.println(orderStr);
        if (StringUtils.isBlank(orderStr)) {
            return null;
        }
        Map<String, String> map = null;
        try {
            map = WXPayUtil.xmlToMap(orderStr);
            // https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
            // trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
            String codeURL = map.get("code_url"); //用户扫描支付的URL
            if(StringUtils.isBlank(codeURL)){
                throw new RuntimeException("codeURL is empty!");
            }
            return codeURL;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
