package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.service.UserService;
import net.xdclass.xdvideo.utils.JwtUtils;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Autowired
    private UserService userService;

    /**
     *
     * @param accessPage 用户扫码确认后的登录地址
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/login_url")
    @ResponseBody
    public JsonData loginUrl(@RequestParam(value = "access_page", required = true) String accessPage) throws UnsupportedEncodingException {
        //获取微信开放平台重定向地址,然后进行编码，http://16webtest.ngrok.xiaomiqiu.cn 是微信开放平台注册单位的授权回调域名
        //wxopen.redirect_url=http://16webtest.ngrok.xiaomiqiu.cn/api/v1/wechat/user/callback1
        String openRedirectUrl = weChatConfig.getOpenRedirectUrl();
        String callbakcUrl = URLEncoder.encode(openRedirectUrl, "GBK");

        //微信开放平台二维码链接：
        //https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect
        String OPEN_QRCODE_URL = weChatConfig.getOPEN_QRCODE_URL();
        //微信开放平台openAppId
        String openAppId = weChatConfig.getOpenAppId();
        //替换s%,拼接成真实url
        String qrcodeUrl = String.format(OPEN_QRCODE_URL, openAppId, callbakcUrl, accessPage);
        return JsonData.buildSuccess(qrcodeUrl);
    }
    //请求： http://localhost:8080/api/v1/wechat/login_url?access_page=http://www.xdclass.net     注意：access_page需要加上http://
    /*响应：
    * {"code":0,"data":"https://open.weixin.qq.com/connect/qrconnect?appid=wx025575eac69a2d5b&redirect_uri=http%3A%2F%2F16webtest.ngrok.xiaomiqiu.cn%2Fapi%2Fv1%2Fwechat%2Fuser%2Fcallback1&response_type=code&scope=snsapi_login&state=www.xdclass.net#wechat_redirect","msg":null}
    * */

    /**
     * 扫码确认之后，微信平台回调的地址
     * 通过授权码code获取微信用户个人信息（这个接口是微信那边回调接口，需要先启动内网穿透工具）
     * 步骤1：通过code获取access_token
     * 步骤2：通过access_token获取微信用户头像和昵称等基本信息
     * @param code
     * @param state 当前用户的页面地址，需要拼接http://
     * @param response
     */
    @GetMapping("/user/callback1")
    public void wechatUserCallBack(@RequestParam(value="code",required = true) String code, String state, HttpServletResponse response) throws IOException {
//        System.out.println("code=" +code + ",state=" + state);
        User user = userService.saveWeChatUser(code);
        if(user != null){
            String token = JwtUtils.generateJsonWebToken(user);
            String name  = URLEncoder.encode(user.getName() ,"UTF-8");
            response.sendRedirect(state + "?token=" + token + "&head_img=" + user.getHeadImg() + "&nickname=" + name);
        }



    }

}
