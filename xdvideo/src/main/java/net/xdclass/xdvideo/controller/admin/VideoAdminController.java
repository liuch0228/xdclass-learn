package net.xdclass.xdvideo.controller.admin;

import net.xdclass.xdvideo.config.WeChatConfig;
import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.mapper.VideoMapper;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 以管理员权限操作视频接口
 */
@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {
    @Autowired
    private VideoService videoService;

    /**
     * 分页查询接口
     *
     * @param page 当前页 默认1
     * @param size 页大小 默认10
     * @return
     */
    @GetMapping("page")
    public Object pageVideo(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        return videoService.findAll();
    }

    /**
     * 根据id查询视频
     *
     * @param vdeioId
     * @return
     */
    @GetMapping("find_by_id")
    public Object findById(@RequestParam(value = "video_id", required = true) int vdeioId) {
        return videoService.findById(vdeioId);
    }







}
