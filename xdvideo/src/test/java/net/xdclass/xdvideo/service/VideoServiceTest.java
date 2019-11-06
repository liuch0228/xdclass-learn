package net.xdclass.xdvideo.service;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.mapper.VideoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 测试类，进入VideoService接口，选中一个方法鼠标右键单击->goto->test 添加单元测试，自动生成如下代码
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void findAll() {
        List<Video> videoList = videoService.findAll();
        assertNotNull(videoList);
        System.out.println("查询结果:");
        videoList.forEach(e -> {
            System.out.println(e.getTitle());
        });
    }

    @Test
    public void findById() {
        Video video = videoService.findById(1);
        assertNotNull(video);

    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void save() {
    }
}