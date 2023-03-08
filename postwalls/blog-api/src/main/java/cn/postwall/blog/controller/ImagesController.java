package cn.postwall.blog.controller;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.utils.CacheHelper;
import cn.postwall.blog.utils.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author: liuhanchao
 * @Date: 2022/12/26 18:04
 */
@Controller
public class ImagesController {

    @RequestMapping(path = "/image/{fileName}", produces = "image/png")
    public void getImages(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        OutputStream outputStream = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(new File(String.valueOf(CacheHelper.get("IMAGES_PATH")) , fileName)));
            outputStream = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", outputStream);
            }
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public Result uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = FileHelper.saveFile(file.getInputStream(), file.getOriginalFilename(), String.valueOf(CacheHelper.get("IMAGES_PATH")));
        return Result.success(SysConstant.API_IMAGE + fileName,"上传成功");
    }

}
