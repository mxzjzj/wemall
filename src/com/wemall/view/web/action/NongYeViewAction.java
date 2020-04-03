package com.wemall.view.web.action;

import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.Md5Encrypt;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.domain.query.BargainGoodsQueryObject;
import com.wemall.foundation.service.*;
import com.wemall.manage.admin.tools.MsgTools;
import com.wemall.view.web.tools.GoodsFloorViewTools;
import com.wemall.view.web.tools.GoodsViewTools;
import com.wemall.view.web.tools.NavViewTools;
import com.wemall.view.web.tools.StoreViewTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商城农业控制器
 */
@Controller
public class NongYeViewAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IGoodsClassService goodsClassService;

    @Autowired
    private IGoodsBrandService goodsBrandService;

    @Autowired
    private IPartnerService partnerService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleClassService articleClassService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IAccessoryService accessoryService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IStoreService storeService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private INavigationService navigationService;

    @Autowired
    private IGroupGoodsService groupGoodsService;

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGoodsFloorService goodsFloorService;

    @Autowired
    private IBargainGoodsService bargainGoodsService;

    @Autowired
    private IDeliveryGoodsService deliveryGoodsService;

    @Autowired
    private IStoreCartService storeCartService;

    @Autowired
    private IGoodsCartService goodsCartService;

    @Autowired
    private NavViewTools navTools;

    @Autowired
    private GoodsViewTools goodsViewTools;

    @Autowired
    private StoreViewTools storeViewTools;

    @Autowired
    private MsgTools msgTools;

    @Autowired
    private GoodsFloorViewTools gf_tools;

    @RequestMapping({"/index_nongye.htm"})
    public ModelAndView index_nongye(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("index_nongye.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        // 设置为PC访问
        request.getSession().setAttribute("wemall_view_type", "pc");

        Map params = new HashMap();

        // 推荐品牌
        params.put("audit", Integer.valueOf(1));
        params.put("recommend", Boolean.valueOf(true));
        List gbs = this.goodsBrandService.query(
                "select obj from GoodsBrand obj where obj.audit=:audit and obj.recommend=:recommend order by obj.sequence",
                params, 0, 4);
        mv.addObject("gbs", gbs);
        // 底部显示的合作伙伴
        params.clear();
        List img_partners = this.partnerService.query(
                "select obj from Partner obj where obj.image.id is not null order by obj.sequence asc", params, -1, -1);
        mv.addObject("img_partners", img_partners);
        List text_partners = this.partnerService.query(
                "select obj from Partner obj where obj.image.id is null order by obj.sequence asc", params, -1, -1);
        mv.addObject("text_partners", text_partners);
        // 底部新闻分类显示
        params.clear();
        params.put("mark", "news");
        List acs = this.articleClassService.query(
                "select obj from ArticleClass obj where obj.parent.id is null and obj.mark!=:mark order by obj.sequence asc",
                params, 0, 9);
        mv.addObject("acs", acs);
        // 商城新闻
        params.clear();
        params.put("class_mark", "news");
        params.put("display", Boolean.valueOf(true));
        List articles = this.articleService.query(
                "select obj from Article obj where obj.articleClass.mark=:class_mark and obj.display=:display order by obj.addTime desc",
                params, 0, 5);
        mv.addObject("articles", articles);
        // 查询推荐店铺商品
        params.clear();
        params.put("store_recommend", Boolean.valueOf(true));
        params.put("goods_status", Integer.valueOf(0));
        List store_reommend_goods_list = this.goodsService.query(
                "select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc",
                params, -1, -1);
        List store_reommend_goods = new ArrayList();
        int max = store_reommend_goods_list.size() >= 5 ? 4 : store_reommend_goods_list.size() - 1;
        for (int i = 0; i <= max; i++) {
            store_reommend_goods.add((Goods) store_reommend_goods_list.get(i));
        }
        // 1、推荐商品可在后台编辑
        mv.addObject("store_reommend_goods", store_reommend_goods);
        mv.addObject("store_reommend_goods_count", Double.valueOf(
                Math.ceil(CommUtil.div(Integer.valueOf(store_reommend_goods_list.size()), Integer.valueOf(5)))));
        mv.addObject("goodsViewTools", this.goodsViewTools);
        mv.addObject("storeViewTools", this.storeViewTools);
        if (SecurityUserHolder.getCurrentUser() != null) {
            mv.addObject("user", this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId()));
        }
        // 团购查询
        params.clear();
        params.put("beginTime", new Date());
        params.put("endTime", new Date());
        List groups = this.groupService.query(
                "select obj from Group obj where obj.beginTime<=:beginTime and obj.endTime>=:endTime", params, -1, -1);
        if (groups.size() > 0) {
            // 2、团购商品
            params.clear();
            params.put("gg_status", Integer.valueOf(1));
            params.put("gg_recommend", Integer.valueOf(1));
            params.put("group_id", ((Group) groups.get(0)).getId());
            List ggs = this.groupGoodsService.query(
                    "select obj from GroupGoods obj where obj.gg_status=:gg_status and obj.gg_recommend=:gg_recommend and obj.group.id=:group_id order by obj.gg_recommend_time desc",
                    params, 0, 5);
            mv.addObject("ggs", ggs);
        }

        // 4、热销商品倒序-疯狂抢购
        params.clear();
        params.put("goods_status", Integer.valueOf(0));
        List list = this.goodsService.query(
                "select obj from Goods obj where obj.goods_status=:goods_status order by obj.goods_salenum desc",
                params, 0, 5);
        mv.addObject("fengKuangs", list);

        // 5、随机生成推荐商品 猜您喜欢
        List store_guess_goods = new ArrayList();
        Random rand = new Random();
        int recommend_goods_random = rand.nextInt(5);
        int begin = recommend_goods_random * 5;
        if (begin > store_reommend_goods_list.size() - 1) {
            begin = 0;
        }
        int maxsize = begin + 5;
        if (maxsize > store_reommend_goods_list.size()) {
            begin -= maxsize - store_reommend_goods_list.size();
            maxsize--;
        }
        for (int i = 0; i < store_reommend_goods_list.size(); i++) {
            if ((i >= begin) && (i < maxsize)) {
                store_guess_goods.add((Goods) store_reommend_goods_list.get(i));
            }
        }
        mv.addObject("cais", store_guess_goods);
        // 6、满送商品
        params.clear();
        params.put("d_status", Integer.valueOf(1));
        params.put("d_begin_time", new Date());
        params.put("d_end_time", new Date());
        List dgs = this.deliveryGoodsService.query(
                "select obj from DeliveryGoods obj where obj.d_status=:d_status and obj.d_begin_time<=:d_begin_time and obj.d_end_time>=:d_end_time order by obj.d_audit_time desc",
                params, 0, 5);
        mv.addObject("dgs", dgs);

        // 7、新品上架
        params.clear();
        params.put("goods_status", Integer.valueOf(0));
        List new_goods_list = this.goodsService.query(
                "select obj from Goods obj where obj.goods_status=:goods_status order by obj.addTime desc", params, 0,
                5);
        mv.addObject("xinShangs", new_goods_list);

        // 8、点击数最多:人气商品
        params.clear();
        params.put("goods_status", Integer.valueOf(0));
        List hot_goods_list = this.goodsService.query(
                "select obj from Goods obj where obj.goods_status=:goods_status order by obj.goods_click desc", params,
                0, 5);
        mv.addObject("hots", hot_goods_list);

        // 9、锡好秒杀
        List<Goods> spike_list = new ArrayList<Goods>();
        try {
            params.clear();
            params.put("bg_status", Integer.valueOf(1));
            params.put("bg_time", new Date());
            params.put("deleteStatus", false);
            List<BargainGoods> bargain_list = this.bargainGoodsService.query(
                    "SELECT obj FROM BargainGoods obj WHERE obj.bg_status=:bg_status AND obj.bg_time=:bg_time AND obj.deleteStatus=:deleteStatus order by obj.audit_time desc",
                    params, 0, 5);
            for (int i = 0; i < bargain_list.size(); i++) {
                spike_list.add(bargain_list.get(i).getBg_goods());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("spikes", spike_list);
        return mv;
    }

    @RequestMapping({"/floor_nongye.htm"})
    public ModelAndView floor(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("floor.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        Map params = new HashMap();
        params.put("gf_display", Boolean.valueOf(true));
        params.put("start", 100);
        params.put("end", 199);
        List floors = this.goodsFloorService.query(
                "select obj from GoodsFloor obj where obj.gf_display=:gf_display and obj.parent.id is null and obj.gf_sequence>=:start and obj.gf_sequence<=:end order by obj.gf_sequence asc",
                params, -1, -1);
        mv.addObject("floors", floors);
        mv.addObject("gf_tools", this.gf_tools);
        mv.addObject("url", CommUtil.getURL(request));
        return mv;
    }

    @RequestMapping({"/wap/index_nongye.htm"})
    public ModelAndView wap_index_nongye(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("goodsclass.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
        if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
            mv = new JModelAndView("wap/index_nongye.html", this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
        }
        return mv;
    }

    @RequestMapping({"/wap/floor_nongye.htm"})
    public ModelAndView fwap_floor(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("wap/floor.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gf_display", Boolean.valueOf(true));
        params.put("start", 100);
        params.put("end", 199);
        List floors = this.goodsFloorService.query(
                "select obj from GoodsFloor obj where obj.gf_display=:gf_display and obj.parent.id is null and obj.gf_sequence>=:start and obj.gf_sequence<=:end order by obj.gf_sequence asc",
                params, -1, -1);
        mv.addObject("floors", floors);
        mv.addObject("gf_tools", this.gf_tools);
        mv.addObject("url", CommUtil.getURL(request));
        return mv;
    }
}
