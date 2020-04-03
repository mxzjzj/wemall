package com.wemall.manage.seller.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Store;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.service.*;
import com.wemall.manage.seller.tools.MenuTools;
import com.wemall.view.web.tools.AreaViewTools;
import com.wemall.view.web.tools.OrderViewTools;
import com.wemall.view.web.tools.StoreViewTools;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卖家中心控制器
 */
@Controller
public class BaseSellerAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IStoreService storeService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private StoreViewTools storeViewTools;

    @Autowired
    private OrderViewTools orderViewTools;

    @Autowired
    private AreaViewTools areaViewTools;

    @Autowired
    private MenuTools menuTools;

    @SecurityMapping(display = false, rsequence = 0, title = "卖家中心", value = "/seller/index.htm*", rtype = "buyer", rname = "卖家中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/seller/index.htm"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/seller_index.html", this.configService.getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        User user = this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId());
        List msgs = new ArrayList();
        Map params = new HashMap();
        params.put("status", Integer.valueOf(0));
        params.put("user_id", SecurityUserHolder.getCurrentUser().getId());
        msgs = this.messageService.query(
                   "select obj from Message obj where obj.status=:status and obj.toUser.id=:user_id and obj.parent.id is null",
                   params, -1, -1);
        params.clear();
        params.put("class_mark", "notice");
        params.put("display", Boolean.valueOf(true));
        List articles = this.articleService.query(
                            "select obj from Article obj where obj.articleClass.mark=:class_mark and obj.display=:display order by obj.addTime desc",
                            params, 0, 5);
        mv.addObject("articles", articles);
        mv.addObject("user", user);
        mv.addObject("store", user.getStore());
        mv.addObject("msgs", msgs);
        mv.addObject("storeViewTools", this.storeViewTools);
        mv.addObject("orderViewTools", this.orderViewTools);
        mv.addObject("areaViewTools", this.areaViewTools);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家中心导航", value = "/seller/nav.htm*", rtype = "buyer", rname = "卖家中心导航", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/seller/nav.htm"})
    public ModelAndView nav(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("user/default/usercenter/seller_nav.html", this.configService.getSysConfig(),
                                            this.userConfigService.getUserConfig(), 0, request, response);
        int store_status = 0;
        Store store = this.storeService.getObjByProperty("user.id", SecurityUserHolder.getCurrentUser().getId());
        if (store != null) {
            store_status = store.getStore_status();
        }
        String op = CommUtil.null2String(request.getAttribute("op"));
        mv.addObject("op", op);
        mv.addObject("store_status", Integer.valueOf(store_status));
        mv.addObject("user", this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId()));

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家中心导航", value = "/seller/head.htm*", rtype = "buyer", rname = "卖家中心导航", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/seller/nav_head.htm"})
    public ModelAndView nav_head(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/seller_head.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        String type = CommUtil.null2String(request.getAttribute("type"));
        mv.addObject("type", type.equals("") ? "goods" : type);
        mv.addObject("menuTools", this.menuTools);
        mv.addObject("user", this.userService.getObjById(
                         SecurityUserHolder.getCurrentUser().getId()));

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家中心快捷功能设置", value = "/seller/store_quick_menu.htm*", rtype = "seller", rname = "用户中心", rcode = "user_center_seller", rgroup = "用户中心")
    @RequestMapping( {"/seller/store_quick_menu.htm"})
    public ModelAndView store_quick_menu(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/store_quick_menu.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "卖家中心快捷功能设置保存", value = "/seller/store_quick_menu_save.htm*", rtype = "seller", rname = "用户中心", rcode = "user_center_seller", rgroup = "用户中心")
    @RequestMapping( {"/seller/store_quick_menu_save.htm"})
    public ModelAndView store_quick_menu_save(HttpServletRequest request, HttpServletResponse response, String menus) {
        String[] menu_navs = menus.split(",");
        User user = this.userService.getObjById(
                        SecurityUserHolder.getCurrentUser().getId());
        List list = new ArrayList();
        for (String menu_nav : menu_navs) {
            if (!menu_nav.equals("")) {
                String[] infos = menu_nav.split("\\|");
                Map map = new HashMap();
                map.put("menu_url", infos[0]);
                map.put("menu_name", infos[1]);
                list.add(map);
            }
        }
        user.setStore_quick_menu(Json.toJson(list, JsonFormat.compact()));
        this.userService.update(user);
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/store_quick_menu_info.html",
            this.configService.getSysConfig(), this.userConfigService
            .getUserConfig(), 0, request, response);
        mv.addObject("user", user);
        mv.addObject("menuTools", this.menuTools);

        return mv;
    }
}




