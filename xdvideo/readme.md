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