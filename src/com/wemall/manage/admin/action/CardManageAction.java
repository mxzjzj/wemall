package com.wemall.manage.admin.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dater.Dater;
import org.apache.number.NumberUtil;
import org.apache.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.easyjf.beans.BeanUtils;
import com.easyjf.beans.BeanWrapper;
import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.base.Constant;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.helper.CardHelper;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.CardType;
import com.wemall.foundation.domain.Card;
import com.wemall.foundation.domain.CardItem;
import com.wemall.foundation.domain.query.CardItemQueryObject;
import com.wemall.foundation.domain.query.CardQueryObject;
import com.wemall.foundation.service.ICardItemService;
import com.wemall.foundation.service.ICardPublishWayService;
import com.wemall.foundation.service.ICardService;
import com.wemall.foundation.service.ICardTypeService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;

/**
 * 活动卡管理
 */
@Controller
public class CardManageAction {

  @Autowired
  private ISysConfigService configService;
  @Autowired
  private IUserConfigService userConfigService;
  @Autowired
  private ICardService cardService;
  @Autowired
  private ICardTypeService cardTypeService;
  @Autowired
  private CardHelper cardHelper;
  @Autowired
  private ISysConfigService sysConfigService;
  @Autowired
  private ICardPublishWayService cardPublishWayService;
  @Autowired
  private ICardItemService cardItemService;


  @SecurityMapping(display = false, rsequence = 0, title = "活动卡列表", value = "/admin/card_list.htm*", rtype = "admin", rname = "活动卡管理", rcode = "card_admin", rgroup = "运营")
  @RequestMapping({"/admin/card_list.htm"})
  public ModelAndView card_list(HttpServletRequest request, HttpServletResponse response,
      String currentPage, String orderBy, String orderType) {
    ModelAndView mv = new JModelAndView("admin/blue/card_list.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    String url = this.configService.getSysConfig().getAddress();
    if ((url == null) || (url.equals(""))) {
      url = CommUtil.getURL(request);
    }
    String params = "";
    CardQueryObject qo = new CardQueryObject(currentPage, mv, "createDate", orderType);
    qo.addQuery("obj.validFlag", new SysMap("validFlag", Boolean.TRUE), null);

    IPageList pList = this.cardService.list(qo);
    mv.addObject("cardHelper", cardHelper);
    mv.addObject("dateUtil", new Dater());
    CommUtil.saveIPageList2ModelAndView(url + "/admin/card_list.htm", "",
        params, pList, mv);

    return mv;
  }

  @SecurityMapping(display = false, rsequence = 0, title = "活动卡增加", value = "/admin/card_add.htm*", rtype = "admin", rname = "活动卡管理", rcode = "card_admin", rgroup = "运营")
  @RequestMapping({"/admin/card_add.htm"})
  public ModelAndView group_add(HttpServletRequest request, HttpServletResponse response,
      String currentPage) {
    ModelAndView mv = new JModelAndView("admin/blue/card_add.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    mv.addObject("currentPage", currentPage);
    List<CardType> cardTypes = cardTypeService.getObjs();
    mv.addObject("cardTypes", cardTypes);
    mv.addObject("dateUtil", new Dater());
    return mv;
  }

  @SecurityMapping(display = false, rsequence = 0, title = "活动卡保存", value = "/admin/card_save.htm*", rtype = "admin", rname = "活动卡管理", rcode = "card_admin", rgroup = "运营")
  @RequestMapping({"/admin/card_save.htm"})
  public ModelAndView card_save(HttpServletRequest request, HttpServletResponse response, String id,
      String currentPage, String cardTypeId) {
    WebForm wf = new WebForm();
    Card card = null;
    if (id.equals("")) {
      card = (Card) wf.toPo(request, Card.class);
      card.setCreateDate(new Date());
    } else {
      Card obj = this.cardService.getObjById(Long.valueOf(Long.parseLong(id)));
      card = (Card) wf.toPo(request, obj);
    }

    card.setSystemModule(this.sysConfigService.getSysConfig());
    card.setCardType(this.cardTypeService.getObjById(6l));
    card.setCardPublishWay(this.cardPublishWayService.getObjById(Constant.CARD_PUBLISH_WAY_OFF2ON));
    //图片
    String uploadFilePath = this.configService.getSysConfig().getUploadFilePath();
    String saveFilePathName = request.getSession().getServletContext().getRealPath("/") +
        uploadFilePath + File.separator + "card";
    Map map = new HashMap();
    String fileName = "";
    if (card.getCardImage()!=null)
      fileName = card.getCardImage();
    try {
      map = CommUtil.saveFileToServer(request, "cardImage", saveFilePathName, fileName, null);
      if (map.get("fileName")!= "")
         card.setCardImage(uploadFilePath + "/card/" + CommUtil.null2String(map.get("fileName")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (id.equals("")) {
      this.cardService.save(card);
    } else {
      this.cardService.update(card);
    }
    ModelAndView mv = new JModelAndView("admin/blue/success.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    mv.addObject("list_url", CommUtil.getURL(request) +
        "/admin/card_list.htm");
    mv.addObject("op_title", "保存活动卡成功");
    mv.addObject("add_url", CommUtil.getURL(request) +
        "/admin/card_add.htm" + "?currentPage=" + currentPage);
    return mv;
  }

  @SecurityMapping(display = false, rsequence = 0, title = "活动卡号列表", value = "/admin/card_item_list.htm*", rtype = "admin", rname = "活动卡管理", rcode = "card_admin", rgroup = "运营")
  @RequestMapping({"/admin/card_item_list.htm"})
  public ModelAndView cardItemList(HttpServletRequest request, HttpServletResponse response,
      String currentPage, String orderBy, String orderType)
      throws Exception {
    long cardId = NumberUtil.toLong(request.getParameter("cardId"));
    ModelAndView mv = new JModelAndView("admin/blue/card_item_list.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    String url = this.configService.getSysConfig().getAddress();
    if ((url == null) || (url.equals(""))) {
      url = CommUtil.getURL(request);
    }
    String params = "";
    CardItemQueryObject qo = new CardItemQueryObject(currentPage, mv, "createDate", orderType);
    qo.addQuery("obj.card.id", new SysMap("cardId", cardId), null);

    IPageList pList = this.cardItemService.list(qo);
    Card card = cardService.getObjById(cardId);
    mv.addObject("card", card);

    mv.addObject("dateUtil", new Dater());
    CommUtil.saveIPageList2ModelAndView(url + "/admin/card_item_list.htm", "",
        params, pList, mv);

    return mv;
  }

  @SecurityMapping(display = false, rsequence = 0, title = "活动卡是否冻结", value = "/admin/card_item_update.htm*", rtype = "admin", rname = "活动卡管理", rcode = "card_admin", rgroup = "运营")
  @RequestMapping({"/admin/card_item_update.htm"})
  public void cardItemUpdate(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    long cardItemId = NumberUtil.toLong(request.getParameter("cardItemId"));
    String fieldName = StringUtil.toString(request.getParameter("fieldName"));
    String value = StringUtil.toString(request.getParameter("value"));

    Object data = null;
    CardItem cardItem = cardItemService.getObjById(cardItemId);
    BeanWrapper wrapper = new BeanWrapper(cardItem);
    Field[] fields1 = CardItem.class.getDeclaredFields();
    Field[] fields2 = CardItem.class.getSuperclass().getDeclaredFields();
    List<Field> fields = new ArrayList<Field>(Arrays.asList(fields1));
    fields.addAll(new ArrayList<Field>(Arrays.asList(fields2)));
    for (Field field : fields) {
      if (field.getName().equals(fieldName)) {
        Class clazz = Class.forName("java.lang.String");
        if (field.getType().getName().equals("int")) {
          clazz = Class.forName("java.lang.Integer");
        } else if (field.getType().getName().equals("boolean")) {
          clazz = Class.forName("java.lang.Boolean");
        }

        if (!value.equals("")) {
          data = BeanUtils.convertType(value, clazz);
        } else {
          data = Boolean.valueOf(!NumberUtil.toBoolean(wrapper.getPropertyValue(fieldName)));
        }
        wrapper.setPropertyValue(fieldName, data);
      }
    }
    cardItemService.update(cardItem);

    response.setContentType("text/plain");
    response.setHeader("Cache-Control", "no-cache");
    response.setCharacterEncoding("UTF-8");

    PrintWriter writer = response.getWriter();
    writer.print(data.toString());
  }

  @RequestMapping({"/admin/card_item_remove.htm"})
  public String cardItemRemove(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    int currentPage = NumberUtil.toPage(request.getParameter("currentPage"));
    long cardId = NumberUtil.toLong(request.getParameter("cardId"));
    String ids = StringUtil.toString(request.getParameter("ids"));

    List<Serializable> cardItemIds = new ArrayList<Serializable>();
    String[] _ids = ids.split(",");
    for (String id : _ids) {
      long _id = NumberUtil.toLong(id);
      if (_id > 0) {
        cardItemIds.add(_id);
      }
    }

    if (cardItemIds.size() > 0) {
      cardItemService.batchDelete(cardItemIds);
    }

    return "redirect:card_item_list.htm?currentPage=" + currentPage + "&cardId=" + cardId;
  }

  @RequestMapping({"/admin/card_item_publish.htm"})
  public ModelAndView cardItemPublish(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    long cardId = NumberUtil.toLong(request.getParameter("cardId"));

    ModelAndView mv = new JModelAndView("admin/blue/card_item_publish.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    Card card = cardService.getObjById(cardId);
    mv.addObject("card", card);
    return mv;
  }

  @RequestMapping({"/admin/card_item_publish_do.htm"})
  public ModelAndView cardItemPublishDo(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    long cardId = NumberUtil.toLong(request.getParameter("cardId"));
    int count = NumberUtil.toInt(request.getParameter("count"));

    for (int i = 0; i < count; i++) {
      cardHelper.addItem(cardId, true);
    }

    ModelAndView mv = new JModelAndView("admin/blue/success.html",
        this.configService.getSysConfig(),
        this.userConfigService.getUserConfig(), 0, request, response);
    mv.addObject("list_url", CommUtil.getURL(request) +
        "/admin/card_item_list.htm?cardId" + cardId);
    mv.addObject("op_title", "发行卡号成功");
    return mv;
  }

  @RequestMapping({"/admin/card_remove.htm"})
  public String cardRemove(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    int currentPage = NumberUtil.toPage(request.getParameter("currentPage"));
    String ids = StringUtil.toString(request.getParameter("ids"));

    List<Serializable> cardIds = new ArrayList<Serializable>();
    String[] _ids = ids.split(",");
    for (String id : _ids) {
      long _id = NumberUtil.toLong(id);
      if (_id > 0) {
        cardIds.add(_id);
      }
    }

    if (cardIds.size() > 0) {
      cardService.removeObjs(cardIds);
      cardItemService.removeObjsByCardNoOwner(cardIds);
    }
    return "redirect:card_list.htm?currentPage=" + currentPage;
  }

  @RequestMapping({"/admin/card_edit.htm"})
  public ModelAndView cardEdit(HttpServletRequest request, HttpServletResponse response,String currentPage){
    ModelAndView mv = new JModelAndView("admin/blue/card_add.html",
            this.configService.getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
    mv.addObject("currentPage", currentPage);

    long cardId = NumberUtil.toLong(request.getParameter("cardId"));
    Card card = cardService.getObjById(cardId);
    mv.addObject("card", card);

    mv.addObject("dateUtil", new Dater());
    return mv;
  }

}
