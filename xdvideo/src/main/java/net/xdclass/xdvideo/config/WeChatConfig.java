package net.xdclass.xdvideo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

/**
 * 2-7 接口配置文件自动映射到属性和实体类配置
 * 配置类（微信支付相关配置扫描）
 */
//@Component
@Configuration
@PropertySource(value = "classpath:application.properties")
public class WeChatConfig {

    /**
     * 公众号appid
     */
    @Value("${wxpay.appid}")
    private String appId;

    /**
     * 公众号密钥
     */
    @Value("${wxpay.appsecret}")
    private String appsecret;

    /**
     * 开放平台appId
     */
    @Value("${wxopen.appid}")
    private String openAppId;

    /**
     * 开放平台appsecret
     */
    @Value("${wxopen.appsecret}")
    private String openAppSecret;

    /**
     * 开放平台回调url
     *
     */
    @Value("${wxopen.redirect_url}")
    private String openRedirectUrl;

    /**
     * 微信开放平台二维码连接,使用 s% 占位符表示可变参数
     */
    public final String OPEN_QRCODE_URL= "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";
    /**
     * 获取微信开放平台access_token地址，APPID  SECRET  CODE要替换
     * https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
     */
    public static  final String OPEN_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 获取微信用户基本信息API地址 ACCESS_TOKEN OPENID参数需要替换  lang=zh_CN需要指定！！！不然拿到的所有信息都是英文的
     * https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
     */
    public static final String OPNE_USER_INFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s%lang=zh_CN";

    /**
     * 微信商户号id
     */
    @Value("${wxpay.mer_id}")
    private String mchId;

    /**
     * 支付key
     */
    @Value("${wxpay.key}")
    private String key;

    /**
     * 支付回调url
     */
    @Value("${wxpay.callback}")
    private String payCallbackUrl;

    /**
     * 统一下单url
     */
    private String UNIFIED_ORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";

    public String getUNIFIED_ORDER_URL() {
        return UNIFIED_ORDER_URL;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayCallbackUrl() {
        return payCallbackUrl;
    }

    public void setPayCallbackUrl(String payCallbackUrl) {
        this.payCallbackUrl = payCallbackUrl;
    }

    public String getOPEN_QRCODE_URL() {
        return OPEN_QRCODE_URL;
    }

    public String getOpenRedirectUrl() {
        return openRedirectUrl;
    }

    public void setOpenRedirectUrl(String openRedirectUrl) {
        this.openRedirectUrl = openRedirectUrl;
    }

    public String getOpenAppId() {
        return openAppId;
    }

    public void setOpenAppId(String openAppId) {
        this.openAppId = openAppId;
    }

    public String getOpenAppSecret() {
        return openAppSecret;
    }

    public void setOpenAppSecret(String openAppSecret) {
        this.openAppSecret = openAppSecret;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }
}
