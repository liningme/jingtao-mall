package com.jt.service;

import annoation.CacheFind;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImp implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    /*@Autowired(required = false)
    private Jedis jedis;*/


    @CacheFind(key = "ITEM_CAT_ID")
    @Override
    public ItemCat findItemCat(Long itemCatId) {

        return itemCatMapper.selectById(itemCatId);
    }


    @CacheFind(key = "ITEM_CAT_PARENT")//添加自定义注解 添加业务名称
    @Override
    public List<EasyUITree> findItemList(Long parentId) {
        List<EasyUITree> easyUITrees = new ArrayList<>();
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",parentId);
        List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);

        for (ItemCat it : itemCatList)
        {
            long id = it.getId();
            String text = it.getName();
            String state = it.getIsParent() ? "closed" : "open";
            EasyUITree tree = new EasyUITree(id,text,state);
            easyUITrees.add(tree);
        }
        return easyUITrees;
    }

    /*
     * 实现步骤:
     *      1.先定义key     ITEM_CAT_PARENT::0
     *      2.先查询缓存
     *          有  true    获取缓存数据之后,将JSON转化为对象 之后返回
     *          没有 false   应该查询数据库, 之后将数据保存到redis中. 默认30天超时.
     * * @param parentId
     * @return
     */
    /*@Override
    public List<EasyUITree> findItemCatCache(long parentId) {

        List<EasyUITree> treeList = new ArrayList<>();

//        1 定义key
        String key = "ITEM_CAT_PARENT::" + parentId;


//        从缓存中获取数据
        if (jedis.exists(key))
        {
            String json = jedis.get(key);
            treeList = ObjectMapperUtil.toObj(json,findItemList(parentId).getClass());
            System.out.println("查询redis缓存,获取数据");
        }
        else
        {
            treeList = findItemList(parentId);
            String json = ObjectMapperUtil.toJSON(treeList);
            jedis.setex(key,7 * 24 * 60 * 60,json );
            System.out.println("查询数据库中获取的结果");


        }


        return treeList;
    }*/
}
