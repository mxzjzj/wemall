package com.wemall.manage.admin.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.easyjf.beans.BeanUtils;
import com.easyjf.beans.BeanWrapper;
import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.core.tools.database.DatabaseTools;
import com.wemall.foundation.domain.Evaluate;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.GoodsCart;
import com.wemall.foundation.domain.Message;
import com.wemall.foundation.domain.OrderForm;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.domain.query.GoodsQueryObject;
import com.wemall.foundation.service.IEvaluateService;
import com.wemall.foundation.service.IGoodsBrandService;
import com.wemall.foundation.service.IGoodsCartService;
import com.wemall.foundation.service.IGoodsClassService;
import com.wemall.foundation.service.IGoodsService;
import com.wemall.foundation.service.IMessageService;
import com.wemall.foundation.service.IOrderFormLogService;
import com.wemall.foundation.service.IOrderFormService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.ITemplateService;
import com.wemall.foundation.service.IUserConfigService;
import com.wemall.foundation.service.IUserService;
import com.wemall.lucene.LuceneUtil;
import com.wemall.lucene.LuceneVo;
import com.wemall.manage.admin.tools.MsgTools;

/**
 * 商品管理控制器
 */
@Controller
public class GoodsManageAction {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private IUserConfigService userConfigService;

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private IGoodsBrandService goodsBrandService;

	@Autowired
	private IGoodsClassService goodsClassService;

	@Autowired
	private ITemplateService templateService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IMessageService messageService;

	@Autowired
	private MsgTools msgTools;

	@Autowired
	private DatabaseTools databaseTools;

	@Autowired
	private IEvaluateService evaluateService;

	@Autowired
	private IGoodsCartService goodsCartService;

	@Autowired
	private IOrderFormService orderFormService;

	@Autowired
	private IOrderFormLogService orderFormLogService;

	@SecurityMapping(display = false, rsequence = 0, title = "商品列表", value = "/admin/goods_list.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_list.htm" })
	public ModelAndView goods_list(HttpServletRequest request, HttpServletResponse response, String currentPage,
			String orderBy, String orderType) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_list.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		String url = this.configService.getSysConfig().getAddress();
		if ((url == null) || (url.equals(""))) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		GoodsQueryObject qo = new GoodsQueryObject(currentPage, mv, orderBy, orderType);
		WebForm wf = new WebForm();
		wf.toQueryPo(request, qo, Goods.class, mv);
		qo.addQuery("obj.goods_status", new SysMap("goods_status", Integer.valueOf(-2)), ">");
		// 毛利检索条件
		setMaxAndMinProfit(request, qo);

		// qo.addQuery("obj.goods_status", new SysMap("goods_status",
		// Integer.valueOf(-2)), ">");
		IPageList pList = this.goodsService.list(qo);
		CommUtil.saveIPageList2ModelAndView(url + "/admin/goods_list.htm", "", params, pList, mv);
		List gbs = this.goodsBrandService.query("select obj from GoodsBrand obj order by obj.sequence asc", null, -1,
				-1);
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null order by obj.sequence asc", null, -1, -1);
		mv.addObject("gcs", gcs);
		mv.addObject("gbs", gbs);

		return mv;
	}

	private void setMaxAndMinProfit(HttpServletRequest request, GoodsQueryObject qo) {
		Map map = request.getParameterMap();
		List<Map> maps = new ArrayList();
		Enumeration enum1 = request.getParameterNames();
		String value;
		while (enum1.hasMoreElements()) {
			String paramName = (String) enum1.nextElement();
			value = request.getParameter(paramName);
			Map m1 = new HashMap();
			m1.put(paramName, value);
			maps.add(m1);
		}
		for (Map m : maps) {
			Iterator keyes = m.keySet().iterator();
			while (keyes.hasNext()) {
				String field = (String) keyes.next();
				if (field.indexOf("q_minProfit") == 0) {
					String minProfit = (String) m.get(field);
					if (StringUtils.isBlank(minProfit)) {
						continue;
					}
					double minProfitInt = 0.0;
					try {
						minProfitInt = Double.valueOf(minProfit).doubleValue();
					} catch (Exception e) {
						minProfitInt = 0.0;
					}
					BigDecimal minBig = new BigDecimal(0.0);
					try {
						minBig = BigDecimal.valueOf(minProfitInt);
					} catch (Exception e) {
						minBig = new BigDecimal(0.0);
					}
					qo.addQuery("obj.jdPrice - obj.originalPrice", new SysMap("minProfit", minBig), ">=");
				}

				if (field.indexOf("q_maxProfit") == 0) {
					String maxProfit = (String) m.get(field);
					if (StringUtils.isBlank(maxProfit)) {
						continue;
					}
					double maxProfitInt = 0.0;
					BigDecimal maxBig = new BigDecimal(0.0);
					try {
						maxProfitInt = Double.valueOf(maxProfit).doubleValue();
					} catch (Exception e) {
						maxProfitInt = 0.0;
					}
					try {
						maxBig = BigDecimal.valueOf(maxProfitInt);
					} catch (Exception e) {
						maxBig = new BigDecimal(0.0);
					}
					qo.addQuery("obj.jdPrice - obj.originalPrice", new SysMap("maxProfit", maxBig), "<=");
				}
			}
		}
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品导出", value = "/admin/goods_export.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_export.htm" })
	public void goods_export(HttpServletRequest request, HttpServletResponse response, String currentPage,
			String orderBy, String orderType) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_list.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		String url = this.configService.getSysConfig().getAddress();
		if ((url == null) || (url.equals(""))) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		GoodsQueryObject qo = new GoodsQueryObject(currentPage, mv, orderBy, orderType);
		WebForm wf = new WebForm();
		wf.toQueryPo(request, qo, Goods.class, mv);
		qo.addQuery("obj.goods_status", new SysMap("goods_status", Integer.valueOf(-2)), ">");

		// 毛利检索条件
		setMaxAndMinProfit(request, qo);
		IPageList pList = this.goodsService.list(qo);
		CommUtil.saveIPageList2ModelAndView(url + "/admin/goods_list.htm", "", params, pList, mv);
		List gbs = this.goodsBrandService.query("select obj from GoodsBrand obj order by obj.sequence asc", null, -1,
				-1);
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null order by obj.sequence asc", null, -1, -1);
		try {
			Goods good = null;
			// 商品名称 商品简要 商品货号 商品链接 商品分类 商品品牌 商品原价 商品现价 商品进价 会员价格 商品属性 商品规格 商品库存
			// 商品重量
			// 商品体积 免运费 商品单位 商品供应商 限购数量 点击次数 收藏人气 售出数量 商品评价 是否推荐
			StringBuffer datas = new StringBuffer();
			datas.append("商品名称").append("\t");
			datas.append("货号").append("\t");
			datas.append("品牌").append("\t");
			datas.append("分类名").append("\t");
			datas.append("所在店铺").append("\t");
			datas.append("商品状态").append("\t");
			datas.append("原价").append("\t");
			datas.append("京东价").append("\t");
			datas.append("协议价").append("\t");
			datas.append("毛利").append("\t");
			datas.append("库存").append("\t");
			datas.append("特别推荐").append("\t");
			datas.append("查看次数").append("\t");

			datas.append("\n");
			for (Object obj : pList.getResult()) {
				good = (Goods) obj;
				datas.append(good.getGoods_name() == null ? "" : good.getGoods_name()).append("\t");
				datas.append(good.getGoodsSkuId() == null ? "" : good.getGoodsSkuId()).append("\t");
				if (null == good.getGoods_brand()) {
					datas.append("\t");
				} else {
					datas.append(good.getGoods_brand().getName() == null ? "" : good.getGoods_brand().getName()).append("\t");
				}
				
				datas.append(good.getGc().getClassName() == null ? "" : good.getGc().getClassName()).append("\t");
				
				datas.append(good.getGoods_store().getStore_name() == null ? "" : good.getGoods_store().getStore_name())
						.append("\t");
				switch (good.getGoods_status()) {
				case 0:
					datas.append("上架").append("\t");
					break;
				case 1:
					datas.append("仓库中").append("\t");
					break;
				case -1:
					datas.append("已下架").append("\t");
					break;
				case 2:
					datas.append("违规下架").append("\t");
					break;
				default:
					break;
				}

				datas.append(good.getStore_price()).append("\t");
				datas.append(good.getJdPrice()).append("\t");
				datas.append(good.getOriginalPrice()).append("\t");
				if (null == good.getJdPrice()) {
					good.setJdPrice(new BigDecimal(0));
				}
				if (null == good.getOriginalPrice()) {
					good.setOriginalPrice(new BigDecimal(0));
				}
				datas.append((good.getJdPrice().doubleValue() - good.getOriginalPrice().doubleValue())).append("\t");
				datas.append(good.getGoods_inventory()).append("\t");

				datas.append(good.isStore_recommend() ? "是" : "否").append("\t");
				datas.append(good.getGoods_click()).append("\t");
				datas.append("\n");
			}
			String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String fileName = "在售商品列表" + date;

			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "UTF-16LE");
			osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
			// 要输出的内容
			String result = datas.toString();
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".csv");

			osw.write(result);
			osw.flush();

			// byte[] bytes = datas.toString().getBytes();
			// response.setContentType("application/OCTET-STREAM;charset=UTF-8");
			// String date = new SimpleDateFormat("yyyy-MM-dd").format(new
			// Date());
			// String fileName = "在售商品列表" + date;
			//
			// // 防止中文文件名乱码
			// fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			// response.setHeader("Content-disposition", "attachment;filename="
			// + fileName + ".csv");
			// response.setHeader("Content-Length", "" + bytes.length);
			// ServletOutputStream outputStream = response.getOutputStream();
			// // 返回到页面
			// outputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte)
			// 0xBF });// 加上bom头，才不会中文乱码
			// outputStream.write(bytes);
			// outputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte)
			// 0xBF });// 加上bom头，才不会中文乱码
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SecurityMapping(display = false, rsequence = 0, title = "违规商品列表", value = "/admin/goods_outline.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_outline.htm" })
	public ModelAndView goods_outline(HttpServletRequest request, HttpServletResponse response, String currentPage,
			String orderBy, String orderType) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_outline.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		String url = this.configService.getSysConfig().getAddress();
		if ((url == null) || (url.equals(""))) {
			url = CommUtil.getURL(request);
		}
		String params = "";
		GoodsQueryObject qo = new GoodsQueryObject(currentPage, mv, orderBy, orderType);
		WebForm wf = new WebForm();
		wf.toQueryPo(request, qo, Goods.class, mv);
		qo.addQuery("obj.goods_status", new SysMap("goods_status", Integer.valueOf(-2)), "=");
		IPageList pList = this.goodsService.list(qo);
		CommUtil.saveIPageList2ModelAndView(url + "/admin/goods_list.htm", "", params, pList, mv);
		List gbs = this.goodsBrandService.query("select obj from GoodsBrand obj order by obj.sequence asc", null, -1,
				-1);
		List gcs = this.goodsClassService.query(
				"select obj from GoodsClass obj where obj.parent.id is null order by obj.sequence asc", null, -1, -1);
		mv.addObject("gcs", gcs);
		mv.addObject("gbs", gbs);

		return mv;
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品添加", value = "/admin/goods_add.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_add.htm" })
	public ModelAndView goods_add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_add.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		mv.addObject("currentPage", currentPage);

		return mv;
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品编辑", value = "/admin/goods_edit.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_edit.htm" })
	public ModelAndView goods_edit(HttpServletRequest request, HttpServletResponse response, String id,
			String currentPage) {
		ModelAndView mv = new JModelAndView("admin/blue/goods_add.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		if ((id != null) && (!id.equals(""))) {
			Goods goods = this.goodsService.getObjById(Long.valueOf(Long.parseLong(id)));
			mv.addObject("obj", goods);
			mv.addObject("currentPage", currentPage);
		}

		return mv;
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品保存", value = "/admin/goods_save.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_save.htm" })
	public ModelAndView goods_save(HttpServletRequest request, HttpServletResponse response, String id,
			String currentPage, String cmd, String list_url, String add_url) {
		WebForm wf = new WebForm();
		Goods goods = null;
		if (id.equals("")) {
			goods = (Goods) wf.toPo(request, Goods.class);
			goods.setAddTime(new Date());
		} else {
			Goods obj = this.goodsService.getObjById(Long.valueOf(Long.parseLong(id)));
			goods = (Goods) wf.toPo(request, obj);
		}
		if (id.equals(""))
			this.goodsService.save(goods);
		else
			this.goodsService.update(goods);
		ModelAndView mv = new JModelAndView("admin/blue/success.html", this.configService.getSysConfig(),
				this.userConfigService.getUserConfig(), 0, request, response);
		mv.addObject("list_url", list_url);
		mv.addObject("op_title", "保存商品成功");
		if (add_url != null) {
			mv.addObject("add_url", add_url + "?currentPage=" + currentPage);
		}

		return mv;
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品删除", value = "/admin/goods_del.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_del.htm" })
	public String goods_del(HttpServletRequest request, String mulitId) throws Exception {
		String[] ids = mulitId.split(",");
		for (String id : ids) {
			if (!id.equals("")) {
				Goods goods = this.goodsService.getObjById(CommUtil.null2Long(id));
				Map map = new HashMap();
				map.put("gid", goods.getId());
				List<GoodsCart> goodCarts = this.goodsCartService
						.query("select obj from GoodsCart obj where obj.goods.id = :gid", map, -1, -1);
				Long ofid = null;
				Long of_id;
				for (GoodsCart gc : goodCarts) {
					of_id = gc.getOf().getId();
					this.goodsCartService.delete(gc.getId());
					OrderForm of = this.orderFormService.getObjById(of_id);
					if (of.getGcs().size() == 0) {
						this.orderFormService.delete(of_id);
					}
				}

				List<Evaluate> evaluates = goods.getEvaluates();
				for (Evaluate e : evaluates) {
					this.evaluateService.delete(e.getId());
				}
				goods.getGoods_ugcs().clear();
				goods.getGoods_ugcs().clear();
				goods.getGoods_photos().clear();
				goods.getGoods_ugcs().clear();
				goods.getGoods_specs().clear();
				this.goodsService.delete(goods.getId());

				String goods_lucene_path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root"))))
						.append(File.separator).append("lucene").append(File.separator).append("goods").toString();
				File file = new File(goods_lucene_path);
				if (!file.exists()) {
					CommUtil.createFolder(goods_lucene_path);
				}
				LuceneUtil lucene = LuceneUtil.instance();
				LuceneUtil.setIndex_path(goods_lucene_path);
				lucene.delete_index(CommUtil.null2String(id));

				send_site_msg(request, "msg_toseller_goods_delete_by_admin_notify", goods.getGoods_store().getUser(),
						goods, "商城存在违规");
			}
		}

		return "redirect:goods_list.htm";
	}

	private void send_site_msg(HttpServletRequest request, String mark, User user, Goods goods, String reason)
			throws Exception {
		com.wemall.foundation.domain.Template template = this.templateService.getObjByProperty("mark", mark);
		if (template.isOpen()) {
			String path = request.getSession().getServletContext().getRealPath("/") + "/vm/";
			PrintWriter pwrite = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(path + "msg.vm", false), "UTF-8"));
			pwrite.print(template.getContent());
			pwrite.flush();
			pwrite.close();

			Properties p = new Properties();
			p.setProperty("file.resource.loader.path", request.getRealPath("/") + "vm" + File.separator);
			p.setProperty("input.encoding", "UTF-8");
			p.setProperty("output.encoding", "UTF-8");
			Velocity.init(p);
			org.apache.velocity.Template blank = Velocity.getTemplate("msg.vm", "UTF-8");
			VelocityContext context = new VelocityContext();
			context.put("reason", reason);
			context.put("user", user);
			context.put("config", this.configService.getSysConfig());
			context.put("send_time", CommUtil.formatLongDate(new Date()));
			StringWriter writer = new StringWriter();
			blank.merge(context, writer);

			String content = writer.toString();
			User fromUser = this.userService.getObjByProperty("userName", "admin");
			Message msg = new Message();
			msg.setAddTime(new Date());
			msg.setContent(content);
			msg.setFromUser(fromUser);
			msg.setTitle(template.getTitle());
			msg.setToUser(user);
			msg.setType(0);
			this.messageService.save(msg);
			CommUtil.deleteFile(path + "temp.vm");
			writer.flush();
			writer.close();
		}
	}

	@SecurityMapping(display = false, rsequence = 0, title = "商品AJAX更新", value = "/admin/goods_ajax.htm*", rtype = "admin", rname = "商品管理", rcode = "admin_goods", rgroup = "商品")
	@RequestMapping({ "/admin/goods_ajax.htm" })
	public void ajax(HttpServletRequest request, HttpServletResponse response, String id, String fieldName,
			String value) throws ClassNotFoundException {
		Goods obj = this.goodsService.getObjById(Long.valueOf(Long.parseLong(id)));
		Field[] fields = Goods.class.getDeclaredFields();
		BeanWrapper wrapper = new BeanWrapper(obj);
		Object val = null;
		for (Field field : fields) {
			if (field.getName().equals(fieldName)) {
				Class clz = Class.forName("java.lang.String");
				if (field.getType().getName().equals("int")) {
					clz = Class.forName("java.lang.Integer");
				}
				if (field.getType().getName().equals("boolean")) {
					clz = Class.forName("java.lang.Boolean");
				}
				if (!value.equals(""))
					val = BeanUtils.convertType(value, clz);
				else {
					val = Boolean.valueOf(!CommUtil.null2Boolean(wrapper.getPropertyValue(fieldName)));
				}
				wrapper.setPropertyValue(fieldName, val);
			}
		}
		if (fieldName.equals("store_recommend")) {
			if (obj.isStore_recommend())
				obj.setStore_recommend_time(new Date());
			else
				obj.setStore_recommend_time(null);
		}
		this.goodsService.update(obj);
		if (obj.getGoods_status() == 0) {
			String goods_lucene_path = (new StringBuilder(String.valueOf(System.getProperty("wemall.root"))))
					.append(File.separator).append("lucene").append(File.separator).append("goods").toString();
			File file = new File(goods_lucene_path);
			if (!file.exists()) {
				CommUtil.createFolder(goods_lucene_path);
			}
			LuceneVo vo = new LuceneVo();
			vo.setVo_id(obj.getId());
			vo.setVo_title(obj.getGoods_name());
			vo.setVo_content(obj.getGoods_details());
			vo.setVo_type("goods");
			vo.setVo_store_price(CommUtil.null2Double(obj.getStore_price()));
			vo.setVo_add_time(obj.getAddTime().getTime());
			vo.setVo_goods_salenum(obj.getGoods_salenum());
			LuceneUtil lucene = LuceneUtil.instance();
			LuceneUtil.setIndex_path(goods_lucene_path);
			lucene.update(CommUtil.null2String(obj.getId()), vo);
		} else {
			String goods_lucene_path = System.getProperty("user.dir") + File.separator + "lucene" + File.separator
					+ "goods";
			File file = new File(goods_lucene_path);
			if (!file.exists()) {
				CommUtil.createFolder(goods_lucene_path);
			}
			LuceneUtil lucene = LuceneUtil.instance();
			LuceneUtil.setIndex_path(goods_lucene_path);
			lucene.delete_index(CommUtil.null2String(id));
		}
		response.setContentType("text/plain");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.print(val.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
