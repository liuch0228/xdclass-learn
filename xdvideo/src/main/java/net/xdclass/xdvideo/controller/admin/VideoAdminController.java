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
     * 根据id删除视频
     *
     * @param vdeioId
     * @return
     */
    @DeleteMapping("del_by_id")
    public Object delById(@RequestParam(value = "video_id", required = true) int vdeioId) {
        return videoService.delete(vdeioId);
    }

    /**
     * 根据id更新视频
     *
     * @param video
     * @return
     */
    @PutMapping("/update_by_id")
    public Object updateVideo(@RequestBody Video video) {
//@RequestBody表示请求参数在请求体中，需要指定请求头contentType为application/json
        return videoService.update(video);
    }
	/*@PutMapping("/update_by_id")
	public Object updateVideo (int videoid,String title){
		Video video = new Video();
		video.setId(videoid);
		video.setTitle(title);
		return videoService.update(video);
	}*/

    /**
     * 保存视频对象
     *
     * @param title
     * @return
     */
    @PostMapping("save")
    public Object save(String title) {
        Video video = new Video();
        video.setTitle(title);
        return videoService.save(video);
    }








}
