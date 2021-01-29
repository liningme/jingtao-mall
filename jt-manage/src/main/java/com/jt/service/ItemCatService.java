package com.jt.service;

import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

import java.util.List;

public interface ItemCatService {
    ItemCat findItemCat(Long itemCatId);

    List<EasyUITree> findItemList(Long parentId);

//    List<EasyUITree> findItemCatCache(long parentId);
}
