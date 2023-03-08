package cn.postwall.blog.utils;

import cn.postwall.blog.pojo.constant.SysConstant;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHelper {

    private static final Logger logger = LoggerFactory.getLogger(FileHelper.class);

    /**
     * response  返回图片
     * @param file
     * @param response
     * @throws IOException
     */
    public static void responseWriteImage(File file, HttpServletResponse response) throws IOException {
        OutputStream os = null;
        FileInputStream fileInputStream = null;
        try{
            os = response.getOutputStream();
            fileInputStream = new FileInputStream(file);
            BufferedImage read = ImageIO.read(fileInputStream);
            if (read!=null){
                String suffix = FileHelper.getSuffix(file.getName()).substring(1);
                ImageIO.write(read, suffix, os);
            }
        }catch (IOException e){
            logger.error("获取图片异常{}",e.getMessage());
        }finally {
            if (os != null){
                os.close();
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }


    /**
     * 删除图片
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        boolean delete = false;
        try {
            delete = file.delete();
        } finally {
            System.gc();
        }
        return delete;
    }

    /**
     * 保存Base64图片,
     * @param base64
     * @return 返回图片地址
     */
    public static String saveBase64Image(String base64) {
        // 保存图片
        String suffix = FileHelper.isBase64(base64);
        if (suffix != null) {
            String imagesPath = String.valueOf(CacheHelper.get("IMAGES_PATH"));
            String generateFileName = FileHelper.generateFileName();
            String fileName = generateFileName + suffix;
            File file = new File(imagesPath, fileName);
            boolean flag = saveBase64File(base64, file.getPath());
            if (flag) {
                return SysConstant.API_IMAGE + file.getName();
            }
        }
        return null;
    }



    /**
     * 保存base64编码文件
     * @param base64
     * @param filePath
     */
    public static boolean saveBase64File(String base64, String filePath) {
        BufferedImage image = null;
        ByteArrayInputStream bis = null;
        byte[] imageByte = null;
        try {
            String replace = base64.replace(" ", "+");
            int index = replace.indexOf("base64,");
            imageByte = DatatypeConverter.parseBase64Binary(replace.substring(index + 7));
            bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            File outputFile = new File(filePath);
            String suffix = getSuffix(outputFile.getName()).substring(1);
            ImageIO.write(image, suffix, outputFile);
        } catch (IOException e) {
            logger.error("Base64编码保存失败：{}", e);
            return false;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     *  判断是否为base64编码
     * @param str
     * @return
     */
    public static String isBase64(String str) {
        if (str == null || "".equals(str)) {
            return null;
        }
        String base64Pattern = "data:image/(?<suffix>\\w+);base64,.*";
        Pattern pattern = Pattern.compile(base64Pattern);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return "." + matcher.group("suffix");
        }
        return null;
    }


    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        if (fileName == null || "".equals(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index);
    }


    /**
     * 随机生成文件名
     * @return
     */
    public static String generateFileName() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        String random = String.valueOf(threadLocalRandom.nextInt(9999));
        String exTime = DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now());
        //外部系统需要的随机数，不足4位的补4位
        String mixId = lpad(random,4,"0");
        //混合随机数（两种随机数算法，减少重复率）
        return exTime + mixId;
    }


    public static String lpad(String s, int n, String replace) {
        if (s.length() >= n) {
            return s.substring(0, n);
        }

        StringBuffer out = new StringBuffer(s);
        while (out.length() < n) {
            out.insert(0, replace);
        }
        return out.toString();
    }

    /**
     * 获取文件的前缀
     * @param fileName
     * @return
     */
    public static String getPrefix(String fileName) {
        int index = fileName.lastIndexOf(".");
        String result = fileName.substring(0, index);
        return result;
    }

    /**
     * 保存文件路径
     * @param inputStream
     * @param fileName
     * @return
     */
    public static String saveFile(InputStream inputStream, String fileName, String filePath) throws IOException {
        fileName = new Date().getTime() + getSuffix(fileName);
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
        byte[] bytes = new byte[2048];
        int line;
        while ((line = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes,0,line);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
        return fileName;
    }

}
