package com.jt.service;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

    EasyUITable findItemByPage(int page, int rows);

    void itemSave(Item item, ItemDesc itemDesc);

    void itemUpdate(Item item, ItemDesc itemDesc);

    void itemDelete(Long[] ids);

    void updateStatus(Long[] ids, Integer status);

    ItemDesc findItemById(Long itemId);
}
