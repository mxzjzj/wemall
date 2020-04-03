package com.wemall.apis.jd.dao.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.wemall.apis.jd.test.base.TestBase;
import com.wemall.foundation.dao.GoodsDAO;
import com.wemall.foundation.domain.GoodsClass;

public class TestGoodsClass   {
	
	
	private JDGoodsPool jDGoodsPoolDao=new JDGoodsPool() ;
	
	
	private JDGoodsInfo jDGoodsInfoDao=new JDGoodsInfo();
	
	
	private JDGoodsClassify jDGoodsClassifyDao=new JDGoodsClassify();

	@Test
	public void testGoodsClass(){
		List<String> pagenums=jDGoodsPoolDao.getPoolNum();
		String skuid;
		List<String> cids=new ArrayList<String>();
		List<String> pools=new ArrayList<String>();
		for(String pagenum:pagenums){
			skuid=jDGoodsPoolDao.getPoolGoodsSkuIdByCategory(pagenum);
			if(skuid==null)
				continue;
			cids=jDGoodsInfoDao.getGoodsCids(skuid);
			for(String cid:cids){
				if(!pools.contains(cid)){
					pools.add(cid);
					GoodsClass goodsclass=jDGoodsClassifyDao.getCatagorys(cid);
					// {"catId":9987,"parentId":0,"name":"手机","catClass":0,"state":1}
					System.out.println(goodsclass.getClassificationJDCid());
					
				}
				   
			}
		}
		
	}
	
	
	
	
	
}
