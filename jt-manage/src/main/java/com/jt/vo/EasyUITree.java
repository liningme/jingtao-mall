package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree implements Serializable {

    private static final long serialVersionUID = -6026585273875121549L;
    private Long id;//编号
    private String text;//节点名称
    private String state;//接点状态
}
