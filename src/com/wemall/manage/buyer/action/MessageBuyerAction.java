package com.wemall.manage.buyer.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.domain.virtual.SysMap;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.foundation.domain.Message;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.domain.query.MessageQueryObject;
import com.wemall.foundation.service.IMessageService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import com.wemall.foundation.service.IUserService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 买家站内信控制器
 */
@Controller
public class MessageBuyerAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信", value = "/buyer/message.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message.htm"})
    public ModelAndView message(HttpServletRequest request, HttpServletResponse response, String type, String from_user_id, String currentPage) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/message.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        MessageQueryObject qo = new MessageQueryObject();
        if ((from_user_id == null) || (from_user_id.equals(""))) {
            if ((type == null) || (type.equals(""))) {
                type = "1";
            }
            qo.addQuery("obj.type",
                        new SysMap("type", Integer.valueOf(CommUtil.null2Int(type))), "=");
            qo.addQuery("obj.toUser.id",
                        new SysMap("user_id",
                                   SecurityUserHolder.getCurrentUser().getId()), "=");
        } else {
            qo.addQuery("obj.fromUser.id",
                        new SysMap("user_id",
                                   SecurityUserHolder.getCurrentUser().getId()), "=");
            type = "2";
        }
        qo.addQuery("obj.parent.id is null", null);
        qo.setOrderBy("addTime");
        qo.setOrderType("desc");
        qo.setCurrentPage(Integer.valueOf(CommUtil.null2Int(currentPage)));
        IPageList pList = this.messageService.list(qo);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))) {
            url = CommUtil.getURL(request);
        }
        CommUtil.saveIPageList2ModelAndView(url + "/buyer/message.htm", "", "",
                                            pList, mv);
        cal_msg_info(mv);
        mv.addObject("type", type);
        mv.addObject("from_user_id", from_user_id);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信删除", value = "/buyer/message_del.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message_del.htm"})
    public String message_del(HttpServletRequest request, HttpServletResponse response, String type, String from_user_id, String mulitId) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                this.messageService.delete(Long.valueOf(Long.parseLong(id)));
            }
        }
        if (CommUtil.null2String(from_user_id).equals("")) {
            return "redirect:message.htm?type=" + type;
        }

        return "redirect:message.htm?from_user_id=" + from_user_id;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信查看", value = "/buyer/message_info.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message_info.htm"})
    public ModelAndView message_info(HttpServletRequest request, HttpServletResponse response, String id, String type) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/message_info.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        Message obj = this.messageService.getObjById(Long.valueOf(Long.parseLong(id)));

        if (obj.getToUser().getId().equals(
                    SecurityUserHolder.getCurrentUser().getId())) {
            obj.setStatus(1);
            this.messageService.update(obj);
        }

        if (obj.getFromUser().getId().equals(
                    SecurityUserHolder.getCurrentUser().getId())) {
            obj.setReply_status(0);
            this.messageService.update(obj);
        }
        mv.addObject("obj", obj);
        mv.addObject("type", type);
        cal_msg_info(mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信发送", value = "/buyer/message_send.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message_send.htm"})
    public ModelAndView message_send(HttpServletRequest request, HttpServletResponse response, String id, String userName) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/message_send.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        cal_msg_info(mv);
        if ((userName != null) && (!userName.equals(""))) {
            mv.addObject("userName", userName);
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信保存", value = "/buyer/message_save.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message_save.htm"})
    public void message_save(HttpServletRequest request, HttpServletResponse response, String users, String content) {
        String[] userNames = users.split(",");
        for (String userName : userNames) {
            User toUser = this.userService.getObjByProperty("userName",
                          userName);
            if (toUser != null) {
                Message msg = new Message();
                msg.setAddTime(new Date());
                Whitelist whiteList = new Whitelist();
                content = content.replaceAll("\n", "iskyhop_br");
                msg.setContent(Jsoup.clean(content, Whitelist.basic())
                               .replaceAll("iskyhop_br", "\n"));
                msg.setFromUser(SecurityUserHolder.getCurrentUser());
                msg.setToUser(toUser);
                msg.setType(1);
                this.messageService.save(msg);
            }
        }
        response.setContentType("text/plain");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping( {"/buyer/message_success.htm"})
    public ModelAndView message_success(HttpServletRequest request, HttpServletResponse response, String users, String content) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/success.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        mv.addObject("op_title", "短信保存成功");
        mv.addObject("url", CommUtil.getURL(request) + "/buyer/message.htm");

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "站内短信回复", value = "/buyer/message_reply.htm*", rtype = "buyer", rname = "用户中心", rcode = "user_center", rgroup = "用户中心")
    @RequestMapping( {"/buyer/message_reply.htm"})
    public ModelAndView message_reply(HttpServletRequest request, HttpServletResponse response, String pid, String type, String content) {
        ModelAndView mv = new JModelAndView(
            "user/default/usercenter/success.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        Message parent = this.messageService.getObjById(Long.valueOf(Long.parseLong(pid)));
        Message reply = new Message();
        reply.setAddTime(new Date());
        reply.setContent(content);
        reply.setFromUser(SecurityUserHolder.getCurrentUser());
        reply.setToUser(parent.getFromUser());
        reply.setType(1);
        reply.setParent(parent);
        this.messageService.save(reply);

        if (!parent.getFromUser().getId().equals(
                    SecurityUserHolder.getCurrentUser().getId())) {
            parent.setReply_status(1);
        }
        this.messageService.update(parent);
        mv.addObject("op_title", "短信回复成功");
        mv.addObject("url", CommUtil.getURL(request) +
                     "/buyer/message.htm?type=" + CommUtil.null2Int(type));

        return mv;
    }

    @RequestMapping( {"/message_validate_user.htm"})
    public void message_validate_user(HttpServletRequest request, HttpServletResponse response, String users) {
        String[] userNames = users.trim().split(",");
        String ret = "";
        for (String userName : userNames) {
            if (!userName.trim().equals("")) {
                User user = this.userService.getObjByProperty("userName",
                            userName.trim());
                if (user == null) {
                    ret = userName.trim() + "," + ret;
                }
            }
        }
        if (ret.indexOf(",") >= 0) {
            ret = ret.substring(0, ret.length() - 1);
        }
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

    private void cal_msg_info(ModelAndView mv) {
        Map params = new HashMap();
        params.put("status", Integer.valueOf(0));
        params.put("status1", Integer.valueOf(3));
        params.put("type", Integer.valueOf(1));
        params.put("user_id", SecurityUserHolder.getCurrentUser().getId());
        List user_msgs = this.messageService
                         .query(
                             "select obj from Message obj where (obj.status=:status or obj.status=:status1) and obj.type=:type and obj.toUser.id=:user_id and obj.parent.id is null order by obj.addTime desc",
                             params, -1, -1);
        params.clear();
        params.put("status", Integer.valueOf(0));
        params.put("type", Integer.valueOf(0));
        params.put("user_id", SecurityUserHolder.getCurrentUser().getId());
        List sys_msgs = this.messageService
                        .query(
                            "select obj from Message obj where obj.status=:status and obj.type=:type and obj.toUser.id=:user_id and obj.parent.id is null order by obj.addTime desc",
                            params, -1, -1);
        params.clear();
        params.put("reply_status", Integer.valueOf(1));
        params.put("from_user_id", SecurityUserHolder.getCurrentUser().getId());
        List replys = this.messageService
                      .query(
                          "select obj from Message obj where obj.reply_status=:reply_status and obj.fromUser.id=:from_user_id",
                          params, -1, -1);
        mv.addObject("user_msgs", user_msgs);
        mv.addObject("sys_msgs", sys_msgs);
        mv.addObject("reply_msgs", replys);
    }
}




