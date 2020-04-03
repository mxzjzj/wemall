package com.wemall.manage.admin.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wemall.apis.jd.service.order.JdHandOrderService;
import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.OrderForm;
import com.wemall.foundation.domain.SysConfig;
import com.wemall.foundation.domain.query.OrderFormQueryObject;
import com.wemall.foundation.domain.virtual.TransInfo;
import com.wemall.foundation.service.IOrderFormService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;

/**
 * 订单管理控制器
 */
@Controller
public class OrderManageAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IOrderFormService orderFormService;
    
    @Autowired
    private JdHandOrderService jdHandOrderService;

    @SecurityMapping(display = false, rsequence = 0, title = "订单设置", value = "/admin/set_order_confirm.htm*", rtype = "admin", rname = "订单设置", rcode = "set_order_confirm", rgroup = "交易")
    @RequestMapping( {"/admin/set_order_confirm.htm"})
    public ModelAndView set_order_confirm(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView(
            "admin/blue/set_order_confirm.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "订单设置保存", value = "/admin/set_order_confirm_save.htm*", rtype = "admin", rname = "订单设置", rcode = "set_order_confirm", rgroup = "交易")
    @RequestMapping( {"/admin/set_order_confirm_save.htm"})
    public ModelAndView set_order_confirm_save(HttpServletRequest request, HttpServletResponse response, String id, String auto_order_confirm, String auto_order_notice, String auto_order_return, String auto_order_evaluate) {
        SysConfig obj = this.configService.getSysConfig();
        WebForm wf = new WebForm();
        SysConfig config = null;
        if (id.equals("")) {
            config = (SysConfig)wf.toPo(request, SysConfig.class);
            config.setAddTime(new Date());
        } else {
            config = (SysConfig)wf.toPo(request, obj);
        }
        config.setAuto_order_confirm(CommUtil.null2Int(auto_order_confirm));
        config.setAuto_order_notice(CommUtil.null2Int(auto_order_notice));
        config.setAuto_order_return(CommUtil.null2Int(auto_order_return));
        config.setAuto_order_evaluate(CommUtil.null2Int(auto_order_evaluate));
        if (id.equals(""))
            this.configService.save(config);
        else {
            this.configService.update(config);
        }
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("op_title", "订单设置成功");
        mv.addObject("list_url", CommUtil.getURL(request) +
                     "/admin/set_order_confirm.htm");

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "订单列表", value = "/admin/order_list.htm*", rtype = "admin", rname = "订单管理", rcode = "order_admin", rgroup = "交易")
    @RequestMapping( {"/admin/order_list.htm"})
    public ModelAndView order_list(HttpServletRequest request, HttpServletResponse response, String order_status, String type, String type_data, String payment, String beginTime, String endTime, String begin_price, String end_price, String currentPage) {
        ModelAndView mv = new JModelAndView("admin/blue/order_list.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        OrderFormQueryObject ofqo = new OrderFormQueryObject(currentPage, mv,
                "addTime", "desc");
        if (!CommUtil.null2String(order_status).equals("")) {
            ofqo.addQuery("obj.order_status",
                          new SysMap("order_status",
                                     Integer.valueOf(CommUtil.null2Int(order_status))), "=");
        }
        if (!CommUtil.null2String(type_data).equals("")) {
            if (type.equals("store")) {
                ofqo.addQuery("obj.store.store_name", new SysMap("store_name", "%" + type_data + "%"), "like");
            }
            // 会员名
            if (type.equals("buyer")) {
                ofqo.addQuery("obj.user.userName", new SysMap("userName", "%" + type_data + "%"), "like");
            }
            // 手机号码
            if (type.equals("mobile")) {
                ofqo.addQuery("obj.user.mobile", new SysMap("mobile", "%" + type_data + "%"), "like");
            }
            // 订单号
            if (type.equals("order")) {
                ofqo.addQuery("obj.order_id", new SysMap("order_id", "%" + type_data + "%"), "like");
            }
            
            // 收件人姓名
            if (type.equals("trueName")) {
            	ofqo.addQuery("obj.addr.trueName", new SysMap("trueName", "%" + type_data + "%"), "like");
            }
            
         // 商品名
//            if (type.equals("goodsName")) {
//                ofqo.addQuery("obj.addr.mobile",
//                              new SysMap("mobile",
//                                         type_data), "=");
//            }
        }
        if (!CommUtil.null2String(payment).equals("")) {
            ofqo.addQuery("obj.payment.mark", new SysMap("mark", payment), "=");
        }
        if (!CommUtil.null2String(beginTime).equals("")) {
            ofqo.addQuery("obj.addTime",
                          new SysMap("beginTime",
                                     CommUtil.formatDate(beginTime)), ">=");
        }
        if (!CommUtil.null2String(endTime).equals("")) {
            ofqo.addQuery("obj.addTime",
                          new SysMap("endTime",
                                     CommUtil.formatDate(endTime)), "<=");
        }
        if (!CommUtil.null2String(begin_price).equals("")) {
            ofqo.addQuery("obj.totalPrice",
                          new SysMap("begin_price",
                                     BigDecimal.valueOf(CommUtil.null2Double(begin_price))),
                          ">=");
        }
        if (!CommUtil.null2String(end_price).equals("")) {
            ofqo.addQuery("obj.totalPrice",
                          new SysMap("end_price",
                                     BigDecimal.valueOf(CommUtil.null2Double(end_price))), "<=");
        }
        IPageList pList = this.orderFormService.list(ofqo);
        CommUtil.saveIPageList2ModelAndView("", "", "", pList, mv);
        mv.addObject("order_status", order_status);
        mv.addObject("type", type);
        mv.addObject("type_data", type_data);
        mv.addObject("payment", payment);
        mv.addObject("beginTime", beginTime);
        mv.addObject("endTime", endTime);
        mv.addObject("begin_price", begin_price);
        mv.addObject("end_price", end_price);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "订单详情", value = "/admin/order_view.htm*", rtype = "admin", rname = "订单管理", rcode = "order_admin", rgroup = "交易")
    @RequestMapping( {"/admin/order_view.htm"})
    public ModelAndView order_view(HttpServletRequest request, HttpServletResponse response, String id) {
        ModelAndView mv = new JModelAndView("admin/blue/order_view.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        OrderForm obj = this.orderFormService
                        .getObjById(CommUtil.null2Long(id));
        TransInfo transInfo = query_ship_getData(id);
        mv.addObject("transInfo", transInfo);
        mv.addObject("obj", obj);

        return mv;
    }
    
    
    @RequestMapping({"/admin/jd_hand_order.htm"})
    public void jdHandOrder(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
      long id = Long.parseLong(request.getParameter("id"));//NumberUtil.toLong(request.getParameter("id"));
      String info = "下单失败，请稍后再试！";
      try
      {
        info = this.jdHandOrderService.JdSendHandOrder(id);
      }
      catch (Exception localException)
      {
    	  System.out.println(localException.getMessage());
      }
      Map params = new HashMap();
      params.put("info", info);

      response.setContentType("text/plain");
      response.setHeader("Cache-Control", "no-cache");
      response.setCharacterEncoding("UTF-8");

      PrintWriter writer = response.getWriter();
      writer.print(Json.toJson(params, JsonFormat.compact()));
    }

    public TransInfo query_ship_getData(String id) {
        TransInfo info = new TransInfo();
        OrderForm obj = this.orderFormService
                        .getObjById(CommUtil.null2Long(id));
        try {
            URL url = new URL(
                "http://api.kuaidi100.com/api?id=" +
                this.configService.getSysConfig().getKuaidi_id() +
                "&com=" + (
                    obj.getEc() != null ? obj.getEc()
                    .getCompany_mark() : "") + "&nu=" +
                obj.getShipCode() + "&show=0&muti=1&order=asc");
            URLConnection con = url.openConnection();
            con.setAllowUserInteraction(false);
            InputStream urlStream = url.openStream();
            String type = URLConnection.guessContentTypeFromStream(urlStream);
            String charSet = null;
            if (type == null)
                type = con.getContentType();
            if ((type == null) || (type.trim().length() == 0) ||
                    (type.trim().indexOf("text/html") < 0))
                return info;
            if (type.indexOf("charset=") > 0)
                charSet = type.substring(type.indexOf("charset=") + 8);
            byte[] b = new byte[10000];
            int numRead = urlStream.read(b);
            String content = new String(b, 0, numRead, charSet);
            while (numRead != -1) {
                numRead = urlStream.read(b);
                if (numRead == -1)
                    continue;
                String newContent = new String(b, 0, numRead, charSet);
                content = content + newContent;
            }

            info = (TransInfo)Json.fromJson(TransInfo.class, content);
            urlStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return info;
    }
}




