package com.wemall.apis.jd.dao.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import com.wemall.apis.jd.dao.util.JDBase;


/**
 * 6、库存API接口
 * @author zhangjun
 *
 */

@SuppressWarnings("unchecked")
@Repository("jDInventoryDao")
public class JDInventory extends JDBase
{
  //6.2 批量获取库存接口
  private String orderInventoryUrl = "https://bizapi.jd.com/api/stock/getNewStockById";

  //6.3 批量获取库存接口(建议商品列表页使用)
  private String articleInventoryUrl = "https://bizapi.jd.com/api/stock/getStockById";

  

public String JDOrderInventory(String skuNums, String area)
  {
    Map createMap = new HashMap();
    createMap.put("token", accessToken);
    createMap.put("skuNums", skuNums);
    createMap.put("area", area);

    String httpResult = this.httpClientUtil.doPost(this.orderInventoryUrl, createMap, this.charset);
    JSONObject json_parent = JSONObject.fromObject(httpResult);

    boolean success = json_parent.getBoolean("success");
    if (!success) {
      String resultMessage = json_parent.getString("resultMessage");
      logger.error("JDInventoryError:" + resultMessage);
      return null;
    }
    String jsonResult = json_parent.getString("result");
    int length = jsonResult.indexOf("\"stockStateId\":");
    String stockStateId = jsonResult.substring(length + 15, length + 17);
    return stockStateId;
  }

  public String JDArticleInventory(String sku, String area)
  {
    Map createMap = new HashMap();
    createMap.put("token", accessToken);
    createMap.put("sku", sku);
    createMap.put("area", area);

    String httpResult = this.httpClientUtil.doPost(this.articleInventoryUrl, createMap, this.charset);
    JSONObject json_parent = JSONObject.fromObject(httpResult);

    boolean success = json_parent.getBoolean("success");
    if (!success) {
      String resultMessage = json_parent.getString("resultMessage");
      logger.error("JDInventoryError:" + resultMessage);
      return null;
    }
    String jsonResult = json_parent.getString("result");
    int length = jsonResult.indexOf("\"state\":");
    String state = jsonResult.substring(length + 9, length + 11);
    return state;
  }

  public List<String> JDArticleInventoryByList(List<String> skuIdes, String area)
  {
    if (skuIdes.size() == 0) {
      return new ArrayList();
    }
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < skuIdes.size(); i++) {
      if ((i != 0) && (i != skuIdes.size())) {
        sb.append(",");
      }
      sb.append((String)skuIdes.get(i));
    }
    Map createMap = new HashMap();
    createMap.put("token", accessToken);
    createMap.put("sku", sb.toString());
    createMap.put("area", area);
    String httpResult = null;
    try {
      httpResult = this.httpClientUtil.doPost(this.articleInventoryUrl, createMap, this.charset);
    } catch (Exception e) {
      httpResult = this.httpClientUtil.doPost(this.articleInventoryUrl, createMap, this.charset);
      logger.error("jdInventory JDArticleInventoryByList is error:httpResult:" + e);
    }
    JSONObject json_parent = JSONObject.fromObject(httpResult);
    boolean success = json_parent.getBoolean("success");
    if (!success) {
      String resultMessage = json_parent.getString("resultMessage");
      logger.error("JDInventoryError:" + resultMessage);
      return null;
    }
    List goodsSkuId = new ArrayList();
    JSONArray resultes = JSONArray.fromObject(json_parent.getString("result"));
    for (int i = 0; i < resultes.size(); i++) {
      JSONObject result = resultes.getJSONObject(i);
      String state = result.getString("state");
      if (("33".equals(state)) || ("39".equals(state)) || ("40".equals(state))) {
        String goodsSku = result.getString("sku");
        goodsSkuId.add(goodsSku);
      }
    }
    return goodsSkuId;
  }

  public String JDOrderInventory2(String skuNums, String area)
  {
    Map createMap = new HashMap();
    createMap.put("token", accessToken);
    createMap.put("skuNums", skuNums);
    createMap.put("area", area);

    String httpResult = this.httpClientUtil.doPost(this.orderInventoryUrl, createMap, this.charset);
    JSONObject json_parent = JSONObject.fromObject(httpResult);

    boolean success = json_parent.getBoolean("success");
    if (!success) {
      String resultMessage = json_parent.getString("resultMessage");
      logger.error("JDInventoryError:" + resultMessage);
      return null;
    }
    JSONArray jsonArray = JSONArray.fromObject(json_parent.getString("result"));
    Iterator it = jsonArray.iterator();
    while (it.hasNext()) {
      JSONObject ob = (JSONObject)it.next();
      System.out.println(ob);
    }
    String jsonResult = json_parent.getString("result");

    int length = jsonResult.indexOf("\"stockStateId\":");
    String stockStateId = jsonResult.substring(length + 15, length + 17);
    return stockStateId;
  }
}