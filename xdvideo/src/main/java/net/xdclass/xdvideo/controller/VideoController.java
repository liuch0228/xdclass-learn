package net.xdclass.xdvideo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
