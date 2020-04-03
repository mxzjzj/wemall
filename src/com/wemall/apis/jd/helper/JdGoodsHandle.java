package com.wemall.apis.jd.helper;

import com.wemall.apis.jd.service.goods.JDGoodsService;
import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.domain.GoodsClass;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JdGoodsHandle {

    @Value("#{config.parentid}")
    private String parentid;

    @Resource(name = "jDGoodsService")
    private JDGoodsService jDGoodsService;

    @Resource(name = "goodsClassDAO")
    private IGenericDAO<GoodsClass> goodsClassDAO;

    @RequestMapping("/jdgoods.htm")
    public String updategoods(@RequestParam(value = "skuid") String skuid){
        CacheManager manager = CacheManager.create();
        String[] names = manager.getCacheNames();
        for (String name : names) {
            manager.clearAllStartingWith(name);
        }

        Map params = new HashMap();
        params.put("classificationLevel", 2);
        String hql = " select obj from GoodsClass obj where   obj.level=:classificationLevel  and obj.classificationJDCid is not null  and obj.parent.id in ("+skuid+") ";

        List<GoodsClass> lists = this.goodsClassDAO.query(hql, params);
        for (GoodsClass classification : lists) {
            String classificationCid = classification.getClassificationJDCid();
            if (classificationCid != null) {
                try {
                    this.jDGoodsService.batchAddOrUpdateGoods_rest(classificationCid);
                } catch (Exception e) {
                    try {
                        e.printStackTrace();
                        this.jDGoodsService.batchAddOrUpdateGoods_rest(classificationCid);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }

        return "数据采集成功";


    }
}
