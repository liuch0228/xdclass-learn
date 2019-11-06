package net.xdclass.xdvideo.interceptor;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import net.xdclass.xdvideo.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 进入controller之前进行拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static Gson gson = new Gson();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isNotBlank(token)) {
            Claims claims = JwtUtils.checkToken(token);
            if (claims != null) {
                Integer userId = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                request.setAttribute("user_id", userId);
                request.setAttribute("name", name);
                return true;
            }
        }
        sendJsonMessage(response, "请登陆");
        return false;
    }

    /**
     * 相应数据给客户端
     *
     * @param response
     * @param message
     */
    public static void sendJsonMessage(HttpServletResponse response, Object message) {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(gson.toJson(message));
            writer.close();
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
