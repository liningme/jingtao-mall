package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	/*
	/item/query/item/desc/

	*/

	@RequestMapping("/query/item/desc/{itemId}")
	public SysResult findItemDescByID(@PathVariable Long itemId)
	{
		ItemDesc itemDesc = itemService.findItemById(itemId);
		return SysResult.success(itemDesc);
	}




	/*利用一个方法实现 商品的上架/下架
	* url1：/item/instock 下架 status=2
	* url2：/item/reshelf 上架 status=1
	* */
	@RequestMapping("/{status}")
	public SysResult updateStatus(Long[] ids, @PathVariable Integer status)
	{
		itemService.updateStatus(ids, status);
		return SysResult.success();
	}





	/*商品删除
	* url：/item/delete
	* 参数：ids:100,101,102
	* 返回值*/
	@RequestMapping("/delete")
	public SysResult itemDelete(Long... ids)
	{
		itemService.itemDelete(ids);
		return SysResult.success();
	}


	/*url:/item/update
	* 参数：form表单
	* 返回值：sysresult
	* */
	@RequestMapping("/update")
	public SysResult itemUpate(Item item,ItemDesc itemDesc)
	{
		itemService.itemUpdate(item, itemDesc);
		return SysResult.success();
	}


	/*实现新增
	 * url：/item/save
	 * 参数：form表单
	 * 返回值：sysresult
	 * */
	@Transactional
	@RequestMapping("/save")
	public SysResult itemSave(Item item, ItemDesc itemDesc) {

		itemService.itemSave(item,itemDesc);

		return SysResult.success();
	}
	/*
	 * 1.实现商品类标展现
	 * url地址: http://localhost:8091/item/query?page=1&rows=20
	 * 参数:	page页数    rows 行数
	 * 返回值:  EasyUITable
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(int page, int rows)
	{
		return itemService.findItemByPage(page,rows);
	}



}
