package net.xdclass.xdvideo.service;

import net.xdclass.xdvideo.domain.User;

import java.io.UnsupportedEncodingException;

/**
 * 用户业务接口
 */
public interface UserService {
    User  saveWeChatUser(String code);
}
