package net.xdclass.xdvideo.controller;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * video接口
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {
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
