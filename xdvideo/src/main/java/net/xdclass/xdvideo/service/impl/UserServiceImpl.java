package net.xdclass.xdvideo.service.impl;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.mapper.UserMapper;
import net.xdclass.xdvideo.service.UserService;
import net.xdclass.xdvideo.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User saveWeChatUser(String code) {
        // 组装accessTokenUrl
        // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String openAccessTokenUrl = WeChatConfig.OPEN_ACCESS_TOKEN_URL;
        String accessTokenUrl = String.format(openAccessTokenUrl, weChatConfig.getOpenAppId(), weChatConfig.getOpenAppSecret(), code);
        //获取微信accessToken
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        //响应示例：	{"access_token":"ACCESS_TOKEN", "expires_in":7200, "refresh_token":"REFRESH_TOKEN","openid":"OPENID", "scope":"SCOPE","unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"}
        if (null == baseMap && baseMap.isEmpty()) {
            return null;
        }
        String accessToken = (String) baseMap.get("access_token");
        String openid = (String) baseMap.get("openid");
        User dbUser = userMapper.findByopenid(openid);
        if(dbUser!=null){
            return dbUser; //用户信息已存在，就直接使用
        }

        //获取用户基本信息 https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        /**
         * 响应：
         * {"openid":"OPENID","nickname":"NICKNAME","sex":1,"province":"PROVINCE","city":"CITY","country":"COUNTRY",
         * "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
         * "privilege":["PRIVILEGE1","PRIVILEGE2"],"unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"}
         */
        String userInfoUrl = WeChatConfig.OPNE_USER_INFO_URL;
        String userInfoReqUrl = String.format(userInfoUrl, accessToken, openid);
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoReqUrl);
        if (null == baseUserMap && baseUserMap.isEmpty()) {
            return null;
        }

        Double sexTemp = (Double) baseUserMap.get("sex"); //微信返回的sex是double类型
        int sex = sexTemp.intValue();
        String province = (String) baseUserMap.get("province");
        String city = (String) baseUserMap.get("city");
        String country = (String) baseUserMap.get("country");
        String headimgurl = (String) baseUserMap.get("headimgurl");
        String nickname = (String) baseUserMap.get("nickname");
        city = new StringBuilder(country).append("||").append(province)
                .append("||").append(city).toString();
        try {
            //编码转换，解决中文乱码问题
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        User user = new User();
        user.setName(nickname);
        user.setHeadImg(headimgurl);
        user.setCity(city);
        user.setOpenid(openid);
        user.setSex(sex);
        user.setCreateTime(new Date());
        userMapper.save(user);
        return user;
    }
}
