package com.wemall.apis.jd.dao.goods;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import com.wemall.apis.jd.dao.util.JDBase;
import com.wemall.core.base.GenericDAO;
import com.wemall.core.dao.IGenericDAO;
import com.wemall.foundation.domain.Goods;
import com.wemall.foundation.domain.GoodsClass;


@Repository("jDGoodsClassifyDao")
public class JDGoodsClassify extends JDBase {
	// 3.15 查询分类信息接口
	private String getCategoryUrl = "https://bizapi.jd.com/api/product/getCategory";

	// 3.16 查询分类列表信息接口
	private String getCategorysUrl = "https://bizapi.jd.com/api/product/getCategorys";

	// 3.17 同类商品查询
	private String getSimilarSkuUrl = "https://bizapi.jd.com/api/product/getSimilarSku";

	public String getCategoryParentId(String cid) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("cid", cid);

		JSONObject jsonResult = returnJDObj(this.getCategoryUrl, createMap);
		if (jsonResult != null) {
			String parentId = jsonResult.getString("parentId");
			return parentId;
		}
		return null;
	}

	public GoodsClass getCatagorys(String cid) {
		GoodsClass goodsclass = new GoodsClass();
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("cid", cid);
		// {"catId":9987,"parentId":0,"name":"手机","catClass":0,"state":1}
		//{"catId":653,"parentId":9987,"name":"手机通讯","catClass":1,"state":1}
		//{"catId":655,"parentId":653,"name":"手机","catClass":2,"state":1}
		JSONObject jsonResult = returnJDObj(this.getCategoryUrl, createMap);
		if (jsonResult != null) {
			goodsclass.setClassificationJDCid(cid);
			insertGoodsClass(jsonResult);
			return goodsclass;
		}
		return goodsclass;
	}

	public String getCategoryCatClass(String cid) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("cid", cid);

		JSONObject jsonResult = returnJDObj(this.getCategoryUrl, createMap);
		if (jsonResult == null) {
			return null;
		}
		String catClass = jsonResult.getString("catClass");
		return catClass;
	}

	public Map<String, String> getCategorys(String pageNo, String pageSize, String parentId, String catClass) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("pageNo", pageNo);
		createMap.put("pageSize", pageSize);
		createMap.put("parentId", parentId);
		createMap.put("catClass", catClass);
		JSONObject jsonResult = returnJDObj(this.getCategorysUrl, createMap);

		if (jsonResult == null) {
			return null;
		}

		String totalRows = jsonResult.getString("totalRows");

		JSONArray categorys = jsonResult.getJSONArray("categorys");

		Map maps = new HashMap();
		for (int i = 0; i < categorys.size(); i++) {
			JSONObject jsonObj = categorys.getJSONObject(i);
			String catId = jsonObj.getString("catId");
			String name = jsonObj.getString("name");
			if (!"赠品".equals(name)) {
				maps.put(catId, name);
			}
		}
		return maps;
	}

	public void getSimilarSku(String skuId) {
		Map createMap = new HashMap();
		createMap.put("token", accessToken);
		createMap.put("skuId", skuId);

		JSONArray jsonResult = returnJDArray(this.getSimilarSkuUrl, createMap);
		System.out.println(jsonResult);
	}
}
