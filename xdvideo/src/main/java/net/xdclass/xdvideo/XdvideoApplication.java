package net.xdclass.xdvideo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//整合mybatis,增加mapper扫描,加载配置和注入到sqlSessionFactory等都是springBoot完成
@MapperScan("net.xdclass.xdvideo.mapper")
public class XdvideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(XdvideoApplication.class, args);
	}
}
