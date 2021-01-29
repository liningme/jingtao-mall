package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service(timeout = 3000)
public class DobboCartServiceImp implements DubboCartService{
    @Autowired
    private CartMapper cartMapper;
    //删除商品
    @Override
    public void deleteItemByItemId(Cart cart) {
        cartMapper.delete(new QueryWrapper<>(cart));

    }


    /*
     * 业务逻辑:
     *  如果购物车中已经存在数据 则更新数量.
     *  如果购物车中没有数据     则新增数据.
     *
     *  更新数量:
     *      update tb_cart set num=#{num} where id = #{id}
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //1 查询数据库，检验是否有值 user_id item_id
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",cart.getUserId());
        queryWrapper.eq("item_id",cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB == null)//没有值，直接插入值
        {
            cartMapper.insert(cart);
        }else
        {
            //更新数据库
            int num = cart.getNum() + cartDB.getNum();
            Cart CartTem = new Cart();
            CartTem.setId(cartDB.getId());
            CartTem.setNum(num);
            cartMapper.updateById(CartTem);
        }

    }





    /*
     * Sql: update tb_cart set num=#{num},updated = #{updated}
     *      where item_id=#{itemId} and user_id = #{userId}
     * @param cart
     *
     * 更新操作:  entity 表示要更新的数据
     *           更新的条件
     */

    @Override
    public void updateCartNumById(Cart cart) {
        Cart cartTem = new Cart();
        cartTem.setNum(cart.getNum());
        UpdateWrapper<Cart> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id",cart.getUserId());
        updateWrapper.eq("item_id",cart.getItemId());
        cartMapper.update(cartTem, updateWrapper);

    }



    //购物车列表展示
    @Override
    public List<Cart> findCartListByUserId(long userId) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return cartMapper.selectList(queryWrapper);
    }



}
