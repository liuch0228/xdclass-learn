package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private WeChatConfig weChatConfig;

    //VideoMapper不加@Component，这里会有报错提示(IDEA设置问题)
    @Autowired
    private VideoMapper videoMapper;

    //测试热部署
    @RequestMapping("test1")
    public String test1() {
        System.out.println("xdclass.net");
        return "hello xdclass.net777";
    }

    //测试微信公众号的配置读取
    //http://localhost:8080/testconfig
    //  {"appId":"wx5beac15ca207cdd40c","appsecret":"554801238f17fdsdsdd6f96b382fe548215e9"}
    @GetMapping("testconfig")
    @ResponseBody
    public Object testconfig() {
        String appId = weChatConfig.getAppId();
        String appsecret = weChatConfig.getAppsecret();
        Map map = new HashMap();
        map.put("appId", appId);
        map.put("appsecret", appsecret);
        return map;
    }

    //http://localhost:8080/testDB
    //{"id":1,"title":"SpringBoot+Maven整合Websocket课程","summary":"这是概要","coverImg":null,"viewNum":null,"price":1000,"createTime":null,"online":0,"point":8.7}
    /* 接口中增加@Results注解之后：{"id":1,"title":"SpringBoot+Maven整合Websocket课程","summary":"这是概要","coverImg":"https://xd-video-pc-img.oss-cn-beijing.aliyuncs.com/upload/video/video_cover.png","viewNum":12,"price":1000,"createTime":null,"online":0,"point":8.7}
    * */
    @GetMapping("testDB")
    @ResponseBody
    public Object testDB() {
        return videoMapper.findAll();
    }


}
