package com.wemall.view.web.tools;

import com.wemall.foundation.domain.Accessory;
import com.wemall.foundation.domain.SysConfig;
import com.wemall.foundation.service.ISysConfigService;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
	ͼ�񹤾����
**/
@Component
public class ImageViewTools {

    @Autowired
    private ISysConfigService configService;

    public String random_login_img() {
        String img = "";
        SysConfig config = this.configService.getSysConfig();
        if (config.getLogin_imgs().size() > 0) {
            Random random = new Random();
            Accessory acc = (Accessory)config.getLogin_imgs().get(
                                random.nextInt(config.getLogin_imgs().size()));
            img = acc.getPath() + "/" + acc.getName();
        } else {
            img = "resources/style/common/images/login_img.jpg";
        }

        return img;
    }
}




