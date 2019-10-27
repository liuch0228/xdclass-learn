package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 */
@Controller
@RequestMapping("/api/v1/wechat")
public class WechatController {

    @Autowired
    private WeChatConfig weChatConfig;

    /**
     * 拼装扫一扫url
     *
     * @return
     */
    @GetMapping("/login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        //获取微信开放平台重定向地址
        String openRedirectUrl = weChatConfig.getOpenRedirectUrl();
        String callbakcUrl = URLEncoder.encode(openRedirectUrl, "GBK");
        String OPEN_QRCODE_URL = weChatConfig.getOPEN_QRCODE_URL();
        String openAppId = weChatConfig.getOpenAppId();
        String qrcodeUrl = String.format(OPEN_QRCODE_URL,openAppId ,callbakcUrl,accessPage);
        return JsonData.buildSuccess(qrcodeUrl);
    }
    //http://localhost:8080/api/v1/wechat/login_url?access_page=www.xdclass.net

}
