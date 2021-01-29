package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_cart")
public class Cart extends BasePojo{

    @TableId(type = IdType.AUTO)
    private Long id;     //id属性
    private Long userId;    //用户编号
    private Long itemId;    //商品编号
    private String itemTitle;   //商品标题
    private String itemImage;   //商品图片
    private Long itemPrice;     //商品价格
    private Integer num;        //商品数量
}
