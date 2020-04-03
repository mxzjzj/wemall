package com.wemall.manage.admin.action;

import com.wemall.core.annotation.SecurityMapping;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.query.support.IPageList;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.WebForm;
import com.wemall.foundation.domain.ComplaintSubject;
import com.wemall.foundation.domain.query.ComplaintSubjectQueryObject;
import com.wemall.foundation.service.IComplaintSubjectService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 投诉主题管理控制器
 */
@Controller
public class ComplaintSubjectManageAction {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IComplaintSubjectService complaintsubjectService;

    @SecurityMapping(display = false, rsequence = 0, title = "投诉主题列表", value = "/admin/complaintsubject_list.htm*", rtype = "admin", rname = "投诉管理", rcode = "complaint_manage", rgroup = "交易")
    @RequestMapping( {"/admin/complaintsubject_list.htm"})
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response, String currentPage, String orderBy, String orderType) {
        ModelAndView mv = new JModelAndView(
            "admin/blue/complaintsubject_list.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        String url = this.configService.getSysConfig().getAddress();
        if ((url == null) || (url.equals(""))) {
            url = CommUtil.getURL(request);
        }
        String params = "";
        ComplaintSubjectQueryObject qo = new ComplaintSubjectQueryObject(
            currentPage, mv, orderBy, orderType);
        WebForm wf = new WebForm();
        wf.toQueryPo(request, qo, ComplaintSubject.class, mv);
        IPageList pList = this.complaintsubjectService.list(qo);
        CommUtil.saveIPageList2ModelAndView(url +
                                            "/admin/complaintsubject_list.htm", "", params, pList, mv);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "投诉主题添加", value = "/admin/complaintsubject_add.htm*", rtype = "admin", rname = "投诉管理", rcode = "complaint_manage", rgroup = "交易")
    @RequestMapping( {"/admin/complaintsubject_add.htm"})
    public ModelAndView add(HttpServletRequest request, HttpServletResponse response, String currentPage) {
        ModelAndView mv = new JModelAndView(
            "admin/blue/complaintsubject_add.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        mv.addObject("currentPage", currentPage);

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "投诉主题编辑", value = "/admin/complaintsubject_edit.htm*", rtype = "admin", rname = "投诉管理", rcode = "complaint_manage", rgroup = "交易")
    @RequestMapping( {"/admin/complaintsubject_edit.htm"})
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, String id, String currentPage) {
        ModelAndView mv = new JModelAndView(
            "admin/blue/complaintsubject_add.html", this.configService
            .getSysConfig(),
            this.userConfigService.getUserConfig(), 0, request, response);
        if ((id != null) && (!id.equals(""))) {
            ComplaintSubject complaintsubject = this.complaintsubjectService
                                                .getObjById(Long.valueOf(Long.parseLong(id)));
            mv.addObject("obj", complaintsubject);
            mv.addObject("currentPage", currentPage);
            mv.addObject("edit", Boolean.valueOf(true));
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "投诉主题保存", value = "/admin/complaintsubject_save.htm*", rtype = "admin", rname = "投诉管理", rcode = "complaint_manage", rgroup = "交易")
    @RequestMapping( {"/admin/complaintsubject_save.htm"})
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response, String id, String currentPage, String cmd, String list_url, String add_url) {
        WebForm wf = new WebForm();
        ComplaintSubject complaintsubject = null;
        if (id.equals("")) {
            complaintsubject = (ComplaintSubject)wf.toPo(request, ComplaintSubject.class);
            complaintsubject.setAddTime(new Date());
        } else {
            ComplaintSubject obj = this.complaintsubjectService.getObjById(
                                       Long.valueOf(Long.parseLong(id)));
            complaintsubject = (ComplaintSubject)wf.toPo(request, obj);
        }

        if (id.equals(""))
            this.complaintsubjectService.save(complaintsubject);
        else
            this.complaintsubjectService.update(complaintsubject);
        ModelAndView mv = new JModelAndView("admin/blue/success.html",
                                            this.configService.getSysConfig(), this.userConfigService
                                            .getUserConfig(), 0, request, response);
        mv.addObject("list_url", list_url);
        mv.addObject("op_title", "保存投诉主题成功");
        if (add_url != null) {
            mv.addObject("add_url", add_url + "?currentPage=" + currentPage);
        }

        return mv;
    }

    @SecurityMapping(display = false, rsequence = 0, title = "投诉主题删除", value = "/admin/complaintsubject_del.htm*", rtype = "admin", rname = "投诉管理", rcode = "complaint_manage", rgroup = "交易")
    @RequestMapping( {"/admin/complaintsubject_del.htm"})
    public String delete(HttpServletRequest request, HttpServletResponse response, String mulitId, String currentPage) {
        String[] ids = mulitId.split(",");
        for (String id : ids) {
            if (!id.equals("")) {
                ComplaintSubject complaintsubject = this.complaintsubjectService
                                                    .getObjById(Long.valueOf(Long.parseLong(id)));
                this.complaintsubjectService.delete(Long.valueOf(Long.parseLong(id)));
            }
        }

        return "redirect:complaintsubject_list.htm?currentPage=" + currentPage;
    }
}




