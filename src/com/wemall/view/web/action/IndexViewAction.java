package com.wemall.view.web.action;

import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.Md5Encrypt;
import com.wemall.core.tools.SmsBase;
import com.wemall.foundation.domain.*;
import com.wemall.foundation.domain.query.BargainGoodsQueryObject;
import com.wemall.foundation.service.*;
import com.wemall.manage.admin.tools.MsgTools;
import com.wemall.view.web.tools.GoodsFloorViewTools;
import com.wemall.view.web.tools.GoodsViewTools;
import com.wemall.view.web.tools.NavViewTools;
import com.wemall.view.web.tools.StoreViewTools;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * 商城首页控制器
 */
@Controller
public class IndexViewAction {

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

	@Value("#{config.sms_host}")
	private String host;
	@Value("#{config.sms_account}")
	private String account;
	@Value("#{config.sms_password}")
	private String password;
	@Value("#{config.sms_signature}")
	private String signature;

	/**
	 * 页面最上面部分
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/top.htm" })
	public ModelAndView top(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("top.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		List msgs = new ArrayList();
		if (SecurityUserHolder.getCurrentUser() != null) {
			Map params = new HashMap();
			params.put("status", Integer.valueOf(0));
			params.put("reply_status", Integer.valueOf(1));
			params.put("from_user_id", SecurityUserHolder.getCurrentUser().getId());
			params.put("to_user_id", SecurityUserHolder.getCurrentUser().getId());
			msgs = this.messageService.query(
					"select obj from Message obj where obj.parent.id is null and (obj.status=:status and obj.toUser.id=:to_user_id) or (obj.reply_status=:reply_status and obj.fromUser.id=:from_user_id) ",
					params, -1, -1);
		}
		Store store = null;
		if (SecurityUserHolder.getCurrentUser() != null)
			store = this.storeService.getObjByProperty("user.id", SecurityUserHolder.getCurrentUser().getId());
		mv.addObject("store", store);
		mv.addObject("navTools", this.navTools);
		mv.addObject("msgs", msgs);
		List<GoodsCart> list = new ArrayList<GoodsCart>();
		List<StoreCart> cart = new ArrayList<StoreCart>();
		List<StoreCart> user_cart = new ArrayList<StoreCart>();
		List<StoreCart> cookie_cart = new ArrayList<StoreCart>();
		User user = null;
		if (SecurityUserHolder.getCurrentUser() != null) {
			user = this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId());
		}
		String cart_session_id = "";
		Map params = new HashMap();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart_session_id")) {
					cart_session_id = CommUtil.null2String(cookie.getValue());
				}
			}
		}
		if (user != null) {
			if (!cart_session_id.equals("")) {
				if (user.getStore() != null) {
					params.clear();
					params.put("cart_session_id", cart_session_id);
					params.put("user_id", user.getId());
					params.put("sc_status", Integer.valueOf(0));
					params.put("store_id", user.getStore().getId());
					List<StoreCart> store_cookie_cart = this.storeCartService.query(
							"select obj from StoreCart obj where (obj.cart_session_id=:cart_session_id or obj.user.id=:user_id) and obj.sc_status=:sc_status and obj.store.id=:store_id",
							params, -1, -1);
					for (StoreCart sc : store_cookie_cart) {
						for (GoodsCart gc : ((StoreCart) sc).getGcs()) {
							gc.getGsps().clear();
							this.goodsCartService.delete(gc.getId());
						}
						this.storeCartService.delete(((StoreCart) sc).getId());
					}
				}

				params.clear();
				params.put("cart_session_id", cart_session_id);
				params.put("sc_status", Integer.valueOf(0));
				cookie_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status",
						params, -1, -1);

				params.clear();
				params.put("user_id", user.getId());
				params.put("sc_status", Integer.valueOf(0));
				user_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params,
						-1, -1);
			} else {
				params.clear();
				params.put("user_id", user.getId());
				params.put("sc_status", Integer.valueOf(0));
				user_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params,
						-1, -1);
			}

		} else if (!cart_session_id.equals("")) {
			params.clear();
			params.put("cart_session_id", cart_session_id);
			params.put("sc_status", Integer.valueOf(0));
			cookie_cart = this.storeCartService.query(
					"select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status",
					params, -1, -1);
		}

		for (StoreCart sc : user_cart) {
			boolean sc_add = true;
			for (StoreCart sc1 : cart) {
				if (sc1.getStore().getId().equals(sc.getStore().getId())) {
					sc_add = false;
				}
			}
			if (sc_add)
				cart.add(sc);
		}
		boolean sc_add;
		for (StoreCart sc : cookie_cart) {
			sc_add = true;
			for (StoreCart sc1 : cart) {
				if (sc1.getStore().getId().equals(sc.getStore().getId())) {
					sc_add = false;
					for (GoodsCart gc : sc.getGcs()) {
						gc.setSc(sc1);
						this.goodsCartService.update(gc);
					}
					this.storeCartService.delete(sc.getId());
				}
			}
			if (sc_add) {
				cart.add(sc);
			}
		}
		if (cart != null) {
			for (StoreCart sc : cart) {
				if (sc != null) {
					list.addAll(sc.getGcs());
				}
			}
		}
		float total_price = 0.0F;
		for (GoodsCart gc : list) {
			Goods goods = this.goodsService.getObjById(gc.getGoods().getId());
			if (CommUtil.null2String(gc.getCart_type()).equals("combin"))
				total_price = CommUtil.null2Float(goods.getCombin_price());
			else {
				total_price = CommUtil.null2Float(
						Double.valueOf(CommUtil.mul(Integer.valueOf(gc.getCount()), goods.getGoods_current_price())))
						+ total_price;
			}
		}
		mv.addObject("total_price", Float.valueOf(total_price));
		mv.addObject("cart", list);

		return (ModelAndView) mv;
	}

	/**
	 * 横着的导航栏
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/nav.htm" })
	public ModelAndView nav(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("nav.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		mv.addObject("navTools", this.navTools);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("display", Boolean.valueOf(true));
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc",
				params, 0, 14);
		List childs = ((GoodsClass) gcs.get(0)).getChilds();
		mv.addObject("gcs", gcs);

		return mv;
	}

	@RequestMapping({ "/nav1.htm" })
	public ModelAndView nav1(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("nav1.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		Map params = new HashMap();
		params.put("display", Boolean.valueOf(true));
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc",
				params, 0, 14);
		mv.addObject("gcs", gcs);
		mv.addObject("navTools", this.navTools);

		return mv;
	}

	@RequestMapping({ "/head.htm" })
	public ModelAndView head(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("head.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String type = CommUtil.null2String(request.getAttribute("type"));
		mv.addObject("type", type.equals("") ? "goods" : type);

		return mv;
	}

	@RequestMapping({ "/login_head.htm" })
	public ModelAndView login_head(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("login_head.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		return mv;
	}

	@RequestMapping({ "/floor.htm" })
	public ModelAndView floor(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("floor.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		Map params = new HashMap();
		params.put("gf_display", Boolean.valueOf(true));
		params.put("start", 0);
		params.put("end", 99);
		List floors = this.goodsFloorService.query(
				"select obj from GoodsFloor obj where obj.gf_display=:gf_display and obj.parent.id is null and obj.gf_sequence>=:start and obj.gf_sequence<=:end order by obj.gf_sequence asc",
				params, -1, -1);
		mv.addObject("floors", floors);
		mv.addObject("gf_tools", this.gf_tools);
		mv.addObject("url", CommUtil.getURL(request));
		return mv;
	}

	@RequestMapping({ "/floor_ajax.htm" })
	public void floorAjax(HttpServletRequest request, HttpServletResponse response, Long id, Integer count) {
		Map params = new HashMap();
		params.put("gf_display", Boolean.valueOf(true));
		params.put("id", id);
		List floors = this.goodsFloorService.query(
				"select obj from GoodsFloor obj where obj.gf_display=:gf_display and obj.parent.id is null and obj.id=:id order by obj.gf_sequence asc",
				params, -1, -1);
		Map<String, Object> map = new HashMap<String, Object>();
		CommUtil.saveWebPaths(map, this.configService.getSysConfig(), request);
		String ret = showLoadFloorAjaxHtml(floors, count, CommUtil.getURL(request), map);
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping({ "/footer.htm" })
	public ModelAndView footer(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("footer.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			mv = new JModelAndView("wap/footer.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
		}
		mv.addObject("navTools", this.navTools);

		return mv;
	}

	@RequestMapping({ "/index.htm" })
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("index.html", this.configService.getSysConfig(),
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
			// params.put("group_id", ((Group) groups.get(0)).getId()); and
			// obj.group.id=:group_id
			List ggs = this.groupGoodsService.query(
					"select obj from GroupGoods obj where obj.gg_status=:gg_status and obj.gg_recommend=:gg_recommend  order by obj.gg_recommend_time desc",
					params, 0, 0);
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
					params, -1, -1);
			for (int i = 0; i < bargain_list.size(); i++) {
				spike_list.add(bargain_list.get(i).getBg_goods());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("spikes", spike_list);
		return mv;
	}

	@RequestMapping({ "/close.htm" })
	public ModelAndView wapclose(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("close.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			mv = new JModelAndView("wap/close.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
		}

		return mv;
	}

	@RequestMapping({ "/404.htm" })
	public ModelAndView error404(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("404.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			// String store_id = CommUtil.null2String( request.getSession( false
			// ).getAttribute( "store_id" ) );
			mv = new JModelAndView("wap/404.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("url", CommUtil.getURL(request) + "/wap/index.htm");
		}

		return mv;
	}

	@RequestMapping({ "/500.htm" })
	public ModelAndView error500(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("500.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			String store_id = CommUtil.null2String(request.getSession(false).getAttribute("store_id"));
			mv = new JModelAndView("wap/500.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("url", CommUtil.getURL(request) + "/wap/index.htm?store_id=" + store_id);
		}

		return mv;
	}

	@RequestMapping({ "/goods_class.htm" })
	public ModelAndView goods_class(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("goods_class.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			mv = new JModelAndView("wap/goods_class.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
		}
		Map params = new HashMap();
		params.put("display", Boolean.valueOf(true));
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc",
				params, -1, -1);
		mv.addObject("gcs", gcs);

		return mv;
	}

	@RequestMapping({ "/goodsclass.htm" })
	public ModelAndView goodsclass(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new JModelAndView("goodsclass.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			mv = new JModelAndView("wap/goodsclass.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
		}
		Map params = new HashMap();
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null  order by obj.sequence asc", params, -1,
				-1);
		mv.addObject("gcs", gcs);

		return mv;
	}

	@RequestMapping({ "/forget.htm" })
	public ModelAndView forget(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("forget.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		SysConfig config = this.configService.getSysConfig();
		if (!config.isEmailEnable()) {
			mv = new JModelAndView("error.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("op_title", "系统关闭邮件功能，不能找回密码");
			mv.addObject("url", CommUtil.getURL(request) + "/index.htm");
		}

		return mv;
	}

	@RequestMapping({ "/find_pws.htm" })
	public ModelAndView find_pws(HttpServletRequest request, HttpServletResponse response, String userName,
			String code) {
		ModelAndView mv = new JModelAndView("success.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		HttpSession session = request.getSession(false);
		String verify_code = (String) session.getAttribute("verify_code");

		if (!code.toUpperCase().equals(verify_code)) {
			mv = new JModelAndView("error.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("op_title", "验证码不正确");
			mv.addObject("url", CommUtil.getURL(request) + "/forget.htm");
			return mv;
		}

		User user = this.userService.getObjByProperty("mobile", userName);
		if (null == user) {
			user = this.userService.getObjByProperty("userName", userName);
		}
		if (null == user) {
			mv = new JModelAndView("error.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("op_title", "手机号不存在");
			mv.addObject("url", CommUtil.getURL(request) + "/forget.htm");
			return mv;
		}

		String pws = "123456";
		SmsBase sms = new SmsBase(host, account, password, signature);
		boolean ret = false;
		try {
			ret = sms.sendSms(userName, "尊敬的会员，您的忘记密码已重置为" + pws + "，请尽快登录系统重新设置。");
		} catch (Exception e) {
		}

		if (ret) {
			user.setPassword(Md5Encrypt.md5(pws));
			this.userService.update(user);
			mv.addObject("op_title", "新密码已经您的手机:<font color=red>" + userName + "</font>，请查收后重新登录");
			mv.addObject("url", CommUtil.getURL(request) + "/user/login.htm");
		} else {
			mv = new JModelAndView("error.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("op_title", "短信发送失败，密码暂未执行重置");
			mv.addObject("url", CommUtil.getURL(request) + "/forget.htm");
		}

		return mv;
	}

	@RequestMapping({ "/switch_recommend_goods.htm" })
	public ModelAndView switch_recommend_goods(HttpServletRequest request, HttpServletResponse response,
			int recommend_goods_random) {
		ModelAndView mv = new JModelAndView("switch_recommend_goods.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		Map params = new HashMap();
		params.put("store_recommend", Boolean.valueOf(true));
		params.put("goods_status", Integer.valueOf(0));
		List store_reommend_goods_list = this.goodsService.query(
				"select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc",
				params, -1, -1);
		List store_reommend_goods = new ArrayList();
		int begin = recommend_goods_random * 5;
		if (begin > store_reommend_goods_list.size() - 1) {
			begin = 0;
		}
		int max = begin + 5;
		if (max > store_reommend_goods_list.size()) {
			begin -= max - store_reommend_goods_list.size();
			max--;
		}
		for (int i = 0; i < store_reommend_goods_list.size(); i++) {
			if ((i >= begin) && (i < max)) {
				store_reommend_goods.add((Goods) store_reommend_goods_list.get(i));
			}
		}
		mv.addObject("store_reommend_goods", store_reommend_goods);

		return mv;
	}

	@RequestMapping({ "/outline.htm" })
	public ModelAndView outline(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("error.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		mv.addObject("op_title", "该用户在其他地点登录，您被迫下线！");
		mv.addObject("url", CommUtil.getURL(request) + "/index.htm");
		String wemall_view_type = CommUtil.null2String(request.getSession().getAttribute("wemall_view_type"));
		if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
			mv = new JModelAndView("wap/error.html", this.configService.getSysConfig(),
					this.userConfigService.getUserConfig(), 1, request, response);
			mv.addObject("url", CommUtil.getURL(request) + "/wap/index.htm");
		}

		return mv;
	}

	/** wap首页业务逻辑begin */

	/**
	 * wap首页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "/wap/index.htm" })
	public ModelAndView wapindex(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new JModelAndView("wap/index.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 1, request, response);
		// 设置为wap访问
		request.getSession().setAttribute("wemall_view_type", "wap");
		Map params = new HashMap();
		params.put("display", Boolean.valueOf(true));
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null and obj.display=:display order by obj.sequence asc",
				params, 0, 15);
		mv.addObject("gcs", gcs);
		params.clear();
		params.put("audit", Integer.valueOf(1));
		params.put("recommend", Boolean.valueOf(true));
		List gbs = this.goodsBrandService.query(
				"select obj from GoodsBrand obj where obj.audit=:audit and obj.recommend=:recommend order by obj.sequence",
				params, -1, -1);
		mv.addObject("gbs", gbs);
		params.clear();
		List img_partners = this.partnerService.query(
				"select obj from Partner obj where obj.image.id is not null order by obj.sequence asc", params, -1, -1);
		mv.addObject("img_partners", img_partners);
		List text_partners = this.partnerService.query(
				"select obj from Partner obj where obj.image.id is null order by obj.sequence asc", params, -1, -1);
		mv.addObject("text_partners", text_partners);
		params.clear();
		params.put("mark", "news");
		List acs = this.articleClassService.query(
				"select obj from ArticleClass obj where obj.parent.id is null and obj.mark!=:mark order by obj.sequence asc",
				params, 0, 9);
		mv.addObject("acs", acs);
		params.clear();
		params.put("store_recommend", Boolean.valueOf(true));
		params.put("goods_status", Integer.valueOf(0));
		List store_reommend_goods_list = this.goodsService.query(
				"select obj from Goods obj where obj.store_recommend=:store_recommend and obj.goods_status=:goods_status order by obj.store_recommend_time desc",
				params, 0, 6);
		List store_reommend_goods = new ArrayList();
		int max = store_reommend_goods_list.size() >= 21 ? 20 : store_reommend_goods_list.size() - 1;
		for (int i = 0; i <= max; i++) {
			store_reommend_goods.add((Goods) store_reommend_goods_list.get(i));
		}
		mv.addObject("store_reommend_goods", store_reommend_goods);

		mv.addObject("store_reommend_goods_count", Double.valueOf(
				Math.ceil(CommUtil.div(Integer.valueOf(store_reommend_goods_list.size()), Integer.valueOf(5)))));
		mv.addObject("goodsViewTools", this.goodsViewTools);
		mv.addObject("storeViewTools", this.storeViewTools);
		if (SecurityUserHolder.getCurrentUser() != null) {
			mv.addObject("user", this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId()));
		}
		params.clear();
		params.put("beginTime", new Date());
		params.put("endTime", new Date());
		List groups = this.groupService.query(
				"select obj from Group obj where obj.beginTime<=:beginTime and obj.endTime>=:endTime", params, -1, -1);
		if (groups.size() > 0) {
			params.clear();
			params.put("gg_status", Integer.valueOf(1));
			params.put("gg_recommend", Integer.valueOf(1));
			params.put("group_id", ((Group) groups.get(0)).getId());
			List ggs = this.groupGoodsService.query(
					"select obj from GroupGoods obj where obj.gg_status=:gg_status and obj.gg_recommend=:gg_recommend and obj.group.id=:group_id order by obj.gg_recommend_time desc",
					params, 0, 1);
			if (ggs.size() > 0)
				mv.addObject("group", ggs.get(0));
		}
		params.clear();
		params.put("bg_time", CommUtil.formatDate(CommUtil.formatShortDate(new Date())));
		params.put("bg_status", Integer.valueOf(1));
		List bgs = this.bargainGoodsService.query(
				"select obj from BargainGoods obj where obj.bg_time=:bg_time and obj.bg_status=:bg_status", params, 0,
				5);
		mv.addObject("bgs", bgs);
		params.clear();
		params.put("d_status", Integer.valueOf(1));
		params.put("d_begin_time", new Date());
		params.put("d_end_time", new Date());
		List dgs = this.deliveryGoodsService.query(
				"select obj from DeliveryGoods obj where obj.d_status=:d_status and obj.d_begin_time<=:d_begin_time and obj.d_end_time>=:d_end_time order by obj.d_audit_time desc",
				params, 0, 3);
		mv.addObject("dgs", dgs);

		List msgs = new ArrayList();
		if (SecurityUserHolder.getCurrentUser() != null) {
			params.clear();
			params.put("status", Integer.valueOf(0));
			params.put("reply_status", Integer.valueOf(1));
			params.put("from_user_id", SecurityUserHolder.getCurrentUser().getId());
			params.put("to_user_id", SecurityUserHolder.getCurrentUser().getId());
			msgs = this.messageService.query(
					"select obj from Message obj where obj.parent.id is null and (obj.status=:status and obj.toUser.id=:to_user_id) or (obj.reply_status=:reply_status and obj.fromUser.id=:from_user_id) ",
					params, -1, -1);
		}
		Store store = null;
		if (SecurityUserHolder.getCurrentUser() != null)
			store = this.storeService.getObjByProperty("user.id", SecurityUserHolder.getCurrentUser().getId());
		mv.addObject("store", store);
		mv.addObject("navTools", this.navTools);
		mv.addObject("msgs", msgs);
		List<GoodsCart> list = new ArrayList<GoodsCart>();
		List<StoreCart> cart = new ArrayList<StoreCart>();
		List<StoreCart> user_cart = new ArrayList<StoreCart>();
		List<StoreCart> cookie_cart = new ArrayList<StoreCart>();
		User user = null;
		if (SecurityUserHolder.getCurrentUser() != null) {
			user = this.userService.getObjById(SecurityUserHolder.getCurrentUser().getId());
		}
		String cart_session_id = "";
		params.clear();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("cart_session_id")) {
					cart_session_id = CommUtil.null2String(cookie.getValue());
				}
			}
		}
		if (user != null) {
			if (!cart_session_id.equals("")) {
				if (user.getStore() != null) {
					params.clear();
					params.put("cart_session_id", cart_session_id);
					params.put("user_id", user.getId());
					params.put("sc_status", Integer.valueOf(0));
					params.put("store_id", user.getStore().getId());
					List<StoreCart> store_cookie_cart = this.storeCartService.query(
							"select obj from StoreCart obj where (obj.cart_session_id=:cart_session_id or obj.user.id=:user_id) and obj.sc_status=:sc_status and obj.store.id=:store_id",
							params, -1, -1);
					for (StoreCart sc : store_cookie_cart) {
						for (GoodsCart gc : ((StoreCart) sc).getGcs()) {
							gc.getGsps().clear();
							this.goodsCartService.delete(gc.getId());
						}
						this.storeCartService.delete(((StoreCart) sc).getId());
					}
				}

				params.clear();
				params.put("cart_session_id", cart_session_id);
				params.put("sc_status", Integer.valueOf(0));
				cookie_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status",
						params, -1, -1);

				params.clear();
				params.put("user_id", user.getId());
				params.put("sc_status", Integer.valueOf(0));
				user_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params,
						-1, -1);
			} else {
				params.clear();
				params.put("user_id", user.getId());
				params.put("sc_status", Integer.valueOf(0));
				user_cart = this.storeCartService.query(
						"select obj from StoreCart obj where obj.user.id=:user_id and obj.sc_status=:sc_status", params,
						-1, -1);
			}

		} else if (!cart_session_id.equals("")) {
			params.clear();
			params.put("cart_session_id", cart_session_id);
			params.put("sc_status", Integer.valueOf(0));
			cookie_cart = this.storeCartService.query(
					"select obj from StoreCart obj where obj.cart_session_id=:cart_session_id and obj.sc_status=:sc_status",
					params, -1, -1);
		}

		for (StoreCart sc : user_cart) {
			boolean sc_add = true;
			for (StoreCart sc1 : cart) {
				if (sc1.getStore().getId().equals(sc.getStore().getId())) {
					sc_add = false;
				}
			}
			if (sc_add)
				cart.add(sc);
		}
		boolean sc_add;
		for (StoreCart sc : cookie_cart) {
			sc_add = true;
			for (StoreCart sc1 : cart) {
				if (sc1.getStore().getId().equals(sc.getStore().getId())) {
					sc_add = false;
					for (GoodsCart gc : sc.getGcs()) {
						gc.setSc(sc1);
						// gc.setSc( sc );
						this.goodsCartService.update(gc);
					}
					this.storeCartService.delete(sc.getId());
				}
			}
			if (sc_add) {
				cart.add(sc);
			}
		}
		if (cart != null) {
			for (StoreCart sc : cart) {
				if (sc != null) {
					list.addAll(sc.getGcs());
				}
			}
		}
		float total_price = 0.0F;
		for (GoodsCart gc : list) {
			Goods goods = this.goodsService.getObjById(gc.getGoods().getId());
			if (CommUtil.null2String(gc.getCart_type()).equals("combin"))
				total_price = CommUtil.null2Float(goods.getCombin_price());
			else {
				total_price = CommUtil.null2Float(
						Double.valueOf(CommUtil.mul(Integer.valueOf(gc.getCount()), goods.getGoods_current_price())))
						+ total_price;
			}
		}
		mv.addObject("total_price", Float.valueOf(total_price));
		mv.addObject("cart", list);

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

		// 10、最新上架，按id倒序 取33条
		List<Goods> newGoodsList = new ArrayList<Goods>();
		try {
			newGoodsList = this.goodsService.query(
					"Select goods from Goods goods where goods.goods_status=0 order by goods.id desc", null, 0, 9);
		} catch (Exception e) {
		}
		mv.addObject("new_list", newGoodsList);
		// 生鲜食饮的第一个子类下面的商品
		List<Goods> sxysGoods = getGoodsByParentGoodsFloorId(2);
		mv.addObject("sxysGoods", sxysGoods);
		// 手机数码的第一个子类下面的商品
		List<Goods> sjsmGoods = getGoodsByParentGoodsFloorId(131091);
		mv.addObject("sjsmGoods", sjsmGoods);
		// 美妆个护的第一个子类下面的商品
		List<Goods> mzghGoods = getGoodsByParentGoodsFloorId(98304);
		mv.addObject("mzghGoods", mzghGoods);
		// 户外运动 的第一个子类下面的商品
		List<Goods> hwydGoods = getGoodsByParentGoodsFloorId(131092);
		mv.addObject("hwydGoods", hwydGoods);
		return mv;
	}

	private List<Goods> getGoodsByParentGoodsFloorId(int parentGoodsFloorId) {

		List<GoodsFloor> floors = this.goodsFloorService.query("select obj from GoodsFloor obj where  obj.parent.id = "
				+ parentGoodsFloorId + "order by obj.gf_sequence asc", null, 0, 1);
		if (floors.isEmpty() || floors.get(0) == null) {
			return Collections.emptyList();
		}

		GoodsFloor floor = floors.get(0);
		String gf_gc_goods = floor.getGf_gc_goods();
		try {
			List<Integer> goodsIds = new ArrayList<Integer>();
			JSONObject json = JSONObject.fromObject(gf_gc_goods);
			Collection<String> values = json.values();
			for (String value : values) {
				try {
					goodsIds.add(Integer.valueOf(value));
				} catch (Exception e) {
				}
			}
			if (goodsIds.isEmpty()) {
				return Collections.emptyList();
			}
			String hql = "Select obj From Goods obj where obj.id in (" + StringUtils.join(goodsIds, ",") + ") ";
			List<Goods> goods = goodsService.query(hql, null, 0, 9);
			return goods;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String showLoadFloorAjaxHtml(List lists, int i, String url, Map<String, Object> map) {

		String img = null;
		String loadimg = map.get("imageWebServer") + "/resources/style/common/images/loader.gif";
		String errorimg = map.get("webPath") + "/" + map.get("goodsImagePath") + "/" + map.get("goodsImageName");
		String goods_url = null;
		String goods_class_url = null;
		String child_goods_class_url = null;
		GoodsFloor floor = (GoodsFloor) lists.get(0);

		img = null;
		StringBuffer sb = new StringBuffer(1000);
		sb.append("<div class='floor " + floor.getGf_css() + "'>")
				.append("<div class='floor_box' id='floor_" + i + "'>");
		sb.append("<div class='floor_menu'>").append("<div class='title'>").append("<div class='txt-type'>")
				.append("<span>").append(i).append("</span>");
		sb.append("<h2 title='").append(floor.getGf_name()).append("'>").append(floor.getGf_name())
				.append("</h2></div></div><div class='flr_m_details'>");
		List<GoodsClass> gcs = this.gf_tools.generic_gf_gc(floor.getGf_gc_list());
		sb.append("<div class='flr_advertisment'>");
		// 拼接左侧广告
		sb.append(gf_tools.generic_adv(url, floor.getGf_left_adv()));
		sb.append("</div></div></div><div class='floorclass'><ul class='floorul'>");

		int num = 0;
		for (GoodsFloor info : floor.getChilds()) {
			num++;
			sb.append("<li ");
			if (num == 1) {
				sb.append("class='this'");
			}
			sb.append("style='cursor:pointer;' id='").append(info.getId()).append("' store_gc='").append(floor.getId())
					.append("' >");
			sb.append(info.getGf_name()).append("<s></s></li>");
		}
		sb.append("</ul>");

		int count = 0;
		for (GoodsFloor info : floor.getChilds()) {
			count++;
			sb.append("<div id='").append(info.getId()).append("' store_gc='").append(floor.getId())
					.append("' class='ftab'");
			if (count > 1) {
				sb.append("style='display:none;'");
			}
			sb.append("><div class='ftabone'><div class='classpro'>");
			for (Goods goods : this.gf_tools.generic_goods(info.getGf_gc_goods())) {
				if (goods != null) {
					if (goods.getGoods_main_photo() != null)
						img = goods.getGoods_main_photo().getPath() + "/" + goods.getGoods_main_photo().getName();
					else
						img = map.get("imageWebServer") + "/" + map.get("goodsImagePath") + "/"
								+ map.get("goodsImageName");

					goods_url = map.get("webPath") + "/goods_" + goods.getId() + ".htm";

					if ((Boolean) map.get("IsSecondDomainOpen")) {
						goods_url = "http://" + goods.getGoods_store().getStore_second_domain() + "."
								+ map.get("domainPath") + "/goods_" + goods.getId() + ".htm";
					}

					String goodsName = goods.getGoods_name();
					if (goodsName.length() >= 20) {
						goodsName = goodsName.substring(0, 20);
					} else {
						goodsName = goodsName.substring(0, goodsName.length() - 1);
					}

					sb.append(
							"<div class='productone' style='width:25%'><ul class='this'><li><span class='center_span'>");
					sb.append("<p><a href='").append(goods_url)
							.append("' target='_blank' ><img style='width:140px;height:140px' src='").append(img)
							.append("' original='");
					sb.append(img).append("' onerror=\"this.src=").append(errorimg)
							.append(";\" /></a></p></span></li>");
					sb.append("<li class='pronames'><a href='").append(goods_url).append("' target='_blank'>")
							.append(goodsName).append("</a></li>");
					sb.append("<li><span class=\"hui2\">市场价：</span><span class=\"through hui\">￥")
							.append(goods.getGoods_price());
					sb.append("</span></li><li><span class=\"hui2\">商城价：</span><strong class=\"red\">￥")
							.append(goods.getGoods_current_price());
					sb.append("</strong></li></ul></div>");
				}
			}
			sb.append("</div></div></div>");
		}

		sb.append("</div>");
		sb.append(
				"</div></div><div class=\"floor_brand\"><span class=\"fl_brand_sp\"></span><span class=\"flr_sp_brand\">");
		for (GoodsBrand brand : this.gf_tools.generic_brand(floor.getGf_brand_list())) {
			String brand_url = map.get("webPath") + "/brand_goods_" + brand.getId() + ".htm";
			String brand_img = map.get("imageWebServer") + "/" + brand.getBrandLogo().getPath() + "/"
					+ brand.getBrandLogo().getName();
			sb.append("<a href='").append(brand_url).append("' target='_blank'><img src='").append(brand_img)
					.append("' width='98' height='35' /></a>");
		}
		sb.append("</span></div></div>");
		return sb.toString();
	}

	@RequestMapping({ "/wap/floor_ajax.htm" })
	public void wapFloorAjax(HttpServletRequest request, HttpServletResponse response, Long id, Integer count) {
		Map params = new HashMap();
		params.put("gf_display", Boolean.valueOf(true));
		params.put("id", id);
		List floors = this.goodsFloorService.query(
				"select obj from GoodsFloor obj where obj.gf_display=:gf_display and obj.parent.id is null and obj.id=:id order by obj.gf_sequence asc",
				params, -1, -1);
		Map<String, Object> map = new HashMap<String, Object>();
		CommUtil.saveWebPaths(map, this.configService.getSysConfig(), request);
		String ret = showWapLoadFloorAjaxHtml(floors, count, CommUtil.getURL(request), map);
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(ret);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String showWapLoadFloorAjaxHtml(List lists, int i, String url, Map<String, Object> map) {

		String img = null;
		String loadimg = map.get("imageWebServer") + "/resources/style/common/images/loader.gif";
		String errorimg = map.get("webPath") + "/" + map.get("goodsImagePath") + "/" + map.get("goodsImageName");
		String goods_url = null;
		String goods_class_url = null;
		String child_goods_class_url = null;
		GoodsFloor floor = (GoodsFloor) lists.get(0);

		img = null;

		StringBuffer sb = new StringBuffer(1000);
		String gf_left_adv = floor.getGf_left_adv();
		if (!StringUtils.isBlank(gf_left_adv)) {
			JSONObject json = JSONObject.fromObject(gf_left_adv);
			Long accId = null;
			try {
				accId = json.getLong("acc_id");
			} catch (Exception e) {
				accId = null;
			}
			List<Accessory> accs = null;
			Accessory accessory = null;
			if (null != accId && accId > 0) {
				accessory = accessoryService.getObjById(accId);
			}

			if (null != accessory) {
				img = map.get("webPath") + "/" + accessory.getPath() + "/" + accessory.getName();
			}
		}

		sb.append("<div class='jiu-dv'>");
		sb.append("<a href='#'>");
		sb.append("<img  class='jiu' src='").append(img).append("' original='").append(img)
				.append("' onerror=\"this.src=").append(errorimg).append(";\" />");
		sb.append("</a>");
		sb.append("</div>");
		img = null;
		int count = 0;
		for (GoodsFloor info : floor.getChilds()) {
			for (Goods goods : this.gf_tools.generic_goods(info.getGf_gc_goods())) {
				if (goods != null) {
					if (goods.getGoods_main_photo() != null) {
						if (!StringUtils.isBlank(goods.getGoods_main_photo().getPath())
								&& goods.getGoods_main_photo().getPath().indexOf("http") == 0) {
							img = goods.getGoods_main_photo().getPath() + "/" + goods.getGoods_main_photo().getName();
						} else {
							img = map.get("webPath") + "/" + goods.getGoods_main_photo().getPath() + "/"
									+ goods.getGoods_main_photo().getName();
						}

					} else {
						img = map.get("imageWebServer") + "/" + map.get("goodsImagePath") + "/"
								+ map.get("goodsImageName");
					}

					goods_url = map.get("webPath") + "/goods_" + goods.getId() + ".htm";

					if ((Boolean) map.get("IsSecondDomainOpen")) {
						goods_url = "http://" + goods.getGoods_store().getStore_second_domain() + "."
								+ map.get("domainPath") + "/goods_" + goods.getId() + ".htm";
					}

					String goodsName = goods.getGoods_name();
					if (goodsName.length() >= 20) {
						goodsName = goodsName.substring(0, 20);
					} else {
						goodsName = goodsName.substring(0, goodsName.length() - 1);
					}

					sb.append("<div>");
					sb.append("<a href='").append(goods_url).append("' target='_blank' >");
					sb.append("<img style='width:140px;height:140px' name='flowImage' class='jiu1' src='").append(img)
							.append("' original='").append(img).append("' onerror=\"this.src=").append(errorimg)
							.append(";\" /> <br />");
					sb.append("<span>").append(goodsName).append("</span><br />");
					sb.append("<b>￥").append(goods.getGoods_current_price()).append("</b>");
					sb.append("</a></div>");
				}
			}
		}
		return sb.toString();
	}

}
