package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class FileController {
    @Autowired
    private FileService fileService;
    /*
     * 实现文件上传
     * url地址:  http://localhost:8091/pic/upload?dir=image
     * 参数:     uploadFile
     * 返回值:    ImageVO对象
     *
     * 文件上传考虑哪些:
     *     1.校验图片类型   .jpg
     *     2.校验是否为恶意程序
     *     3.分目录存储
     *     4.文件名称禁止重复...
     */
    @RequestMapping("/pic/upload")
    public ImageVo upload(MultipartFile uploadFile)
    {
        return fileService.upload(uploadFile);
    }




    /*文件上传入门
    * url：/file
    * 参数： xxx fileImage
    * 返回值：string类型
    * springmvc提供顶级接口，来上传文件
    * */

    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        String dirPath = "D:/JT-SOFT/images";
        File dirFile = new File(dirPath);
        //        判断地址是否存在
        if(!dirFile.exists())
            dirFile.mkdirs();//        不存在就创建目录
//        动态获取图片存储目录
        String fileName = fileImage.getOriginalFilename();
//        确定文件上传全路径
        File file = new File(dirFile + "/" + fileName);
//        实现文件上传
        fileImage.transferTo(file);
        return "文件上传成功";
    }
}
