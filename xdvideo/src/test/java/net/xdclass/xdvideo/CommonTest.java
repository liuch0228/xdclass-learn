package net.xdclass.xdvideo;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.jsonwebtoken.Claims;
import net.xdclass.xdvideo.domain.User;
import net.xdclass.xdvideo.utils.JwtUtils;
import org.junit.Test;

public class CommonTest {

    @Test
    public void testJwt() {
        User user = new User();
        user.setId(1);
        user.setHeadImg("www.xdclass.net");
        user.setName("admin");
        String token = JwtUtils.generateJsonWebToken(user);
        System.out.println(token);
        /*
         * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjEsIm5hbWUiOiJhZG1pbiIsImltZyI6Ind3dy54ZGNsYXNzLm5ldCIsImlhdCI6MTU3MjA5ODI0MSwiZXhwIjoxNTcyMTg0NjQxfQ.dOEL4e6QJRBeVIzWoDDedtKn62X3ecHEeWiGIIhfGRY

         * */
    }

    @Test
    public void testCheckJwt() {

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZGNsYXNzIiwiaWQiOjEsIm5hbWUiOiJhZG1pbiIsImltZyI6Ind3dy54ZGNsYXNzLm5ldCIsImlhdCI6MTU3MjA5ODI0MSwiZXhwIjoxNTcyMTg0NjQxfQ.dOEL4e6QJRBeVIzWoDDedtKn62X3ecHEeWiGIIhfGRY";
        Claims claims = JwtUtils.checkToken(token);
        if(claims != null){
            Integer id = claims.get("id",Integer.class);
            String name = claims.get("name", String.class) ;
            String img = claims.get("img", String.class) ;
            System.out.println(id);
            System.out.println(name);  //输出 admin
            System.out.println(img);  // 输出 www.xdclass.net




        }


    }
}
