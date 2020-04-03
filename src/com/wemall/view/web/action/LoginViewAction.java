package com.wemall.view.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wemall.apis.jd.service.WxConfigureService;
import com.wemall.apis.jdutil.WeixinUtil;
import com.wemall.core.mv.JModelAndView;
import com.wemall.core.security.support.SecurityUserHolder;
import com.wemall.core.tools.CommUtil;
import com.wemall.core.tools.Md5Encrypt;
import com.wemall.foundation.domain.Album;
import com.wemall.foundation.domain.IntegralLog;
import com.wemall.foundation.domain.User;
import com.wemall.foundation.service.IAlbumService;
import com.wemall.foundation.service.IIntegralLogService;
import com.wemall.foundation.service.IRoleService;
import com.wemall.foundation.service.ISysConfigService;
import com.wemall.foundation.service.IUserConfigService;
import com.wemall.foundation.service.IUserService;
import com.wemall.uc.api.UCClient;
import com.wemall.uc.api.UCTools;
import com.wemall.view.web.tools.ImageViewTools;

import net.sf.json.JSONObject;

/**
 * 买家登录控制器
 */
@Controller
public class LoginViewAction {

    public static List<String> browserList = new ArrayList<String>(45);

    static {
        browserList.add("nokia");
        browserList.add("samsung");
        browserList.add("midp-2");
        browserList.add("cldc1.1");
        browserList.add("symbianos");
        browserList.add("maui");
        browserList.add("untrusted/1.0");
        browserList.add("windows ce");
        browserList.add("iphone");
        browserList.add("ipad");
        browserList.add("android");
        browserList.add("blackberry");
        browserList.add("ucweb");
        browserList.add("brew");
        browserList.add("j2me");
        browserList.add("yulong");
        browserList.add("coolpad");
        browserList.add("tianyu");
        browserList.add("ty-");
        browserList.add("k-touch");
        browserList.add("haier");
        browserList.add("dopod");
        browserList.add("lenovo");
        browserList.add("mobile");
        browserList.add("huaqin");
        browserList.add("aigo-");
        browserList.add("ctc/1.0");
        browserList.add("ctc/2.0");
        browserList.add("cmcc");
        browserList.add("daxian");
        browserList.add("mot-");
        browserList.add("sonyericsson");
        browserList.add("gionee");
        browserList.add("htc");
        browserList.add("zte");
        browserList.add("huawei");
        browserList.add("webos");
        browserList.add("gobrowser");
        browserList.add("iemobile");
        browserList.add("wap2.0");
        browserList.add("ucbrowser");
        browserList.add("ipod");
    }

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IUserConfigService userConfigService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IIntegralLogService integralLogService;

    @Autowired
    private IAlbumService albumService;

    @Autowired
    private ImageViewTools imageViewTools;

    @Autowired
    private UCTools ucTools;

    @Autowired
    private WxConfigureService wxConfigureService;


    /**
     * 用户登录跳转页面
     */
    @RequestMapping({"/user/login.htm"})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        ModelAndView mv = new JModelAndView("login.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);

        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        for (String browser : browserList) {
            if (!userAgent.contains(browser)) {
                continue;
            }
            request.getSession().setAttribute("wemall_view_type", "wap");
            // 客户端
            break;
        }
        String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));

		/*String openid = null;
        try {
			openid = request.getSession().getAttribute("openid").toString();
		} catch (Exception e) {
			openid = null;
		}

		if (openid != null && (wemall_view_type.equals("wap"))) {
			String targetUrl = request.getContextPath() + "/wap/buyer/index.htm";
			response.sendRedirect(targetUrl);
		}*/

        if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
            mv = new JModelAndView("/wap/login.html", this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
        }

        request.getSession(false).removeAttribute("verify_code");
        boolean domain_error = CommUtil.null2Boolean(request.getSession(false).getAttribute("domain_error"));
        if ((url != null) && (!url.equals(""))) {
            request.getSession(false).setAttribute("refererUrl", url);
        }
        if (domain_error) {
            mv = new JModelAndView("error.html", this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
            if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
                mv = new JModelAndView("wap/error.html", this.configService.getSysConfig(),
                        this.userConfigService.getUserConfig(), 1, request, response);
            }
        } else {
            mv.addObject("imageViewTools", this.imageViewTools);
        }
        mv.addObject("uc_logout_js", request.getSession(false).getAttribute("uc_logout_js"));

        return mv;
    }


    /**
     * 登录操作成功之后跳转判断
     */
    @RequestMapping({"/user_login_success.htm"})
    public ModelAndView user_login_success(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        ModelAndView mv = new JModelAndView("success.html", this.configService.getSysConfig(), this.userConfigService.getUserConfig(), 1, request, response);
        String url = CommUtil.getURL(request) + "/index.htm";
        String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));
        //跳转到微信端
        if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
            String store_id = CommUtil.null2String(request.getSession(false).getAttribute("store_id"));
            mv = new JModelAndView("wap/success.html",
                    this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
            url = CommUtil.getURL(request) + "/wap/index.htm?store_id=" + store_id;
        }
        HttpSession session = request.getSession(false);
        if ((session.getAttribute("refererUrl") != null) &&
                (!session.getAttribute("refererUrl").equals(""))) {
            url = (String)session.getAttribute("refererUrl");
            session.removeAttribute("refererUrl");
        }
        if (this.configService.getSysConfig().isUc_bbs()) {
            String uc_login_js = CommUtil.null2String(request.getSession(false).getAttribute("uc_login_js"));
            mv.addObject("uc_login_js", uc_login_js);
        }
        //第三方登录：QQ、新浪等
        String bind = CommUtil.null2String(request.getSession(false).getAttribute("bind"));
        if (!bind.equals("")) {
            mv = new JModelAndView(bind + "_login_bind.html",
                    this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
            User user = SecurityUserHolder.getCurrentUser();
            mv.addObject("user", user);
            request.getSession(false).removeAttribute("bind");
        }
        mv.addObject("op_title", "登录成功");
        mv.addObject("url", url);

        return mv;
    }


    @RequestMapping("/user_wechat_login.htm")
    public ModelAndView user_wechat_login(HttpServletRequest request, HttpServletResponse response) {
        String store_id = CommUtil.null2String(request.getSession(false).getAttribute("store_id"));
        ModelAndView mv = new JModelAndView("wap/success.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        String url = CommUtil.getURL(request) + "/wap/index.htm?store_id=" + store_id;
        String code = request.getParameter("code");

        String openid = WeixinUtil.getOpenid(code);
//		String wechatOpenId=userService.getObjByProperty("wechatOpenId",openid).toString();
        mv.addObject("url", url);
        mv.addObject("op_title", "正在登录系统,请稍后");
        User  user= (User) request.getSession().getAttribute("user");
        String username=user.getUsername();
        this.userService.saveUserNameOpenid(openid,username);
        return mv;

    }

    /**
     * 注册跳转页面
     */
    @RequestMapping({"/register.htm"})
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("register.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);

        String wemall_view_type = CommUtil.null2String(request.getSession(false).getAttribute("wemall_view_type"));

        if ((wemall_view_type != null) && (!wemall_view_type.equals("")) && (wemall_view_type.equals("wap"))) {
            mv = new JModelAndView("wap/register.html", this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
        }
        request.getSession(false).removeAttribute("verify_code");

        return mv;
    }

    /**
     * 注册保存
     */
    @RequestMapping({"/register_finish.htm"})
    public String register_finish(HttpServletRequest request, HttpServletResponse response, String userName,
                                  String password, String email, String code) throws HttpException, IOException {
        boolean reg = true;

        // 判断验证码
        if ((code != null) && (!code.equals(""))) {
            code = CommUtil.filterHTML(code);
        }
        if (this.configService.getSysConfig().isSecurityCodeRegister()) {
            if (!request.getSession(false).getAttribute("verify_code").equals(code)) {
                reg = false;
            }
        }

        // 用户名不能重复
        Map params = new HashMap();
        params.put("userName", userName);
        List users = this.userService.query("select obj from User obj where obj.userName=:userName", params, -1, -1);
        if ((users != null) && (users.size() > 0)) {
            reg = false;
        }

        if (reg) {
            User user = new User();
            user.setUserName(userName);
            user.setUserRole("BUYER");
            user.setAddTime(new Date());
            user.setEmail(email);
            user.setPassword(Md5Encrypt.md5(password).toLowerCase());
            params.clear();
            params.put("type", "BUYER");
            List roles = this.roleService.query("select obj from Role obj where obj.type=:type", params, -1, -1);
            user.getRoles().addAll(roles);

            // 如果系统开启积分功能，则给会员新增积分
            if (this.configService.getSysConfig().isIntegral()) {
                user.setIntegral(this.configService.getSysConfig().getMemberRegister());
                this.userService.save(user);
                IntegralLog log = new IntegralLog();
                log.setAddTime(new Date());
                log.setContent("用户注册增加" + this.configService.getSysConfig().getMemberRegister() + "分");
                log.setIntegral(this.configService.getSysConfig().getMemberRegister());
                log.setIntegral_user(user);
                log.setType("reg");
                this.integralLogService.save(log);
            } else {
                this.userService.save(user);
            }

            // 设置相册
            Album album = new Album();
            album.setAddTime(new Date());
            album.setAlbum_default(true);
            album.setAlbum_name("默认相册");
            album.setAlbum_sequence(-10000);
            album.setUser(user);
            this.albumService.save(album);

            request.getSession(false).removeAttribute("verify_code");

            // UC会员同步
            if (this.configService.getSysConfig().isUc_bbs()) {
                UCClient client = new UCClient();
                String ret = client.uc_user_register(userName, password, email);
                int uid = Integer.parseInt(ret);
                if (uid <= 0) {
                    if (uid == -1)
                        System.out.print("用户名不合法");
                    else if (uid == -2)
                        System.out.print("包含要允许注册的词语");
                    else if (uid == -3)
                        System.out.print("用户名已经存在");
                    else if (uid == -4)
                        System.out.print("Email 格式有误");
                    else if (uid == -5)
                        System.out.print("Email 不允许注册");
                    else if (uid == -6)
                        System.out.print("该 Email 已经被注册");
                    else
                        System.out.print("未定义");
                } else {
                    this.ucTools.active_user(userName, user.getPassword(), email);
                }
            }

            return "redirect:wemall_login.htm?username=" + CommUtil.encode(userName) + "&password=" + password
                    + "&encode=true";
        }

        return "redirect:register.htm";
    }


    /**
     * 登录模态窗口
     */
    @RequestMapping({"/user_dialog_login.htm"})
    public ModelAndView user_dialog_login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new JModelAndView("user_dialog_login.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);

        return mv;
    }

    /**
     * wap登录业务逻辑begin
     */

    @RequestMapping({"/user/wap/login.htm"})
    public ModelAndView waplogin(HttpServletRequest request, HttpServletResponse response, String url) {

        ModelAndView mv = new JModelAndView("wap/login.html", this.configService.getSysConfig(),
                this.userConfigService.getUserConfig(), 1, request, response);
        request.getSession(false).removeAttribute("verify_code");

        boolean domain_error = CommUtil.null2Boolean(request.getSession(false).getAttribute("domain_error"));
        if ((url != null) && (!url.equals(""))) {
            request.getSession(false).setAttribute("refererUrl", url);
        }
        if (domain_error)
            mv = new JModelAndView("wap/error.html", this.configService.getSysConfig(),
                    this.userConfigService.getUserConfig(), 1, request, response);
        else {
            mv.addObject("imageViewTools", this.imageViewTools);
        }
        mv.addObject("uc_logout_js", request.getSession(false).getAttribute("uc_logout_js"));

		/*
         * String wemall_view_type =
		 * CommUtil.null2String(request.getSession(false).getAttribute(
		 * "wemall_view_type"));
		 *
		 * if ((wemall_view_type != null) && (!wemall_view_type.equals("")) &&
		 * (wemall_view_type.equals("wap"))) { //String store_id =
		 * CommUtil.null2String(request.getSession(false).getAttribute(
		 * "store_id")); mv = new JModelAndView("wap/success.html",
		 * this.configService.getSysConfig(),
		 * this.userConfigService.getUserConfig(), 1, request, response);
		 * mv.addObject("op_title", "成功！"); mv.addObject("url",
		 * CommUtil.getURL(request) + "/wap/index.htm"); }
		 */

        return mv;
    }

}
