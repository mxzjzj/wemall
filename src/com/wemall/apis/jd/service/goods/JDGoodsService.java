package com.wemall.apis.jd.service.goods;

import java.util.List;

import com.wemall.apis.jd.response.CheckSkuResponse;
import com.wemall.apis.jd.response.CheckStockResponse;

public interface JDGoodsService {

	void batchAddOrUpdateGoods(String paramString);

	void addOrUpdateGoods(String paramString);

	String getSkuid(Long paramLong);

	void updateInventory(Long paramLong, int paramInt);
	
	CheckSkuResponse getCheckSkuResponse(List<String> ids);
	
	CheckStockResponse getStockByIdResponse(List<String> ids);

	void batchAddOrUpdateGoods_rest(String paramString);
}
