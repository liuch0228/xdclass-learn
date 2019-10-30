### 1.springboot项目的启动类注意事项
springboot项目的启动类，必须放到项目根路径下，springboot启动时，
会自动扫描启动类所在的包的所有子包里面的类然后去创建bean


### @MapperScan注解：扫描mapper接口
在springboot启动类上面加@MapperScan("net.xdclass.xdvideo.mapper")注解，增加mapper扫描


### 使用注解开发时，数据库字段与POJO属性名不一致的处理
方法1-使用@Results注解：@Result(column = "数据库字段名", property = "POJO属性名"),
```java
  @Select("select * from video")
    @Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "cover_img", property = "coverImg"),
            @Result(column = "view_num", property = "viewNum")
    })
    List<Video> findAll();
```
方法2-开启mybatis的下划线自动转驼峰
在配置文件中添加：
```
#mybatis.configuration.mapUnderscoreToCamelCase=true
 mybatis.configuration.map-underscore-to-camel-case=true
```
### springboot动态Sql语句Mybaties SqlProvider
```java
package net.xdclass.xdvideo.provider;

import net.xdclass.xdvideo.domain.Video;
import org.apache.ibatis.jdbc.SQL;

/**
 * video模块的mybatis sqlProvider 构建动态sql语句
 */
public class VideoProvider {
  
    public String updateVideo(final Video video) {
        return new SQL() {
            {
                UPDATE("video");
                if (video.getTitle() != null) {
                    SET("title=#{title}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
//使用：
  @UpdateProvider(type= VideoProvider.class,method = "updateVideo")
  int update(Video video);
```
### springboot下mybatis分页插件PageHelper的使用
配置插件
```java
package net.xdclass.xdvideo.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis分页插件配置类
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        //设置为true，会将 RowBounds第一个参数offset当成pageNum页码使用
        p.setProperty("offSetAsPageNum", "true");
        //设置为true时，使用rowBounds分页会进行count查询
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
```
 controller中使用插件
```java
 /*
@GetMapping("page")
    public Object pageVideo(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "8") int size) {
		PageHelper.startPage(page,size);
		List<Video> list = videoService.findAll();
		//返回分页数据 pageInfo包含了与分页有关的所有数据
		PageInfo<Video> pageInfo = new PageInfo(list);
		//只取total和list返回
		Map map = new HashMap<String,Object>();
		map.put("total_size",pageInfo.getTotal());
		map.put("total_page",pageInfo.getPages());
		map.put("current_page",page);
		map.put("data",pageInfo.getList());
		return map;
    }
*/
```
分页插件原理：
sqlsessionFactory -> sqlSession-> executor -> mybatis sql statement
			通过mybatis plugin 增加拦截器，然后拼装分页
			org.apache.ibatis.plugin.Interceptor
			
### 微信Oath2.0交互流程
1. 微信用户请求三方应用后台(如小D课堂)，换句话说，用户使用微信账号请求登录小D课堂
2. 三方应用请求微信Oath2.0授权登录，微信返回三方应用一个二维码链接地址，
前端拿到这个地址进行渲染，展示扫码页面（请求用户确认）
3. 用户扫码确认后，请求微信，微信平台获取用户同意授权之后
4. 微信平台携带临时授权票据code,拉起第三方应用或重定向到第三方应用
5. 三方应用通过code加上appid和appsecret请求微信平台换取access_token
6. 微信平台返回access_token给三方应用
7. 三方应用通过access_token去获取微信用户基本开放信息
https://developers.weixin.qq.com/doc/oplatform/Website_App/WeChat_Login/Wechat_Login.html

### 微信扫码页面测试
1. 本地hosts配置127.0.0.1 16webtest.ngrok.xiaomiqiu.cn 即微信开放平台的授权回调地址
2. 配置文件配置：
- 微信开放平台配置
  1. wxopen.appid=wx025575eac69a2d5b
  2. wxopen.appsecret=f5b6730c592ac15b8b1a5aeb8948a9f3
  3. 重定向URL
wxopen.redirect_url=http://16webtest.ngrok.xiaomiqiu.cn/api/v1/wechat/user/callback1
3. 启动项目，访问http://localhost:8080/api/v1/wechat/login_url?access_page=www.xdclass.net
，响应为：
```java
{"code":0,"data":"https://open.weixin.qq.com/connect/qrconnect?appid=wx025575eac69a2d5b&redirect_uri=http%3A%2F%2F16webtest.ngrok.xiaomiqiu.cn%2Fapi%2Fv1%2Fwechat%2Fuser%2Fcallback1&response_type=code&scope=snsapi_login&state=www.xdclass.net#wechat_redirect","msg":null}
```
取其中的data的值，拷贝出来贴到浏览器中访问，微信返回一个二维码页面，用手机扫描二维码，
手机微信提示："狼图申请使用以下信息  你的账号信息（昵称，头像，地区及性别）"，点击同意之后，
将会跳转到配置文件中配置的重定向URL，并且带上微信返回的票据ocde和access_page,通过这个code去换取access_token
http://16webtest.ngrok.xiaomiqiu.cn/api/v1/wechat/user/callback1?code=001Pdzed1rishA0HMXgd1lRned1Pdze7&state=www.xdclass.net