package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ItemDescMapper itemDescMapper;

	@Transactional
	@Override
	public ItemDesc findItemById(Long itemId) {
		return itemDescMapper.selectById(itemId);
	}


	@Transactional
	@Override
	public void updateStatus(Long[] ids, Integer status) {

		Item item = new Item();
		item.setStatus(status);

		UpdateWrapper updateWrapper = new UpdateWrapper();
		updateWrapper.in("id",Arrays.asList(ids));
		itemMapper.update(item, updateWrapper);


	}




	@Transactional
	@Override
	public void itemDelete(Long[] ids) {

//		1 mp方式
//		itemMapper.deleteBatchIds(Arrays.asList(ids));
//		2 手写sql
		itemMapper.deleteIDs(ids);
		itemDescMapper.deleteBatchIds(Arrays.asList(ids));

	}


	@Transactional
	@Override
	public void itemUpdate(Item item, ItemDesc itemDesc) {
		itemMapper.updateById(item);
		itemDesc.setItemId(item.getId());//item的id传给itemDesc
		itemDescMapper.updateById(itemDesc);//itemdesc更新


	}


	@Transactional
	@Override
	public void itemSave(Item item, ItemDesc itemDesc) {
		item.setStatus(1);
		itemMapper.insert(item);

//		商品详情入库，mp自动回显
		itemDesc.setItemId(item.getId());
		/*
		.setCreated(new Date())
		.setUpdated(new Date());*/

		itemMapper.insert(item);

	}



	/*方法一*/
	/*@Override
	public EasyUITable findItemByPage(int page, int rows) {


		Integer total = itemMapper.selectCount(null);
		int start = (page - 1) * rows;

		List<Item> itemList = itemMapper.findItem(start,rows);

		return new EasyUITable(total,itemList);

	}*/

	/*
	 * 业务需求:	1.分页查询数据库记录
	 * 			2.封装VO对象
	 * 作业1:  手写分页的方式实现分页查询  sql
	 * 作业2:  查看官网API 实现MP分页查询
	 *
	 * @param page
	 * @param rows
	 * @return
	 */
	@Override
	public EasyUITable findItemByPage(int page, int rows) {
		IPage<Item> iPage = new Page<>(page,rows);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByDesc("updated");
		iPage = itemMapper.selectPage(iPage, queryWrapper);
		long total = iPage.getTotal();
		List<Item> items = iPage.getRecords();


		return new EasyUITable(total,items);

	}


}
