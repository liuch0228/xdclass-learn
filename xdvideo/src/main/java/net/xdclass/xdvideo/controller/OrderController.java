package net.xdclass.xdvideo.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.domain.VideoOrder;
import net.xdclass.xdvideo.dto.VideoOrderDto;
import net.xdclass.xdvideo.service.VideoOrderService;
import net.xdclass.xdvideo.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单接口
 */
@RestController
//@RequestMapping("/user/api/v1/order")
@RequestMapping("/api/v1/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Logger dataLogger = LoggerFactory.getLogger("dataLogger"); //dataLogger是日志配置文件中定义的log名称

    @Autowired
    private VideoOrderService videoOrderService;
    //    @GetMapping("/add")
//    public JsonData saveOrder(){
//        return JsonData.buildSuccess("下单成功");
//    }
    @GetMapping("/add")
    public void saveOrder(@RequestParam(value = "video_id", required = true) int videoId,
                              HttpServletRequest request, HttpServletResponse response) {

        //获取用户真实ip
        // String ip = IpUtils.getIpAddr(request);
        String ip = "";// 测试时，ip地址不能是本机回环地址
        //获取用户userId
        // int userId = (int)request.getAttribute("user_id");
        int userId= 1;
        VideoOrderDto videoOrderDto = new VideoOrderDto();
        videoOrderDto.setUserId(userId);
        videoOrderDto.setIp(ip);
        //下单的是哪个视频的订单
        videoOrderDto.setVideoId(videoId);

        String codeurl = videoOrderService.save(videoOrderDto);

        try {
            //生成支付二维码图片展示给用户
            Map<EncodeHintType,Object> hints = new HashMap<>();
            //设置纠错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //设置编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            //生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeurl, BarcodeFormat.QR_CODE, 400, 400);

            ServletOutputStream out = response.getOutputStream();
            //二维码写到客户端
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }


    }

}




