package com.jt.controller;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    /*url:'/item/cat/list'
    * 请求参数：*/


    @RequestMapping("/list")
    public List<EasyUITree> findItemList(Long id)
    {
        long parentId = (id == null) ? 0 : id;
        return itemCatService.findItemList(parentId);
//        return itemCatService.findItemCatCache(parentId);
    }

    @RequestMapping("/queryItemName")
    public String findItemCat(Long itemCatId)
    {
        ItemCat itemCat = itemCatService.findItemCat(itemCatId);
        return itemCat.getName();




    }
}
