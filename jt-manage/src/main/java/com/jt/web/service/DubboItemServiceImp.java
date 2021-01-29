package com.jt.web.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(timeout = 3000)
public class DubboItemServiceImp implements DubboItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;

    /*
     * 检查数据库中是否有562379的数据
     * @param itemId
     * @return
     */

    @Override
    public Item findItemById(long itemId) {
        return itemMapper.selectById(itemId);
    }

    @Override
    public ItemDesc findItemDescById(long itemId) {
        return itemDescMapper.selectById(itemId);
    }
}
