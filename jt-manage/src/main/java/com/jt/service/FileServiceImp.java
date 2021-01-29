package com.jt.service;

import com.jt.vo.ImageVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImp implements FileService{

    @Value("${image.localDir}")
    private String localDir ;
    @Value("${image.urlName}")
    private String urlName;
    private static Set<String> sets = new HashSet<>();
    static{
        sets.add(".jpg");
        sets.add(".png");
        sets.add(".gif");
    }
    /*
     * 1.校验图片的类型    a.jpg
     * 2.校验是否为恶意程序
     * 3.采用分目录方式进行存储
     * 4.防止文件重名 动态生成ID
     * @param uploadFile
     * @return
     */
    @Override
    public ImageVo upload(MultipartFile uploadFile) {

//        1 获取图片名称
        String fileName = uploadFile.getOriginalFilename();
        fileName = fileName.toLowerCase();

//        2 检验是否为图片
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(!sets.contains(fileType))
        {
            return ImageVo.fail();
        }

//        如果不属于类型，就不是图片

//        3 校验是否为恶意程序，将数据转换为图片类型检查是否正常转化
        try {
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if (width == 0 || height == 0)
                return ImageVo.fail();
//        4 hash方式/时间方式
            String dateDir = new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());///yyyy/MM/dd

//        5 定义磁盘文件存储的目录
            String dirPath = localDir + dateDir;//D:/JT-SOFT/images/yyyy/MM/dd
            File dirFile = new File(dirPath);
            if (!dirFile.exists())
            {
                dirFile.mkdirs();
            }

//        6 动态生成UUID
            String uuid = UUID.randomUUID().toString();
            String realFileName = uuid + fileType;//uuid.jpg

//        7 实现文件上传
            //磁盘地址: D:/JT-SOFT/images/yyyy/MM/dd/uuid.jpg
            uploadFile.transferTo(new File(dirPath + realFileName));
            //虚拟地址: http://image.jt.com/yyyy/MM/dd/uuid.jpg
            String url = urlName + dateDir + realFileName;

            return ImageVo.success(url,width,height);
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVo.fail();
        }


    }
}
