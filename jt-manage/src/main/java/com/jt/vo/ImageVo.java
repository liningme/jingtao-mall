package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.rmi.server.UnicastRemoteObject;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ImageVo {
    private Integer error;      //0 上传成功  1上传失败
    private String  url;        //网络虚拟地址
    private Integer width;      //宽度
    private Integer height;     //高度

    public static ImageVo fail()
    {
        return new ImageVo(1,null,null,null);
    }
    public static ImageVo success(String url,Integer width, Integer height)
    {
        return new ImageVo(0, url, width, height);
    }
// {"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
}