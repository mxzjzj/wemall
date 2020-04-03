package com.wemall.apis.jd.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wemall.apis.jd.service.goods.JDGoodsService;
import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.domain.GoodsClass;

@Service("jdGoodsHelper")
public class JdGoodsHelper {

	@Resource(name = "jDGoodsService")
	private JDGoodsService jDGoodsService;

	@Resource(name = "goodsClassDAO")
	private IGenericDAO<GoodsClass> goodsClassDAO;
	protected static Logger logger = Logger.getLogger(JdGoodsHelper.class);

	@Value("#{config.classid}")
	private int classid;

	@Value("#{config.parentid}")
	private String parentid;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void updateGoods() {
		CacheManager manager = CacheManager.create();
		String[] names = manager.getCacheNames();
		for (String name : names) {
			manager.clearAllStartingWith(name);
		}

		Map params = new HashMap();
		params.put("classificationLevel", 2);
		String hql = " select obj from GoodsClass obj where   obj.level=:classificationLevel  and obj.classificationJDCid is not null  and obj.parent.id in ("+parentid+") ";
//		String hql = " select obj from GoodsClass obj where   obj.level=:classificationLevel  and obj.classificationJDCid is not null   ";

		List<GoodsClass> lists = this.goodsClassDAO.query(hql, params);
		for (GoodsClass classification : lists) {
			String classificationCid = classification.getClassificationJDCid();
			if (classificationCid != null) {
				try {
					this.jDGoodsService.batchAddOrUpdateGoods(classificationCid);
					logger.info(this.sdf.format(new Date()) + " where jdGoodsHelper asc ok!:" + classificationCid);
				} catch (Exception e) {
					try {
						e.printStackTrace();
						this.jDGoodsService.batchAddOrUpdateGoods(classificationCid);
					} catch (Exception e2) {
						logger.error(this.sdf.format(new Date()) + " 批量增加商品：" + classificationCid + ":" + e);
					}
				}
			}
		}
	}
}
