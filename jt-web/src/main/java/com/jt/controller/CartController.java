package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
@RequestMapping("/cart")
public class CartController {
    @Reference(check = false)
    private DubboCartService dubboCartService;
    /*
     * 删除购物车Controller
     * URL地址: http://www.jt.com/cart/delete/1474391990.html
     * 返回值:  重定向到购物车列表页面
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteItem(Cart cart)
    {

        long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.deleteItemByItemId(cart);
        return "redirect:/cart/show.html";

    }




    /*
     * 完成购物车新增操作
     * URL地址:   http://www.jt.com/cart/add/562379.html
     * 参数:      整个form表单
     * 返回值:    重定向到购物车列表页面
     */
    @RequestMapping("/add/{itemId}")
    public String addCart(Cart cart)
    {
        long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.addCart(cart);
        return "redirect:/cart/show.html";
    }



    /*
     * 实现商品数量的更新操作
     * url地址:   http://www.jt.com/cart/update/num/1474391990/16
     * 参数:      itemId/num
     * 返回值:    void
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public void updateCartNum(Cart cart)
    {
        long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.updateCartNumById(cart);
    }

    /*
     * url: http://www.jt.com/cart/show.html
     * 返回值: 购物车列表页面 cart.jsp
     * 页面数据取值:  ${cartList}
     */
    @RequestMapping("/show")
    public String cartShow(Model model, HttpServletRequest request)
    {
//        User user = (User)request.getAttribute("JT_USER");

//        long userId = user.getId();
        long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = dubboCartService.findCartListByUserId(userId);
        model.addAttribute("cartList",cartList);
        return "cart";
    }

}
