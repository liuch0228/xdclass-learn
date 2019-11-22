package net.xdclass.xdvideo.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.service.UserService;
import net.xdclass.xdvideo.service.VideoOrderService;
import net.xdclass.xdvideo.utils.JwtUtils;
import net.xdclass.xdvideo.utils.WXPayUtil;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

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

    @Autowired
    private VideoOrderService videoOrderService;

    /**
     * 生成扫一扫登录url
     *
     * @param accessPage 用户扫码确认后的访问地址(也就是用户登录之前的当前所在页面地址)
     * @return 微信授权一键登录授权URL
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
     *
     * @param code
     * @param state    当前用户的页面地址，需要拼接http://
     * @param response
     */
    @GetMapping("/user/callback1")
    public void wechatUserCallBack(@RequestParam(value = "code", required = true) String code, String state, HttpServletResponse response) throws IOException {
        System.out.println("code=" + code + ",state=" + state);
        User user = userService.saveWeChatUser(code);
        if (user != null) {
            String token = JwtUtils.generateJsonWebToken(user);
            String name = URLEncoder.encode(user.getName(), "UTF-8");
            //回调域名有问题，把域名替换成localhost:8080就可以正确回调了
            String state1 = state.replace("http://xdclasstest2.ngrok.xiaomiqiu.cn", "http://localhost:8080");
            //重定向到用户 登录之前的页面,下面这种写法有问题
            response.sendRedirect(state1 + "?token=" + token + "&head_img=" + user.getHeadImg() + "&nickname=" + name);
        }
    }

    /**
     * 微信支付回调接口
     * 1.接口幂等性：相同 参数和值，不管接口调用多少次，响应结果和调用一次是一样的
     * 2.校验签名是否正确，防止伪造回调
     * 3.更新订单状态
     * 4.通知微信订单状态更新结果
     *
     * @param request
     * @param response
     */
    @GetMapping("/order/callback")
    public void oroderCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("微信支付成功！回调通知接口");
        //微信返回的数据是流的形式
        ServletInputStream inputStream = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        inputStream.close();
        Map<String, String> callBackMap = WXPayUtil.xmlToMap(sb.toString());
        System.out.println(callBackMap.toString());
        SortedMap<String, String> sortedMap = WXPayUtil.getSortedMap(callBackMap);

        //判断签名是否正确
        if (WXPayUtil.isCorrectSign(sortedMap, weChatConfig.getKey())) {
            System.out.println("ok");
            if ("SUCCESS".equals(sortedMap.get("result_code"))) {
                //从微信的响应中获取订单流水号
                String outTradeNo = sortedMap.get("out_trade_no");
                VideoOrder dbVideoOrder = videoOrderService.findByOutTradeNo(outTradeNo);
                //未支付状态，才更新
                if (null != dbVideoOrder && dbVideoOrder.getState() == 0) {
                    VideoOrder videoOrder = new VideoOrder();
                    videoOrder.setOpenid(sortedMap.get("openid"));
                    videoOrder.setOutTradeNo(outTradeNo);
                    videoOrder.setNotifyTime(new Date());
                    videoOrder.setState(1);
                    //更新订单状态
                    int row = videoOrderService.updateVideoOderByOutTradeNo(videoOrder);
                    if (row == 1) {
                        //通知微信，订单支付成功
                        response.setContentType("text/xml");
                        response.getWriter().println("success");
                        return;
                    }
                }

            }
        }
        //订单支付失败
        response.setContentType("text/xml");
        response.getWriter().println("fail");
    }


}
