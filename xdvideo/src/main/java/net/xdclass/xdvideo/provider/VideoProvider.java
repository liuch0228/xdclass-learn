package net.xdclass.xdvideo.provider;

import net.xdclass.xdvideo.domain.Video;
import org.apache.ibatis.jdbc.SQL;

/**
 * video模块的mybatis sqlProvider 构建动态sql语句
 */
public class VideoProvider {
    /**
     * 动态更新video语句
     *
     * @param video
     * @return
     */
    public String updateVideo(final Video video) {
        return new SQL() {
            {
                UPDATE("video");
                if (video.getTitle() != null) {
                    SET("title=#{title}");
                }
                if (video.getSummary() != null) {
                    SET("summary=#{summary}");
                }
                if (video.getCoverImg() != null) {
                    SET("cover_img=#{coverImg}");
                }

                if (video.getViewNum() != null) {
                    SET("view_num=#{viewNum}");
                }
                if (video.getPrice() != null) {
                    SET("price=#{price}");
                }

                if (video.getOnline() != null) {
                    SET("online=#{online}");
                }
                Double point = video.getPoint();
                if (point != null) {
                    SET("point=#{point}");
                }
                WHERE("id=#{id}");
            }
        }.toString();
    }
}
